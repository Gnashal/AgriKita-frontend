package mobdev.agrikita.models.user;

import com.google.gson.annotations.SerializedName;

public class UpdatePasswordRequest {
    @SerializedName("uid")
    private String uid;
    @SerializedName("password")
    private String password;

    public UpdatePasswordRequest(String uid, String password) {
        this.uid = uid;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUid() {
        return uid;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
