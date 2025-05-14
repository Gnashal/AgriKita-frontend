package mobdev.agrikita.models.products.request;

public class RateProductRequest {
    private String productID;
    private float rating;

    public RateProductRequest(String productID, float rating) {
        this.productID = productID;
        this.rating = rating;
    }

    public String getProductIDRequest() {
        return productID;
    }

    public float getRatingRequest() {
        return rating;
    }
}
