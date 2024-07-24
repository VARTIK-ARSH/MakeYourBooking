package com.example.apifetching;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    private static final String TAG = "MainActivity2";
    private TextView userLogin, userName;
    private ImageView userLogo;


    private ListView listView;
    private List<MenuItem> menuItems;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);






        drawerLayout = findViewById(R.id.drawel);
        navigationView = findViewById(R.id.navigationview);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        setupDrawerToggle();

        userLogin = findViewById(R.id.user_login);
        userName = findViewById(R.id.user_name);
        userLogo = findViewById(R.id.user_logo);
        listView = findViewById(R.id.listView);
        menuItems = new ArrayList<>();

        Intent intent = getIntent();
        String response = intent.getStringExtra("response");

        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONObject userInfo = jsonResponse.getJSONObject("userInfo");

            userLogin.setText(userInfo.getString("user_login"));
            userName.setText(userInfo.getString("user_name"));

            String logoUrl = userInfo.getString("user_logo").trim();
            Log.d(TAG, "Logo URL: " + logoUrl);

            Glide.with(this)
                    .load(logoUrl)
                    .placeholder(R.drawable.user_logo)
                    .into(userLogo);

            JSONArray headers = userInfo.getJSONArray("headers");

            for (int i = 0; i < headers.length(); i++) {
                JSONObject header = headers.getJSONObject(i);
                String headerName = header.getString("header_name_la");
                JSONArray menuArray = header.getJSONArray("menu");

                List<SubMenuItem> subMenuItems = new ArrayList<>();

                for (int j = 0; j < menuArray.length(); j++) {
                    JSONObject menuItem = menuArray.getJSONObject(j);
                    String menuName = menuItem.getString("mn_name_la");
                    String menuIconUrl = menuItem.getString("mn_icon");
                    String fullIconUrl = "http://www.umraoperation.com/images/" + menuIconUrl;
                    Log.d(TAG, "Menu Icon URL: " + fullIconUrl);
                    subMenuItems.add(new SubMenuItem(menuName, fullIconUrl));
                }

                menuItems.add(new MenuItem(headerName, subMenuItems));
            }

            MenuAdapter adapter = new MenuAdapter(this, menuItems);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener((parent, view, position, id) -> {

                MenuItem clickedItem = menuItems.get(position);
                Intent newIntent;
                switch (position) {
                    case 0:
                        newIntent = new Intent(MainActivity2.this, Activity1.class);
                        break;
                    case 1:
                        newIntent = new Intent(MainActivity2.this, Activity2.class);
                        break;
                    case 2:
                        newIntent = new Intent(MainActivity2.this, Activity3.class);
                        break;
                    case 3:
                        newIntent = new Intent(MainActivity2.this, Activity4.class);
                        break;
                    default:
                        newIntent = new Intent(MainActivity2.this, DefaultActivity.class);
                        break;
                }
                startActivity(newIntent);
                drawerLayout.closeDrawer(GravityCompat.START);
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setupDrawerToggle() {
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }






    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }




}


