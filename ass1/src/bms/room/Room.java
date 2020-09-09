package bms.room;

import java.util.ArrayList;
import java.util.List;

import bms.sensors.Sensor;
import bms.exceptions.*;

/**
 * Represents a room on a floor of a building.
 * <p>
 * Each room has a room number (unique for this floor, ie. no two rooms on the
 * same floor can have the same room number), a type to indicate its intended
 * purpose, and a total area occupied by the room in square metres.
 * <p>
 * Rooms also need to record whether a fire drill is currently taking place in
 * the room.
 * <p>
 * Rooms can have one or more sensors to monitor hazard levels in the room.
 */
public class Room {
    private int roomNumber;
    private RoomType type;
    private double area;
    private boolean fireDrillState = false;
    private ArrayList<Sensor> sensors;

    /**
     * Creates a new room with the given room number.
     *
     * @param roomNumber the unique room number of the room on this floor
     * @param type       the type of room
     * @param area       the area of the room in square metres
     */
    public Room(int roomNumber, RoomType type, double area) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.area = area;
        this.sensors = new ArrayList<Sensor>();
    }

    /**
     * Returns room number of the room.
     *
     * @return the room number on the floor
     */
    public int getRoomNumber() {
        return roomNumber;
    }

    /**
     * Returns area of the room.
     *
     * @return the room area in square metres
     */
    public double getArea() {
        return area;
    }

    /**
     * Returns the minimum area for all rooms.
     * <p>
     * Rooms must be at least 5 square metres in area.
     *
     * @return the minimum room area in square metres
     */
    public static int getMinArea() {
        return 5;
    }

    /**
     * Returns the type of the room.
     *
     * @return the room type
     */
    public RoomType getType() {
        return type;
    }

    /**
     * Returns whether there is currently a fire drill in progress.
     *
     * @return current status of fire drill
     */
    public boolean fireDrillOngoing() {
        return fireDrillState;
    }

    /**
     * Returns the list of sensors in the room.
     * <p>
     * The list of sensors stored by the room should always be in alphabetical
     * order, by the sensor's class name.
     * <p>
     * Adding or removing sensors from this list should not affect the room's
     * internal list of sensors.
     *
     * @return list of all sensors in alphabetical order of class name
     */
    public List<Sensor> getSensors() {
        return (ArrayList<Sensor>) sensors.clone();
    }

    /**
     * Change the status of the fire drill to the given value.
     *
     * @param fireDrill whether there is a fire drill ongoing
     */
    public void setFireDrill(boolean fireDrill) {
        fireDrillState = fireDrill;
    }

    /**
     * Return the given type of sensor if there is one in the list of sensors;
     * return null otherwise.
     *
     * @param sensorType the type of sensor which matches the class name
     *                   returned by the getSimpleName() method, e.g.
     *                   "NoiseSensor" (no quotes)
     * @return the sensor in this room of the given type; null if none found
     */
    public Sensor getSensor(String sensorType) {
        for (int i = 0; i < sensors.size(); i++) {
            if (sensors.get(i).getClass().getSimpleName().equals(sensorType)) {
                return sensors.get(i);
            }
        }
        return null;
    }

    /**
     * Adds a sensor to the room if a sensor of the same type is not already in
     * the room.
     * <p>
     * The list of sensors should be sorted after adding the new sensor, in
     * alphabetical order by simple class name (Class.getSimpleName()).
     *
     * @param sensor the sensor to add to the room
     * @throws DuplicateSensorException if the sensor to add is of the same
     *                                  type as a sensor already in this room
     */
    public void addSensor(Sensor sensor) throws DuplicateSensorException {
        if (this.getSensor(sensor.getClass().getSimpleName()) != null) {
            throw new DuplicateSensorException();
        } else {
            if (sensors.size() == 0) {
                sensors.add(sensor);
            } else {
                for (int i = 0; i < sensors.size(); i++) {
                    if (sensor.getClass().getSimpleName().compareTo
                            (sensors.get(i).getClass().getSimpleName()) < 0) {
                        sensors.add(i, sensor);
                        break;
                    } else {
                        if (i == (sensors.size() - 1)) {
                            sensors.add(sensor);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns the human-readable string representation of this room.
     *
     * @return string representation of this room
     */
    @Override
    public String toString() {
        return String.format("Room #%d: type=%s, area=%.2fm^2, sensors=%d",
                roomNumber, type.toString(), area, sensors.size());
    }
}
