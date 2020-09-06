package bms.sensors;

import bms.util.TimedItem;
import bms.util.TimedItemManager;
import java.util.Arrays;

public abstract class TimedSensor implements TimedItem, Sensor{

    private int updateFrequency;
    private int[] sensorReadings;
    private int currentReading;
    private int currentPosition;
    private int timeElapsed;


    public TimedSensor(int[] sensorReadings, int updateFrequency) throws IllegalArgumentException{
        if(updateFrequency>5 || updateFrequency < 1 || sensorReadings == null ||
                sensorReadings.length < 1){
            throw new IllegalArgumentException();
        }
        for(int i=0;i< sensorReadings.length;i++){
            if(sensorReadings[i] <0){
                throw new IllegalArgumentException();
            }
        }
        this.updateFrequency = updateFrequency;
        this.sensorReadings = sensorReadings;
        this.currentPosition = 0;
        this.currentReading = sensorReadings[currentPosition];
        this.timeElapsed = 0;
        TimedItemManager.getInstance().registerTimedItem(this);

    }

    @Override
    public int getCurrentReading(){
        return currentReading;
    }

    public int getTimeElapsed(){
        return timeElapsed;
    }

    public int getUpdateFrequency(){
        return updateFrequency;

    }

    @Override
    public void elapseOneMinute(){
        timeElapsed++;
        int remainder = (this.getTimeElapsed()) % (this.getUpdateFrequency());
        if(remainder == 0 && timeElapsed!= 0){
            if(currentPosition == (sensorReadings.length -1)){
                currentPosition = 0;
            }else{
                currentPosition+=1;
            }
            currentReading = sensorReadings[currentPosition];
        }
    }

    @Override
    public String toString(){
        String commaSeparated = Arrays.toString(sensorReadings);
        commaSeparated = commaSeparated.substring(1, commaSeparated.length()-1);
        commaSeparated = commaSeparated.replaceAll("\\s+","");
        return String.format("TimedSensor: freq=%d, readings=%s",
                updateFrequency, commaSeparated);
    }
}
