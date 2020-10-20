package bms.floor;

import bms.room.Room;
import bms.room.RoomType;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MaintenanceScheduleTest {
    private Room room1 = new Room(101, RoomType.OFFICE, 15);
    private Room room2 = new Room(102, RoomType.STUDY, 6);
    private Room room3 = new Room(103, RoomType.LABORATORY, 15);
    private List<Room> rooms = new ArrayList<>();
    MaintenanceSchedule maintenanceSchedule;

    @Before
    public void setup() {
        rooms.add(room1);
        rooms.add(room2);
        rooms.add(room3);
        maintenanceSchedule = new MaintenanceSchedule(rooms);
    }

    @Test
    public void getMaintenanceTime() {
        Room room = new Room(104, RoomType.OFFICE, 15);
        assertEquals(11,
                maintenanceSchedule.getMaintenanceTime(room), 0.001);
    }

    @Test
    public void getCurrentRoom() {
        assertEquals(101,
                maintenanceSchedule.getCurrentRoom().getRoomNumber());
    }

    @Test
    public void getTimeElapsedCurrentRoom() {
        assertEquals(0,
                maintenanceSchedule.getTimeElapsedCurrentRoom());
    }

    @Test
    public void elapseOneMinute() {
        maintenanceSchedule.elapseOneMinute();
        assertEquals(1,
                maintenanceSchedule.getTimeElapsedCurrentRoom());
        maintenanceSchedule.elapseOneMinute();
        assertEquals(2,
                maintenanceSchedule.getTimeElapsedCurrentRoom());
        maintenanceSchedule.elapseOneMinute();
        maintenanceSchedule.elapseOneMinute();
        maintenanceSchedule.elapseOneMinute();
        maintenanceSchedule.elapseOneMinute();
        maintenanceSchedule.elapseOneMinute();
        maintenanceSchedule.elapseOneMinute();
        maintenanceSchedule.elapseOneMinute();
        maintenanceSchedule.elapseOneMinute();
        assertEquals(10,
                maintenanceSchedule.getTimeElapsedCurrentRoom());
        maintenanceSchedule.elapseOneMinute();
        assertEquals(11,
                maintenanceSchedule.getTimeElapsedCurrentRoom());
        maintenanceSchedule.elapseOneMinute();
        assertEquals(0,
                maintenanceSchedule.getTimeElapsedCurrentRoom());
    }

    @Test
    public void skipCurrentMaintenance() {
        assertEquals(101,
                maintenanceSchedule.getCurrentRoom().getRoomNumber());
        maintenanceSchedule.elapseOneMinute();
        assertEquals(1,
                maintenanceSchedule.getTimeElapsedCurrentRoom());
        maintenanceSchedule.skipCurrentMaintenance();
        assertEquals(102,
                maintenanceSchedule.getCurrentRoom().getRoomNumber());
        assertEquals(0,
                maintenanceSchedule.getTimeElapsedCurrentRoom());
    }

    @Test
    public void testToString() {
        maintenanceSchedule.elapseOneMinute();
        assertEquals("MaintenanceSchedule: " + "currentRoom=#101, " +
                "currentElapsed=1", maintenanceSchedule.toString());
    }

    @Test
    public void encode() {
        assertEquals("101,102,103", maintenanceSchedule.encode());
    }
}