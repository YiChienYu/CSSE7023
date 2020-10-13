package bms.room;

import bms.exceptions.DuplicateSensorException;
import bms.hazardevaluation.*;
import bms.sensors.Sensor;
import bms.sensors.TemperatureSensor;
import bms.sensors.TimedSensor;
import bms.util.Encodable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;

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
 * Rooms can have one or more sensors to monitor hazard levels
 * in the room.
 * @ass1
 */
public class Room implements Encodable {

    /**
     * Unique room number for this floor.
     */
    private int roomNumber;

    /**
     * The type of room. Different types of rooms can be used for different
     * activities.
     */
    private RoomType type;

    /**
     * List of sensors located in the room. Rooms may only have up to one of
     * each type of sensor. Alphabetically sorted by class name.
     */
    private List<Sensor> sensors;

    /**
     * Area of the room in square metres.
     */
    private double area;

    /**
     * Minimum area of all rooms, in square metres.
     * (Note that dimensions of the room are irrelevant).
     * Defaults to 5.
     */
    private static final int MIN_AREA = 5;

    /**
     * Records whether there is currently a fire drill.
     */
    private boolean fireDrill;

    /**
     * Records whether there is currently a maintenance.
     */
    private boolean maintenance;

    /**
     * Hazard evaluator of the room
     */
    private HazardEvaluator hazardEvaluator;

    /**
     * The current state of the room
     */
    private RoomState state;

    /**
     * Creates a new room with the given room number.
     *
     * @param roomNumber the unique room number of the room on this floor
     * @param type the type of room
     * @param area the area of the room in square metres
     * @ass1
     */
    public Room(int roomNumber, RoomType type, double area) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.area = area;
        this.state = RoomState.OPEN;

