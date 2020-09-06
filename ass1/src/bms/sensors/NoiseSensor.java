package bms.sensors;

import java.lang.Math;

public class NoiseSensor extends TimedSensor implements HazardSensor{

    public NoiseSensor(int[] sensorReadings, int updateFrequency){
        super(sensorReadings, updateFrequency);

    }
    public double calculateRelativeLoudness(){
        return Math.pow(2, (this.getCurrentReading()-70.0/10.0));
    }

    @Override
    public int getHazardLevel(){
        float relativeLoudness = ((float)this.calculateRelativeLoudness())*100;
        int roundRelativeLoudness = (int) Math.floor(relativeLoudness);
        if(roundRelativeLoudness > 100){
            return 100;
        }
        return roundRelativeLoudness;
    }

    @Override
    public String toString(){
        return super.toString() + ", type=NoiseSensor";
    }
}