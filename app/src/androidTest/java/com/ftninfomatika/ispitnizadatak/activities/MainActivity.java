package com.ftninfomatika.ispitnizadatak.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.ftninfomatika.ispitnizadatak.R;
import com.ftninfomatika.ispitnizadatak.adapters.DrawerListViewAdapter;
import com.ftninfomatika.ispitnizadatak.fragments.DetailsFragment;
import com.ftninfomatika.ispitnizadatak.fragments.MovieDetailsFragment;
import com.ftninfomatika.ispitnizadatak.fragments.SearchFragment;
import com.ftninfomatika.ispitnizadatak.fragments.SettingsFragment;
import com.ftninfomatika.ispitnizadatak.model.Movie;
import com.ftninfomatika.ispitnizadatak.model.NavigationItem;
import com.ftninfomatika.ispitnizadatak.net.ormlight.DatabaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


    public class MainActivity extends AppCompatActivity implements SearchFragment.onListItemClickListener, MovieDetailsFragment.onItemClickListener {

        public static final int NOTIF_ID = 10;
        public static final String NOTIF_CHANNEL_ID = "Notification Channel";

        private DatabaseHelper databaseHelper;

        private DrawerLayout drawerLayout;
        private ListView drawerList;
        private CharSequence drawerTitle;
        private CharSequence title;

        private final List<NavigationItem> navigationItems = new ArrayList<>();

        private boolean showedSearch = false, showedSettings = false;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            createNotificationChannel();

            setupDrawer();

            showMovieDetailsFragment();
        }

        private void createNotificationChannel() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(NOTIF_CHANNEL_ID, "Nas Notif Kanal", importance);
                channel.setDescription("Opis naseg kanala");
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
        }

        private void setupDrawer() {
            setupDrawerNavigationItems();
            title = drawerTitle = getTitle();
            setupDrawerItems();
            setupToolbar();
        }

        private void setupDrawerNavigationItems() {
            navigationItems.add(new NavigationItem("Pregled filmova", "Pregled svih filmova", R.drawable.ic_baseline_favorite_24));
            navigationItems.add(new NavigationItem("Podesenja", "Podesenja aplikacije Application", R.drawable.ic_baseline_settings_24));
        }

        private void setupDrawerItems() {
            drawerLayout = findViewById(R.id.drawer_layout);
            drawerList = findViewById(R.id.leftDrawer);

            DrawerListViewAdapter adapter = new DrawerListViewAdapter(navigationItems, this);
            drawerList.setAdapter(adapter);
            drawerList.setOnItemClickListener((parent, view, position, id) -> {
                switch (position) {
                    case 0:
                        showDetailsFragment();
                        break;
                    case 1:
                        showSettingsFragment();
                        break;
                }
                drawerLayout.closeDrawer(drawerList);
            });
        }


        private void setupToolbar() {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
                actionBar.setHomeButtonEnabled(true);
                actionBar.show();
            }

            new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
                public void onDrawerClosed(View view) {
                    getSupportActionBar().setTitle(title);
                    invalidateOptionsMenu();
                }

                public void onDrawerOpened(View drawerView) {
                    getSupportActionBar().setTitle(drawerTitle);
                    invalidateOptionsMenu();
                }
            };
        }

        private void showSearchFragment() {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            SearchFragment fragment = new SearchFragment();
            transaction.replace(R.id.root, fragment);
            transaction.commit();

            showedSearch = true;
            showedSettings = false;
        }

        private void showSettingsFragment() {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            SettingsFragment fragment = new SettingsFragment();
            transaction.replace(R.id.root, fragment);
            transaction.commit();

            showedSearch = false;
            showedSettings = true;
        }

        private void showMovieDetailsFragment() {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            MovieDetailsFragment fragment = new MovieDetailsFragment();
            transaction.replace(R.id.root, fragment);
            transaction.commit();

            showedSearch = true;
            showedSettings = false;
        }

        private void showDetailsFragment(Movie movie) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            DetailsFragment fragment = new DetailsFragment();
            fragment.setMovie(movie);
            transaction.replace(R.id.root, fragment);
            transaction.commit();

            showedSearch = true;
            showedSettings = false;
        }

        @Override
        public void onBackPressed() {
            if (showedSettings) {
                finish();
            }
        }

        public DatabaseHelper getDatabaseHelper() {
            if (databaseHelper == null) {
                databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
            }
            return databaseHelper;
        }

        private void showNotification(String text) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIF_CHANNEL_ID);
            builder.setContentTitle(getString(R.string.app_name))
                    .setContentText(text)
                    .setSmallIcon(R.drawable.ic_launcher_foreground);

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(NOTIF_ID, builder.build());
        }

        @Override
        public void onListItemClicked(Movie movie) {
            try {
                List<Movie> movies = getDatabaseHelper().getMovieDao().queryForAll();

                if (movies.size() > 0) {
                    if (!movies.contains(movie)) {
                        getDatabaseHelper().getMovieDao().create(movie);

                        showNotification(movie.getTitle() + " uspesno dodat");

                    }else {
                        showNotification(movie.getTitle() + " vec postoji u bazi");
                    }
                } else {
                    getDatabaseHelper().getMovieDao().create(movie);
                    showNotification(movie.getTitle() + " uspesno dodat");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onaddClicked() {
            showSearchFragment();
        }

        @Override
        public void showTehnickiDetalji(Movie movie) {
            showDetailsFragment(movie);
        }

        @Override
        public void showDetaljiOUcesnicima(Movie movie) {

        }

}