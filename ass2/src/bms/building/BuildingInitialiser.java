package bms.building;

import bms.exceptions.FileFormatException;
import bms.floor.Floor;
import bms.hazardevaluation.HazardEvaluator;
import bms.hazardevaluation.RuleBasedHazardEvaluator;
import bms.hazardevaluation.WeightingBasedHazardEvaluator;
import bms.room.Room;
import bms.room.RoomType;
import bms.sensors.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Class which manages the initialisation and saving of buildings by reading and
 * writing data to a file.
 */
public class BuildingInitialiser {
    /**
     * Loads a list of buildings from a save file with the given filename.
     *
     * @param filename path of the file from which to load a list of buildings
     * @return a list containing all the buildings loaded from the file
     * @throws IOException if an IOException is encountered when calling any
     * IO methods
     * @throws FileFormatException if the file format of the given file
     * is invalid according to the rules above
     */
    public static List<Building> loadBuildings(String filename) throws
            IOException, FileFormatException {
        List<Building> buildings = new ArrayList<>();
        BufferedReader tempReader =
                new BufferedReader(new FileReader(filename));
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        int numberOfBuilding = 0;
        int numberOfFloor = 0;
        String line;

        // Get the number of buildings.
        while ((line = tempReader.readLine()) != null) {
            if ("".equals(line)) {
                throw new FileFormatException();
            }
            try {
                int i = Integer.parseInt(line);
                numberOfBuilding += 1;
            } catch (Exception e) {
                continue;
            }
        }

        // Initialise building
        for (int j = 0; j < numberOfBuilding; j++) {
            Building building = new Building(reader.readLine());
            try {
                String temp = reader.readLine();
                numberOfFloor = Integer.parseInt(temp);
            } catch (Exception e) {
                throw new FileFormatException();
            }

            for (int i = 0; i < numberOfFloor; i++) {
                try {
                    Floor floor = BuildingInitialiser.readFloor(reader);
                    building.addFloor(floor);
                } catch (Exception e) {
                    throw new FileFormatException();
                }
            }
            buildings.add(building);
        }

        // Check if there is missing information.
        String checkMissing = reader.readLine();
        if (checkMissing != null) {
            throw new FileFormatException();
        }

        return buildings;
    }


    private static Floor readFloor(BufferedReader r) throws IOException,
            FileFormatException {
        String temp = r.readLine();
        String[] floorInformation = temp.split(":");
        List<Integer> numberOfMaintenance = new ArrayList<>();
        List<Room> roomsToMaintenance = new ArrayList<>();
        List<Room> rooms = new ArrayList<>();
        int floorNumber = 0;
        double width = 0;
        double length = 0;
        int numberOfRoom = 0;

        if (floorInformation.length == 5) {
            String[] lastelement = floorInformation[4].split(",");
            for (int i = 0; i < lastelement.length; i++) {
                try {
                    numberOfMaintenance.add(Integer.parseInt(lastelement[i]));
                } catch (Exception e) {
                    throw new FileFormatException();
                }
            }
        }

        try {
            floorNumber = Integer.parseInt(floorInformation[0]);

            if (floorNumber < 1) {
                throw new FileFormatException();
            }

            width = Double.parseDouble(floorInformation[1]);
            length = Double.parseDouble(floorInformation[2]);
            numberOfRoom = Integer.parseInt(floorInformation[3]);
        } catch (Exception e) {
            throw new FileFormatException();
        }
        Floor floor =  new Floor(floorNumber, width, length);

        for (int i = 0; i < numberOfRoom; i++) {
            try{
                Room room = BuildingInitialiser.readRoom(r);
                rooms.add(room);
                floor.addRoom(room);
            } catch (Exception e) {
                throw new FileFormatException();
            }
        }

        if (floorInformation.length == 5) {
            for (int i : numberOfMaintenance) {
                List<Integer> numbers = new ArrayList<>();
                for (int j = 0; j < rooms.size(); j ++) {
                    numbers.add(rooms.get(j).getRoomNumber());
                    if (rooms.get(j).getRoomNumber() == i) {
                        roomsToMaintenance.add(rooms.get(j));
                    }
                } if (numbers.contains(i) != true) {
                    throw new FileFormatException();
                }
            }
            floor.createMaintenanceSchedule(roomsToMaintenance);
        }
        return floor;
    }

