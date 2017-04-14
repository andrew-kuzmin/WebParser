package util;

public class ParserTest {

    /**
     * This is our test. It creates a spider (which creates spider legs) and crawls the web.
     *
     * @param args - not used
     */
    public static void main(String[] args) {
        SearchEngine searchEngine = new SearchEngine();
        searchEngine.search("tom tailor denim");
    }
}
