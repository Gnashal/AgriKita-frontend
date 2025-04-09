package mobdev.agrikita.models;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("idToken")
    private String idToken;

    @SerializedName("refreshToken")
    private String refreshToken;

    @SerializedName("expiresIn")
    private String expiresIn;

    @SerializedName("localId")
    private String localId;

    // Constructor
    public LoginResponse(String idToken, String refreshToken, String expiresIn, String localId) {
        this.idToken = idToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.localId = localId;
    }

    // Getters and Setters
    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }
}
