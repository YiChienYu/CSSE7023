package bms.hazardevaluation;

import bms.sensors.HazardSensor;

import java.util.*;

/**
 * Evaluates the hazard level of a location using weightings for
 * the sensor values.
 * <p>
 * The sum of the weightings of all sensors must equal 100.
 */
public class WeightingBasedHazardEvaluator implements HazardEvaluator {
    /**
     * mapping of sensors to their respective weighting
     */
    private Map<HazardSensor,Integer> sensors = new LinkedHashMap<>();

    /**
     * Creates a new weighting-based hazard evaluator with the given sensors
     * and weightings.
     * <p>
     * Each weighting must be between 0 and 100 inclusive, and the total sum of
     * all weightings must equal 100.
     *
     * @param sensors mapping of sensors to their respective weighting
     */
    public WeightingBasedHazardEvaluator(Map<HazardSensor,Integer> sensors)
            throws IllegalArgumentException {
        int total = 0;
        Iterator<Map.Entry<HazardSensor,Integer>> iterator =
                sensors.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<HazardSensor, Integer> entry = iterator.next();
            int value = entry.getValue();

            if (value < 0 || value > 100) {
                throw new IllegalArgumentException();
            }
            total += value;
        }

        if (total != 100) {
            throw new IllegalArgumentException();
        }

        this.sensors = sensors;
    }

    /**
     * Returns the weighted average of the current hazard levels of
     * all sensors in the map passed to the constructor.
     * <p>
     * The weightings given in the constructor should be used.
     * The final evaluated hazard level should be rounded
     * to the nearest integer between 0 and 100.
     *
     * @return weighted average of current sensor hazard levels
     */
    @Override
    public int evaluateHazardLevel() {
        float average = 0;

        Iterator<Map.Entry<HazardSensor,Integer>> iterator =
                sensors.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<HazardSensor, Integer> entry = iterator.next();
            average += ((entry.getKey().getHazardLevel() *
                    entry.getValue()) / 100);
        }
        return Math.round(average);
    }

    /**
     * Returns a list containing the weightings associated with all of
     * the sensors monitored by this hazard evaluator.
     *
     * @return weightings
     */
    public List<Integer> getWeightings() {
        List<Integer> weightings = new ArrayList<Integer>();
        List<Integer> unSortedWeights = new ArrayList<>();
        List<String> sortedName = new ArrayList<>();
        List<String> unSortedName = new ArrayList<>();

        Iterator<Map.Entry<HazardSensor,Integer>> iterator =
                sensors.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<HazardSensor, Integer> entry = iterator.next();
            HazardSensor sensor = entry.getKey();
            int value = entry.getValue();
            unSortedName.add(sensor.getClass().getSimpleName());
            sortedName.add(sensor.getClass().getSimpleName());
            unSortedWeights.add(value);
        }

        Collections.sort(sortedName);

        for (int i = 0; i < sortedName.size(); i++) {
            int indexOfElement = unSortedName.indexOf(sortedName.get(i));
            weightings.add(unSortedWeights.get(indexOfElement));
        }


        return weightings;
    }

    /**
     * Returns the string representation of this hazard evaluator.
     * <p>
     * The format of the string to return is simply "WeightingBased"
     * without the double quotes.
     * <p>
     * See the demo save file for an example (uqstlucia.txt).
     *
     * @return string representation of this hazard evaluator
     */
    @Override
    public String toString() {
        return "WeightingBased";
    }
}
