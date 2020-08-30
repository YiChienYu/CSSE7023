package bms.sensors;

import java.util.Arrays;

public class OccupancySensor extends TimedSensor implements HazardSensor{

    private int capacity;

    public OccupancySensor(int[] sensorReadings, int updateFrequency, int capacity) throws IllegalArgumentException{
        super(sensorReadings, updateFrequency);

        if(capacity < 0){
            throw new IllegalArgumentException();
        }
        this.capacity = capacity;

    }

    public int getCapacity(){
        return capacity;
    }

    @Override
    public int getHazardLevel(){
        if(getCurrentReading() > capacity){
            return 100;
        }
        return 0;
    }

    @Override
    public String toString(){
        String commaSeparated = Arrays.toString(sensorReadings);
        commaSeparated = commaSeparated.substring(1, commaSeparated.length()-1);
        commaSeparated = commaSeparated.replaceAll("\\s+","");
        return String.format("TimedSensor: freq=%d, readings=%s, type=OccupancySensor, capacity=%d",
                this.getUpdateFrequency(), commaSeparated, this.capacity);
    }
}
