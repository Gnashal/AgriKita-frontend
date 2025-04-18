package mobdev.agrikita.models.user;

import java.util.Map;

public class CurrentUser {
    private static CurrentUser instance;

    private String uid;
    private Map<String, Object> userData;
    private Map<String, Object> shopData;

    // Private constructor to enforce singleton
    private CurrentUser() {}

    // Accessor to get the single instance
    public static synchronized CurrentUser getInstance() {
        if (instance == null) {
            instance = new CurrentUser();
        }
        return instance;
    }

    // Clear method in case of logout or reset
    public static void clear() {
        instance = null;
    }

    // Getters and setters
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Map<String, Object> getUserData() {
        return userData;
    }

    public void setUserData(Map<String, Object> userData) {
        this.userData = userData;
    }

    public Map<String, Object> getShopData() {
        return shopData;
    }

    public void setShopData(Map<String, Object> shopData) {
        this.shopData = shopData;
    }

    public boolean hasShop() {
        return shopData != null;
    }

    /*USER Getters*/


    public String getUserUsername() {
        return userData != null ? (String) userData.get("username") : null;
    }
    public String getUserName() {
        return userData != null ? (String) userData.get("name") : null;
    }

    public String getUserEmail() {
        return userData != null ? (String) userData.get("email") : null;
    }

    public String getUserPhone() {
        return userData != null ? (String) userData.get("phone") : null;
    }

    /*SHOP getters*/
    public String getShopName() {
        return shopData != null ? (String) shopData.get("name") : null;
    }

    public String getShopId() {
        return shopData != null ? (String) shopData.get("id") : null;
    }

}
