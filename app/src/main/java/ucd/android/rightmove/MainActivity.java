package ucd.android.rightmove;

import android.app.FragmentManager;
import android.os.Bundle;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import ucd.android.rightmove.Fragments.GmapFragment;
import ucd.android.rightmove.Fragments.ImportFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // An actionBarDrawerToggle is a special type of DrawerListener that works with an action bar.It allows you
        // to listen for DL events and it also allows you to open and close the drawer by clicking on an icon on the action bar

        toggle = new ActionBarDrawerToggle( this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (savedInstanceState != null ){
            //Toast.makeText(getApplicationContext(), "Not first time in App ...", Toast.LENGTH_LONG).show();
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Create a fragmentManager that will be used for interfacing with fragments associated with this Activity.

        FragmentManager fm = getFragmentManager();

        // We can put any fragment in the context_frame Layout using replace() method in Activity.

        //fm.beginTransaction().replace(R.id.content_frame, new GmapFragment()).commit();

        fm.beginTransaction().replace(R.id.content_frame, new ImportFragment()).commit();
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

        // need to ensure that the ActionBarDrawer Toggle can handle being clicked.

        if (toggle.onOptionsItemSelected(item)){
            return true;
        }


        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_settings) {

            Toast.makeText(getApplicationContext(), "No Settings Available...", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected( MenuItem item ) {

        FragmentManager fm = getFragmentManager();

        int id = item.getItemId();

        //getSupportActionBar().setTitle("Camera ..");

        if (id == R.id.nav_camara) {
            fm.beginTransaction().replace(R.id.content_frame, new ImportFragment()).commit();

        } else if (id == R.id.nav_gallery) {

            //GoogleMapFragment frag = new GoogleMapFragment();

            //getSupportActionBar().setTitle("Google Maps ..");

            //fm.beginTransaction().replace(R.id.content_frame, new ImportFragment()).commit();

            fm.beginTransaction().replace(R.id.content_frame, new GmapFragment()).commit();


        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        // Get a reference to the DrawerLayout

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }
}
