package mobdev.agrikita.models.shop.response;

import com.google.gson.annotations.SerializedName;

public class GetShopByShopIDResponse {

    @SerializedName("ImageUrl")
    private String imageUrl;

    @SerializedName("address")
    private String address;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("description")
    private String description;

    @SerializedName("name")
    private String name;

    @SerializedName("owner")
    private Owner owner;

    @SerializedName("shop_id")
    private String shopId;

    @SerializedName("status")
    private String status;

    @SerializedName("zip_code")
    private String zipCode;

    public static class Owner {
        @SerializedName("Parent")
        private Parent parent;

        @SerializedName("Path")
        private String path;

        @SerializedName("ID")
        private String id;

        public static class Parent {
            @SerializedName("Parent")
            private Object parent;

            @SerializedName("Path")
            private String path;

            @SerializedName("ID")
            private String id;

            public Object getParent() { return parent; }
            public void setParent(Object parent) { this.parent = parent; }

            public String getPath() { return path; }
            public void setPath(String path) { this.path = path; }

            public String getId() { return id; }
            public void setId(String id) { this.id = id; }
        }

        public Parent getParent() { return parent; }
        public void setParent(Parent parent) { this.parent = parent; }

        public String getPath() { return path; }
        public void setPath(String path) { this.path = path; }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
    }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Owner getOwner() { return owner; }
    public void setOwner(Owner owner) { this.owner = owner; }

    public String getShopId() { return shopId; }
    public void setShopId(String shopId) { this.shopId = shopId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }
}
