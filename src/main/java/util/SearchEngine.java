package util;

import java.util.*;

public class SearchEngine {

    private List<String> searchReferences = new ArrayList<>();
    private int requestAmount;
    private int offersAmount;

    /**
     * @param keyword - The keyword for searching
     */
    public void search(String keyword) {
        createReferences(keyword);
        WebsiteParser parser = new WebsiteParser();
        this.searchReferences.forEach(parser::parse);
        this.requestAmount = parser.getRequestsAmount();
        this.offersAmount = parser.getOffersAmount();
    }

    public int getRequestsAmount() {
        return this.requestAmount;
    }

    public int getProductsAmount() {
        return this.offersAmount;
    }

    private void createReferences(String keyword) {
        String searchReferenceForWomen = "https://www.aboutyou.de/suche?term=" + keyword + "&category=20201";
        String searchReferenceForMen = "https://www.aboutyou.de/suche?term=" + keyword + "&category=20202";
        String searchReferenceForChildren = "https://www.aboutyou.de/suche?term=" + keyword + "&category=138113";
        this.searchReferences.add(searchReferenceForMen);
        this.searchReferences.add(searchReferenceForWomen);
        this.searchReferences.add(searchReferenceForChildren);
    }

}