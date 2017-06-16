package pl.edu.wat.gymnotes;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

public class NavigationActivity extends BaseActivity {

    private static final int MAIN_SITE_MENU_INDEX = 1;
    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private MenuItem currentMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
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
                break;

            case R.id.nav_third_activity:
                intent = new Intent(this, DiaryActivity.class);
                startActivity(intent);
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

        // Highlight the selected item has been done by NavigationView
//        currentMenuItem.setChecked(true);
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        nvDrawer.getMenu().getItem(MAIN_SITE_MENU_INDEX).setChecked(true);
    }

}
