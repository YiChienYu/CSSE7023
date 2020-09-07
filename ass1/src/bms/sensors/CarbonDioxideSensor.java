package bms.sensors;

/**
 * A sensor that measures levels of carbon dioxide (CO2) in the air, in parts
 * per million (ppm).
 */
public class CarbonDioxideSensor extends TimedSensor implements HazardSensor {

    private int idealValue;
    private int variationLimit;

    /**
     * Creates a new carbon dioxide sensor with the given sensor readings,
     * update frequency, ideal CO2 value and acceptable variation limit.
     * <p>
     * Different rooms and environments may naturally have different "normal"
     * CO2 concentrations, for example, a large room with many windows may have
     * lower typical CO2 concentrations than a small room with poor airflow.
     * <p>
     * To allow for these discrepancies, each CO2 sensor has an "ideal" CO2
     * concentration and a maximum acceptable variation from this value. Both
     * the ideal value and variation limit must be greater than zero. These two
     * values must be such that (idealValue - variationLimit) >= 0.
     *
     * @param sensorReadings array of CO2 sensor readings in ppm
     * @param updateFrequency indicates how often the sensor readings update,
     *                        in minutes
     * @param idealValue ideal CO2 value in ppm
     * @param variationLimit acceptable range above and below ideal value in ppm
     * @throws IllegalArgumentException if idealValue <= 0; or if variationLimit
     * <= 0; or if (idealValue - variationLimit) < 0
     */
    public CarbonDioxideSensor(int[] sensorReadings, int updateFrequency,
            int idealValue, int variationLimit) throws
            IllegalArgumentException {
        super(sensorReadings, updateFrequency);
        if (idealValue <= 0 || variationLimit <= 0 ||
                (idealValue - variationLimit) < 0) {
            throw new IllegalArgumentException();
        }
        this.idealValue = idealValue;
        this.variationLimit = variationLimit;
    }

    /**
     * Returns the sensor's CO2 variation limit.
     *
     * @return variation limit in ppm
     */
    public int getVariationLimit() {
        return variationLimit;
    }

    /**
     * Returns the sensor's ideal CO2 value.
     *
     * @return ideal value in ppm
     */
    public int getIdealValue() {
        return idealValue;
    }

    /**
     * Returns the hazard level as detected by this sensor.
     *
     * @return the current hazard level as an integer between 0 and 100
     */
    @Override
    public int getHazardLevel() {
        if (0 <= this.getCurrentReading() && this.getCurrentReading() < 1000) {
            return 0;
        } else if (1000 <= this.getCurrentReading()
                && this.getCurrentReading() < 2000) {
            return 25;
        } else if (2000 <= this.getCurrentReading()
                && this.getCurrentReading() < 5000) {
            return 50;
        } else {
            return 100;
        }
    }

    /**
     * Returns the human-readable string representation of this CO2 sensor.
     * <p>
     * The format of the string to return is "TimedSensor:
     * freq='updateFrequency', readings='sensorReadings',
     * type=CarbonDioxideSensor, idealPPM='idealValue',
     * varLimit='variationLimit'" without the single quotes, where
     * 'updateFrequency' is this sensor's update frequency (in minutes),
     * 'sensorReadings' is a comma-separated list of this sensor's readings,
     * 'idealValue' is this sensor's ideal CO2 concentration, and
     * 'variationLimit' is this sensor's variation limit.
     * <p>
     * For example: "TimedSensor: freq=5, readings=702,694,655,680,711,
     * type=CarbonDioxideSensor, idealPPM=600, varLimit=250"
     *
     * @return string representation of this sensor
     */
    @Override
    public String toString() {
        return super.toString() + String.format(", type=CarbonDioxideSensor, " +
                "idealPPM=%d, varLimit=%d", idealValue, variationLimit);
    }
}
