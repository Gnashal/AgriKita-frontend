package mobdev.agrikita.models.shop;

public class Shop {
    private String shopId;
    private String userId;
    private String shopImgUrl;
    private String name;
    private String addr;
    private String zipcode;
    private String desc;

    // Constructor
    public Shop(String shopId, String userId, String shopImgUrl, String name, String addr, String zipcode, String desc) {
        this.shopId = shopId;
        this.userId = userId;
        this.shopImgUrl = shopImgUrl;
        this.name = name;
        this.addr = addr;
        this.zipcode = zipcode;
        this.desc = desc;
    }

    // Getters and Setters
    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getShopImgUrl() {
        return shopImgUrl;
    }

    public void setShopImgUrl(String shopImgUrl) {
        this.shopImgUrl = shopImgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "shopId='" + shopId + '\'' +
                ", userId='" + userId + '\'' +
                ", shopImgUrl='" + shopImgUrl + '\'' +
                ", name='" + name + '\'' +
                ", addr='" + addr + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
