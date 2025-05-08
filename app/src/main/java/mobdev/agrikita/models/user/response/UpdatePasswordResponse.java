package mobdev.agrikita.models.user.response;

import com.google.gson.annotations.SerializedName;

public class UpdatePasswordResponse {
    @SerializedName("message")
    private String message;
    @SerializedName("error")
    private String error;

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
