package mobdev.agrikita.pages.shop;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import mobdev.agrikita.R;
import mobdev.agrikita.adapters.OrdersPagerAdapter;

public class MyOrders extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private OrdersPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPagerOrders);

        pagerAdapter = new OrdersPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            View customTabView = getLayoutInflater().inflate(R.layout.custom_tab, null);
            TextView tabTextView = customTabView.findViewById(R.id.tabText);
            tabTextView.setText(pagerAdapter.getTabTitle(position));

            tab.setCustomView(customTabView);
        }).attach();
    }
}
