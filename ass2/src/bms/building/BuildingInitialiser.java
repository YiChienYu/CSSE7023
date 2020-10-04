package bms.building;

import bms.exceptions.FileFormatException;
import bms.hazardevaluation.HazardEvaluator;
import bms.hazardevaluation.RuleBasedHazardEvaluator;
import bms.room.Room;
import bms.room.RoomType;
import bms.sensors.*;

import java.io.BufferedReader;
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

    }


    private static Room readRoom (BufferedReader r) throws IOException,
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

            for (int i = 0; i < sensorNumber; i++) {
                String[] sensorInfo = BuildingInitialiser.readSensor(r);
                Sensor sensor = null;











                String[] readingString = sensorInfo[1].split(",");
                int[] readings = new int[readingString.length];

                if (information.length == 4 || (information.length == 5 &&
                        information[4] == "RuleBased")) {

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
                    if (information.length == 5 &&
                            information[4] == "RuleBased") {
                        HazardSensor hazardSensor = (HazardSensor) sensor;
                        hazardSensors.add(hazardSensor);
                    }

                } else {

                }
            }

            if ((information.length == 5 &&
                    information[4] == "RuleBased")) {
                HazardEvaluator evaluator =
                        new RuleBasedHazardEvaluator(hazardSensors);
                room.setHazardEvaluator(evaluator);
            }



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
