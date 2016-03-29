package com.awesome.byunghwa.app.alexandria;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.awesome.byunghwa.app.alexandria.api.Callback;
import com.awesome.byunghwa.app.alexandria.util.LogUtil;


public class MainActivity extends AppCompatActivity implements Callback {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    //private NavigationDrawerFragment navigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #()}.
     */
    private CharSequence title;
    public static boolean IS_TABLET = false;
    private BroadcastReceiver messageReciever;

    public static final String MESSAGE_EVENT = "MESSAGE_EVENT";
    public static final String MESSAGE_KEY = "MESSAGE_EXTRA";
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IS_TABLET = isTablet();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
        }

        if(IS_TABLET){
            setContentView(R.layout.activity_main_tablet);
        }else {
            setContentView(R.layout.activity_main);
        }

        messageReciever = new MessageReciever();
        IntentFilter filter = new IntentFilter(MESSAGE_EVENT);
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReciever,filter);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_tablet);
        if (toolbar != null) {
            setSupportActionBar(toolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        // on activity initial start, there is no fragment inflated. we should initialize one
        if (savedInstanceState == null) {
            // first read from shared prefs

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String startScreenSettings = prefs.getString("pref_startFragment", "0");

            Fragment fragment = null;
            String tag = null;
            if (startScreenSettings.equals("0")) {
                fragment = new ListOfBooks();
                tag = String.valueOf(R.id.drawer_book_list);
                navigationView.setCheckedItem(R.id.drawer_book_list);
            } else if (startScreenSettings.equals("1")) {
                fragment = new AddBook();
                tag = String.valueOf(R.id.drawer_add_scan_book);
                navigationView.setCheckedItem(R.id.drawer_add_scan_book);
            }

            int containerViewId= R.id.container;

            getSupportFragmentManager().beginTransaction()
                    .replace(containerViewId, fragment, tag)
                    .addToBackStack(tag)
                    .commit();
        }

        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    mDrawerLayout.closeDrawers();
                    FragmentManager fragmentManager = getSupportFragmentManager();

                    if (fragmentManager.findFragmentByTag(String.valueOf(menuItem.getItemId())) != null) {
                        LogUtil.log_i("MainActivity", "popping from back stack...");
                        fragmentManager.popBackStack(String.valueOf(menuItem.getItemId()), 0);
                        return true;
                    } else {
                        Fragment nextFragment = null;

                        switch (menuItem.getItemId()){

                            case R.id.drawer_book_list:
                                nextFragment = new ListOfBooks();
                                if (getSupportActionBar() != null) {
                                    getSupportActionBar().setTitle(getResources().getString(R.string.drawer_book_list));
                                }
                                break;
                            case R.id.drawer_add_scan_book:
                                nextFragment = new AddBook();
                                if (getSupportActionBar() != null) {
                                    getSupportActionBar().setTitle(getResources().getString(R.string.drawer_add_scan_book));
                                }
                                break;
                            case R.id.drawer_about:
                                nextFragment = new About();
                                if (getSupportActionBar() != null) {
                                    getSupportActionBar().setTitle(getResources().getString(R.string.drawer_about));
                                }
                                break;
                            default:
                        }

                        int containerViewId = R.id.container;

                        fragmentManager.beginTransaction()
                                .replace(containerViewId, nextFragment, String.valueOf(menuItem.getItemId()))
                                .addToBackStack(String.valueOf(menuItem.getItemId()))
                                .commit();
                        return true;
                    }
                }
            });
        }

    }

    public void setTitle(int titleId) {
        title = getString(titleId);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //if (!navigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            //restoreActionBar();
            //return true;
        //}
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                // if user is in book detail fragment, then do not open drawer
                if (getSupportFragmentManager().findFragmentByTag("Book Detail") != null && (findViewById(R.id.right_container) == null)) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }

                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReciever);
        super.onDestroy();
    }

    @Override
    public void onItemSelected(String ean) {
        //Toast.makeText(this, "on item selected gets called...", Toast.LENGTH_SHORT).show();
        Bundle args = new Bundle();
        args.putString(BookDetail.EAN_KEY, ean);

        BookDetail fragment = new BookDetail();
        fragment.setArguments(args);

        int id = R.id.container;
        if(findViewById(R.id.right_container) != null){
            id = R.id.right_container;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(id, fragment, "Book Detail")
                .addToBackStack("Book Detail")
                .commit();

    }

    private class MessageReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra(MESSAGE_KEY)!=null){
                Toast.makeText(MainActivity.this, intent.getStringExtra(MESSAGE_KEY), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void goBack(View view){
        getSupportFragmentManager().popBackStack();
    }

    private boolean isTablet() {
        return (getApplicationContext().getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(navigationView)) {
            mDrawerLayout.closeDrawers();
            return;
        }
        if(getSupportFragmentManager().getBackStackEntryCount()<2){
            finish();
        }
        super.onBackPressed();
    }


}