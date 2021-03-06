package com.sikhcentre.activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.sikhcentre.R;
import com.sikhcentre.entities.Topic;
import com.sikhcentre.fragments.TopicDetailFragment;
import com.sikhcentre.fragments.TopicListFragment;
import com.sikhcentre.network.TopicMetadataDownloadHandler;
import com.sikhcentre.schedulers.ISchedulerProvider;
import com.sikhcentre.schedulers.MainSchedulerProvider;
import com.sikhcentre.utils.FragmentUtils;
import com.sikhcentre.utils.IssueReporter;
import com.sikhcentre.viewmodel.SearchViewModel;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @NonNull
    private SearchViewModel searchViewModel = SearchViewModel.INSTANCE;

    @NonNull
    private CompositeDisposable subscription;

    @NonNull
    ISchedulerProvider schedulerProvider = MainSchedulerProvider.INSTANCE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

        FragmentUtils.createFragment(R.id.container_framelayout, new TopicListFragment(),
                getIntent().getExtras(), getSupportFragmentManager(), FragmentUtils.FragmentTag.TOPIC_LIST);
    }

//    @Override
//    public void onBackPressed() {
////        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
////        if (drawer.isDrawerOpen(GravityCompat.START)) {
////            drawer.closeDrawer(GravityCompat.START);
////        } else {
////            super.onBackPressed();
////        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.menu_item_action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchViewModel.handleSearchTopic(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                searchViewModel.handleSearchTopic(query);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_item_action_refresh:
                TopicMetadataDownloadHandler.fetchData(MainActivity.this);
                break;
            case R.id.menu_item_action_report:
                IssueReporter.report(MainActivity.this);
                break;
            case R.id.menu_item_action_settings:
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void bind() {
        subscription = new CompositeDisposable();

        subscription.add(searchViewModel.getSelectedTopicSubjectAsObservable()
                .observeOn(schedulerProvider.ui())
                .subscribe(new Consumer<Topic>() {

                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Topic topic) throws Exception {
                        getIntent().putExtra(TopicDetailFragment.TOPIC, topic);
                        FragmentUtils.replaceFragment(R.id.container_framelayout, new TopicDetailFragment(),
                                getIntent().getExtras(), getSupportFragmentManager(), FragmentUtils.FragmentTag.TOPIC_DETAIL);
                    }

                }));

    }

    private void unBind() {
        subscription.dispose();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bind();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unBind();
    }
}
