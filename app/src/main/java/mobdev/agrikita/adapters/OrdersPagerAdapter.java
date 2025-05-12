package mobdev.agrikita.adapters;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class OrdersPagerAdapter extends FragmentStateAdapter {

    private final String[] statuses = {"All", "Processing", "In Transit", "Delivered"};

    public OrdersPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mobdev.agrikita.adapters.OrderListFragment.newInstance(statuses[position]);
    }
    

    @Override
    public int getItemCount() {
        return statuses.length;
    }

    public String getTabTitle(int position) {
        return statuses[position];
    }
}
