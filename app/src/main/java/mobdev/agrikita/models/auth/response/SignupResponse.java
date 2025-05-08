package mobdev.agrikita.models.auth.response;

import com.google.gson.annotations.SerializedName;

import mobdev.agrikita.models.user.UserRecord;

public class SignupResponse {
    @SerializedName("message")
    private String message;

    @SerializedName("newUser")
    private UserRecord newUser;

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public UserRecord getNewUser() {
        return newUser;
    }

    public void setNewUser(UserRecord newUser) {
        this.newUser = newUser;
    }

    public String getEmail() {
        return newUser != null ? newUser.getEmail() : null;
    }
}
