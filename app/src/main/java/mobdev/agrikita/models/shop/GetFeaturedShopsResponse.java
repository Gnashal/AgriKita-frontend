package mobdev.agrikita.models.shop;

import com.google.gson.annotations.SerializedName;

import java.util.List;

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
