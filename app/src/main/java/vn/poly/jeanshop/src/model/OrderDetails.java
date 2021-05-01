package vn.poly.jeanshop.src.model;


public class OrderDetails  {
    private String idOrderDetails;
    private String idOrder;
    private String productId;
    private String size;
    private String products;
    private String image;
    private int amount;
    private double price;

    //Tuấn Anh
    //Thêm 2 trường name với image
    private String nameProduct;
    private String imageProduct;

    public OrderDetails(String idOrderDetails, String idOrder, String productId,String size,String products, String image, int amount, double price) {
        this.idOrderDetails = idOrderDetails;
        this.idOrder = idOrder;
        this.productId = productId;
        this.size = size;
        this.products = products;
        this.image = image;
        this.amount = amount;
        this.price = price;

    }

    public OrderDetails() {
    }

    public String getImageProduct() {
        return imageProduct;
    }

    public void setImageProduct(String imageProduct) {
        this.imageProduct = imageProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String getIdOrderDetails() {
        return idOrderDetails;
    }

    public void setIdOrderDetails(String idOrderDetails) {
        this.idOrderDetails = idOrderDetails;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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
