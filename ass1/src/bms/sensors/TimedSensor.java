package bms.sensors;

import bms.util.TimedItem;
import bms.util.TimedItemManager;

import java.util.Arrays;

/**
 * An abstract class to represent a sensor that iterates through observed values
 * on a timer.
 */
public abstract class TimedSensor implements TimedItem, Sensor {
    private int updateFrequency;
    private int[] sensorReadings;
    private int currentReading;

    // the position of currentReading
    private int currentPosition;

    // the number of times elapseOneMinute() has been called
    private int timeElapsed;

    /**
     * Creates a new timed sensor, using the provided list of sensor readings.
     * These represent "raw" data values, and have different meanings depending
     * on the concrete sensor class used.
     * <p>
     * The provided update frequency must be greater than or equal to one (1),
     * and less than or equal to five (5). The provided sensor readings array
     * must not be null, and must have at least one element. All sensor readings
     * must be non-negative.
     * <p>
     * The new timed sensor should be configured such that the first call to
     * getCurrentReading() after calling the constructor must return the first
     * element of the given array.
     * <p>
     * The sensor should be registered as a timed item, see
     * TimedItemManager.registerTimedItem(TimedItem).
     *
     * @param sensorReadings  a non-empty array of sensor readings
     * @param updateFrequency indicates how often the sensor readings updates,
     *                        in minutes
     * @throws IllegalArgumentException if updateFrequency is < 1 or > 5; or
     *                                  if sensorReadings is null;
     *                                  if sensorReadings is empty;
     *                                  or if any value in sensorReadings is
     *                                  less than zero
     */
    public TimedSensor(int[] sensorReadings, int updateFrequency) throws
            IllegalArgumentException {
        if (updateFrequency > 5
                || updateFrequency < 1 || sensorReadings == null
                || sensorReadings.length < 1) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < sensorReadings.length; i++) {
            if (sensorReadings[i] < 0) {
                throw new IllegalArgumentException();
            }
        }
        this.updateFrequency = updateFrequency;
        this.sensorReadings = sensorReadings;
        this.currentPosition = 0;
        this.currentReading = sensorReadings[currentPosition];
        this.timeElapsed = 0;
        TimedItemManager.getInstance().registerTimedItem(this);
    }

    @Override
    public int getCurrentReading() {
        return currentReading;
    }

    /**
     * Returns the number of minutes that have elapsed since the sensor was
     * instantiated. Should return 0 immediately after the constructor is
     * called.
     *
     * @return the sensor's time elapsed in minutes
     */
    public int getTimeElapsed() {
        return timeElapsed;
    }

    /**
     * Returns the number of minutes in between updates to the current sensor
     * reading.
     *
     * @return the sensor's update frequency in minutes
     */
    public int getUpdateFrequency() {
        return updateFrequency;

    }

    /**
     * Increments the time elapsed (in minutes) by one.
     * <p>
     * If getTimeElapsed() divided by getUpdateFrequency() leaves zero (0)
     * remainder, the sensor reading needs to be updated. In this case, the
     * current sensor reading is updated to the next value in the array.
     * <p>
     * When the end of the sensor readings array is reached, it must start again
     * at the beginning of the array (in other words it wraps around).
     */
    @Override
    public void elapseOneMinute() {
        timeElapsed++;
        int remainder = (this.getTimeElapsed()) % (this.getUpdateFrequency());
        if (remainder == 0 && timeElapsed != 0) {
            if (currentPosition == (sensorReadings.length - 1)) {
                currentPosition = 0;
            } else {
                currentPosition += 1;
            }
            currentReading = sensorReadings[currentPosition];
        }
    }

    /**
     * Returns the human-readable string representation of this timed sensor.
     * <p>
     * The format of the string to return is "TimedSensor: freq=
     * 'updateFrequency', readings='sensorReadings'" without the single quotes,
     * where 'updateFrequency' is this sensor's update frequency (in minutes)
     * and 'sensorReadings' is a comma-separated list of this sensor's readings.
     * <p>
     * For example: "TimedSensor: freq=5, readings=24,25,25,23,26"
     *
     * @return string representation of this sensor
     */
    @Override
    public String toString() {
        String commaSeparated = Arrays.toString(sensorReadings);
        commaSeparated = commaSeparated.substring
                (1, commaSeparated.length() - 1);
        commaSeparated = commaSeparated.replaceAll("\\s+", "");
        return String.format("TimedSensor: freq=%d, readings=%s",
                updateFrequency, commaSeparated);
    }
}
