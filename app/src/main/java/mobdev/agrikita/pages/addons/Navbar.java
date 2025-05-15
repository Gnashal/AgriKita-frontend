package mobdev.agrikita.pages.addons;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.List;

import mobdev.agrikita.R;
import mobdev.agrikita.controllers.NotificationService;
import mobdev.agrikita.models.notifications.NotificationList;
import mobdev.agrikita.models.notifications.Notifications;
import mobdev.agrikita.pages.addons.checkout.ShoppingCartPage;
import mobdev.agrikita.pages.index.Home;
import mobdev.agrikita.pages.index.Profile;

public class Navbar extends Fragment {
    ImageView profileButton;
    ImageButton menuButton, cartButton, logoButton, notificationButton;
    View notificationDot;


    public Navbar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navbar, container, false);

        menuButton = view.findViewById(R.id.menuIcon);
        profileButton = view.findViewById(R.id.profileIcon);
        cartButton = view.findViewById(R.id.cartIcon);
        logoButton = view.findViewById(R.id.homeButton);
        notificationButton = view.findViewById(R.id.notificationIcon);
        notificationDot = view.findViewById(R.id.notificationDot);

        profileButton.setOnClickListener(v -> toProfile());
        cartButton.setOnClickListener(v -> toShoppingCart());
        logoButton.setOnClickListener(v -> toHome());
        notificationButton.setOnClickListener(v -> toNotification());

        checkUnreadNotifications();

        return view;
    }

    private void toProfile() {
        startActivity(new Intent(getContext(), Profile.class));
    }
    private void toHome() {
        startActivity(new Intent(getContext(), Home.class ));
    }
    private void toShoppingCart() { startActivity(new Intent(getContext(), ShoppingCartPage.class)); }

    private void toNotification() { startActivity(new Intent(getContext(), Notification.class));}
    /*TODO: Fix this*/
    /*private void setProfilePic() {
        CurrentUser currentUser = CurrentUser.getInstance(getContext());
        if (currentUser.getImageUrl() != null && !currentUser.getImageUrl().isEmpty()) {
            Glide.with(this)
                    .load(currentUser.getImageUrl())
                    .circleCrop()
                    .into(profileButton);

        }
    }*/

    private void checkUnreadNotifications() {
       boolean hasUnreadNotifications = NotificationList.getInstance().hasUnreadNotifications();
       if (hasUnreadNotifications) {
           notificationDot.setVisibility(View.VISIBLE);
       } else {
           notificationDot.setVisibility(View.GONE);
       }
    }


}

