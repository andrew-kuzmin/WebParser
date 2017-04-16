package model;

public class Offer {
    private String articleId;
    private String name;
    private String brand;
    private String color;
    private String price;
    private String initialPrice;
    private String shippingCosts;
    private String description;

    public Offer(String articleId, String name, String brand, String color, String price, String initialPrice,
                 String shippingCosts, String description) {
        this.articleId = articleId;
        this.name = name;
        this.brand = brand;
        this.color = color;
        this.price = price;
        this.initialPrice = initialPrice;
        this.shippingCosts = shippingCosts;
        this.description = description;
    }

    public String getArticleId() {
        return articleId;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getColor() {
        return color;
    }

    public String getPrice() {
        return price;
    }

    public String getInitialPrice() {
        return initialPrice;
    }

    public String getShippingCosts() {
        return shippingCosts;
    }

    public String getDescription() {
        return description;
    }
}
