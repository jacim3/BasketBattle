package com.example.basketbattle.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.basketbattle.R;
import com.example.basketbattle.popup.dialog.Context_Popup;
import com.example.basketbattle.popup.dialog.ZoomInDialog;
import com.example.basketbattle.tools.DBHelper;
import com.example.basketbattle.tools.OnSwipeTouchListener;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Objects;

public class Frag_Fame extends Fragment {

    private final static int NAME = 1, IMAGE = 2, URL = 3, PRICE = 4, DATE = 5;
    private final static int START = 0, END = 2;
    private final static String[] DBNAME = {"CHALLENGERS", "OUTDATED", "WINNER"};
    private final static String[] TITLE = {"Registered Goods", "Defeated Goods", "Winners"};
    private final static String[] TITLE_COLOR={"#9ED5FF","#F0A294","#ffb067"};
    private int selector = 0, mPosition;
    private ListView lvFame;
    private TextView fame_Title;
    private LinearLayout snack_View;
    private ArrayList<String> url, images;
    private ZoomInDialog zoomInDialog;
    private ConstraintLayout lay_Fame;
    private ImageView iv_Left;
    private ImageView iv_Right;
    ImageView iv_Delete;
    private Animation slide_left, slide_right;
    private int con_Menu_Idx;

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.activity_fame, container, false);

        lay_Fame = root.findViewById(R.id.lay_Fame);
        lvFame = root.findViewById(R.id.lV_Fame);
        fame_Title = root.findViewById(R.id.fame_Title);
        iv_Left = root.findViewById(R.id.iv_Left);
        iv_Right = root.findViewById(R.id.iv_Right);
        iv_Delete = root.findViewById(R.id.iv_Delete);
        snack_View = root.findViewById(R.id.snack_View2);
        lay_Fame.getForeground().setAlpha(0);
        showList();
        con_Menu_Idx = 5;
        slide_left = AnimationUtils.loadAnimation(getContext(),R.anim.slide_right);
        slide_right = AnimationUtils.loadAnimation(getContext(),R.anim.slide_left);

        iv_Left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipe_Left();
            }
        });
        iv_Right.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipe_Right();
            }
        });

        lvFame.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getContext(), Context_Popup.class);
                startActivityForResult(intent,102);
                mPosition = position;
            }
        });

        lvFame.setOnTouchListener(new OnSwipeTouchListener(getContext()){

            //오른쪽 -> 왼쪽
            public void onSwipeRight() {
                swipe_Left();
            }
            //왼쪽 -> 오른쪽
            public void onSwipeLeft() {
                swipe_Right();
            }
        });

        iv_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_Delete.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.nav_default_pop_exit_anim));
                MaterialStyledDialog.Builder mDialog = new MaterialStyledDialog.Builder(Objects.requireNonNull(getContext()))
                        .setTitle(" 경고")
                        .setDescription("\n해당 리스트가 전부 지워집니다.\n")
                        .setStyle(Style.HEADER_WITH_ICON)
                        .setIcon(R.drawable.alert)
                        .withIconAnimation(true)
                        .setHeaderColorInt(Color.parseColor("#F0A294"))
                        .withDarkerOverlay(true)
                        .setHeaderScaleType(ImageView.ScaleType.FIT_CENTER)
                        .onPositive(null).onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                DBHelper dbHelper = new DBHelper(getContext(), "CHALLENGERS.db", null, 1);
                                dbHelper.delete_Records(DBNAME[selector]);
                                dbHelper.close();
                                lay_Fame.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.slide_left));
                                showList();
                            }
                        }).setPositiveText("CANCEL").setNegativeText("OK");
                mDialog.show();
            }
        });
        return root;
    }
    private void showList() {

        ArrayList<String> names = new ArrayList<>();
        images = new ArrayList<>();
        url = new ArrayList<>();
        ArrayList<String> price = new ArrayList<>();
        ArrayList<String> date = new ArrayList<>();

        getListView(DBNAME[selector], names, images, url, price, date);
        CustomAdapter adapter = new CustomAdapter(getContext(), 0, names, images, date);
        registerForContextMenu(lvFame);
        fame_Title.setText(TITLE[selector]);
        fame_Title.setTextColor(Color.parseColor(TITLE_COLOR[selector]));
        lvFame.setAdapter(adapter);
    }

    private class CustomAdapter extends ArrayAdapter<String> {
        private ArrayList<String> names;
        private ArrayList<String> images;
        private ArrayList<String> date;
        CustomAdapter(Context context, int textViewResourceId, ArrayList<String> names, ArrayList<String> images, ArrayList<String> date) {
            super(context, textViewResourceId, names);

            this.names = names;
            this.images = images;
            this.date = date;
        }

        @SuppressLint("InflateParams")
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) Objects.requireNonNull(getActivity()).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = Objects.requireNonNull(vi).inflate(R.layout.listview_item, null);
            }
            ImageView imageView = v.findViewById(R.id.imageView);

            TextView textView = v.findViewById(R.id.textView);
            TextView tv_Date = v.findViewById(R.id.tv_Date);
            Glide.with(Objects.requireNonNull(getActivity()).getApplicationContext()).load(images.get(position)).transform(new RoundedCorners(5)).override(100, 100).into(imageView);
            textView.setText(names.get(position));
            tv_Date.setText(date.get(position));

            return v;
        }
    }

    private void getListView(String db_Name, ArrayList<String> names, ArrayList<String> images,
                             ArrayList<String> url, ArrayList<String> price, ArrayList<String> date) {

        final DBHelper dbHelper = new DBHelper(getContext(), "CHALLENGERS.db", null, 1);

        String[][] getDatabases = dbHelper.getArray(db_Name);

        for (int i = 0; i < dbHelper.getIndex(db_Name); i++) {
            names.add(getDatabases[i][NAME]);
            images.add(getDatabases[i][IMAGE]);
            url.add(getDatabases[i][URL]);
            price.add(getDatabases[i][PRICE]);
            date.add(getDatabases[i][DATE]);

            // Log.d("db내용 : ",getChallengers[0][i]+"");
        }

        if(url.size() == 0){
            iv_Delete.setVisibility(View.INVISIBLE);
        }else{
            iv_Delete.setVisibility(View.VISIBLE);
        }

        dbHelper.close();
    }

    private View.OnClickListener zoomClose = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            lay_Fame.getForeground().setAlpha(0);
            zoomInDialog.dismiss();
        }
    };

    private void delRefresh(){
        final DBHelper dbHelper = new DBHelper(getContext(), "CHALLENGERS.db", null, 1);

        switch (selector) {
            case 0:
                dbHelper.delete_Records(DBNAME[0], url.get(mPosition));
                break;
            case 1:
                dbHelper.delete_Records(DBNAME[1], url.get(mPosition));
                break;
            case 2:
                dbHelper.delete_Records(DBNAME[2], url.get(mPosition));
                break;
        }

        showList();
        lay_Fame.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.slide_left));
        Snackbar.make(snack_View, "삭제가 완료되었습니다.", 1500).show();
        dbHelper.close();
    }

    private void swipe_Right(){
        if (selector < END) {
            selector++;
            if(selector == END)
                iv_Right.setVisibility(View.INVISIBLE);
            else
                iv_Left.setVisibility(View.VISIBLE);

            lvFame.startAnimation(slide_left);
            iv_Right.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.nav_default_pop_exit_anim));
            fame_Title.startAnimation(slide_right);
            showList();
        }
    }

    private void swipe_Left(){
        if (selector > START) {
            selector--;
            if (selector == START)
                iv_Left.setVisibility(View.INVISIBLE);
            else
                iv_Right.setVisibility(View.VISIBLE);

            lvFame.startAnimation(slide_right);
            iv_Left.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.nav_default_exit_anim));
            fame_Title.startAnimation(slide_right);
            showList();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final int M_IMAGE = 0, M_DETAIL = 1, M_URL = 2, M_DELETE = 3, M_CANCEL = 4, NO_MENU = 5;

        if(resultCode == Activity.RESULT_OK){
            con_Menu_Idx = Objects.requireNonNull(data).getIntExtra("index",5);

            if(con_Menu_Idx != NO_MENU){

                switch (con_Menu_Idx) {

                    case M_IMAGE:
                        zoomInDialog = new ZoomInDialog(Objects.requireNonNull(getContext()),images.get(mPosition),zoomClose);
                        zoomInDialog.setCancelable(false);
                        Objects.requireNonNull(zoomInDialog.getWindow()).getAttributes().windowAnimations =R.style.PauseDialogAnimation;
                        lay_Fame.getForeground().setAlpha(150);
                        zoomInDialog.show();
                        break;

                    case M_DETAIL:

                        break;

                    case M_URL:

                        try {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url.get(mPosition)));
                            startActivity(browserIntent);
                        }catch(ActivityNotFoundException ae){
                            Snackbar.make(snack_View,"해당 상품의 판매가 만료되어 \n현재는 이동할 수 없습니다. ",2000).show();
                        }
                        break;

                    case M_DELETE:
                        MaterialStyledDialog.Builder mDialog = new MaterialStyledDialog.Builder(Objects.requireNonNull(getContext()))
                                .setTitle(" 경고")
                                .setDescription("\n해당 항목을 지웁니다.\n")
                                .setStyle(Style.HEADER_WITH_ICON)
                                .setIcon(R.drawable.alert)
                                .withIconAnimation(true)
                                .setHeaderColorInt(Color.parseColor("#F0A294"))
                                .withDarkerOverlay(true)
                                .setHeaderScaleType(ImageView.ScaleType.FIT_CENTER)
                                .onPositive(null).onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        delRefresh();
                                    }
                                }).setPositiveText("CANCEL").setNegativeText("OK");
                        mDialog.show();

                        break;
                    case M_CANCEL:
                        break;
                }
            }
        }
    }
}

