package mobdev.agrikita.pages;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import mobdev.agrikita.MainActivity;
import mobdev.agrikita.R;
import mobdev.agrikita.models.user.CurrentUser;
import mobdev.agrikita.pages.Login;

public class Navbar extends Fragment {
    ImageButton menuButton, profileButton, cartButton;

    public Navbar() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navbar, container, false);

        menuButton = view.findViewById(R.id.menuIcon);
        profileButton = view.findViewById(R.id.profileButton);
        cartButton = view.findViewById(R.id.cartIcon);

        profileButton.setOnClickListener(v -> toProfile());
        cartButton.setOnClickListener(v -> toShoppingCart());
       /* menuButton.setOnClickListener(v -> logout());*/

        return view;
    }

    private void toProfile() {
        startActivity(new Intent(getContext(), Profile.class));
    }

    private void toShoppingCart() { startActivity(new Intent(getContext(), ShoppingCartPage.class)); }

    /*private void logout() {
        SharedPreferences authPrefs = requireActivity().getSharedPreferences("AuthPrefs", getContext().MODE_PRIVATE);
        SharedPreferences.Editor authEditor = authPrefs.edit();
        authEditor.remove("idToken");
        authEditor.remove("refreshToken");
        authEditor.remove("localId");
        authEditor.putBoolean("isLoggedIn", false);
        authEditor.apply();
        SharedPreferences userPrefs = requireActivity().getSharedPreferences("UserPrefs", getContext().MODE_PRIVATE);
        SharedPreferences.Editor userEditor = userPrefs.edit();
        userEditor.remove("UserID");
        if (userPrefs.getBoolean("HasShop", false)) {
            userEditor.remove("ShopID");
        }
        userEditor.remove("HasShop");
        userEditor.apply();

        CurrentUser.clear();
        Intent intent = new Intent(requireActivity(), Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        requireActivity().finish();
    }*/

}