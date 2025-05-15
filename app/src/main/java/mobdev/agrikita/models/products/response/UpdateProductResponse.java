package mobdev.agrikita.models.products.response;

public class UpdateProductResponse {
    String message;

    public UpdateProductResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
