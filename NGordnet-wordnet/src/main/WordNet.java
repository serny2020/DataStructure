package main;

import java.util.List;
import java.util.Set;

public class WordNet {
    private Graph graph;

    /**
     * Creates an instance of direct graph from the Wordnet dataset
     *
     * @param synsetFile  from the synset file
     * @param hyponymFile from the hyponym file
     */
    public WordNet(String synsetFile, String hyponymFile) {
        graph = new Graph(synsetFile, hyponymFile);
        // build the graph and add all the edges
    }

    /**
     * Traverse the graph and find all the hyponyms of
     * input in given graph.
     *
     * @param input of words
     * @return set of hyponyms in alphabetical order
     */
    public Set<String> readInput(List<String> input) {
        return graph.processInput(input);
    }
}
