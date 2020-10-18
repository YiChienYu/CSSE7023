package bms.floor;

import bms.room.Room;
import bms.room.RoomState;
import bms.room.RoomType;
import bms.util.TimedItem;
import bms.util.Encodable;
import bms.util.TimedItemManager;

import java.util.List;

public class MaintenanceSchedule implements TimedItem, Encodable {
    /**
     * list of rooms
     */
    private List<Room> rooms;

    /**
     * current room
     */
    private Room currentRoom;

    /**
     * The amount of time in minutes that the sensor has been running
     * (according to the system, not real life).
     */
    private int timeElapsed;

    /**
     * The index of next room
     */
    private int indexOfNextRoom;

    /**
     * Creates a new maintenance schedule for a floor's list of rooms.
     * <p>
     * In this constructor, the new maintenance schedule should be registered as
     * a timed item with the timed item manager.
     * <p>
     * The first room in the given order should be set to "in maintenance",
     * see Room.setMaintenance(boolean).
     *
     * @param roomOrder list of rooms on which to perform maintenance, in order
     * @requires roomOrder != null && roomOrder.size() > 0
     */
    public MaintenanceSchedule(List<Room> roomOrder) {
        this.rooms = roomOrder;
        this.timeElapsed = 0;
        this.currentRoom = rooms.get(0);
        this.indexOfNextRoom = 1;

        TimedItemManager.getInstance().registerTimedItem(this);
        currentRoom.setMaintenance(true);
    }

    /**
     * Returns the time taken to perform maintenance on the given room,
     * in minutes.
     * <p>
     * The maintenance time for a given room depends on its size
     * (larger rooms take longer to maintain) and its room type
     * (rooms with more furniture and equipment take take longer to maintain).
     *
     * @param room  room on which to perform maintenance
     * @return room's maintenance time in minutes
     */
    public int getMaintenanceTime(Room room) {
        double minimumMinute = 5;
        double multiplier = 1;

        if (room.getType() == RoomType.OFFICE) {
            multiplier = 1.5;
        } else if (room.getType() == RoomType.LABORATORY) {
            multiplier = 2;
        }

        if (room.getArea() > Room.getMinArea()) {
            double difference = room.getArea() - Room.getMinArea();
            minimumMinute += (0.2 * difference);
        }
        return (int) Math.round(minimumMinute * multiplier);
    }

    /**
     * Returns the room which is currently in the process of being maintained.
     *
     * @return room currently in maintenance
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Returns the number of minutes that have elapsed while maintaining
     * the current room (getCurrentRoom()).
     *
     * @return time elapsed maintaining current room
     */
    public int getTimeElapsedCurrentRoom() {
        return timeElapsed;
    }

    /**
     * Progresses the maintenance schedule by one minute.
     * <p>
     * If the room currently being maintained has a room state of EVACUATE,
     * then no action should occur.
     */
    @Override
    public void elapseOneMinute() {
        if (currentRoom.maintenanceOngoing() == true &&
                currentRoom.evaluateRoomState() != RoomState.EVACUATE) {
            if (this.timeElapsed >= getMaintenanceTime(currentRoom)) {
                currentRoom.setMaintenance(false);
                if (rooms.size() != 1) {
                    currentRoom = rooms.get(indexOfNextRoom);
                } else {
                    currentRoom = rooms.get(0);
                }
                if (indexOfNextRoom == (rooms.size() -1)) {
                    indexOfNextRoom = 0;
                } else {
                    indexOfNextRoom += 1;
                }
                currentRoom.setMaintenance(true);
                this.timeElapsed = 0;
            } else {
                this.timeElapsed += 1;
            }
        }
    }

    /**
     * Stops the in-progress maintenance of the current room and progresses
     * to the next room.
     */
    public void skipCurrentMaintenance() {
        currentRoom.setMaintenance(false);
        if (rooms.size() != 1) {
            currentRoom = rooms.get(indexOfNextRoom);
        } else {
            currentRoom = rooms.get(0);
        }
        if (indexOfNextRoom == (rooms.size() -1)) {
            indexOfNextRoom = 0;
        } else {
            indexOfNextRoom += 1;
        }
        currentRoom.setMaintenance(true);
        this.timeElapsed = 0;
    }

    /**
     * Returns the human-readable string representation of
     * this maintenance schedule.
     *
     * @return string representation of this maintenance schedule
     */
    @Override
    public String toString() {
        return String.format("MaintenanceSchedule: currentRoom=#%s, " +
                "currentElapsed=%s", currentRoom.getRoomNumber(), timeElapsed);
    }

    /**
     * Returns the machine-readable string representation of
     * this maintenance schedule.
     *
     * @return encoded string representation of this maintenance schedule
     */
    @Override
    public String encode() {
        String temp = "";

        for (int i = 0; i < rooms.size(); i++) {
            if (i == rooms.size() -1) {
                temp += String.format("%s", rooms.get(i).getRoomNumber());
            } else {
                temp += String.format("%s,", rooms.get(i).getRoomNumber());
            }
        }

        return temp;
    }
}
