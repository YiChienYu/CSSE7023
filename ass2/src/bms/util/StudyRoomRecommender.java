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
    public static Room recommendStudyRoom(Building building) {
        List<Floor> floors = building.getFloors();
        List<Room> tempRooms = new ArrayList<>();

        Room currentRoom = null;
        int currentLevel = -1;
        Room previousRoom = null;
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
            previousLevel = currentLevel;
            previousRoom = currentRoom;
            List<Integer> levels = new ArrayList<>();
            List<Room> rooms = floors.get(i).getRooms();
            List<Room> roomsAvailable = new ArrayList<>();

            if (rooms.size() != 0) {
                for (Room r : rooms) {
                    int comfortLevel = 0;
                    List<Sensor> sensors = r.getSensors();

                    if (r.evaluateRoomState() != RoomState.EVACUATE &&
                            r.evaluateRoomState() != RoomState.MAINTENANCE &&
                            r.evaluateRoomState() == RoomState.OPEN &&
                            r.getType() == RoomType.STUDY) {
                        roomsAvailable.add(r);
                    }

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


            if (roomsAvailable.size() == 0) {
                currentRoom = null;
                currentLevel = -1;
            } else {
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
