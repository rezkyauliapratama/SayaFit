package android.cybereye_community.com.sayafit.controller.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.cybereye_community.com.sayafit.EventBus;
import android.cybereye_community.com.sayafit.R;
import android.cybereye_community.com.sayafit.controller.database.Facade;
import android.cybereye_community.com.sayafit.controller.fragment.BaseFragment;
import android.cybereye_community.com.sayafit.controller.fragment.FeedDialogFragment;
import android.cybereye_community.com.sayafit.controller.fragment.GuideFragment;
import android.cybereye_community.com.sayafit.controller.fragment.HomeFragment;
import android.cybereye_community.com.sayafit.controller.fragment.MapDialogFragment;
import android.cybereye_community.com.sayafit.controller.fragment.ScheduleFragment;
import android.cybereye_community.com.sayafit.controller.fragment.ShareLocationFragment;
import android.cybereye_community.com.sayafit.databinding.ActivityMainBinding;
import android.cybereye_community.com.sayafit.handler.ApiClient;
import android.cybereye_community.com.sayafit.handler.api.FeedApi;
import android.cybereye_community.com.sayafit.model.request.FeedPost;
import android.cybereye_community.com.sayafit.utility.Constant;
import android.databinding.DataBindingUtil;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.github.kobakei.materialfabspeeddial.FabSpeedDial;
import io.github.kobakei.materialfabspeeddial.FabSpeedDialMenu;
import timber.log.Timber;

/**
 * Created by Rezky Aulia Pratama on 9/3/2017.
 */

public class MainActivity extends BaseActivity
   {

    ActivityMainBinding binding;

    List<BaseFragment> fragments;
    BaseFragment fragment;
    FabSpeedDialMenu menu;

    FeedDialogFragment feedDialogFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fragments = new ArrayList<>();

        initTab();
        initViewPager();

        initFab();
        listenerFab();

    }


    private void initFab() {
        binding.content.fab.closeMenu();

        menu = new FabSpeedDialMenu(this);

        if (fragment instanceof HomeFragment || fragment instanceof ShareLocationFragment){
            menu.add(Constant.getInstance().POST).setIcon(R.drawable.ic_create_24dp);
            menu.add(Constant.getInstance().SHARE_LOCATION).setIcon(R.drawable.ic_favorite_place);
        }else if (fragment instanceof ScheduleFragment){
            menu.add(Constant.getInstance().ADD_SCHEDULE).setIcon(R.drawable.ic_create_24dp);
        }

        if (menu.size() > 0)
            binding.content.fab.show();
        else
            binding.content.fab.hide();


        binding.content.fab.setMenu(menu);
    }

    private void listenerFab(){

        binding.content.fab.addOnMenuItemClickListener(new FabSpeedDial.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(FloatingActionButton miniFab, @Nullable TextView label, int itemId) {
                if (label.getText().equals(Constant.getInstance().POST))
                startActivity(new Intent(MainActivity.this,PostActivity.class));

            }
        });
    }
    private void initTab(){
        TabLayout.Tab[] tabs = {
                binding.content.tabLayout.newTab().setIcon(R.drawable.ic_house_black_building_shape),
                binding.content.tabLayout.newTab().setIcon(R.drawable.ic_favorite_place),
                binding.content.tabLayout.newTab().setIcon(R.drawable.ic_calendar),
                binding.content.tabLayout.newTab().setIcon(R.drawable.ic_stretching_exercises),
                binding.content.tabLayout.newTab().setIcon(R.drawable.ic_icon),
        };

        for (TabLayout.Tab tab : tabs) {
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setGravity(Gravity.CENTER);
            ImageView newTab = (ImageView) LayoutInflater
                    .from(this)
                    .inflate(android.support.design.R.layout.design_layout_tab_icon, null,false);

            newTab.setImageDrawable(tab.getIcon());
            layout.addView(newTab);

            tab.setCustomView(layout);
            binding.content.tabLayout.addTab(tab);
        }

        binding.content.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                fragment = fragments.get(tab.getPosition());
                initFab();

                binding.content.viewPager.setCurrentItem(tab.getPosition());
                int tabIconColor = ContextCompat.getColor(MainActivity.this, R.color.colorWhite);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int tabIconColor = ContextCompat.getColor(MainActivity.this, R.color.colorPrimaryDark);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initViewPager(){
        fragments.add(HomeFragment.newInstance());
        fragments.add(ShareLocationFragment.newInstance());
        fragments.add(ScheduleFragment.newInstance());
        fragments.add(new BaseFragment());
        fragments.add(GuideFragment.newInstance());

        fragment = fragments.get(0);

        binding.content.viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return binding.content.tabLayout.getTabCount();
            }
        });

        binding.content.viewPager.setPagingEnabled(false);
        binding.content.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.content.tabLayout));
    }


    private void showDialogFeed(){
        feedDialogFragment = FeedDialogFragment.newInstance();
        feedDialogFragment.show(getFragmentManager().beginTransaction(),feedDialogFragment.DiALOG);

    }


       @Override
       public boolean onCreateOptionsMenu(Menu menu) {
           // Inflate the menu; this adds items to the action bar if it is present.
           getMenuInflater().inflate(R.menu.main, menu);

           return true;

       }

       @Override
       public boolean onOptionsItemSelected(MenuItem item) {
           int id = item.getItemId();

            Timber.e("MENU !");
           //noinspection SimplifiableIfStatement
           //noinspection SimplifiableIfStatement
           if (id == R.id.action_logout) {
               Timber.e("LOGOUT !");
               AlertDialog.Builder builder = new AlertDialog.Builder(this)
                       .setMessage(R.string.logout_confirmation)

                       .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {

                               Facade.getInstance().getManageUserTbl().removeAll();
                               startActivity(new Intent(MainActivity.this,LoginActivity.class));

                           }
                       })
                       .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               dialog.cancel();
                           }
                       });

               AlertDialog dialog = builder.create();
               dialog.show();

               return true;
           }
           return super.onOptionsItemSelected(item);

       }




}
