package bms.util;

import bms.building.Building;
import bms.floor.Floor;
import bms.room.Room;
import bms.room.RoomState;
import bms.room.RoomType;
import bms.sensors.ComfortSensor;
import bms.sensors.Sensor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility class that provides a recommendation for a study room in a building.
 */
public class StudyRoomRecommender {
    /**
     * Returns a room in the given building that is most suitable
     * for study purposes.
     *
     * @param building building in which to search for a study room
     * @return the most suitable study room in the building; null
     * if there are none
     */
    public static Room recommendStudyRoom(Building building) {
        // The floors of the given building
        List<Floor> floors = building.getFloors();

        // Total number of rooms, for checking if the given building has no room
        List<Room> tempRooms = new ArrayList<>();

        // The room selected to be recommended in the current floor
        Room currentRoom = null;

        // The comfort level of selected room
        int currentLevel = -1;

        // The room selected to be recommended in the previous floor
        Room previousRoom = null;

        // The comfort level of previous selected room
        int previousLevel = -1;

        if (floors.size() == 0) {
            return null;
        }
        for (Floor f : floors) {
            for (Room r : f.getRooms()) {
                tempRooms.add(r);
            }
        }
        if (tempRooms.size() == 0) {
            return null;
        }
        for (int i = 0; i < floors.size(); i++) {
            // Moving to next floor
            previousLevel = currentLevel;
            previousRoom = currentRoom;

            // The comfort level of each floor on the current floor.
            List<Integer> levels = new ArrayList<>();

            List<Room> rooms = floors.get(i).getRooms();

            // Rooms that meet the requirement
            List<Room> roomsAvailable = new ArrayList<>();

            if (rooms.size() != 0) {
                for (Room r : rooms) {
                    int comfortLevel = 0;

                    // Sensors in a room of current floor
                    List<Sensor> sensors = r.getSensors();

                    if (r.evaluateRoomState() != RoomState.EVACUATE &&
                            r.evaluateRoomState() != RoomState.MAINTENANCE &&
                            r.evaluateRoomState() == RoomState.OPEN &&
                            r.getType() == RoomType.STUDY) {
                        roomsAvailable.add(r);
                        for (Sensor s : sensors) {
                            if (s instanceof ComfortSensor) {
                                ComfortSensor comfortSensor = (ComfortSensor) s;
                                comfortLevel += comfortSensor.getComfortLevel();
                            }
                        }

                        if (sensors.size() != 0) {
                            comfortLevel = (int) (currentLevel / sensors.size());
                        }
                        levels.add(comfortLevel);
                    }
                }
            }


            if (roomsAvailable.size() == 0) {
                currentRoom = null;
                currentLevel = -1;
            } else {
                // Find the room with max comfort level
                int max = Collections.max(levels);
                int index = levels.indexOf(max);
                currentLevel = max;
                currentRoom = roomsAvailable.get(index);
            }
            if (i == 0 && currentRoom == null) {
                return null;
            }
            if (i != 0 && previousRoom == null) {
                return currentRoom;
            }
            if (currentLevel < previousLevel) {
                return previousRoom;
            }
        }
        return currentRoom;
    }
}