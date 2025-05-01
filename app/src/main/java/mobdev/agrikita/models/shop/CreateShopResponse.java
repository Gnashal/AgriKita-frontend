package mobdev.agrikita.models.shop;

import com.google.gson.annotations.SerializedName;

public class CreateShopResponse {

    @SerializedName("message")
    private String message;

    @SerializedName("shop_id")
    private String shopId;

    public CreateShopResponse() {}

    public CreateShopResponse(String message, String shopId) {
        this.message = message;
        this.shopId = shopId;
    }

    public String getMessage() {
        return message;
    }

    public String getShopId() {
        return shopId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
}
