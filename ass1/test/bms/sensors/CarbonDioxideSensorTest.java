package bms.sensors;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CarbonDioxideSensorTest {

    private int[] readings;
    private CarbonDioxideSensor sensor;
    private CarbonDioxideSensor sensor1;

    @Before
    public void setSensor() {
        readings = new int[]{100,1500,4000,5000};
        sensor1 = new CarbonDioxideSensor(readings, 2, 11, 7);
    }

    @After
    public void tearDown() throws Exception {
        sensor = null;
        sensor1 = null;
        readings = null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeVariationLimit() {
        sensor = new CarbonDioxideSensor(readings, 2,11,-7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeIdealValue() {
        sensor = new CarbonDioxideSensor(readings, 2,-11,7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeDifference() {
    sensor = new CarbonDioxideSensor(readings, 2,1,7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateFrequencyLessThan0() {
        sensor = new CarbonDioxideSensor(readings, 0,11,7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateFrequencyLargerThan5() {
        sensor = new CarbonDioxideSensor(readings, 6,11,7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSensorReadings() {
        sensor = new CarbonDioxideSensor(null, 2,11,7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void sensorReadingsNoElement() {
        int[] noSensorReadings = new int[]{};
        sensor = new CarbonDioxideSensor(noSensorReadings, 2,11,7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void sensorReadingsNegativeElement() {
        int[] negativeSensorReadings = new int[]{100, 200, -1000, 6000};
        sensor = new CarbonDioxideSensor(negativeSensorReadings, 2, 11, 7);
    }

    @Test
    public void getVariationLimit() {
        assertEquals(7, sensor1.getVariationLimit());
    }

    @Test
    public void getIdealValue() {
        assertEquals(11, sensor1.getIdealValue());
    }

    @Test
    public void getHazardLevel() {
        assertEquals(0, sensor1.getHazardLevel());
        sensor1.elapseOneMinute();
        sensor1.elapseOneMinute();
        assertEquals(25, sensor1.getHazardLevel());
        sensor1.elapseOneMinute();
        sensor1.elapseOneMinute();
        assertEquals(50, sensor1.getHazardLevel());
        sensor1.elapseOneMinute();
        sensor1.elapseOneMinute();
        assertEquals(100, sensor1.getHazardLevel());
        sensor1.elapseOneMinute();
        sensor1.elapseOneMinute();
        assertEquals(0, sensor1.getHazardLevel());
    }

    @Test
    public void toStringTest() {
        assertEquals("TimedSensor: freq=2, readings=100,1500,4000," +
                        "5000, type=CarbonDioxideSensor, idealPPM=11," +
                        " varLimit=7",
                sensor1.toString());
    }

}
