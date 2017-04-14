package dto;

public class Product {
    private String name;
    private String brand;
    private String color;
    private String price;
    private String description;
    private String articleId;

    public Product(String name, String brand, String color, String price, String description, String articleId) {
        this.name = name;
        this.brand = brand;
        this.color = color;
        this.price = price;
        this.description = description;
        this.articleId = articleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", color='" + color + '\'' +
                ", price='" + price + '\'' +
                ", description='" + description + '\'' +
                ", articleId='" + articleId + '\'' +
                '}';
    }
}
