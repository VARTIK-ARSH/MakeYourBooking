//package com.example.apifetching;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.squareup.picasso.Picasso;
//
//import java.util.List;
//
//public class MenuAdapter extends BaseAdapter {
//
//    private Context context;
//    private List<MenuItem> menuItems;
//    private LayoutInflater inflater;
//
//    public MenuAdapter(Context context, List<MenuItem> menuItems) {
//        this.context = context;
//        this.menuItems = menuItems;
//        this.inflater = LayoutInflater.from(context);
//    }
//
//    @Override
//    public int getCount() {
//        return menuItems.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return menuItems.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        if (convertView == null) {
//            convertView = inflater.inflate(R.layout.list_item, parent, false);
//        }
//
//        TextView headerName = convertView.findViewById(R.id.header_name);
//        LinearLayout subMenuContainer = convertView.findViewById(R.id.sub_menu_container);
//
//        MenuItem menuItem = menuItems.get(position);
//
//        headerName.setText(menuItem.getHeaderName());
//
//        subMenuContainer.removeAllViews();
//
//        for (SubMenuItem subMenuItem : menuItem.getSubMenuItems()) {
//            View subMenuView = inflater.inflate(R.layout.sub_item_list, subMenuContainer, false);
//
//            TextView menuName = subMenuView.findViewById(R.id.menu_name);
//            ImageView subMenuIcon = subMenuView.findViewById(R.id.sub_menu_icon);
//
//            menuName.setText(subMenuItem.getMenuName());
//
//            Picasso.get()
//                    .load(subMenuItem.getMenuIconUrl())
//                    .placeholder(R.drawable.ic_launcher_background)
//                    .error(R.drawable.ic_launcher_background)
//                    .into(subMenuIcon);
//
//            subMenuContainer.addView(subMenuView);
//        }
//
//        return convertView;
//    }
//}
package com.example.apifetching;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MenuAdapter extends BaseAdapter {

    private Context context;
    private List<MenuItem> menuItems;
    private LayoutInflater inflater;

    public MenuAdapter(Context context, List<MenuItem> menuItems) {
        this.context = context;
        this.menuItems = menuItems;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return menuItems.size();
    }

    @Override
    public Object getItem(int position) {
        return menuItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.headerName = convertView.findViewById(R.id.header_name);
            holder.subMenuContainer = convertView.findViewById(R.id.sub_menu_container);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MenuItem menuItem = menuItems.get(position);

        holder.headerName.setText(menuItem.getHeaderName());

        holder.subMenuContainer.removeAllViews();

        for (SubMenuItem subMenuItem : menuItem.getSubMenuItems()) {
            View subMenuView = inflater.inflate(R.layout.sub_item_list, holder.subMenuContainer, false);

            TextView menuName = subMenuView.findViewById(R.id.menu_name);
            ImageView subMenuIcon = subMenuView.findViewById(R.id.sub_menu_icon);

            menuName.setText(subMenuItem.getMenuName());

            Picasso.get()
                    .load(subMenuItem.getMenuIconUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(subMenuIcon);

            holder.subMenuContainer.addView(subMenuView);
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView headerName;
        LinearLayout subMenuContainer;
    }
}