        this.sensors = new ArrayList<>();
        this.fireDrill = false;
        this.maintenance = false;
        this.hazardEvaluator = null;
    }

    /**
     * Returns room number of the room.
     *
     * @return the room number on the floor
     * @ass1
     */
    public int getRoomNumber() {
        return this.roomNumber;
    }

    /**
     * Returns area of the room.
     *
     * @return the room area in square metres
     * @ass1
     */
    public double getArea() {
        return this.area;
    }

    /**
     * Returns the minimum area for all rooms.
     * <p>
     * Rooms must be at least 5 square metres in area.
     *
     * @return the minimum room area in square metres
     * @ass1
     */
    public static int getMinArea() {
        return MIN_AREA;
    }

    /**
     * Returns the type of the room.
     *
     * @return the room type
     * @ass1
     */
    public RoomType getType() {
        return type;
    }

    /**
     * Returns whether there is currently a fire drill in progress.
     *
     * @return current status of fire drill
     * @ass1
     */
    public boolean fireDrillOngoing() {
        return this.fireDrill;
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
     * @ass1
     */
    public List<Sensor> getSensors() {
        return new ArrayList<>(this.sensors);
    }

    /**
     * Change the status of the fire drill to the given value.
     *
     * @param fireDrill whether there is a fire drill ongoing
     * @ass1
     */
    public void setFireDrill(boolean fireDrill) {
        this.fireDrill = fireDrill;

        if (fireDrill) {
            state = RoomState.EVACUATE;
        } else {
            state = RoomState.OPEN;
        }
    }

    /**
     * Return the given type of sensor if there is one in the list of sensors;
     * return null otherwise.
     *
     * @param sensorType the type of sensor which matches the class name
     *                   returned by the getSimpleName() method,
     *                   e.g. "NoiseSensor" (no quotes)
     * @return the sensor in this room of the given type; null if none found
     * @ass1
     */
    public Sensor getSensor(String sensorType) {
        for (Sensor s : this.getSensors()) {
            if (s.getClass().getSimpleName().equals(sensorType)) {
                return s;
            }
        }
        return null;
    }

    /**
     * Adds a sensor to the room if a sensor of the same type is not
     * already in the room.
     * <p>
     * The list of sensors should be sorted after adding the new sensor, in
     * alphabetical order by simple class name ({@link Class#getSimpleName()}).
     * <p>
     * Adding a sensor should remove any hazard evaluator currently in the room.
     *
     * @param sensor the sensor to add to the room
     * @throws DuplicateSensorException if the sensor to add is of the
     * same type as a sensor already in this room
     * @ass1
     */
    public void addSensor(Sensor sensor)
            throws DuplicateSensorException {
        for (Sensor s : sensors) {
            if (s.getClass().equals(sensor.getClass())) {
                throw new DuplicateSensorException(
                        "Duplicate sensor of type: "
                                + s.getClass().getSimpleName());
            }
        }
        sensors.add(sensor);
        sensors.sort(Comparator.comparing(s -> s.getClass().getSimpleName()));
        this.hazardEvaluator = null;
    }

    /**
     * Returns the human-readable string representation of this room.
     * <p>
     * The format of the string to return is
     * "Room #'roomNumber': type='roomType', area='roomArea'm^2,
     * sensors='numSensors'"
     * without the single quotes, where 'roomNumber' is the room's unique
     * number, 'roomType' is the room's type, 'area' is the room's type,
     * 'numSensors' is the number of sensors in the room.
     * <p>
     * The room's area should be formatted to two (2) decimal places.
     * <p>
     * For example:
     * "Room #42: type=STUDY, area=22.50m^2, sensors=2"
     *
     * @return string representation of this room
     * @ass1
     */
    @Override
    public String toString() {
        return String.format("Room #%d: type=%s, area=%.2fm^2, sensors=%d",
                this.roomNumber,
                this.type,
                this.area,
                this.sensors.size());
    }

    /**
     * Returns whether there is currently maintenance in progress.
     *
     * @return current status of maintenance
     */
    public boolean maintenanceOngoing() {
        return maintenance;
    }

    /**
     * Change the status of maintenance to the given value.
     *
     * @param maintenance whether there is maintenance ongoing
     */
    public void setMaintenance(boolean maintenance) {
        this.maintenance = maintenance;
    }

    /**
     * Returns this room's hazard evaluator, or null if none exists.
     *
     * @return room's hazard evaluator
     */
    public HazardEvaluator getHazardEvaluator() {
        return hazardEvaluator;
    }

    /**
     * Sets the room's hazard evaluator to a new hazard evaluator.
     *
     * @param hazardEvaluator new hazard evaluator for the room to use
     */
    public void setHazardEvaluator(HazardEvaluator hazardEvaluator) {
        this.hazardEvaluator = hazardEvaluator;
    }

    /**
     * Evaluates the room status based upon current information.
     *
     * @return current room status
     */
    public RoomState evaluateRoomState() {
        if (this.getSensor("TemperatureSensor") != null) {
            TemperatureSensor sensor =
                    (TemperatureSensor) this.getSensor("TemperatureSensor");
            if (sensor.getHazardLevel() == 100) {
                state = RoomState.EVACUATE;
            }
        }

        if (this.fireDrill) {
            state = RoomState.EVACUATE;
        } else if (!(this.fireDrillOngoing()) && this.maintenanceOngoing()) {
            state = RoomState.MAINTENANCE;
        } else {
            state = RoomState.OPEN;
        }
        return state;
    }

    /**
     * Returns true if and only if this room is equal to the other given room.
     *
     * @param obj other object to compare equality
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Room) {
            Room room = (Room) obj;
            if (this.getRoomNumber() == room.getRoomNumber() &&
                    this.getType() == room.getType() &&
                    Math.abs(this.getArea() - room.getArea()) <= 0.001 &&
                    this.getSensors().size() == room.getSensors().size()) {
                for (int i = 0; i < this.getSensors().size(); i++) {
                    String name = this.getSensors().get(i).getClass()
                            .getSimpleName();
                    if (room.getSensor(name) == null) {
                        return false;
                    } else {
                        int index = room.getSensors().
                                indexOf(room.getSensor(name));
                        if (!this.getSensors().get(i).
                                equals(room.getSensors().get(index))) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the hash code of this room.
     *
     * @return hash code of this room
     */
    @Override
    public int hashCode() {
        int roundedArea = (int) (Math.round(this.getArea() * 100));
        int sensorsHashCode = 0;

        for (int i = 0; i < this.getSensors().size(); i++) {
            sensorsHashCode +=
                    sensors.get(i).getClass().getSimpleName().hashCode();
        }
        return roomNumber + type.hashCode() + roundedArea + sensors.size() +
                sensorsHashCode;
    }

    /**
     * Returns the machine-readable string representation of this room and
     * all of its sensors.
     *
     * @return encoded string representation of this room
     */
    @Override
    public String encode() {
        String begin = String.format("%s:%s:%s:%s", roomNumber, type, area,
                sensors.size());
        if (this.hazardEvaluator != null) {
            begin += String.format(":%s", this.hazardEvaluator.toString());
        }

        StringJoiner joiner = new StringJoiner(System.lineSeparator());

        joiner.add(begin);


        for (int i = 0; i < sensors.size(); i++) {
            TimedSensor temp = (TimedSensor) sensors.get(i);
            String tempString = temp.encode();
            if (this.hazardEvaluator instanceof WeightingBasedHazardEvaluator) {
                List<Integer> weights = ((WeightingBasedHazardEvaluator)
                        this.hazardEvaluator).getWeightings();
                if (weights.size() != 0) {
                    tempString += String.format("@%s", String.valueOf(weights.get(i)));
                }
            }
            joiner.add(tempString);
        }

        return joiner.toString();
    }
}
