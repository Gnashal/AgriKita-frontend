package mobdev.agrikita.models.shop.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import mobdev.agrikita.models.shop.Shop;

public class GetFeaturedShopsResponse {
    @SerializedName("message")
    private String message;
    @SerializedName("shops")
    private List<Shop> shops;

    public String getMessage() {
        return message;
    }

    public List<Shop> getShops() {
        return shops;
    }
}
