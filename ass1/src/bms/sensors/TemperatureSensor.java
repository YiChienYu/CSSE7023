package bms.sensors;

import java.util.Arrays;

public class TemperatureSensor extends TimedSensor implements HazardSensor{

    public TemperatureSensor(int[] sensorReadings){
        super(sensorReadings, 1);

    }

    @Override
    public int getHazardLevel() {
        if(this.getCurrentReading() >= 68){
            return 100;
        }
        return 0;
    }

    @Override
    public String toString(){
        String commaSeparated = Arrays.toString(sensorReadings);
        commaSeparated = commaSeparated.substring(1, commaSeparated.length()-1);
        commaSeparated = commaSeparated.replaceAll("\\s+","");
        return String.format("TimedSensor: freq=%d, readings=%s, type=TemperatureSensor", 1, commaSeparated);
    }
}
