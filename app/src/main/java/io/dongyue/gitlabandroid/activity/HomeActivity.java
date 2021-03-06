package io.dongyue.gitlabandroid.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.dongyue.gitlabandroid.R;
import io.dongyue.gitlabandroid.activity.base.BaseActivity;
import io.dongyue.gitlabandroid.fragment.ActivitiesFragment;
import io.dongyue.gitlabandroid.fragment.ProjectsFragment;
import io.dongyue.gitlabandroid.utils.NavigationManager;
import io.dongyue.gitlabandroid.utils.ToastUtils;
import io.dongyue.gitlabandroid.utils.eventbus.RxBus;
import io.dongyue.gitlabandroid.utils.eventbus.events.APIErrorEvent;
import io.dongyue.gitlabandroid.utils.eventbus.events.CloseDrawerEvent;
import io.dongyue.gitlabandroid.utils.eventbus.events.NewActivitiesEvent;
import io.dongyue.gitlabandroid.view.NoScrollViewPager;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class HomeActivity extends BaseActivity{

    @Bind(R.id.container) NoScrollViewPager mViewPager;
    @Bind(R.id.tabs) TabLayout tabLayout;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setScanScroll(false);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(mViewPager);

        initTab();

        addSubscription(RxBus.getBus().observeEvents(CloseDrawerEvent.class)
                .observeOn(AndroidSchedulers.mainThread()).subscribe(closeDrawerEvent -> {
                    DrawerLayout drawer1 = (DrawerLayout) findViewById(R.id.drawer_layout);
                    if (drawer1.isDrawerOpen(GravityCompat.START)) {
                        drawer1.closeDrawer(GravityCompat.START);
                    }
                }));

        addSubscription(RxBus.getBus().observeEvents(APIErrorEvent.class)
            .observeOn(AndroidSchedulers.mainThread()).subscribe(apiErrorEvent -> {
                if(apiErrorEvent.getCode()==401) {
                    NavigationManager.toLogin(HomeActivity.this);
                    finish();
                }
                //other cases
            }));

        addSubscription(RxBus.getBus().observeEvents(NewActivitiesEvent.class)
            .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<NewActivitiesEvent>() {
                    @Override
                    public void call(NewActivitiesEvent newActivitiesEvent) {

                    }
                }));
    }

    private void initTab(){
        View indicatorProject = getLayoutInflater().inflate(R.layout.tab_layout,null);
        //View indicatorA = getLayoutInflater().inflate(R.layout.tab_layout,null);
        //View indicatorProject = getLayoutInflater().inflate(R.layout.tab_layout,null);
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
        getMenuInflater().inflate(R.menu.home, menu);
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

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        final int COUNT = 3;
        Fragment[] fragments;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new Fragment[COUNT];
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position){
                case 0:
                    fragments[0]= ProjectsFragment.newInstance();
                    return fragments[0];
                case 1:
                    fragments[1]= ActivitiesFragment.newInstance(ActivitiesFragment.FEED_TYPE_ALL);
                    return fragments[1];
                case 2:
                    fragments[2]= ProjectsFragment.newInstance();
                    return fragments[2];
                default:
                    fragments[position]=ProjectsFragment.newInstance();
                    return fragments[position];
            }
        }

        public Fragment getFragment(int position){
            if(position<0||position>COUNT)return null;
            return fragments[position];
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Project";
                case 1:
                    return "Activity";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }

}
