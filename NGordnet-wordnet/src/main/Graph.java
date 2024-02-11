package main;

import edu.princeton.cs.algs4.In;

import java.util.*;

public class Graph {
    private List<List<String>> wordList = new ArrayList<>(); //adjacent list of words
    private HashMap<Integer, List<Integer>> wordNet = new HashMap<>(); //words of relationship

    /**
     * Constructor of Graph that reads input files and convert the dataset file into a graph.
     * Storing the node of words with corresponding index into an array;
     * Storing the edges of relationship between words from synsets into a map.
     *
     * @param hyponyms files of input that used to create nodes.
     * @param synsets  files of input that used to create edges.
     */
    public Graph(String hyponyms, String synsets) {
        In readHyponyms = new In(hyponyms);
        In readSynsets = new In(synsets);
        int lineIndex = 0;

        //creates nodes by initializing the wordList from hyponyms file.
        while (readHyponyms.hasNextLine()) {
            wordList.add(new ArrayList<>());
            String line = readHyponyms.readLine();
            String[] parser = line.split(",");
            String[] words = parser[1].split(" ");
            for (String word : words) {
                wordList.get(lineIndex).add(word); //append word to the corresponding line index
            }
            lineIndex++;
        }

        //add edges by initializing the wordNet from synsets file.
        while (readSynsets.hasNextLine()) {
            String line = readSynsets.readLine();
            String[] parser = line.split(",");
            int parent = Integer.parseInt(parser[0]);
            List<Integer> children = new ArrayList<>();
            for (int i = 1; i < parser.length; i++) {
                children.add(Integer.parseInt(parser[i]));
            }
            //add to the map if parent id exist
            if (wordNet.containsKey(parent)) {
                wordNet.get(parent).addAll(children);
            } else {
                //create a new map if parent id does not exist
                wordNet.put(parent, children);
            }

        }
    }

    /**
     * Find all the corresponding index of a given word from the wordList
     *
     * @param word the needs to get the index
     * @return all the word index in hyponyms file
     */
    private List<Integer> findWordIndex(String word) {
        List<Integer> wordIndex = new ArrayList<>();
        for (int i = 0; i < wordList.size(); i++) {
            if (wordList.get(i).contains(word)) {
                wordIndex.add(i);
            }
        }
        return wordIndex;
    }

    /**
     * Use graph traversal to find all the hyponyms of that word
     * in the given graph.
     * Note this function returns a set of all subtree index,
     * including the root.
     *
     * @param parentIndex is the root index of subtree
     * @return the set of all index of the subtree
     */
    private Set<Integer> getAllSubtree(Integer parentIndex) {
        Set<Integer> childrenIndex = new HashSet<>();
        getAllSubtreeHelper(parentIndex, childrenIndex);
        childrenIndex.add(parentIndex); //add back the root index
        return childrenIndex;
    }

    /**
     * Recursive helper to get all the subtree index of any given word index
     * Note this function gets all the nodes of subtree including the root
     *
     * @param parentIndex   is the root index of the current subtree
     * @param childrenIndex is all child index of current subtree
     */
    private void getAllSubtreeHelper(int parentIndex, Set<Integer> childrenIndex) {
        if (!wordNet.containsKey(parentIndex)) { //add all the children
            childrenIndex.add(parentIndex);
        } else {
            for (int child : wordNet.get(parentIndex)) {
                getAllSubtreeHelper(child, childrenIndex);
                childrenIndex.add(child); //add back current root
            }
        }
    }

    /**
     * Find the relations of the synsets words
     * based on the index from the hyponyms file
     *
     * @param word of single word that needs to fetch the hypomyns from the database.
     * @return the set of hyponyms related to the input word in alphabetical order.
     */
    private Set<String> findHyponyms(String word) {
        //find the subtree index from the hyponyms file.
        List<Integer> wordIndex = findWordIndex(word);
        Set<Integer> allSubtreeIndex = new HashSet<>();
        for (int parentIndex : wordIndex) {
            allSubtreeIndex.addAll(getAllSubtree(parentIndex));
        }
        //find the corresponding words from the synsets file based on the index
        Set<String> allSubtreeWords = new TreeSet<>(); //returns ordered set of words
        for (int index : allSubtreeIndex) {
            allSubtreeWords.addAll(this.wordList.get(index));
        }
        return allSubtreeWords;
    }

    /**
     * Reads inputs from the website interface,
     * and gives the hyponyms of that input.
     *
     * @param input is a list of words that needs to search for their hyponyms
     * @return the set of hyponyms of all the input words.
     */
    public Set<String> processInput(List<String> input) {
        if (input.isEmpty()) {
            return null;
        }
        int index = 0;
        String word = input.get(0);
        index++;
        Set<String> hyponyms = findHyponyms(word);
        while (index < input.size()) {
            String nextWord = input.get(index);
            index++;
            Set<String> nextHyponyms = findHyponyms(nextWord);
            hyponyms.retainAll(nextHyponyms); //only keeps the common hyponmys
        }
        return hyponyms;
    }

}
