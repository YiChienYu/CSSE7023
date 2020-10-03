package bms.hazardevaluation;

import bms.sensors.HazardSensor;

import java.util.List;

/**
 * Evaluates the hazard level of a location using a rule based system.
 */
public class RuleBasedHazardEvaluator implements HazardEvaluator {
    /**
     * sensors to be used in the hazard level calculation
     */
    private List<HazardSensor> sensors;

    /**
     * Creates a new rule-based hazard evaluator with the given list of sensors.
     *
     * @param sensors sensors to be used in the hazard level calculation
     */
    public RuleBasedHazardEvaluator(List<HazardSensor> sensors) {
        this.sensors = sensors;
    }

    /**
     * Returns a calculated hazard level based on applying a set of rules
     * to the list of sensors passed to the constructor.
     *
     * @return calculated hazard level according to a set of rules
     */
    @Override
    public int evaluateHazardLevel() {
        float average = 0;
        float multiplier = 1;

        if (sensors.size() == 0) {
            return 0;
        } else if (sensors.size() == 1) {
            return sensors.get(0).getHazardLevel();
        } else {
            int total = 0;
            int num = 0;
            for (int i = 0; i < sensors.size(); i++) {
                HazardSensor sensor = sensors.get(i);
                if (sensor.getClass().getSimpleName() != "OccupancySensor") {
                    if (sensor.getHazardLevel() == 100) {
                        return 100;
                    }
                    total += sensor.getHazardLevel();
                    num += 1;
                } else {
                    multiplier = (float) (sensor.getHazardLevel() / 100);
                }
            }
            average = (float) (total / num);
        }
        return (int) (average * multiplier);
    }

    /**
     * Returns the string representation of this hazard evaluator.
     * <p>
     * The format of the string to return is simply "RuleBased" without
     * double quotes.
     * <p>
     * See the demo save file for an example (uqstlucia.txt).
     *
     * @return string representation of this room
     */
    @Override
    public String toString() {
        return "RuleBased";
    }
}
