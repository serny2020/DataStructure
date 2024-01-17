package ngrams;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

/**
 * An object for mapping a year number (e.g. 1996) to numerical data. Provides
 * utility methods useful for data analysis.
 *
 * @author Josh Hug
 * Modified by Xiaocheng Sun
 * Date: Jan 14th, 2024
 */
public class TimeSeries extends TreeMap<Integer, Double> {

    public static final int MIN_YEAR = 1400;
    public static final int MAX_YEAR = 2100;

    /**
     * Constructs a new empty TimeSeries.
     */
    public TimeSeries() {
        super();
    }

    /**
     * Creates a copy of TS, but only between STARTYEAR and ENDYEAR,
     * inclusive of both end points.
     */
    public TimeSeries(TimeSeries ts, int startYear, int endYear) {
        super();
        HashSet<Integer> validYears = validYears(startYear, endYear);
        for (int year : ts.years()) {
            if (validYears.contains(year)) {
                this.put(year, ts.get(year));
            }
        }
    }

    /**
     * Returns the years in which this time series is valid.
     * Use a set to store all the unique valid years.
     *
     * @param startYear to indicate staring point, set to MIN_YEAR if smaller.
     * @param endYear   to indicate the end point, set to MAX_YEAR if larger.
     * @return a set of valid unique years.
     */
    private HashSet<Integer> validYears(int startYear, int endYear) {
        HashSet<Integer> result = new HashSet<>();
        if (startYear < MIN_YEAR) {
            startYear = MIN_YEAR;
        }
        if (endYear > MAX_YEAR) {
            endYear = MAX_YEAR;
        }
        for (int i = startYear; i <= endYear; i++) {
            result.add(i);
        }
        return result;
    }

    /**
     * Returns all years for this TimeSeries (in any order).
     */
    public List<Integer> years() {
        return new ArrayList<Integer>(this.keySet());
    }

    /**
     * Returns all data for this TimeSeries (in any order).
     * Must be in the same order as years().
     */
    public List<Double> data() {
        ArrayList<Double> result = new ArrayList<>();
        for (Integer year : years()) { //add data from corresponding years.
            result.add(get(year));
        }
        return result;
    }

    /**
     * Returns the year-wise sum of this TimeSeries with the given TS. In other words, for
     * each year, sum the data from this TimeSeries with the data from TS. Should return a
     * new TimeSeries (does not modify this TimeSeries).
     * <p>
     * If both TimeSeries don't contain any years, return an empty TimeSeries.
     * If one TimeSeries contains a year that the other one doesn't, the returned TimeSeries
     * should store the value from the TimeSeries that contains that year.
     */
    public TimeSeries plus(TimeSeries ts) {
        TimeSeries result = new TimeSeries();
        HashSet<Integer> allYears = new HashSet<>(years()); //create a set of all unique years
        allYears.addAll(ts.years()); //append all years.

        for (Integer year : allYears) {
            Double thisValue = this.get(year);
            Double tsValue = ts.get(year);
            if (thisValue == null) {
                result.put(year, tsValue);
            } else if (tsValue == null) {
                result.put(year, thisValue);
            } else {
                result.put(year, (thisValue + tsValue));
            }
        }
        return result;
    }

    /**
     * Returns the quotient of the value for each year this TimeSeries divided by the
     * value for the same year in TS. Should return a new TimeSeries (does not modify this
     * TimeSeries).
     * <p>
     * If TS is missing a year that exists in this TimeSeries, throw an
     * IllegalArgumentException.
     * If TS has a year that is not in this TimeSeries, ignore it.
     */
    public TimeSeries dividedBy(TimeSeries ts) {
        TimeSeries result = new TimeSeries();
        for (Integer year : this.years()) { //ignore if TS has a year not in this TimeSeries.
            Double thisValue = this.get(year);
            Double tsValue = ts.get(year);
            if (tsValue == null) {
                throw new IllegalArgumentException("Missing a year from divisor series");
            } else {
                result.put(year, thisValue / tsValue);
            }
        }
        return result;
    }

}
