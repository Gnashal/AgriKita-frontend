package mobdev.agrikita.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mobdev.agrikita.R;
import mobdev.agrikita.models.address.Address;

public class AddressAdapter extends BaseAdapter {
    private Context context;
    private List<Address> addressList;
    private LayoutInflater inflater;
    public AddressAdapter(Context context, List<Address> addressList) {
        this.context = context;
        this.addressList = addressList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return addressList.size();
    }

    @Override
    public Object getItem(int position) {
        return addressList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    static class ViewHolder {
        TextView nameTextView;
        TextView phoneTextView;
        TextView addressTextView;
        TextView defaultTag;
        TextView labelTag;
        ImageView deleteBtn;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.address_card, parent, false);
            holder = new ViewHolder();
            holder.nameTextView = convertView.findViewById(R.id.nameTextView);
            holder.phoneTextView = convertView.findViewById(R.id.phoneTextView);
            holder.addressTextView = convertView.findViewById(R.id.addressTextView);
            holder.defaultTag = convertView.findViewById(R.id.defaultTag);
            holder.labelTag = convertView.findViewById(R.id.labelTag);
            holder.deleteBtn = convertView.findViewById(R.id.deleteBtn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Address address = addressList.get(position);
        String addressText = address.getRegion() + ", " + address.getProvince() + ", " + address.getCity() + ", " + address.getBarangay() + ", " + address.getStreetName() + ", " + address.getZipCode();
        holder.nameTextView.setText(address.getName());
        holder.phoneTextView.setText(address.getPhone());
        holder.addressTextView.setText(addressText);
        holder.labelTag.setText(address.getLabel());

        if (address.isDefault()) {
            holder.defaultTag.setVisibility(View.VISIBLE);
        } else {
            holder.defaultTag.setVisibility(View.GONE);
        }

        holder.deleteBtn.setOnClickListener(v -> {
            addressList.remove(position);
            notifyDataSetChanged();
        });

        return convertView;
    }
}
