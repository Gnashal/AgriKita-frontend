package mobdev.agrikita.models.user;

import android.content.Context;
import android.util.Log;

import java.util.Map;

import mobdev.agrikita.controllers.UserService;
import mobdev.agrikita.models.user.response.UserResponse;

public class CurrentUser {
    private static CurrentUser instance;

    private UserService userService;

    private String uid;
    private Map<String, Object> userData;
    private Map<String, Object> shopData;
    private Context context;

    // Private constructor to enforce singleton
    private CurrentUser(Context context) {
        this.context = context;
        userService = new UserService(context);
    }

    // Accessor to get the single instance
    public static synchronized CurrentUser getInstance(Context context) {
        if (instance == null) {
            instance = new CurrentUser(context);
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
    public String getImageUrl() {
        return userData != null ? (String) userData.get("ImageURL") : null;
    }

    /*SHOP getters*/
    public String getShopName() {
        return shopData != null ? (String) shopData.get("name") : null;
    }

    public String getShopId() {
        if (shopData != null && shopData.get("id") instanceof String) {
            return (String) shopData.get("id");
        }
        return null;
    }

    public void fetchUserData(String uid, final UserService.FetchUserCallback callback) {
        userService.fetchUserData(uid, new UserService.FetchUserCallback() {
            @Override
            public void onSuccess(UserResponse userResponse) {
                setUserData(userResponse.user);
                setShopData(userResponse.shop);
                Log.v("CurrentUser", "Shop data: " + CurrentUser.getInstance(context).getShopData());
                Log.v("CurrentUser", "User data: " + CurrentUser.getInstance(context).getUserData());
                setUid(uid);
                Log.v("UserSetup", "User and shop data successfully set.");
                if (callback != null) callback.onSuccess(userResponse);
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("UserSetup", errorMessage);
                if (callback != null) callback.onFailure(errorMessage);
            }
        });
    }

}
