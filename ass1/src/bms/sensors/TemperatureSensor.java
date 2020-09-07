package bms.sensors;

/**
 * A sensor that measures ambient temperature in a room.
 */
public class TemperatureSensor extends TimedSensor implements HazardSensor {

    /**
     * Creates a new temperature sensor with the given sensor readings and
     * update frequency.
     * <p>
     * For safety reasons, all temperature sensors must have an update frequency
     * of 1 minute.
     *
     * @param sensorReadings a non-empty array of sensor readings
     */
    public TemperatureSensor(int[] sensorReadings) {
        super(sensorReadings, 1);
    }

    /**
     * Returns the hazard level as detected by this sensor.
     * <p>
     * A temperature sensor detects a hazard if the current temperature reading
     * (TimedSensor.getCurrentReading()) is greater than or equal to 68 degrees,
     * indicating a fire. In this case, a hazard level of 100 should be
     * returned. Otherwise, the returned hazard level is 0.
     *
     * @return sensor's current hazard level, 0 to 100
     */
    @Override
    public int getHazardLevel() {
        if (this.getCurrentReading() >= 68) {
            return 100;
        }
        return 0;
    }

    /**
     * Returns the human-readable string representation of this temperature
     * sensor.
     *
     * @return string representation of this sensor
     */
    @Override
    public String toString() {
        return super.toString() + ", type=TemperatureSensor";
    }
}
