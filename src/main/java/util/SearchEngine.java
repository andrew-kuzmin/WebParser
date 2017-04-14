package util;

import java.util.*;

public class SearchEngine {

    private List<String> searchRefences = new ArrayList<>();


    /**
     *
     *
     * @param keyword
     *            - The keyword for searching

     */
    public void search(String keyword)
    {
            createReferences(keyword);
            WebsiteParser parser = new WebsiteParser();
        // Lots of stuff happening here. Look at the parse method in
        this.searchRefences.forEach(parser::parse);
            // SpiderLeg

    }

    private void createReferences(String keyword){
        String searchReferenceForWomen = "https://www.aboutyou.de/suche?term=" + keyword + "&category=20201";
        String searchReferenceForMen = "https://www.aboutyou.de/suche?term=" + keyword + "&category=20202";
        this.searchRefences.add(searchReferenceForMen);
        this.searchRefences.add(searchReferenceForWomen);
    }

}