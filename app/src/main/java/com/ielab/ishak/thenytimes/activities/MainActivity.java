package com.ielab.ishak.thenytimes.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ielab.ishak.thenytimes.R;
import com.ielab.ishak.thenytimes.adapters.ArticleAdapter;
import com.ielab.ishak.thenytimes.fragments.HomeFragment;
import com.ielab.ishak.thenytimes.fragments.MoviesFragment;
import com.ielab.ishak.thenytimes.fragments.PoliticsFragment;
import com.ielab.ishak.thenytimes.fragments.ScienceFragment;
import com.ielab.ishak.thenytimes.fragments.SportsFragment;
import com.ielab.ishak.thenytimes.fragments.TechnologyFragment;
import com.ielab.ishak.thenytimes.models.ArticlesResponse;
import com.ielab.ishak.thenytimes.server.APIClient;
import com.ielab.ishak.thenytimes.server.APIService;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final int HOME_FRAGMENT = 0;
    public static final int MOVIES_FRAGMENT = 1;
    public static final int POLITICS_FRAGMENT = 2;
    public static final int SCIENCE_FRAGMENT = 3;
    public static final int SPORTS_FRAGMENT = 4;
    public static final int TECHNOLOGY_FRAGMENT = 5;

    public static final String SAVED_STATE = "saved_state";
    public static final String FRAGMENT_ID = "fragment_id";

    public static APIService apiService;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    private HomeFragment homeFragment;
    private MoviesFragment moviesFragment;
    private PoliticsFragment politicsFragment;
    private ScienceFragment scienceFragment;
    private SportsFragment sportsFragment;
    private TechnologyFragment technologyFragment;

    public static ArticlesResponse articlesResponse;
    ArticleAdapter articleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        apiService = APIClient.getClient().create(APIService.class);

        showFirstFragment();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            if (!homeFragment.isVisible()) {
                navigationView.setCheckedItem(R.id.nav_home);
                showFragment(HOME_FRAGMENT);
            }
            else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                this.showFragment(HOME_FRAGMENT);
                break;

            case R.id.nav_movies:
                this.showFragment(MOVIES_FRAGMENT);
                break;

            case R.id.nav_politics:
                this.showFragment(POLITICS_FRAGMENT);
                break;

            case R.id.nav_science:
                this.showFragment(SCIENCE_FRAGMENT);
                break;

            case R.id.nav_sports:
                this.showFragment(SPORTS_FRAGMENT);
                break;

            case R.id.nav_technology:
                this.showFragment(TECHNOLOGY_FRAGMENT);
                break;

            default:
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showFirstFragment() {
        Fragment visibleFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (visibleFragment == null)
            this.showFragment(HOME_FRAGMENT);
    }

    private void showFragment(int fragmentIdentifier) {
        switch (fragmentIdentifier) {
            case HOME_FRAGMENT:
                if (this.homeFragment == null)
                    this.homeFragment = HomeFragment.newInstance();
                this.startTransactionFragment(this.homeFragment);
                break;

            case MOVIES_FRAGMENT:
                if (this.moviesFragment == null)
                    this.moviesFragment = MoviesFragment.newInstance();
                this.startTransactionFragment(this.moviesFragment);
                break;

            case POLITICS_FRAGMENT:
                if (this.politicsFragment == null)
                    this.politicsFragment = PoliticsFragment.newInstance();
                this.startTransactionFragment(this.politicsFragment);
                break;

            case SCIENCE_FRAGMENT:
                if (this.scienceFragment == null)
                    this.scienceFragment = ScienceFragment.newInstance();
                this.startTransactionFragment(this.scienceFragment);
                break;

            case SPORTS_FRAGMENT:
                if (this.sportsFragment == null)
                    this.sportsFragment = SportsFragment.newInstance();
                this.startTransactionFragment(this.sportsFragment);
                break;

            case TECHNOLOGY_FRAGMENT:
                if (this.technologyFragment == null)
                    this.technologyFragment = TechnologyFragment.newInstance();
                this.startTransactionFragment(this.technologyFragment);
                break;

            default:
                break;
        }
    }

    private void startTransactionFragment(Fragment fragment) {
        if (!fragment.isVisible()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }
    }
}