    private static Room readRoom(BufferedReader r) throws IOException,
            FileFormatException {
        String temp = r.readLine();
        String[] roomInfo = temp.split(":");
        int lengthOfInformation = roomInfo.length;
        int roomNumber;
        RoomType type;
        double area;
        int numberOfSensor;
        Room room;
        HazardEvaluator evaluator = null;
        Sensor sensor = null;
        HazardSensor hazardSensor = null;

        List<HazardSensor> forRuleBase = new ArrayList<>();
        Map<HazardSensor, Integer> forWeightBase = new HashMap<>();

        try {
            roomNumber = Integer.parseInt(roomInfo[0]);
            type = RoomType.valueOf(roomInfo[1]);
            area = Double.parseDouble(roomInfo[2]);
            numberOfSensor = Integer.parseInt(roomInfo[3]);
            room = new Room(roomNumber, type, area);
        } catch (Exception e) {
            throw new FileFormatException();
        }

        for (int i = 0; i < numberOfSensor; i++) {
            String[] sensorInformation = BuildingInitialiser.loadSensor(r);

            if (lengthOfInformation == 4 || (lengthOfInformation == 5 &&
                    roomInfo[4].equals("RuleBased"))) {
                if (lengthOfInformation == 4) {
                    try {
                        sensor = readSensor(sensorInformation);
                        room.addSensor(sensor);
                    } catch (Exception e) {
                        throw new FileFormatException();
                    }
                } else if (lengthOfInformation == 5 &&
                        roomInfo[4].equals("RuleBased")) {
                    try {
                        forRuleBase =
                                readSensorForRuleBase
                                        (sensorInformation, forRuleBase, room);
                    } catch (Exception e) {
                        throw new FileFormatException();
                    }
                }
            } else if (lengthOfInformation == 5 &&
                    roomInfo[4].equals("WeightingBased")) {
                try {
                    forWeightBase = readSensorForWeightBase
                            (sensorInformation, forWeightBase, room);
                } catch (Exception e) {
                    throw new FileFormatException();
                }
            }

        }
        if (lengthOfInformation == 5) {
            if (roomInfo[4].equals("RuleBased")) {
                evaluator = new RuleBasedHazardEvaluator(forRuleBase);
            } else if (roomInfo[4].equals("WeightingBased")) {
                evaluator = new WeightingBasedHazardEvaluator(forWeightBase);
            } else {
                throw new FileFormatException();
            }
            room.setHazardEvaluator(evaluator);
        }

        return room;
    }

    private static Map<HazardSensor, Integer> readSensorForWeightBase
            (String[] sensorInformation,
             Map<HazardSensor, Integer> forWeightBase, Room r)
            throws FileFormatException {
        String[] lastElementSplit =
                sensorInformation[sensorInformation.length - 1].
                        split("@");
        HazardSensor hazardSensor;
        Sensor sensor;
        Room room = r;
        if (sensorInformation.length != 2) {
            String[] readingString = sensorInformation[1].split(",");
            int[] reading = new int[readingString.length];

            try {
                for (int j = 0; j < readingString.length; j++) {
                    reading[j] = Integer.parseInt(readingString[j]);
                }
            } catch (Exception e) {
                throw new FileFormatException();
            }

            if (sensorInformation.length == 5) {
                try {
                    int updateFrequency =
                            Integer.parseInt(sensorInformation[2]);
                    int idealValue =
                            Integer.parseInt(sensorInformation[3]);
                    int variationLimit =
                            Integer.parseInt(lastElementSplit[0]);
                    int weight = Integer.parseInt(lastElementSplit[1]);
                    sensor = new CarbonDioxideSensor(reading,
                            updateFrequency,
                            idealValue, variationLimit);
                    room.addSensor(sensor);
                    hazardSensor = new CarbonDioxideSensor(reading,
                            updateFrequency,
                            idealValue, variationLimit);
                    forWeightBase.put(hazardSensor, weight);
                } catch (Exception e) {
                    throw new FileFormatException();
                }
            } else if (sensorInformation.length == 4) {
                try {
                    int updateFrequency =
                            Integer.parseInt(sensorInformation[2]);
                    int capacity =
                            Integer.parseInt(lastElementSplit[0]);
                    int weight = Integer.parseInt(lastElementSplit[1]);
                    sensor = new OccupancySensor(reading,
                            updateFrequency, capacity);
                    room.addSensor(sensor);
                    hazardSensor = new OccupancySensor(reading,
                            updateFrequency, capacity);
                    forWeightBase.put(hazardSensor, weight);
                } catch (Exception e) {
                    throw new FileFormatException();
                }
            } else if (sensorInformation.length == 3) {
                try {
                    int updateFrequency =
                            Integer.parseInt(lastElementSplit[0]);
                    int weight = Integer.parseInt(lastElementSplit[1]);
                    sensor = new NoiseSensor(reading, updateFrequency);
                    room.addSensor(sensor);
                    hazardSensor =
                            new NoiseSensor(reading, updateFrequency);
                    forWeightBase.put(hazardSensor, weight);
                } catch (Exception e) {
                    throw new FileFormatException();
                }
            }
        } else {
            String[] readingString = lastElementSplit[0].split(",");
            int[] reading = new int[readingString.length];

            try {
                for (int j = 0; j < readingString.length; j++) {
                    reading[j] = Integer.parseInt(readingString[j]);
                }
            } catch (Exception e) {
                throw new FileFormatException();
            }

            try {
                int weight = Integer.parseInt(lastElementSplit[1]);
                sensor = new TemperatureSensor(reading);
                room.addSensor(sensor);
                hazardSensor = new TemperatureSensor(reading);
                forWeightBase.put(hazardSensor, weight);
            } catch (Exception e) {
                throw new FileFormatException();
            }
        }
        return forWeightBase;
    }

