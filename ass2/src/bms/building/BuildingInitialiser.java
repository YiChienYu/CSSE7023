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


    private static Room readRoom(BufferedReader r) throws IOException,
            FileFormatException {

        BufferedReader reader = r;


        String[] temp = {RoomType.LABORATORY.toString(),
                RoomType.OFFICE.toString(), RoomType.STUDY.toString()};
        List<String> concreteType = Arrays.asList(temp);

        try {
            Room room = null;
            String informationString = reader.readLine();
            String[] roomInformation = informationString.split(":");
            int number = Integer.parseInt(roomInformation[0]);
            String type = roomInformation[1];
            RoomType roomType= null;
            int area = Integer.parseInt(roomInformation[2]);
            int sensorNumber = Integer.parseInt(roomInformation[3]);

            if (!concreteType.contains(type)) {
                throw new FileFormatException();
            }

            if (type == "LABORATORY") {
                roomType = RoomType.LABORATORY;
            } else if (type == "OFFICE") {
                roomType = RoomType.OFFICE;
            } else if (type == "STUDY") {
                roomType = RoomType.STUDY;
            }

            room = new Room(number, roomType, area);

            for (int i = 0; i < sensorNumber; i++) {
                Sensor sensor = BuildingInitialiser.readSensor(reader);
                room.addSensor(sensor);
            }

            if (roomInformation.length == 5) {
                String evaluatorType = roomInformation[4];
                List<Sensor> sensors = room.getSensors();
                HazardEvaluator evaluator = null;

                if (evaluatorType == "RuleBased") {
                    List<HazardSensor> hazardSensors = new ArrayList<>();
                    for (Sensor s : sensors) {
                        hazardSensors.add((HazardSensor) s);
                    }

                    evaluator = new RuleBasedHazardEvaluator(hazardSensors);
                } else if (evaluatorType == "WeightingBased") {
                    Map<HazardSensor, Integer> mapOfsensors = new HashMap<>();
                }
            }

        } catch (IOException e) {
            throw new IOException();
        } catch (Exception e) {
            throw new FileFormatException();
        }



    }


    private static Sensor readSensor(BufferedReader reader) throws IOException,
            FileFormatException {

        String[] temp = {"NoiseSensor", "OccupancySensor",
                "CarbonDioxideSensor", "TemperatureSensor"};
        List<String> concreteType = Arrays.asList(temp);

        try{
            Sensor sensor = null;
            String informationString = reader.readLine();
            String[] sensorInformation = informationString.split(":");

            String last = sensorInformation[sensorInformation.length - 1];

            if (last.contains("@")) {
                int index = last.indexOf("@");
                StringBuilder builder = new StringBuilder("");

                for (int i = 0; i < index; i++) {
                    builder.append(last.charAt(i));
                }

                String trueLast = builder.toString();

                sensorInformation[sensorInformation.length - 1] = trueLast;
            }

            String readingString = sensorInformation[1];
            String[] splitReading = readingString.split(",");
            int[] readings = new int[splitReading.length];

            for (int i = 0; i < splitReading.length; i++) {
                int tempInt = Integer.parseInt(splitReading[i]);
                readings[i] = tempInt;
            }


            if (!concreteType.contains(sensorInformation[0])) {
                throw new FileFormatException();
            }

            if (sensorInformation[0] == "TemperatureSensor") {
                sensor = new TemperatureSensor(readings);
            } else if (sensorInformation[0] == "NoiseSensor") {
                int updateFrequency = Integer.parseInt(sensorInformation[2]);
                sensor = new NoiseSensor(readings, updateFrequency);
            } else if (sensorInformation[0] == "OccupancySensor") {
                int updateFrequency = Integer.parseInt(sensorInformation[2]);
                int capacity = Integer.parseInt(sensorInformation[3]);
                sensor = new OccupancySensor(readings, updateFrequency,
                        capacity);
            } else if (sensorInformation[0] == "CarbonDioxideSensor") {
                int updateFrequency = Integer.parseInt(sensorInformation[2]);
                int idealValue = Integer.parseInt(sensorInformation[3]);
                int variation = Integer.parseInt(sensorInformation[4]);
                sensor = new CarbonDioxideSensor(readings, updateFrequency,
                        idealValue, variation);
            }

            return sensor;
        } catch (IOException e) {
            throw new IOException();
        } catch (Exception e) {
            throw new FileFormatException();
        }
    }
}
