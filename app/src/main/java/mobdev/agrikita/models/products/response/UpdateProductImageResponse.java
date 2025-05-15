package mobdev.agrikita.models.products.response;

public class UpdateProductImageResponse {
    String message;
    String newImageUrl;

    public UpdateProductImageResponse(String message, String newImageUrl) {
        this.message = message;
        this.newImageUrl = newImageUrl;
    }

    public String getMessage() {
        return message;
    }

    public String getNewImageUrl() {
        return newImageUrl;
    }
}
