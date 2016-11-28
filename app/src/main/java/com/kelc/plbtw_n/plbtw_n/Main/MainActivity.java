package com.kelc.plbtw_n.plbtw_n.Main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.kelc.plbtw_n.plbtw_n.LoginAndRegister.LoginActivity;
import com.kelc.plbtw_n.plbtw_n.LoginAndRegister.RegisterActivity;
import com.kelc.plbtw_n.plbtw_n.Main.Etertainment.EntertainmentFragment;
import com.kelc.plbtw_n.plbtw_n.Main.TopNews.TopFragment;
import com.kelc.plbtw_n.plbtw_n.Main.Olahraga.OlahragaFragment;
import com.kelc.plbtw_n.plbtw_n.Main.News.NewsFragment;
import com.kelc.plbtw_n.plbtw_n.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager viewPager_main_activity;
    private TabLayout tabLayout_main_activity;
    private NavigationView navView;
    private int opentabs = 0;
    private boolean flag_log_out=false;

    private SharedPreferences shr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        shr = getSharedPreferences(getString(R.string.userpref), MODE_PRIVATE);
        if(shr.contains("keyUsername")){
            //Change Name To Logged In User
           navView = (NavigationView) findViewById(R.id.nav_view);
            View headerView = navView.getHeaderView(0);
            TextView UserName = (TextView) headerView.findViewById(R.id.textViewProfil);
            UserName.setText(shr.getString("keyUsername", ""));

            //Change Button To LogOut If User Logged In Already
            Menu menuView = navView.getMenu();
            MenuItem btnLogin = (MenuItem) menuView.findItem(R.id.nav_login);
            if(btnLogin.getTitle().toString().equalsIgnoreCase(getString(R.string.login))){
                btnLogin.setTitle(getString(R.string.logout));
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //ViewPager and TabLayout-----------------------------
        viewPager_main_activity = (ViewPager) findViewById(R.id.viewpager_main_activity);
        setupViewPager(viewPager_main_activity);
        viewPager_main_activity.setOffscreenPageLimit(3);
        viewPager_main_activity.setCurrentItem(opentabs);
        tabLayout_main_activity = (TabLayout) findViewById(R.id.tabs_main_activity);
        tabLayout_main_activity.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout_main_activity.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout_main_activity.setupWithViewPager(viewPager_main_activity);

        tabLayout_main_activity.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0)
                {
                    getSupportActionBar().setTitle("Top");
                }
                else if(tab.getPosition() == 1)
                {
                    getSupportActionBar().setTitle("News");
                }
                else if(tab.getPosition() == 2)
                {
                    getSupportActionBar().setTitle("Olahraga");
                }
                else if(tab.getPosition() == 3)
                {
                    getSupportActionBar().setTitle("Entertainment");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        if(shr.contains("keyUsername")) {
            adapter.addFrag(new TopFragment(), "Top");

            if(shr.getString("keyCategory", "").equalsIgnoreCase("News")) {
                adapter.addFrag(new NewsFragment(), "News");
            }
            else if(shr.getString("keyCategory", "").equalsIgnoreCase("Olahraga")) {
                adapter.addFrag(new OlahragaFragment(), "Olahraga");
            }
            else if(shr.getString("keyCategory", "").equalsIgnoreCase("Entertaiment")) {
                adapter.addFrag(new EntertainmentFragment(), "Entertaiment");
            }
        }
        else{
            adapter.addFrag(new TopFragment(), "Top");
            adapter.addFrag(new NewsFragment(), "News");
            adapter.addFrag(new OlahragaFragment(), "Olahraga");
            adapter.addFrag(new EntertainmentFragment(), "Entertaiment");
        }

        viewPager.setAdapter(adapter);
    }

    private void Logout(MenuItem item){
        SharedPreferences.Editor editor = shr.edit();
        editor.clear();
        editor.apply();

        item.setTitle(getString(R.string.login));

        ((TextView) navView.getHeaderView(0).findViewById(R.id.textViewProfil)).setText("");

        setupViewPager(viewPager_main_activity);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_login) {
            if(item.getTitle().toString().equalsIgnoreCase("Login")){
                //Button title is login, do login
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
            else if(item.getTitle().toString().equalsIgnoreCase("Logout")){
                //Button title is logout, do logout
                this.Logout(item);
            }
        }
        else if (id == R.id.nav_register) {
            startActivity(new Intent(MainActivity.this,RegisterActivity.class));
        }
        else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
