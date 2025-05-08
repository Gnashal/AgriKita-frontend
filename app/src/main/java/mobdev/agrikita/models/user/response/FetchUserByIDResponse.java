package mobdev.agrikita.models.user.response;

import com.google.gson.annotations.SerializedName;

public class FetchUserByIDResponse {

    @SerializedName("data")
    private UserData data;

    public UserData getData() {
        return data;
    }

    public void setData(UserData data) {
        this.data = data;
    }

    public static class UserData {
        @SerializedName("ImageURL")
        private String imageURL;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("email")
        private String email;

        @SerializedName("has_shop")
        private boolean hasShop;

        @SerializedName("name")
        private String name;

        @SerializedName("phone")
        private String phone;

        @SerializedName("updated_at")
        private String updatedAt;

        @SerializedName("username")
        private String username;

        // Getters and Setters
        public String getImageURL() {
            return imageURL;
        }

        public void setImageURL(String imageURL) {
            this.imageURL = imageURL;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public boolean isHasShop() {
            return hasShop;
        }

        public void setHasShop(boolean hasShop) {
            this.hasShop = hasShop;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}