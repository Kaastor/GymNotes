package pl.edu.wat.gymnotes.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.logging.Level;
import java.util.logging.Logger;

import pl.edu.wat.gymnotes.fragments.DailyExercisesFragment;
import pl.edu.wat.gymnotes.fragments.DiaryCalendarDialog;
import pl.edu.wat.gymnotes.R;
import pl.edu.wat.gymnotes.data.ExerciseDbHelper;

public class NavigationActivity extends BaseActivity {

    private Logger logger = Logger.getLogger(NavigationActivity.class.toString());

    private static final int MAIN_SITE_MENU_INDEX = 1;
    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private MenuItem currentMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logger.log(Level.INFO, "onCreate");

        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        View headerView = nvDrawer.getHeaderView(0);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        setupDrawerContent(nvDrawer);

        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);
        Class startFragment = DailyExercisesFragment.class;

        try {
            fragmentManager.beginTransaction().replace(R.id.flContent, (DailyExercisesFragment)startFragment.newInstance()).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        View headerV= nvDrawer.getHeaderView(0);
        TextView navHeader = (TextView)  headerV.findViewById(R.id.navigation_header_userName);
        navHeader.setText(new ExerciseDbHelper(getApplicationContext()).getUserName(LoginActivity.activeUserEmail));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_navigation;
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE 1: Make sure to override the method with only a single `Bundle` argument
    // Note 2: Make sure you implement the correct `onPostCreate(Bundle savedInstanceState)` method.
    // There are 2 signatures and only `onPostCreate(Bundle state)` shows the hamburger icon.
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        currentMenuItem = menuItem;
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass = DailyExercisesFragment.class;
        switch(menuItem.getItemId()) {
            case R.id.nav_first_activity:
                Intent intent = new Intent(this, CatalogActivity.class);
                startActivity(intent);
                logger.log(Level.INFO, "Send intent to CatalogActivity");
                break;
            case R.id.nav_third_activity:
                new DiaryCalendarDialog().show(getSupportFragmentManager(), "datePicker");
                logger.log(Level.INFO, "Showed DatePicker for DiaryCalendarDialog");
                break;
            case R.id.nav_forth_activity:
                logout();
                startActivity(new Intent(this, LoginActivity.class));
                logger.log(Level.INFO, "Send intent to LoginActivity");
                this.finish();
                logger.log(Level.INFO, "finished");
                break;
            default:
                fragmentClass = DailyExercisesFragment.class;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        mDrawer.closeDrawers();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        logger.log(Level.INFO, "onRestart");
        nvDrawer.getMenu().getItem(MAIN_SITE_MENU_INDEX).setChecked(true);
    }

    private void logout(){
        logger.log(Level.INFO, "sharedPreferences cleared");
        getApplicationContext().getSharedPreferences("userLogin",
                MODE_PRIVATE).edit().putString("userEmail", "").apply();
    }

}
