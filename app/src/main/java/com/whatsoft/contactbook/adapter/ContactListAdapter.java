package com.whatsoft.contactbook.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.whatsoft.contactbook.R;
import com.whatsoft.contactbook.activity.ProfileActivity;
import com.whatsoft.contactbook.model.Contact;
import com.whatsoft.contactbook.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mb on 3/4/16
 */
public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> implements Filterable {
    private List<Contact> mValues;
    private List<Contact> mValuesData;

    public ContactListAdapter(List<Contact> items) {
        mValues = items;
        mValuesData = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Contact contact = getItem(position);
        holder.contact = contact;
        holder.tvName.setText(contact.getName());
        holder.tvAddress.setText(contact.getAddress());

        Glide.with(holder.imvAvatar.getContext())
                .load(contact.getGender().getIcon())
                .fitCenter()
                .into(holder.imvAvatar);
    }

    private Contact getItem(int position) {
        return mValues.get(position);
    }

    @Override
    public int getItemCount() {
        if (mValues == null) {
            return 0;
        }
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public Contact contact;

        public final View mView;
        public final ImageView imvAvatar, imvDirection;
        public final TextView tvName, tvAddress;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            imvAvatar = (ImageView) view.findViewById(R.id.imv_avatar);
            imvDirection = (ImageView) view.findViewById(R.id.imv_direction);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvAddress = (TextView) view.findViewById(R.id.tv_address);
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra(ProfileActivity.EXTRA_CONTACT, contact);
                    context.startActivity(intent);
                }
            });
            imvDirection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
//                    String s = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", 10.8385579f, 106.6766431f, "47 Nguyễn Văn Lượng, Phường 17, Gò Vấp, Hồ Chí Minh, Việt Nam");
                    String s = String.format("google.navigation:q=%s", Utils.formatAddress(contact));
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
                    i.setClassName("com.google.android.apps.maps",
                            "com.google.android.maps.MapsActivity");
                    context.startActivity(i);
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvName.getText();
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                List<Contact> result = new ArrayList<>();
                if (TextUtils.isEmpty(constraint)) {
                    result.addAll(mValuesData);
                } else {
                    for (Contact contact : mValuesData) {
                        if (contact.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            result.add(contact);
                        }
                    }
                }
                filterResults.values = result;
                filterResults.count = result.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mValues = (List<Contact>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
