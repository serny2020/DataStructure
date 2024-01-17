package ngrams;

import edu.princeton.cs.algs4.In;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 * <p>
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 * Modified by Xiaocheng Sun
 * Date: 15th Jan, 2024
 */
public class NGramMap {

    private Map<String, TimeSeries> wordmap = new HashMap<>();
    private TimeSeries counts = new TimeSeries();

    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {
        In readWords = new In(wordsFilename);
        In readCounts = new In(countsFilename);

        while (readWords.hasNextLine()) {
            String line = readWords.readLine();
            String[] parser = line.split("\t");
            String word = parser[0];
            int year = Integer.parseInt(parser[1]); //convert string to int
            double frequency = Integer.parseInt(parser[2]);
            if (wordmap.containsKey(word)) {
                wordmap.get(word).put(year, frequency);
            } else {
                TimeSeries record = new TimeSeries();
                record.put(year, frequency);
                wordmap.put(word, record);
            }
        }

        while (readCounts.hasNextLine()) {
            String line = readCounts.readLine();
            String[] parser = line.split(",");
            int year = Integer.parseInt(parser[0]);
            double total = Double.parseDouble(parser[1]);
            counts.put(year, total);
        }

    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy". If the word is not in the data files,
     * returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        TimeSeries target = wordmap.get(word);
        return new TimeSeries(target, startYear, endYear);
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy, not a link to this
     * NGramMap's TimeSeries. In other words, changes made to the object returned by this function
     * should not also affect the NGramMap. This is also known as a "defensive copy". If the word
     * is not in the data files, returns an empty TimeSries.
     */
    public TimeSeries countHistory(String word) {
        TimeSeries copy = new TimeSeries();
        for (int year : wordmap.get(word).years()) {
            copy.put(year, wordmap.get(word).get(year));
        }
        return copy;
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        TimeSeries copy = new TimeSeries();
        for (int year : counts.years()) {
            copy.put(year, counts.get(year));
        }
        return copy;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        return countHistory(word, startYear, endYear).dividedBy(this.counts);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to all
     * words recorded in that year. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        return countHistory(word).dividedBy(this.counts);
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS between STARTYEAR and
     * ENDYEAR, inclusive of both ends. If a word does not exist in this time frame, ignore it
     * rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words,
                                          int startYear, int endYear) {
        TimeSeries result = new TimeSeries();
        for (String word : words) {
            TimeSeries weightHistory = this.weightHistory(word, startYear, endYear);
            result = result.plus(weightHistory);
        }
        return result;
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS. If a word does not
     * exist in this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        TimeSeries result = new TimeSeries();
        for (String word : words) {
            result = result.plus(weightHistory(word));
        }
        return null;
    }

}
