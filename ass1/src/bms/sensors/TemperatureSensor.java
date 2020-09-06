package bms.sensors;

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
        return super.toString() + ", type=TemperatureSensor";
    }
}
