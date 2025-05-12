package mobdev.agrikita.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import mobdev.agrikita.R;
import mobdev.agrikita.controllers.AddressController;
import mobdev.agrikita.models.address.Address;
import mobdev.agrikita.models.user.CurrentUser;
import mobdev.agrikita.pages.addons.address.AddressPage;

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
            long ok = AddressController.getInstance(context).deleteAddress(CurrentUser.getInstance(context).getUid(), address.getDbIndex());
            if (ok < 0) {
                Log.v("SqliteAddress", "Failed to delete address");
            } else {
               Toast.makeText(context, "You deleted an address", Toast.LENGTH_SHORT).show();
            }
            if (addressList.isEmpty()) {
                context.startActivity(new Intent(context, AddressPage.class));
                if (context instanceof android.app.Activity) {
                    ((android.app.Activity) context).finish();
                }
            }
            notifyDataSetChanged();
        });

        return convertView;
    }
}
