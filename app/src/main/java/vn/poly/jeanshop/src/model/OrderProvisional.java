package vn.poly.jeanshop.src.model;

public class OrderProvisional {
    private Product product;
    private int amount;
    private String size;
    private String products;
    private String image;

    public OrderProvisional(Product product, int amount, String size,String products,String image) {
        this.product = product;
        this.amount = amount;
        this.size = size;
        this.products = products;
        this.image = image;
    }

    public OrderProvisional() {
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
