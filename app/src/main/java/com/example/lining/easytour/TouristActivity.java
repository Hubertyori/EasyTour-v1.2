package com.example.lining.easytour;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;

public class TouristActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        AdapterView.OnItemClickListener{
    private DrawerLayout drawerLayout;
    private ListView lv_list_postpaper;
    private String username;
    private String introduce;
    private String tel;
    private TextView tv_name;
    private TextView tv_intro;
    private TextView tv_tel;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        introduce = intent.getStringExtra("introduce");
        tel = intent.getStringExtra("tel");

        navigationView = findViewById(R.id.nav_view);
        View navigationview = navigationView.getHeaderView(0);
        tv_name = navigationview.findViewById(R.id.tv_guide_name);
        tv_intro = navigationview.findViewById(R.id.tv_intro);
        tv_tel = navigationview.findViewById(R.id.tv_tel);

        tv_name.setText(username+"");
        tv_intro.setText(introduce+"");
        tv_tel.setText(tel+"");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        lv_list_postpaper = (ListView) findViewById(R.id.tourist_listview);/*changed the name of tourist list view*/
        MyMainArrayAdapter adapter = new MyMainArrayAdapter(TouristActivity.this,0,getPostPaperData());
        lv_list_postpaper.setAdapter(adapter);
        lv_list_postpaper.setOnItemClickListener(this);


    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(TouristActivity.this,BoardActivity.class);
        startActivity(intent);

    }

    private List<Postpaper> getPostPaperData() {
        List<Postpaper> list= new ArrayList<Postpaper>();
        int[] images = new int[]{R.drawable.p1,R.drawable.p2,R.drawable.p3,R.drawable.p4,R.drawable.p5};
        for(int i=0;i<5;i++)
        {
            Postpaper p = new Postpaper(images[i],"Description"+Integer.toString(i));
            list.add(p);
        }
        return list;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar order_item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view order_item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_qurryorder) {
            Intent intent = new Intent();
            intent.setClass(TouristActivity.this,QurryActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_sendorder) {
            Intent intent = new Intent();
            intent.setClass(TouristActivity.this,SendOrderActivity.class);
            intent.putExtra("username",username);
            startActivity(intent);
        } else if (id == R.id.nav_message) {
            Intent intent = new Intent();
            intent.setClass(TouristActivity.this,MessageActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent();
            intent.setClass(TouristActivity.this,TouristSettingActivity.class);
            startActivity(intent);
        }else if(id == R.id.nav_quite){
            Intent intent = new Intent(TouristActivity.this,Login.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
