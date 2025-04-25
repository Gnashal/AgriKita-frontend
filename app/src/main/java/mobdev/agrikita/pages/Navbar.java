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
    ImageButton menuButton, profileButton;

    public Navbar() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navbar, container, false);

        menuButton = view.findViewById(R.id.menuIcon);
        profileButton = view.findViewById(R.id.profileIcon);

        profileButton.setOnClickListener(v -> toProfile());

        return view;
    }

    private void toProfile() {
        startActivity(new Intent(getContext(), Profile.class));
    }

}