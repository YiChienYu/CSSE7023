package bms.sensors;

/**
 * Represents a sensor that can be used to evaluate the level of comfort present
 * in a location monitored by the sensor.
 */
public interface ComfortSensor {
    int getComfortLevel();
}
