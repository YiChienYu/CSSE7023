package bms.building;

import bms.exceptions.FileFormatException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class BuildingInitialiserTest {

    private List<Building> buildings;

    @Before
    public void setBuildings() {
        buildings = null;
    }

    @Test
    public void successful() throws IOException, FileFormatException {
        try {
            buildings =
                    BuildingInitialiser.
                            loadBuildings("saves/uqstlucia.txt");
        } catch (IOException ioe) {
            throw new IOException();
        } catch (FileFormatException e) {
            throw new FileFormatException();
        }
    }

    @Test (expected = FileFormatException.class)
    public void  floorsNotMatch() throws IOException, FileFormatException {
            buildings =
                    BuildingInitialiser.loadBuildings("saves/1.txt");
    }

    @Test (expected = FileFormatException.class)
    public void  roomsNotMatch() throws IOException, FileFormatException {
        buildings = BuildingInitialiser.loadBuildings("saves/2.txt");
    }

    @Test (expected = FileFormatException.class)
    public void  sensorsNotMatch() throws IOException, FileFormatException {
        buildings = BuildingInitialiser.loadBuildings("saves/3.txt");
    }

    @Test (expected = FileFormatException.class)
    public void  maintenanceRoomNotExist() throws IOException,
            FileFormatException {
        buildings = BuildingInitialiser.loadBuildings("saves/4.txt");
    }

    @Test (expected = FileFormatException.class)
    public void  maintenanceNull() throws IOException, FileFormatException {
        buildings = BuildingInitialiser.loadBuildings("saves/5.txt");
    }

    @Test (expected = FileFormatException.class)
    public void  maintenanceEmptyOrder() throws IOException,
            FileFormatException {
        buildings = BuildingInitialiser.loadBuildings("saves/6.txt");
    }

    @Test (expected = FileFormatException.class)
    public void  maintenanceRoomTwice() throws IOException,
            FileFormatException {
        buildings = BuildingInitialiser.loadBuildings("saves/7.txt");
    }

    @Test (expected = FileFormatException.class)
    public void  maintenanceRoomNotInFloor() throws IOException,
            FileFormatException {
        buildings = BuildingInitialiser.loadBuildings("saves/8.txt");
    }

    @Test (expected = FileFormatException.class)
    public void  floorInvalidLength() throws IOException, FileFormatException {
        buildings = BuildingInitialiser.loadBuildings("saves/9.txt");
    }

    @Test (expected = FileFormatException.class)
    public void  floorInvalidWidth() throws IOException, FileFormatException {
        buildings = BuildingInitialiser.loadBuildings("saves/10.txt");
    }

    @Test (expected = FileFormatException.class)
    public void  floorNoBelow() throws IOException, FileFormatException {
        buildings = BuildingInitialiser.loadBuildings("saves/11.txt");
    }

    @Test (expected = FileFormatException.class)
    public void  floorTooLarge() throws IOException, FileFormatException {
        buildings = BuildingInitialiser.loadBuildings("saves/12.txt");
    }

    @Test (expected = FileFormatException.class)
    public void  floorDuplicateRoom() throws IOException, FileFormatException {
        buildings = BuildingInitialiser.loadBuildings("saves/13.txt");
    }

    @Test (expected = FileFormatException.class)
    public void  roomTooLarge() throws IOException, FileFormatException {
        buildings = BuildingInitialiser.loadBuildings("saves/14.txt");
    }

    @Test (expected = FileFormatException.class)
    public void  invalidRoomType() throws IOException, FileFormatException {
        buildings = BuildingInitialiser.loadBuildings("saves/15.txt");
    }

    @Test (expected = FileFormatException.class)
    public void  invalidRoomArea() throws IOException, FileFormatException {
        buildings = BuildingInitialiser.loadBuildings("saves/16.txt");
    }

    @Test (expected = FileFormatException.class)
    public void  invalidEvaluatorType() throws IOException,
            FileFormatException {
        buildings = BuildingInitialiser.loadBuildings("saves/17.txt");
    }

    @Test (expected = FileFormatException.class)
    public void  invalidWeight() throws IOException, FileFormatException {
        buildings = BuildingInitialiser.loadBuildings("saves/18.txt");
    }

    @Test (expected = FileFormatException.class)
    public void  duplicateSensor() throws IOException, FileFormatException {
        buildings = BuildingInitialiser.loadBuildings("saves/19.txt");
    }

    @Test (expected = FileFormatException.class)
    public void  invalidSensorType() throws IOException, FileFormatException {
        buildings = BuildingInitialiser.loadBuildings("saves/20.txt");
    }

    @Test (expected = FileFormatException.class)
    public void  updateFrequencyLessOne() throws IOException,
            FileFormatException {
        buildings = BuildingInitialiser.loadBuildings("saves/21.txt");
    }

    @Test (expected = FileFormatException.class)
    public void  updateFrequencyGreaterFive() throws IOException,
            FileFormatException {
        buildings = BuildingInitialiser.loadBuildings("saves/22.txt");
    }

    @Test (expected = FileFormatException.class)
    public void  variationGreaterThanIdealValue() throws IOException,
            FileFormatException {
        buildings = BuildingInitialiser.loadBuildings("saves/23.txt");
    }

    @Test (expected = FileFormatException.class)
    public void  negativeNumberOfFloor() throws IOException,
            FileFormatException {
        buildings = BuildingInitialiser.loadBuildings("saves/24.txt");
    }

    @Test (expected = FileFormatException.class)
    public void  negativeNumberOfRoom() throws IOException,
            FileFormatException {
        buildings = BuildingInitialiser.loadBuildings("saves/25.txt");
    }

    @Test (expected = FileFormatException.class)
    public void  negativeNumberOfSensor() throws IOException,
            FileFormatException {
        buildings = BuildingInitialiser.loadBuildings("saves/26.txt");
    }

    @Test (expected = FileFormatException.class)
    public void  negativeSensorReadings() throws IOException,
            FileFormatException {
        buildings = BuildingInitialiser.loadBuildings("saves/27.txt");
    }

    @Test (expected = FileFormatException.class)
    public void  negativeCapacity() throws IOException,
            FileFormatException {
        buildings = BuildingInitialiser.loadBuildings("saves/28.txt");
    }

    @Test (expected = FileFormatException.class)
    public void  negativeIdealCO2Level() throws IOException,
            FileFormatException {
        buildings = BuildingInitialiser.loadBuildings("saves/29.txt");
    }

    @Test (expected = FileFormatException.class)
    public void  negativeVariationLimit() throws IOException,
            FileFormatException {
        buildings = BuildingInitialiser.loadBuildings("saves/30.txt");
    }

    @Test (expected = FileFormatException.class)
    public void  floorLessThanOne() throws IOException,
            FileFormatException {
        buildings = BuildingInitialiser.loadBuildings("saves/31.txt");
    }

    @Test (expected = FileFormatException.class)
    public void parseFail() throws IOException,
            FileFormatException {
        buildings = BuildingInitialiser.loadBuildings("saves/32.txt");
    }

    @Test (expected = FileFormatException.class)
    public void emptyLine() throws IOException,
            FileFormatException {
        buildings = BuildingInitialiser.loadBuildings("saves/33.txt");
    }
}