package org.mathcuprum.ovm.swing;

/**
 * Created by ebuldygin on 23.09.2017.
 */
public class RangedNumber {

    private final double min;
    private final double max;
    private final int steps;
    private final int step;

    public RangedNumber(double min, double max, int steps, int step) {
        if (min >= max) {
            throw new IllegalArgumentException("Minimal value " + min + " greater or equals to max " + max);
        }
        if (steps <= 0) {
            throw new IllegalArgumentException("Number of steps must be positive: " + steps);
        }
        if (step < 0 || step > steps) {
            throw new IllegalArgumentException("Current step " + step + " out of range [0, " + steps + "]");
        }
        this.min = min;
        this.max = max;
        this.steps = steps;
        this.step = step;
    }

    public RangedNumber(double min, double max, int steps) {
        this(min, max, steps, 0);
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public int getSteps() {
        return steps;
    }

    public int getStep() {
        return step;
    }

    public double getDelta() {
        return (max - min) / steps;
    }

    public double getValue() {
        return min + step * getDelta();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RangedNumber that = (RangedNumber) o;

        if (Double.compare(that.min, min) != 0) return false;
        if (Double.compare(that.max, max) != 0) return false;
        if (steps != that.steps) return false;
        return step == that.step;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(min);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(max);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + steps;
        result = 31 * result + step;
        return result;
    }
}
