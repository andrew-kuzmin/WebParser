package util;

import interfaces.SearchEngine;
import interfaces.XmlBuilder;
import parser.WebParser;
import model.Offer;

import java.util.*;

public class SearchEngineImpl implements SearchEngine, XmlBuilder {
    // We'll use a fake USER_AGENT so the web server thinks the robot is a normal web browser.
    private final String SEARCH_URL = "https://www.aboutyou.de/suche?term=";
    private Set<Offer> offers;
    private int requestsAmount;

    /**
     * This performs all the work. It makes an HTTP request, checks the response, and then gathers
     * up all the links on the page.
     *
     * @param url - The URL to visit
     * @return whether or not the parse was successful
     */

    @Override
    public void searchForKeyword(String keyword) {
        offers = Collections.synchronizedSet(new HashSet<>());
        Set<String> references = createReferences(keyword);
        WebParser parser = WebParser.getInstance();
        references.forEach(ref -> {
                parser.parse(ref, this.offers);
        });
        this.requestsAmount = parser.getRequestsAmount();
        buildXml(this.offers);
    }

    private Set<String> createReferences(String keyword) {
        String searchReferenceForWomen = SEARCH_URL + keyword + "&category=20201";
        String searchReferenceForMen = SEARCH_URL + keyword + "&category=20202";
        String searchReferenceForChildren = SEARCH_URL + keyword + "&category=138113";
        Set<String> searchReferences = new HashSet<>();
        searchReferences.add(searchReferenceForMen);
        searchReferences.add(searchReferenceForWomen);
        searchReferences.add(searchReferenceForChildren);
        return searchReferences;
    }

    public int getRequestsAmount(){ return this.requestsAmount; }

    public int getOffersAmount() { return this.offers.size(); }

}
