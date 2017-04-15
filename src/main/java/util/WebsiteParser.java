package util;

import dto.Offer;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class WebsiteParser {

    // We'll use a fake USER_AGENT so the web server thinks the robot is a normal web browser.
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private Document htmlDocument;
    private List<String> offerLinks = new ArrayList<>();
    private List<Document> searchPagesDocuments = new ArrayList<>();
    private List<Offer> offers = new ArrayList<>();
    private int requestsAmount;

    {
        this.requestsAmount = 0;
    }

    /**
     * This performs all the work. It makes an HTTP request, checks the response, and then gathers
     * up all the links on the page.
     *
     * @param url - The URL to visit
     * @return whether or not the parse was successful
     */
    public boolean parse(String url) {
        try {
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            Document htmlDocument = connection.get();
            this.requestsAmount++;
            this.htmlDocument = htmlDocument;
            if (connection.response().statusCode() == 200)
            {
                System.out.println("\n**Visiting** Received web page at " + url);
            }
            if (!connection.response().contentType().contains("text/html")) {
                System.out.println("**Failure** Retrieved something other than HTML");
                return false;
            }


            if (htmlDocument.text().contains("ergab leider keinen Treffer")){
                System.out.println("NOTHING FOUND!!!");
                return false;
            }

            collectSearchPagesDocuments(url, htmlDocument);
            for (Document doc : this.searchPagesDocuments){
                collectOfferLinks(doc);
            }

            parseOffers();
            clearData();

//            this.offers.forEach(System.out::println);


            return true;
        } catch (IOException ioe) {
            // We were not successful in our HTTP request
            return false;
        }
    }

    private List<String> getSearchPagesReferences(Document htmlDocument){
        Elements productPager = htmlDocument.getElementsByAttributeValue("class", "product-pager");
        List<String> pageReferences = new ArrayList<>();
        if (productPager.size() > 0) {
            Elements li = productPager.get(0).getElementsByTag("li");
            int pages = Integer.parseInt(li.get(li.size() - 2).child(0).ownText());
            String pageRef = productPager.get(0).getElementsByTag("li").get(1).child(0).attr("href");
            for (Integer i = 2 ; i <= pages; i++){
                pageReferences.add("https://www.aboutyou.de" + pageRef.substring(0, pageRef.length()-1).concat(i.toString()));
            }
        }
        return pageReferences;
    }

    private void collectOfferLinks(Document htmlDocument){
        Elements articles = htmlDocument.getElementsByTag("article");
        for (Element article : articles){
            String href = article.getElementsByTag("a").get(0).attr("href");
            this.offerLinks.add("https://www.aboutyou.de" + href);
        }
    }

    private void collectSearchPagesDocuments(String url, Document htmlDocument) throws IOException{
        this.searchPagesDocuments.add(htmlDocument);
        List<String> searchPagesReferences = getSearchPagesReferences(htmlDocument);
        for (String ref : searchPagesReferences){
            this.searchPagesDocuments.add(getHtmlDocument(ref));
        }
//        searchPagesReferences.forEach(ref -> this.searchPagesDocuments.add(getHtmlDocument(ref)));
    }

    private void parseOffers() throws IOException{
        for (String link : this.offerLinks) {
            Document htmlDocument = getHtmlDocument(link);
            String price = getAttributeValue(htmlDocument.getElementsByAttributeValue("property", "og:price:amount"), ArrayList::isEmpty);
            String brand = getAttributeValue(htmlDocument.getElementsByAttributeValue("property", "og:brand"), ArrayList::isEmpty);
            String id = getAttributeValue(htmlDocument.getElementsByAttributeValue("property", "og:isbn"), ArrayList::isEmpty);
            String color = getAttributeValue(htmlDocument.getElementsByAttributeValue("property", "product:color"), ArrayList::isEmpty);
            String description = getAttributeValue(htmlDocument.getElementsByAttributeValue("property", "og:description"), ArrayList::isEmpty);
            String name = getAttributeValue(htmlDocument.getElementsByAttributeValue("property", "og:title"), ArrayList::isEmpty);
            this.offers.add(new Offer(name, brand, color, price, description, id));
        }
    }

    private String getAttributeValue(Elements elements, Predicate<Elements> predicate){
        if (!predicate.test(elements)){
            return elements.get(0).attr("content");
        } else {
            return "No info.";
        }
    }

    private Document getHtmlDocument(String url) throws IOException {
        Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
        Document htmlDocument = connection.get();
        this.requestsAmount++;
        return htmlDocument;
    }

    public List<String> getOfferLinks() {
        return this.offerLinks;
    }

    public int getRequestsAmount() { return this.requestsAmount; }

    public int getOffersAmount() { return this.offers.size(); }

    private void clearData() {
        this.searchPagesDocuments.clear();
        this.offerLinks.clear();
    }


}
