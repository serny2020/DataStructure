package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import ngrams.TimeSeries;

import java.util.*;

public class HyponymHandler extends NgordnetQueryHandler {
    private WordNet wn;
    private NGramMap ngm;

    public HyponymHandler(WordNet wn, NGramMap ngm) {
        this.wn = wn;
        this.ngm = ngm;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();
        int k = q.k();
        Set<String> commonHyponyms = wn.readInput(words);

        //handle the k != 0 cases
        if (k == 0) {
            return commonHyponyms.toString();
        } else {
            //storing the words and its total number of occurrence.
            Map<String, Double> count = new HashMap<>();
            for (String word : commonHyponyms) {
                TimeSeries ts = ngm.countHistory(word, startYear, endYear);
                if (ts.size() != 0) {
                    double occurrence = 0;
                    for (double data : ts.values()) {
                        occurrence += data;
                    }
                    count.put(word, occurrence);
                }
            }
            //if k is smaller than the total record,
            //print the result directly in alphabetical order
            if (count.size() <= k) {
                Set<String> result = new TreeSet<>();
                result.addAll(count.keySet());
                return result.toString();
            }

            //if k is greater, pick the k most popular value from total record
            else {
                Set<String> result = new TreeSet<>();
                //pick the k most popular words based on the occurrence
                for (int i = 0; i < k; i++) {
                    double max = 0;
                    String maxWord = "";
                    for (String word : count.keySet()) { //traverse the map to find next popular word
                        if (count.get(word) > max) {
                            max = count.get(word);
                            maxWord = word;
                        }
                    }
                    result.add(maxWord);
                    count.remove(maxWord); //remove the current max key for next searching.
                }
                return result.toString();
            }
        }
    }
}
