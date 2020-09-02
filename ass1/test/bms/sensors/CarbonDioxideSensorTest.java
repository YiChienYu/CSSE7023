package bms.sensors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CarbonDioxideSensorTest {

    private CarbonDioxideSensor sensor;

    @Before
    public void setSensor(){
        int[] readings = new int[]{1,3,6};
        sensor = new CarbonDioxideSensor(readings, 2, 10,5);
    }

    @After
    public void tearDown(){
        sensor = null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCarbonDioxideSensor(){
        int[] readings = new int[]{1,3,6};
        CarbonDioxideSensor sensor1 = new CarbonDioxideSensor(readings, 2,11,-10);
    }
}
