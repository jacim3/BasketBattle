package com.example.basketbattle;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.basketbattle.fragments.Frag_Fame;
import com.example.basketbattle.fragments.Frag_Pick;
import com.example.basketbattle.fragments.Frag_Select;
import com.shrikanthravi.customnavigationdrawer2.data.MenuItem;
import com.shrikanthravi.customnavigationdrawer2.widget.SNavigationDrawer;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SNavigationDrawer nav_Drawer;
    Class fragmentClass;
    int color1 = 0;
    public static Fragment fragment;
    private long backKeyPressedTime = 0;
    int mPosition=0;

    boolean isFDClicked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        nav_Drawer = findViewById(R.id.nav_drawer);
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("등록하기", R.drawable.tab_1));
        menuItems.add(new MenuItem("선택하기", R.drawable.tab_2));
        menuItems.add(new MenuItem("확인하기", R.drawable.tab_3));
        menuItems.add(new MenuItem("Exit", R.drawable.tab_4));
        nav_Drawer.setMenuItemList(menuItems);
        fragmentClass = Frag_Pick.class;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).replace(R.id.frameLayout, fragment).commit();
        }

        nav_Drawer.setOnHamMenuClickListener(new SNavigationDrawer.OnHamMenuClickListener() {
            @Override
            public void onHamMenuClicked() {
                if (mPosition == 0) {
                    Frag_Pick frag_Pick = (Frag_Pick) getSupportFragmentManager().findFragmentById(R.id.frameLayout);

                    ImageView pointer_Nav = frag_Pick.getView().findViewById(R.id.point_Drawer);
                    ImageView tooltip_Nav = frag_Pick.getView().findViewById(R.id.tooltip_nav);

                    if (!isFDClicked) {
                        pointer_Nav.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slow_fadeout));
                        tooltip_Nav.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slow_fadeout));
                        pointer_Nav.setVisibility(View.INVISIBLE);
                        tooltip_Nav.setVisibility(View.INVISIBLE);
                        isFDClicked = true;
                    } else {
                        pointer_Nav.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slow_fadein));
                        tooltip_Nav.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slow_fadein));
                        pointer_Nav.setVisibility(View.VISIBLE);
                        tooltip_Nav.setVisibility(View.VISIBLE);
                        isFDClicked = false;
                    }
                }
            }
        });
        nav_Drawer.setOnMenuItemClickListener(new SNavigationDrawer.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClicked(int position) {
                switch (position) {
                    case 0:
                        color1 = R.color.red;
                        fragmentClass = Frag_Pick.class;
                        break;
                    case 1:
                        color1 = R.color.orange;
                        fragmentClass = Frag_Select.class;

                        break;
                    case 2:
                        color1 = R.color.green;
                        fragmentClass = Frag_Fame.class;

                        break;
                    case 3:

                        Toast.makeText(MainActivity.this, "Basket Battle을 종료합니다.", Toast.LENGTH_SHORT).show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 1500);

                        break;
                }

                mPosition = position;

                nav_Drawer.setDrawerListener(new SNavigationDrawer.DrawerListener() {
                    @Override
                    public void onDrawerOpening() {

                    }

                    //#F0A294
                    @Override
                    public void onDrawerClosing() {
                        try {
                            fragment = (Fragment) fragmentClass.newInstance();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (fragment != null) {
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).replace(R.id.frameLayout, fragment).commit();
                        }
                    }

                    @Override
                    public void onDrawerOpened() {

                    }

                    @Override
                    public void onDrawerClosed() {

                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {

                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        switch (mPosition){
            case 0:
               Frag_Pick frag_Pick = (Frag_Pick) getSupportFragmentManager().findFragmentById(R.id.frameLayout);
               WebView pick_Webview = frag_Pick.getView().findViewById(R.id.webview);

                if(pick_Webview.canGoBack()){
                    pick_Webview.goBack();
                    break;
                }
            case 1:
            case 2:
                if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                    backKeyPressedTime = System.currentTimeMillis();
                    Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (System.currentTimeMillis() <= backKeyPressedTime + 2000)
                    finish();
                break;
        }
    }
}
