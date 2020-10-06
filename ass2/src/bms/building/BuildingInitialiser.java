package bms.building;

import bms.exceptions.DuplicateFloorException;
import bms.exceptions.FileFormatException;
import bms.exceptions.FloorTooSmallException;
import bms.exceptions.NoFloorBelowException;
import bms.floor.Floor;
import bms.hazardevaluation.HazardEvaluator;
import bms.hazardevaluation.RuleBasedHazardEvaluator;
import bms.hazardevaluation.WeightingBasedHazardEvaluator;
import bms.room.Room;
import bms.room.RoomType;
import bms.sensors.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
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
     * @throws IOException         if an IOException is encountered when calling any
     *                             IO methods
     * @throws FileFormatException if the file format of the given file
     *                             is invalid according to the rules above
     */
    public static List<Building> loadBuildings(String filename) throws
            IOException, FileFormatException {
        List<Building> buildings = new ArrayList<>();
        BufferedReader tempReader = new BufferedReader(new FileReader(filename));
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        int numberOfBuilding = 0;
        int numberOfFloor = 0;
        String line = "";

        while ((line = tempReader.readLine()) != null) {
            try {
                int i = Integer.parseInt(line);
                numberOfBuilding += 1;
            } catch (Exception e) {
                continue;
            }
        }

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
        return buildings;
    }


    private static Floor readFloor(BufferedReader r) throws IOException,
            FileFormatException {
        String temp = r.readLine();
        String[] floorInformation = temp.split(":");
        int floorNumber = 0;
        double width = 0;
        double length = 0;
        int numberOfRoom = 0;
        try {
            floorNumber = Integer.parseInt(floorInformation[0]);
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
                floor.addRoom(room);
            } catch (Exception e) {
                throw new FileFormatException();
            }
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
        int numberOfSensor = 0;
        Room room = null;
        HazardEvaluator evaluator = null;
        Sensor sensor;
        HazardSensor hazardSensor;

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

            if (lengthOfInformation == 4 || (lengthOfInformation == 5 && roomInfo[4] == "RuleBased")) {
                String[] readingString = sensorInformation[1].split(",");
                int[] reading = new int[readingString.length];

                try {
                    for (int j = 0; j < readingString.length; j++) {
                        reading[i] = Integer.parseInt(readingString[i]);
                    }
                } catch (Exception e) {
                    throw new FileFormatException();
                }

                if (sensorInformation.length == 5) {
                    try {
                        int updateFrequency = Integer.parseInt(sensorInformation[2]);
                        int idealValue = Integer.parseInt(sensorInformation[3]);
                        int variationLimit = Integer.parseInt(sensorInformation[4]);
                        sensor = new CarbonDioxideSensor(reading, updateFrequency, idealValue, variationLimit);
                        room.addSensor(sensor);
                        if (lengthOfInformation == 5 && roomInfo[4] == "RuleBased") {
                            hazardSensor = new CarbonDioxideSensor(reading, updateFrequency, idealValue, variationLimit);
                            forRuleBase.add(hazardSensor);
                        }

                    } catch (Exception e) {
                        throw new FileFormatException();
                    }

                } else if (sensorInformation.length == 4) {
                    try {
                        int updateFrequency = Integer.parseInt(sensorInformation[2]);
                        int capacity = Integer.parseInt(sensorInformation[3]);
                        sensor = new OccupancySensor(reading, updateFrequency, capacity);
                        room.addSensor(sensor);
                        if (lengthOfInformation == 5 && roomInfo[4] == "RuleBased") {
                            hazardSensor = new OccupancySensor(reading, updateFrequency, capacity);
                            forRuleBase.add(hazardSensor);
                        }

                    } catch (Exception e) {
                        throw new FileFormatException();
                    }
                } else if (sensorInformation.length == 3) {
                    try {
                        int updateFrequency = Integer.parseInt(sensorInformation[2]);
                        sensor = new NoiseSensor(reading, updateFrequency);
                        room.addSensor(sensor);
                        if (lengthOfInformation == 5 && roomInfo[4] == "RuleBased") {
                            hazardSensor = new NoiseSensor(reading, updateFrequency);
                            forRuleBase.add(hazardSensor);
                        }
                    } catch (Exception e) {
                        throw new FileFormatException();
                    }
                } else if (sensorInformation.length == 2) {
                    try {
                        sensor = new TemperatureSensor(reading);
                        room.addSensor(sensor);
                        if (lengthOfInformation == 5 && roomInfo[4] == "RuleBased") {
                            hazardSensor = new TemperatureSensor(reading);
                            forRuleBase.add(hazardSensor);
                        }
                    } catch (Exception e) {
                        throw new FileFormatException();
                    }
                }

            }

            else if (lengthOfInformation == 5 && roomInfo[4] == "WeightingBased") {
                String[] lastElementSplit = sensorInformation[sensorInformation.length - 1].split("@");

                if (sensorInformation.length != 2) {
                    String[] readingString = sensorInformation[1].split(",");
                    int[] reading = new int[readingString.length];

                    try {
                        for (int j = 0; j < readingString.length; j++) {
                            reading[i] = Integer.parseInt(readingString[i]);
                        }
                    } catch (Exception e) {
                        throw new FileFormatException();
                    }

                    if (sensorInformation.length == 5) {
                        try {
                            int updateFrequency = Integer.parseInt(sensorInformation[2]);
                            int idealValue = Integer.parseInt(sensorInformation[3]);
                            int variationLimit = Integer.parseInt(lastElementSplit[0]);
                            int weight = Integer.parseInt(lastElementSplit[1]);
                            sensor = new CarbonDioxideSensor(reading, updateFrequency, idealValue, variationLimit);
                            room.addSensor(sensor);
                            hazardSensor = new CarbonDioxideSensor(reading, updateFrequency, idealValue, variationLimit);
                            forWeightBase.put(hazardSensor, weight);
                        } catch (Exception e) {
                            throw new FileFormatException();
                        }
                    } else if (sensorInformation.length == 4) {
                        try {
                            int updateFrequency = Integer.parseInt(sensorInformation[2]);
                            int capacity = Integer.parseInt(lastElementSplit[0]);
                            int weight = Integer.parseInt(lastElementSplit[1]);
                            sensor = new OccupancySensor(reading, updateFrequency, capacity);
                            room.addSensor(sensor);
                            hazardSensor = new OccupancySensor(reading, updateFrequency, capacity);
                            forWeightBase.put(hazardSensor, weight);
                        } catch (Exception e) {
                            throw new FileFormatException();
                        }
                    } else if (sensorInformation.length == 3) {
                        try {
                            int updateFrequency = Integer.parseInt(lastElementSplit[0]);
                            int weight = Integer.parseInt(lastElementSplit[1]);
                            sensor = new NoiseSensor(reading, updateFrequency);
                            room.addSensor(sensor);
                            hazardSensor = new NoiseSensor(reading, updateFrequency);
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
                            reading[i] = Integer.parseInt(readingString[i]);
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

            }

        }

        if (lengthOfInformation == 5) {
            if (roomInfo[4] == "RuleBased") {
                evaluator = new RuleBasedHazardEvaluator(forRuleBase);
            } else if (roomInfo[4] == "WeightingBased") {
                evaluator = new WeightingBasedHazardEvaluator(forWeightBase);
            }
            room.setHazardEvaluator(evaluator);
        }
        return room;
    }



    private static String[] loadSensor(BufferedReader r) throws IOException {
        String temp = r.readLine();
        String[] sensorInfo = temp.split(":");
        return sensorInfo;
    }


}