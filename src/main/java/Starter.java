import org.jsoup.helper.Validate;
import util.SearchEngineImpl;

public class Starter {
    /**
     * Starter class which executes parsing.
     *
     * @param args - not used
     */
    public static void main(String[] args) {
        Validate.isTrue(args.length == 1);
        String keyword = args[0];
        long startTime = System.currentTimeMillis();
        SearchEngineImpl searchEngine = new SearchEngineImpl();
        searchEngine.searchForKeyword(keyword);
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println();
        System.out.println("Amount of triggered HTTP request : " + searchEngine.getRequestsAmount());
        System.out.println("Used memory : " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024L + " KB");
        System.out.println("Amount of extracted products : " + searchEngine.getOffersAmount());
        System.out.println("Run-time : " + (totalTime / 1000d) + " seconds");
    }
}
