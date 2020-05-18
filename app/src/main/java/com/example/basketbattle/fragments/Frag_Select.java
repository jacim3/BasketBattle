package com.example.basketbattle.fragments;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
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
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

//https://game-icons.net/
public class Frag_Select extends Fragment {

    private JSONArray product = new JSONArray();
    private String winners = "";
    private WebView pick_Webview;
    private MaterialStyledDialog.Builder mDialog;
    private LinearLayout snack_View;

    @SuppressLint({"SetJavaScriptEnabled", "ClickableViewAccessibility"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_select, container, false);
        snack_View = root.findViewById(R.id.snack_View);

        pick_Webview = root.findViewById(R.id.pick_Webview);
        WebSettings wSettings = pick_Webview.getSettings();
        wSettings.setJavaScriptEnabled(true);
        wSettings.setAllowFileAccessFromFileURLs(true);
        pick_Webview.setWebChromeClient(new WebChromeClient());
        pick_Webview.getSettings().setUseWideViewPort(true);
        pick_Webview.getSettings().setDomStorageEnabled(true);
        pick_Webview.loadUrl("file:///android_asset/www/moex.html");

        TimerTask tTask = new TimerTask() {
            @Override
            public void run() {
                if (!winners.equals("")) {
                    db_Process(winners);
                    Frag_Fame frag_fame = new Frag_Fame();
                    Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.nav_default_exit_anim, R.anim.nav_default_enter_anim).replace(R.id.frameLayout, frag_fame).commit();
                    cancel();
                }
            }
        };

        Timer timer = new Timer();
        timer.schedule(tTask, 0, 3000);
        //페이지가 로드된 이후 실행되는 부분. Native 의 데이터를 Web 으로 보낸다.
        pick_Webview.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {

                try {
                    makeJson();
                } catch (Exception ne) {
                    ne.printStackTrace();
                }

                pick_Webview.loadUrl("javascript:" +

                        "if(bool == true){" +                    //버그로 인하여 반복문이 초과 발생하므로 이를 해결하기 위하여 설정함. 루프를 다 돌고난 후, 다시 돌지 않도록.
                        "obj = " + product + ";" +                   //오브젝트 형태로 읽어와야 하나 ''를 붙임으로써 문자열로 읽어져서 문제가 생겼었음.
                        "$.each(obj,function(index,value){" +
                        "ID[index] = obj[index].ID;" +
                        "title[index] = obj[index].name;" +      //본래 변수명을 name을 썼었으나, Jquery와 충돌 -> Json값을 undefined로 읽는 문제가 생겼음.
                        "image[index] = obj[index].image;" +
                        "url[index] = obj[index].url;" +
                        "price[index] = obj[index].price;" +
                        "time[index] = obj[index].time;" +
                        "});" +
                        "bool=false;" +
                        "}" +
                        "shuffle(ID);"
                );
            }
        });

        //Web 에서 최종 계산된 값을 Native 로 전달 받는 인터페이스.
        pick_Webview.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void getWinner(final String url) {

                winners = url;
            }
        }, "myCall");

        return root;
    }

    private JSONArray makeJson() {
        final DBHelper dbHelper = new DBHelper(getContext(), "CHALLENGERS.db", null, 1);

        JSONObject details;
        //데이터를 받아오는 부분.

        int[] id = new int[]{};
        //name, image, url, price, time 의 순서로...
        String[][] challengers = dbHelper.getArray("CHALLENGERS");
        //for(int i=0; i<challengers.length; i++)
        //Log.d("xxxxxxxxxx", dbHelper.getArray()[i][0]+dbHelper.getArray()[i][1]+dbHelper.getArray()[i][2]+dbHelper.getArray()[i][3]+dbHelper.getArray()[i][4]+dbHelper.getArray()[i][5]);
        if (dbHelper.getIndex("CHALLENGERS") < 2) {

            mDialog = new MaterialStyledDialog.Builder(Objects.requireNonNull(getContext()))
                    .setTitle(" 데이터 필요")
                    .setDescription("\n적어도 데이터를 2개 이상 추가한 이후, 이용해 주세요.\n")
                    .setStyle(Style.HEADER_WITH_ICON)
                    .setIcon(R.drawable.card)
                    .withIconAnimation(true)
                    .setHeaderColorInt(Color.parseColor("#ffb067"))
                    .withDarkerOverlay(true)
                    .onPositive(null).onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Frag_Pick frag_pick = new Frag_Pick();
                            Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                                    .beginTransaction().setCustomAnimations(R.anim.nav_default_exit_anim, R.anim.nav_default_enter_anim).replace(R.id.frameLayout, frag_pick).addToBackStack(null).commit();
                        }
                    }).setPositiveText("CANCEL").setNegativeText("OK");

            mDialog.show();
        }

        String[] jsonName = {"ID", "name", "image", "url", "price", "time"};

        try {
            for (int i = 0; i < dbHelper.getIndex("CHALLENGERS"); i++) {
                details = new JSONObject();
                details.put(jsonName[0], i);

                for (int j = 1; j < jsonName.length; j++) {
                    details.put(jsonName[j], challengers[i][j]);
                }
                product.put(details);
            }
            //battle.put("challenger",product);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        dbHelper.close();
        return product;
    }

    private void db_Process(String win_Url) {


        DBHelper dbHelper = new DBHelper(getContext(), "CHALLENGERS.db", null, 1);
        String[][] tmp_out = dbHelper.getArray("CHALLENGERS");

        for (int i = 0; i < tmp_out.length; i++) {
            try {
                dbHelper.insert_Out(tmp_out[i][1], tmp_out[i][2], tmp_out[i][3], tmp_out[i][4], tmp_out[i][5]);
            } catch (SQLiteConstraintException sqe) {
                Snackbar.make(snack_View, "이미 중복되는 데이터가 존재하여,\n 해당 데이터는 등록하지 않습니다.", BaseTransientBottomBar.LENGTH_SHORT).show();
            }
            if (tmp_out[i][3].equals(win_Url)) {
                try {
                    dbHelper.insert_Win(tmp_out[i][1], tmp_out[i][2], tmp_out[i][3], tmp_out[i][4], tmp_out[i][5]);
                } catch (SQLiteConstraintException sqe) {
                    Snackbar.make(snack_View, "이미 중복되는 데이터가 존재하여,\n 해당 데이터는 등록하지 않습니다.", BaseTransientBottomBar.LENGTH_SHORT).show();
                }
                dbHelper.delete_Records("OUTDATED", win_Url);
            }
        }
        dbHelper.delete_Records("CHALLENGERS");
    }
}
