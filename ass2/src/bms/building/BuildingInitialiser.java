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
     * @throws IOException         if an IOException is encountered when calling any
     *                             IO methods
     * @throws FileFormatException if the file format of the given file
     *                             is invalid according to the rules above
     */
    public static List<Building> loadBuildings(String filename) throws
            IOException, FileFormatException {
        try {
            List<Building> buildings = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            BufferedReader temp = new BufferedReader(new FileReader(filename));
            int buildingNumber = 0;
            String line = "";

            while ((line = temp.readLine()) != null) {
                try {
                    int i = Integer.parseInt(line);
                    buildingNumber += 1;
                } catch (NumberFormatException e) {
                    continue;
                }
            }

            for (int i = 0; i < buildingNumber; i++) {
                String name = reader.readLine();
                int floorNumber = Integer.parseInt(reader.readLine());
                Building building = new Building(name);

                for (int j = 0; j < floorNumber; j++) {
                    Floor floor = BuildingInitialiser.readFloor(reader);
                    building.addFloor(floor);
                }
                buildings.add(building);
            }
            return buildings;

        } catch (IOException e) {
            throw new IOException();
        } catch (Exception ex) {
            throw new FileFormatException();
        }



    }




    private static Floor readFloor(BufferedReader r) throws IOException,
            FileFormatException {
        try {
            String temp = r.readLine();
            String[] information = temp.split(":");
            int number = Integer.parseInt(information[0]);
            int width = Integer.parseInt(information[1]);
            int length = Integer.parseInt(information[2]);
            int roomNumber = Integer.parseInt(information[3]);

            Floor floor = new Floor(number, width, length);

            for (int i = 0; i < roomNumber; i++) {
                Room tempRoom = BuildingInitialiser.readRoom(r);
                floor.addRoom(tempRoom);
            }

            return floor;

        } catch (IOException e) {
            throw new IOException();
        } catch (Exception ex) {
            throw new FileFormatException();
        }

    }

    private static Room readRoom(BufferedReader r) throws IOException,
            FileFormatException {

        try {
            String temp = r.readLine();
            String[] information = temp.split(":");
            RoomType type = null;
            int numner = Integer.parseInt(information[0]);
            int area = Integer.parseInt(information[2]);
            int sensorNumber = Integer.parseInt(information[3]);

            if (information[1] == "STUDY") {
                type = RoomType.STUDY;
            } else if (information[1] == "LABORATORY") {
                type = RoomType.LABORATORY;
            } else if (information[1] == "OFFICE") {
                type = RoomType.OFFICE;
            }

            Room room = new Room(numner, type, area);
            List<HazardSensor> hazardSensors = new ArrayList<>();
            Map<HazardSensor, Integer> hazardPair = new HashMap<>();

            for (int i = 0; i < sensorNumber; i++) {
                String[] sensorInfo = BuildingInitialiser.readSensor(r);
                Sensor sensor = null;
                int weight = 0;

                if (information.length == 5 &&
                        information[4] == "WeightingBased") {

                    String[] last = sensorInfo[sensorInfo.length - 1].
                            split("@");
                    sensorInfo[sensorInfo.length - 1] = last[0];
                    weight = Integer.parseInt(last[1]);
                }


                String[] readingString = sensorInfo[1].split(",");
                int[] readings = new int[readingString.length];

                for (int j = 0; j < readingString.length; j++) {
                    readings[j] = Integer.parseInt(readingString[j]);
                }

                if (sensorInfo[0] == "TemperatureSensor") {
                    sensor = new TemperatureSensor(readings);
                } else if (sensorInfo[0] == "NoiseSensor") {
                    int updateFrequency = Integer.parseInt(sensorInfo[2]);
                    sensor = new NoiseSensor(readings, updateFrequency);
                } else if (sensorInfo[0] == "OccupancySensor") {
                    int updateFrequency = Integer.parseInt(sensorInfo[2]);
                    int capacity = Integer.parseInt(sensorInfo[3]);
                    sensor = new OccupancySensor(readings, updateFrequency,
                            capacity);
                } else if (sensorInfo[0] == "CarbonDioxideSensor") {
                    int updateFrequency = Integer.parseInt(sensorInfo[2]);
                    int idealValue = Integer.parseInt(sensorInfo[3]);
                    int variationLimit = Integer.parseInt(sensorInfo[4]);
                    sensor = new CarbonDioxideSensor(readings,
                            updateFrequency, idealValue, variationLimit);
                } else {
                    throw new FileFormatException();
                }

                room.addSensor(sensor);

                if (information.length == 5) {
                    HazardSensor hazardSensor = (HazardSensor) sensor;
                    if (information[4] == "RuleBased") {
                        hazardSensors.add(hazardSensor);
                    } else {
                        hazardPair.put(hazardSensor, weight);
                    }

                }
            }

            if (information.length == 5) {
                HazardEvaluator evaluator;
                if (information[4] == "RuleBased") {
                    evaluator = new RuleBasedHazardEvaluator(hazardSensors);
                } else {
                    evaluator = new WeightingBasedHazardEvaluator(hazardPair);
                }
                room.setHazardEvaluator(evaluator);
            }
            return room;
        } catch (IOException e) {
            throw new IOException();
        } catch (Exception exception) {
            throw new FileFormatException();
        }
    }

    private static String[] readSensor(BufferedReader r) throws IOException {
        try {
            String temp = r.readLine();
            String[] information = temp.split(":");
            return information;
        } catch (IOException e) {
            throw new IOException();
        }

    }


}
