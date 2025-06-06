package mobdev.agrikita.models.auth.request;
import com.google.gson.annotations.SerializedName;
public class SignupRequest {
    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("phone")
    private String phone;

    @SerializedName("username")
    private String username;

    @SerializedName("name")
    private String name;

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Constructor
    public SignupRequest(String email, String password, String phone, String username, String name) {
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.username = username;
        this.name = name;
    }
}
