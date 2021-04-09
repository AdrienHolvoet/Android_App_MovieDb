package com.example.projettac.Views;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import android.widget.Toast;

import com.example.projettac.R;
import com.example.projettac.Utils.Constants;
import com.example.projettac.Utils.MyApplication;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private String[] tabTitle = {Constants.SEARCH, Constants.FAVORITES};

    private FragmentStateAdapter pagerAdapter;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViewPagerAndTabs();
        //Set the global context
        MyApplication.setContext(this);
        Context context = this.getApplicationContext();

        //verify internet connection
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isWifiConn = false;
        boolean isMobileConn = false;
        for (Network network : connMgr.getAllNetworks()) {
            NetworkInfo networkInfo = connMgr.getNetworkInfo(network);
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                isWifiConn |= networkInfo.isConnected();
            }
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                isMobileConn |= networkInfo.isConnected();
            }
        }

        if(!isWifiConn && !isMobileConn) {
            Toast.makeText(context, getText(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
        }

    }

    //Get view references and initialize viewpager to display our fragments
    private void setupViewPagerAndTabs() {
        viewPager = findViewById(R.id.tab_viewpager);
        tabLayout = findViewById(R.id.tablayout);

        pagerAdapter = new ScreenSlidePagerAdapter(this, tabLayout.getTabCount());
        //Set adapter to viewpager and handle Fragment change inside
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(tabTitle[position])).attach();
    }

    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        private int numOfTabs;

        public ScreenSlidePagerAdapter(FragmentActivity fa, int numOfTabs) {
            super(fa);
            this.numOfTabs = numOfTabs;
        }

        @Override
        public Fragment createFragment(int position) {
            Bundle bundle = new Bundle();
            FragmentRecyclerView fragment = new FragmentRecyclerView();


            if(position == 1) {
                bundle.putBoolean("isRemote", false);
            }
            else {
                    bundle.putBoolean("isRemote", true);
            }

            fragment.setArguments(bundle);
            return  fragment;
        }

        @Override
        public int getItemCount() {
            return numOfTabs;
        }
    }
}