package bms.sensors;

import java.util.Arrays;

public class CarbonDioxideSensor extends TimedSensor implements HazardSensor{

    private int idealValue;
    private int variationLimit;

    public CarbonDioxideSensor(int[] sensorReadings, int updateFrequency,
                               int idealValue, int variationLimit)
                                throws IllegalArgumentException{
        super(sensorReadings, updateFrequency);
        if(idealValue <= 0 || variationLimit <= 0 ||
                (idealValue - variationLimit) < 0){
            throw new IllegalArgumentException();
        }
        this.idealValue = idealValue;
        this.variationLimit = variationLimit;

    }

    public int getVariationLimit(){
        return variationLimit;
    }

    public int getIdealValue(){
        return idealValue;
    }

    @Override
    public int getHazardLevel() {
        if (0 <= this.getCurrentReading() && this.getCurrentReading() < 1000) {
            return 0;
        }
        else if(1000 <= this.getCurrentReading() &&
                this.getCurrentReading() < 2000){
            return 25;
        }
        else if(2000 <= this.getCurrentReading() &&
                this.getCurrentReading() < 5000){
            return 50;
        }else{
            return 100;
        }
    }

    @Override
    public String toString(){
        return super.toString() + String.format(", type=CarbonDioxideSensor, " +
                "idealPPM=%d, varLimit=%d", idealValue, variationLimit);
    }
}
