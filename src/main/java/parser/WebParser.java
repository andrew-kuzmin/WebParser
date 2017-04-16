package parser;

import interfaces.Parser;
import model.Offer;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class WebParser implements Parser {
    private final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private final String SITE_URL = "https://www.aboutyou.de";
    private final String NOTHING_FOUND = "ergab leider keinen Treffer";
    private final String NO_INFO = "No info.";
    private static WebParser instance = new WebParser();
    private Set<Document> searchPagesDocuments = new HashSet<>();
    private Set<String> offerLinks = new HashSet<>();
    private int requestsAmount;

    public static WebParser getInstance() {
        return instance;
    }

    /**
     * Performs a page set parsing using concurrency.
     *
     * @param url - Page URL to parse.
     *
     * @param offers - Synchronized set of offers.
     */
    @Override
    public void parse(String url, Set<Offer> offers) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Document startHtmlDocument = getHtmlDocument(url);
        if (startHtmlDocument != null) {
            if (startHtmlDocument.text().contains(NOTHING_FOUND)) {
                // no search results
                return;
            }
            collectSearchPagesDocuments(startHtmlDocument);
            this.searchPagesDocuments.parallelStream().forEach(this::collectOfferLinks);
            executorService.execute(() -> parseOffers(offers));
            shutdownExecutorService(executorService);
            clearData();
        }
    }

    private List<String> getSearchPagesReferences(Document htmlDocument) {
        Elements productPager = htmlDocument.getElementsByAttributeValue("class", "product-pager");
        List<String> pageReferences = new ArrayList<>();
        Predicate<Elements> isEmpty = ArrayList::isEmpty;
        if (!isEmpty.test(productPager)) {
            Elements li = productPager.get(0).getElementsByTag("li");
            int pages = Integer.parseInt(li.get(li.size() - 2).child(0).ownText());
            String pageRef = productPager.get(0).getElementsByTag("li").get(1).child(0).attr("href");
            for (Integer i = 2; i <= pages; i++) {
                pageReferences.add(SITE_URL + pageRef.substring(0, pageRef.length() - 1).concat(i.toString()));
            }
        }
        return pageReferences;
    }

    private void collectOfferLinks(Document htmlDocument) {
        Elements articles = htmlDocument.getElementsByTag("article");
        articles.forEach(article -> {
            String href = article.getElementsByTag("a").get(0).attr("href");
            this.offerLinks.add(SITE_URL + href);
        });
    }

    private void collectSearchPagesDocuments(Document htmlDocument) {
        this.searchPagesDocuments.add(htmlDocument);
        List<String> searchPagesReferences = getSearchPagesReferences(htmlDocument);
        searchPagesReferences.stream()
                .map(this::getHtmlDocument)
                .filter(Objects::nonNull)
                .forEach(document -> this.searchPagesDocuments.add(document));
    }

    private void parseOffers(Set<Offer> offers) {
        this.offerLinks.parallelStream().forEach(link -> {
            Document htmlDocument = getHtmlDocument(link);
            if (htmlDocument != null) {
                String price = getElementContentAttributeValueByProperty(htmlDocument, "og:price:amount");
                String initialPrice = getElementContentAttributeValueByProperty(htmlDocument, "og:price:standard_amount");
                String brand = getElementContentAttributeValueByProperty(htmlDocument, "og:brand");
                String id = getElementContentAttributeValueByProperty(htmlDocument, "og:isbn");
                String color = getElementContentAttributeValueByProperty(htmlDocument, "product:color");
                String name = getElementContentAttributeValueByProperty(htmlDocument, "og:title");
                String description = getElementTextByClass(htmlDocument, "bottom-0");
                String shippingCosts = getElementTextByClass(htmlDocument, "V3adp__promiseContent");
                offers.add(new Offer(id, name, brand, color, price, initialPrice, shippingCosts, description));
            }
        });
    }

    private String getElementContentAttributeValueByProperty(Document htmlDocument, String property) {
        Predicate<Elements> isEmpty = ArrayList::isEmpty;
        Elements elements = htmlDocument.getElementsByAttributeValue("property", property);
        if (!isEmpty.test(elements)) {
            return elements.get(0).attr("content");
        } else {
            return NO_INFO;
        }
    }

    private String getElementTextByClass(Document htmlDocument, String className) {
        Predicate<Elements> containsDescription = elements -> elements.size() > 1;
        Elements elements = htmlDocument.getElementsByClass(className);
        if (containsDescription.test(elements)) {
            return elements.get(1).text();
        } else {
            return NO_INFO;
        }
    }

    private Document getHtmlDocument(String url) {
        Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
        Document htmlDocument = null;
        try {
            htmlDocument = connection.get();
        } catch (IOException e) {
            System.out.println("Connection lost due to slow Internet connection");
        }
        this.requestsAmount++;
        if ((connection.response().statusCode() != 200) ||
                (!connection.response().contentType().contains("text/html"))) {
            htmlDocument = null;
        }
        return htmlDocument;
    }

    /**
     *
     * @return amount of triggered HTTP requests.
     */
    public int getRequestsAmount() {
        return this.requestsAmount;
    }

    private void clearData() {
        this.searchPagesDocuments.clear();
        this.offerLinks.clear();
    }

    private void shutdownExecutorService(ExecutorService executorService){
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
