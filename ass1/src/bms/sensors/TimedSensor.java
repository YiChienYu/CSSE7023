package bms.sensors;
import bms.util.TimedItem;

public abstract class TimedSensor extends Object implements TimedItem, Sensor{

    private int updateFrequency;
    int[] sensorReadings;

    public TimedSensor(int[] sensorReadings, int updateFrequency) throws IllegalArgumentException{
        if(updateFrequency>5 || updateFrequency < 1 || sensorReadings == null || sensorReadings.length < 1){
            throw new IllegalArgumentException();
        }
        for(int i=0;i< sensorReadings.length;i++){
            if(sensorReadings[i] <0){
                throw new IllegalArgumentException();
            }
        }
        this.updateFrequency = updateFrequency;
        this.sensorReadings = sensorReadings;
    }
}