    private static List<HazardSensor> readSensorForRuleBase
            (String[] sensorInformation, List<HazardSensor> forRuleBase, Room r)
            throws FileFormatException {
        String[] readingString = sensorInformation[1].split(",");
        int[] reading = new int[readingString.length];
        HazardSensor hazardSensor;
        Sensor sensor;
        Room room = r;


        try {
            for (int j = 0; j < readingString.length; j++) {
                reading[j] = Integer.parseInt(readingString[j]);
            }
        } catch (Exception e) {
            throw new FileFormatException();
        }

        if (sensorInformation.length == 5) {
            try {
                int updateFrequency =
                        Integer.parseInt(sensorInformation[2]);
                int idealValue = Integer.parseInt(sensorInformation[3]);
                int variationLimit =
                        Integer.parseInt(sensorInformation[4]);
                sensor = new CarbonDioxideSensor(reading, updateFrequency,
                        idealValue, variationLimit);
                room.addSensor(sensor);
                hazardSensor = new CarbonDioxideSensor(reading, updateFrequency,
                        idealValue, variationLimit);
                forRuleBase.add(hazardSensor);
            } catch (Exception e) {
                throw new FileFormatException();
            }

        } else if (sensorInformation.length == 4) {
            try {
                int updateFrequency =
                        Integer.parseInt(sensorInformation[2]);
                int capacity = Integer.parseInt(sensorInformation[3]);

                sensor = new OccupancySensor(reading,
                        updateFrequency, capacity);
                room.addSensor(sensor);
                hazardSensor = new OccupancySensor(reading,
                        updateFrequency, capacity);
                forRuleBase.add(hazardSensor);

            } catch (Exception e) {
                throw new FileFormatException();
            }
        } else if (sensorInformation.length == 3) {
            try {
                int updateFrequency =
                        Integer.parseInt(sensorInformation[2]);

                sensor = new NoiseSensor(reading, updateFrequency);
                room.addSensor(sensor);
                hazardSensor = new NoiseSensor(reading, updateFrequency);
                forRuleBase.add(hazardSensor);

            } catch (Exception e) {
                throw new FileFormatException();
            }
        } else if (sensorInformation.length == 2) {
            try {
                sensor = new TemperatureSensor(reading);
                room.addSensor(sensor);
                hazardSensor = new TemperatureSensor(reading);
                forRuleBase.add(hazardSensor);
            } catch (Exception e) {
                throw new FileFormatException();
            }
        }
        return forRuleBase;
    }



    private static Sensor readSensor(String[] sensorInformation)
            throws FileFormatException {
        String[] readingString = sensorInformation[1].split(",");
        int[] reading = new int[readingString.length];
        Sensor sensor = null;
        try {
            for (int j = 0; j < readingString.length; j++) {
                reading[j] = Integer.parseInt(readingString[j]);
            }
        } catch (Exception e) {
            throw new FileFormatException();
        }

        if (sensorInformation.length == 5) {
            try {
                int updateFrequency =
                        Integer.parseInt(sensorInformation[2]);
                int idealValue = Integer.parseInt(sensorInformation[3]);
                int variationLimit =
                        Integer.parseInt(sensorInformation[4]);
                sensor = new CarbonDioxideSensor(reading,
                        updateFrequency, idealValue, variationLimit);

            } catch (Exception e) {
                throw new FileFormatException();
            }
        } else if (sensorInformation.length == 4) {
            try {
                int updateFrequency =
                        Integer.parseInt(sensorInformation[2]);
                int capacity = Integer.parseInt(sensorInformation[3]);
                sensor = new OccupancySensor(reading, updateFrequency,
                        capacity);

            } catch (Exception e) {
                throw new FileFormatException();
            }
        } else if (sensorInformation.length == 3) {
            try {
                int updateFrequency =
                        Integer.parseInt(sensorInformation[2]);
                sensor = new NoiseSensor(reading, updateFrequency);

            } catch (Exception e) {
                throw new FileFormatException();
            }
        } else if (sensorInformation.length == 2) {
            try {
                sensor = new TemperatureSensor(reading);

            } catch (Exception e) {
                throw new FileFormatException();
            }
        }
        return sensor;
    }


    private static String[] loadSensor(BufferedReader r) throws IOException,
            FileFormatException {
        String[] typesHolder = {"CarbonDioxideSensor", "NoiseSensor",
                "OccupancySensor", "TemperatureSensor"};
        List<String> types = Arrays.asList(typesHolder);
        String temp = r.readLine();
        String[] sensorInfo = temp.split(":");
        if(!types.contains(sensorInfo[0])) {
            throw new FileFormatException();
        }
        return sensorInfo;
    }
}