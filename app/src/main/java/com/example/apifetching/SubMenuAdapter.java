package com.example.apifetching;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class SubMenuAdapter extends BaseAdapter {

    private Context context;
    private List<SubMenuItem> subMenuItems;
    private LayoutInflater inflater;

    public SubMenuAdapter(Context context, List<SubMenuItem> subMenuItems) {
        this.context = context;
        this.subMenuItems = subMenuItems;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return subMenuItems.size();
    }

    @Override
    public Object getItem(int position) {
        return subMenuItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.sub_item_list, parent, false);
        }

        TextView menuName = convertView.findViewById(R.id.menu_name);
        ImageView subMenuIcon = convertView.findViewById(R.id.sub_menu_icon);

        SubMenuItem subMenuItem = subMenuItems.get(position);

        menuName.setText(subMenuItem.getMenuName());

        Picasso.get()
                .load(subMenuItem.getMenuIconUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(subMenuIcon);

        return convertView;
    }
}
