package bms.sensors;

/**
 * A sensor that measures the number of people in a room.
 */
public class OccupancySensor extends TimedSensor implements HazardSensor {

    private int capacity;

    /**
     * Creates a new occupancy sensor with the given sensor readings, update
     * frequency and capacity.
     * <p>
     * The given capacity must be greater than or equal to zero.
     *
     * @param sensorReadings a non-empty array of sensor readings
     * @param updateFrequency indicates how often the sensor readings update,
     *                        in minutes
     * @param capacity maximum allowable number of people in the room
     * @throws IllegalArgumentException if capacity is less than zero
     */
    public OccupancySensor(int[] sensorReadings, int updateFrequency,
                           int capacity) throws IllegalArgumentException {
        super(sensorReadings, updateFrequency);
        if (capacity < 0) {
            throw new IllegalArgumentException();
        }
        this.capacity = capacity;
    }

    /**
     * Returns the capacity of this occupancy sensor.
     *
     * @return capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Returns the hazard level based on the ratio of the current sensor reading
     * to the maximum capacity.
     * <p>
     * When the current reading is equal to or more than the capacity, the
     * hazard level is equal to 100 percent.
     * <p>
     * For example, a room with a maximum capacity of 21 people and current
     * occupancy of 8 people would have a hazard level of 38. A room with a
     * maximum capacity of 30 people and a current occupancy of 34 people would
     * have a hazard level of 100.
     * <p>
     * Floating point division should be used when performing the calculation,
     * however the resulting floating point number should be rounded to the
     * nearest integer before being returned.
     *
     * @return the current hazard level as an integer between 0 and 100
     */
    @Override
    public int getHazardLevel() {
        if (this.getCurrentReading() >= capacity) {
            return 100;
        }
        float ratio = ((float) this.getCurrentReading()) / capacity;
        int roundRatio = (int) Math.floor(ratio);
        return roundRatio;
    }

    /**
     * Returns the human-readable string representation of this occupancy
     * sensor.
     * <p>
     * The format of the string to return is "TimedSensor: freq=
     * 'updateFrequency', readings='sensorReadings', type=OccupancySensor,
     * capacity='sensorCapacity'" without the single quotes, where
     * 'updateFrequency' is this sensor's update frequency (in minutes),
     * 'sensorReadings' is a comma-separated list of this sensor's readings,
     * and 'sensorCapacity' is this sensor's maximum capacity.
     * <p>
     * For example: "TimedSensor: freq=5, readings=27,28,28,25,3,1,
     * type=OccupancySensor, capacity=30"
     *
     * @return string representation of this sensor
     */
    @Override
    public String toString() {
        return super.toString() + String.format(", type=OccupancySensor, " +
                "capacity=%d", capacity);
    }
}
