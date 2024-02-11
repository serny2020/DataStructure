package main;

public class GraphTest {
    public static void main(String[] args) {
        String synsetFile = "./data/wordnet/synsets11.txt";
        String hyponymFile = "./data/wordnet/hyponyms11.txt";
        WordNet wn = new WordNet(synsetFile, hyponymFile);

    }
}
