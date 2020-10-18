package bms.building;

import bms.exceptions.FileFormatException;
import junit.framework.TestCase;
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
            buildings = BuildingInitialiser.loadBuildings("saves/uqstlucia.txt");
        } catch (IOException ioe) {
            throw new IOException();
        } catch (Exception e) {
            throw new FileFormatException();
        }
    }

    @Test (expected = FileFormatException.class)
    public void  floorsNotMatch() throws IOException, FileFormatException {
            buildings = BuildingInitialiser.loadBuildings("saves/1.txt");
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
    public void  maintenanceRoomNotExist() throws IOException, FileFormatException {
        buildings = BuildingInitialiser.loadBuildings("saves/4.txt");
    }

    @Test (expected = FileFormatException.class)
    public void  maintenanceNull() throws IOException, FileFormatException {
        buildings = BuildingInitialiser.loadBuildings("saves/5.txt");
    }

    @Test (expected = FileFormatException.class)
    public void  maintenanceEmptyOrder() throws IOException, FileFormatException {
        buildings = BuildingInitialiser.loadBuildings("saves/6.txt");
    }

    @Test (expected = FileFormatException.class)
    public void  maintenanceRoomTwice() throws IOException, FileFormatException {
        buildings = BuildingInitialiser.loadBuildings("saves/7.txt");
    }

    @Test (expected = FileFormatException.class)
    public void  maintenanceRoomNotInFloor() throws IOException, FileFormatException {
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
}