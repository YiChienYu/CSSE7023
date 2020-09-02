package bms.sensors;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CarbonDioxideSensorTest {

    private int[] readings;
    private CarbonDioxideSensor sensor;

    @Before
    public void setSensor(){
        readings = new int[]{100,1500,4000,5000};
    }

    @After
    public void tearDown(){
        sensor = null;
        readings = null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeVariationLimit(){
        sensor = new CarbonDioxideSensor(readings, 2,11,-7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeIdealValue(){
        sensor = new CarbonDioxideSensor(readings, 2,-11,7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeDifference(){
    sensor = new CarbonDioxideSensor(readings, 2,1,7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateFrequencyLessThan0(){
        sensor = new CarbonDioxideSensor(readings, 0,11,7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateFrequencyLargerThan5(){
        sensor = new CarbonDioxideSensor(readings, 6,11,7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSensorReadings(){
        sensor = new CarbonDioxideSensor(null, 2,11,7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void sensorReadingsNoElement(){
        int[] noSensorReadings = new int[]{};
        sensor = new CarbonDioxideSensor(noSensorReadings, 2,11,7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void sensorReadingsNegativeElement(){
        int[] noSensorReadings = new int[]{100, 200, -1000, 6000};
        sensor = new CarbonDioxideSensor(noSensorReadings, 2,11,7);
    }

    @Test
    public void getVariationLimit(){
        sensor = new CarbonDioxideSensor(readings, 2,11,7);
        assertEquals(7, sensor.getVariationLimit());
    }

    @Test
    public void getIdealValue(){
        sensor = new CarbonDioxideSensor(readings, 2,11,7);
        assertEquals(11, sensor.getIdealValue());
    }

    @Test
    public void getHazardLevelZero(){
        sensor = new CarbonDioxideSensor(readings, 2, 11, 7);
        assertEquals(0, sensor.getHazardLevel());
    }

    @Test
    public void getHazardLevelTwentyFive(){
        sensor = new CarbonDioxideSensor(readings, 2, 11, 7);
        sensor.elapseOneMinute();
        sensor.elapseOneMinute();
        assertEquals(25, sensor.getHazardLevel());
    }

    @Test
    public void getHazardLevelFifty(){
        sensor = new CarbonDioxideSensor(readings, 3, 11, 7);
        sensor.elapseOneMinute();
        sensor.elapseOneMinute();
        sensor.elapseOneMinute();
        sensor.elapseOneMinute();
        sensor.elapseOneMinute();
        sensor.elapseOneMinute();
        assertEquals(50, sensor.getHazardLevel());
    }

    @Test
    public void getHazardLevelHundred(){
        sensor = new CarbonDioxideSensor(readings, 2, 11, 7);
        sensor.elapseOneMinute();
        sensor.elapseOneMinute();
        sensor.elapseOneMinute();
        sensor.elapseOneMinute();
        sensor.elapseOneMinute();
        sensor.elapseOneMinute();
        assertEquals(100, sensor.getHazardLevel());
    }

}
