package bms.sensors;

import java.util.Arrays;

public class OccupancySensor extends TimedSensor implements HazardSensor{

    private int capacity;

    public OccupancySensor(int[] sensorReadings, int updateFrequency,
                           int capacity) throws IllegalArgumentException{
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
        if(this.getCurrentReading() >= capacity){
            return 100;
        }
        float ratio = ((float) this.getCurrentReading())/capacity;
        int roundRatio = (int) Math.floor(ratio);
        return roundRatio;
    }

    @Override
    public String toString(){
        return super.toString() + String.format(", type=OccupancySensor, " +
                "capacity=%d", capacity);
    }
}
