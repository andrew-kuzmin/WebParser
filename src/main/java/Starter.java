import util.SearchEngineImpl;

public class Starter {

    /**
     * This is our test. It creates a spider (which creates spider legs) and crawls the web.
     *
     * @param args - not used
     */
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        SearchEngineImpl searchEngine = new SearchEngineImpl();
        searchEngine.searchForKeyword("tom tailor denim");
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println();
        System.out.println("Amount of triggered HTTP request : " + searchEngine.getRequestsAmount());
        System.out.println("Used memory : " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024L + " KB");
        System.out.println("Amount of extracted products : " + searchEngine.getOffersAmount());
        System.out.println("Run-time : " + (totalTime/1000d) + " seconds" );

    }
}
