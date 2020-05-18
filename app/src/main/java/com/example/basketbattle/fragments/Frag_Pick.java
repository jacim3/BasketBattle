package com.example.basketbattle.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.basketbattle.R;
import com.example.basketbattle.tools.DBHelper;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Frag_Pick extends Fragment {

    private final static String[] urls  = {"file:///android_asset/www/index.html","http://www.auction.co.kr/","https://front.wemakeprice.com/main","https://www.tmon.co.kr/","http://www.interpark.com/malls/index.html","https://www.gmarket.co.kr/","https://www.coupang.com/","http://ebay.auction.co.kr/","https://shopping.naver.com/"};
    private final static String[] url_Name = {"옥션","위메프","티몬","인터파크","지마켓","쿠팡","이베이 옥션","네이버 쇼핑","직접 입력"};
    private final static int TITLES = 0, IMAGES = 1, URLS = 2;
    private LinearLayout snackView;
    private FloatingActionButton btn_Plus, btn_Register, btn_Address;
    private WebView mWebView;
    private String currentUrl;
    private boolean isButtonClicked = false;
    private int index =0;
    private Animation anim_fadeIn, anim_fadeOut;
    private ConstraintLayout main_Page;
    private ImageView pointer_Nav, pointer_Menu, tooltip_Nav, tooltip_Menu;

    @SuppressLint({"SetJavaScriptEnabled", "ClickableViewAccessibility"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_pick, container, false);

        snackView = root.findViewById(R.id.snack_View);
        mWebView = root.findViewById(R.id.webview);
        main_Page = root.findViewById(R.id.main_Page);

        btn_Plus = root.findViewById(R.id.btnPlus);
        btn_Register = root.findViewById(R.id.btnIn);
        btn_Address = root.findViewById(R.id.btnAddress);

        pointer_Nav = root.findViewById(R.id.point_Drawer);
        pointer_Menu = root.findViewById(R.id.pointer_Fab);
        tooltip_Nav = root.findViewById(R.id.tooltip_nav);
        tooltip_Menu = root.findViewById(R.id.tooltip_menu);

        anim_fadeIn = AnimationUtils.loadAnimation(getContext(),R.anim.fadein);
        anim_fadeOut = AnimationUtils.loadAnimation(getContext(),R.anim.fadeout);

        init_Anim();

        if (index == 0){
            mWebView.setVisibility(View.INVISIBLE);
            main_Page.setVisibility(View.VISIBLE);
        }else{
            mWebView.setVisibility(View.VISIBLE);
            main_Page.setVisibility(View.INVISIBLE);
        }

        WebSettings wSettings = mWebView.getSettings();
        wSettings.setJavaScriptEnabled(true);
        wSettings.setAllowFileAccessFromFileURLs(true);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setDomStorageEnabled(true);

        //웹뷰가 다른 브라우저앱에서 열리는 것을 방지.
        //새창에서 열릴 지 or 현재 창에서 열릴지 등을 결정하기 유용한 기능.
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        btn_Plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isButtonClicked = !isButtonClicked;

                if(isButtonClicked) {
                    pointer_Menu.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slow_fadeout));
                    tooltip_Menu.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slow_fadeout));
                    pointer_Menu.setVisibility(View.INVISIBLE);
                    tooltip_Menu.setVisibility(View.INVISIBLE);
                }else{
                    pointer_Menu.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slow_fadein));
                    tooltip_Menu.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slow_fadein));
                    pointer_Menu.setVisibility(View.VISIBLE);
                    tooltip_Menu.setVisibility(View.VISIBLE);
                }
                if(isButtonClicked){
                    btn_Register.setVisibility(View.VISIBLE);
                    btn_Register.startAnimation(anim_fadeIn);
                    btn_Address.setVisibility(View.VISIBLE);
                    btn_Address.startAnimation(anim_fadeIn);
                }else{
                    btn_Register.setVisibility(View.INVISIBLE);
                    btn_Register.startAnimation(anim_fadeOut);
                    btn_Address.setVisibility(View.INVISIBLE);
                    btn_Address.startAnimation(anim_fadeOut);
                }
            }
        });

        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialStyledDialog.Builder mDialog = new MaterialStyledDialog.Builder(Objects.requireNonNull(getContext()))
                        .setTitle(" 확인")
                        .setDescription("\n현재 데이터를 등록 하시겠습니까?\n")
                        .setStyle(Style.HEADER_WITH_ICON)
                        .setIcon(R.drawable.disc)
                        .withIconAnimation(true)
                        .setHeaderColorInt(Color.parseColor("#9ED5FF"))
                        .withDarkerOverlay(true)
                        .onPositive(null).onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                currentUrl = mWebView.getUrl();
                                getWebsite();
                                dialog.dismiss();
                                btn_Plus.performClick();
                            }
                        }).setPositiveText("CANCEL").setNegativeText("OK");

                mDialog.show();
            }
        });

        btn_Address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater) Objects.requireNonNull(getActivity()).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View custView = Objects.requireNonNull(inflater).inflate(R.layout.address_widget,null);

                Spinner spin_Address = custView.findViewById(R.id.spin_Address);
                ArrayAdapter mAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item,url_Name);
                spin_Address.setAdapter(mAdapter);

                spin_Address.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        index = position+1;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        index = 0;
                    }
                });

                MaterialStyledDialog.Builder mDialog = new MaterialStyledDialog.Builder(Objects.requireNonNull(getContext()))
                        .setTitle(" 페이지 이동")
                        .setDescription("이동할 장소를 선택해 주세요.")
                        .setStyle(Style.HEADER_WITH_ICON)
                        .setIcon(R.drawable.clover)
                        .setCustomView(custView)
                        .withIconAnimation(true)
                        .setHeaderColorInt(Color.parseColor("#66FF9E"))
                        .withDarkerOverlay(true)
                        .onPositive(null).onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                if (index == 0){
                                    mWebView.setVisibility(View.INVISIBLE);
                                    main_Page.setVisibility(View.VISIBLE);
                                }else{
                                    mWebView.setVisibility(View.VISIBLE);
                                    main_Page.setVisibility(View.INVISIBLE);
                                }

                                mWebView.loadUrl(urls[index]);
                                btn_Plus.performClick();
                            }
                        }).setPositiveText("CANCEL").setNegativeText("OK");

                mDialog.show();

            }
        });
        mWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(isButtonClicked)
                btn_Plus.performClick();

                return false;
            }
        });

        return root;
    }

    private void getWebsite() {

        final DBHelper dbHelper = new DBHelper(getContext(), "CHALLENGERS.db", null, 1);
        new Thread(new Runnable() {
            @Override
            //배열에 저장하기 전 가공처리를 거치고 배열에 저장하는 부분.
            public void run() {

                final String[] og_Link = new String[3];     //사용자가 버튼을 누를 즉시, Web 에서 받아 올 title, image, url 을 저장할 배열 선언.

                final String[] linkSperator = new String[]{"meta[property='og:title']", "meta[property='og:image']", "meta[property='og:url']"};
                final String[] delSpit = new String[]{"-", "▶"};
                try {

                    Document doc = Jsoup.connect(currentUrl).get();

                    for (int i = 0; i < og_Link.length; i++) {
                        Elements links = doc.select(linkSperator[i]);

                        og_Link[i] = links.toString().substring(links.toString().indexOf("content=") + 9, links.toString().length() - 2).trim();
                        if (og_Link[i].contains("'"))
                            og_Link[i] = og_Link[i].replaceAll("'", "");
                        //builder.append(links).append("\n")
                        if (og_Link[i].contains("]") && og_Link[i].split("]")[1].length() >15)
                            og_Link[i] = og_Link[i].split("]")[1];
                    }
                    for (int i = 0; i < delSpit.length; i++) {
                        if (og_Link[TITLES].contains(delSpit[i])) {
                            String tmp = og_Link[TITLES].split(delSpit[i])[0];
                            og_Link[TITLES] = tmp;
                        }
                    }
                } catch (IOException e) {
                    Toast.makeText(getContext(), "에러", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Snackbar.make(snackView, "정보를 읽을 수 없습니다.", 1000).show();

                }

                //위의 가공 처리가 완료되고 저장이 된 배열을 출력하는 부분.
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            long now = System.currentTimeMillis();
                            Date date = new Date(now);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 M월 d일 hh시 mm분 ");

                            if(og_Link[TITLES] == null || og_Link[IMAGES] == null || og_Link[URLS] == null){
                                Snackbar.make(snackView, "DB에 저장할 수 없었습니다.\n정보를 바꿔 다시 시도해 보세요.", 2000).show();
                            }else {
                                dbHelper.insert_Chal(og_Link[TITLES], og_Link[IMAGES], og_Link[URLS], "가격", simpleDateFormat.format(date));
                                Snackbar.make(snackView, "DB에 저장이 완료 되었습니다.", 2000).show();
                            }
                        } catch (SQLiteConstraintException sq) {
                            Snackbar.make(snackView, "이미 동일한 데이터가 존재합니다.", 2000).show();
                        }
                            dbHelper.close();
                    }
                });
            }
        }).start();
    }

    //아직 보완해야 함 미완성 부분.
    String price_Selector(Document doc, String og_Link){

        String[] linkName = {"auction","wemakeprice","coupang", "tmon","gmarket",".interpark","naver","other"};
        Elements linked = doc.select("strong[class='price_real']");
        String price = "";

        int index = 7;
        for(int i=0; i<linkName.length; i++){
            if(og_Link.contains(linkName[i])) {
                index = i;
                break;
            }
        }
        switch (index){
            case 0 :
                price = doc.select("strong[class='price_real']").toString();
                price = price.split(">")[1].split("<")[0];
                break;
            case 1 :
                price = doc.select("strong[class='sale_price']").toString();
                price = price.split("><")[1].split("</")[0].split(">")[1];
                break;
            case 2 :
                price = doc.select("strong[class='price_value']").toString();
                price = price.split(">")[1].split("<")[0];
                break;
            case 3 :
                price = doc.select("p[class='num']").toString();
                price = price.split(">")[1].split("<")[0];
                break;
            case 4 :
                price = doc.select("strong[class='price_real']").toString();
                price = price.split(">")[1].split("<")[0];
                break;
            case 5 :

                price = price.split(">")[1].split("<")[0];
                break;
            case 6 :

                price = price.split(">")[1].split("<")[0];
                break;
            case 7 :
                price = "0";
                break;
        }
        return price;
    }
    private void init_Anim(){
        Handler handlerA =new Handler();
        handlerA.postDelayed(new Runnable() {
            @Override
            public void run() {
                pointer_Nav.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.slow_fadein));

                pointer_Nav.setVisibility(View.VISIBLE);

                Handler handlerB =new Handler();
                handlerB.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tooltip_Nav.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.slow_fadein));
                        tooltip_Nav.setVisibility(View.VISIBLE);

                        Handler handlerC =new Handler();
                        handlerC.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pointer_Menu.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.slow_fadein));

                                pointer_Menu.setVisibility(View.VISIBLE);

                                Handler handlerD =new Handler();
                                handlerD.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        tooltip_Menu.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.slow_fadein));
                                        tooltip_Menu.setVisibility(View.VISIBLE);
                                    }
                                },500);
                            }
                        },1000);
                    }
                },500);
            }
        },1000);

    }
}
