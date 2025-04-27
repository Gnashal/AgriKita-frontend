package mobdev.agrikita.models.user;

import com.google.gson.annotations.SerializedName;

public class UpdateUserResponse {
    @SerializedName("message")
    private String message;
    @SerializedName("error")
    private String error;

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }
}

