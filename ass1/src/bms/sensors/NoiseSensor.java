package bms.sensors;

import java.lang.Math;

/**
 * A sensor that measures the noise levels in a room.
 */
public class NoiseSensor extends TimedSensor implements HazardSensor {

    /**
     * Creates a new noise sensor with the given sensor readings and update
     * frequency.
     *
     * @param sensorReadings  array of noise sensor readings in decibels
     * @param updateFrequency indicates how often the sensor readings update,
     *                        in minutes
     */
    public NoiseSensor(int[] sensorReadings, int updateFrequency) {
        super(sensorReadings, updateFrequency);
    }

    /**
     * Calculates the relative loudness level compared to a reference of 70.0
     * decibels.
     * <p>
     * The loudness of sounds in comparison to 70.0 decibels is given by the
     * formula:
     * <p>
     * 2^((measured volume - 70.0)/10.0)
     * <p>
     * For example, a sound reading of 67 decibels would have a relative
     * loudness of 0.8123. A Sound reading of 82 decibels would have a relative
     * loudness of 2.2974.
     * <p>
     * Refer to: http://www.sengpielaudio.com/calculator-levelchange.htm https:
     * //www.iacacoustics.com/blog-full/comparative-examples-of-noise-levels.
     * html https://www.safeworkaustralia.gov.au/noise
     *
     * @return relative loudness of current reading to 70dB
     */
    public double calculateRelativeLoudness() {
        double relativeLoudness = Math.round(Math.pow(2,
                ((this.getCurrentReading() - 70.0) / 10.0)) * 10000);
        double roundedRelativeLoudness = relativeLoudness / 10000;
        return roundedRelativeLoudness;
    }

    /**
     * Returns the current hazard level observed by the sensor, based on the
     * current loudness reading.
     * <p>
     * Retrieves the relative loudness using calculateRelativeLoudness(),
     * multiplies the result by 100 (floating point multiplication should be
     * used), then rounds down to the largest integer that is less than or equal
     * to the calculated value.
     * <p>
     * If the result is > 100, 100 is returned. Otherwise, the result is
     * returned.
     * <p>
     * For example, if calculateRelativeLoudness() returns 0.8968 then 89 must
     * be returned. If calculateRelativeLoudness() returns 1.7646 then 100 must
     * be returned.
     *
     * @return level of hazard at sensor location, 0 to 100
     */
    @Override
    public int getHazardLevel() {
        float relativeLoudness = ((float) this.calculateRelativeLoudness())
                * 100;
        int roundRelativeLoudness = (int) Math.floor(relativeLoudness);
        if (roundRelativeLoudness > 100) {
            return 100;
        }
        return roundRelativeLoudness;
    }

    /**
     * Returns the human-readable string representation of this noise sensor.
     *
     * @return string representation of this sensor
     */
    @Override
    public String toString() {
        return super.toString() + ", type=NoiseSensor";
    }
}