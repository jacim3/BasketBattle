package com.example.basketbattle.popup.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.example.basketbattle.fragments.Frag_Fame;
import com.example.basketbattle.R;

public class Context_Popup extends Activity {

    TextView[] menu_Tv = new TextView[5];
    Integer[] menu_ID = {R.id.menu_Image, R.id.menu_Detail, R.id.menu_Url, R.id.menu_Delete, R.id.menu_Cancel};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.popup_context_menu);

        for (int i = 0; i < menu_ID.length; i++) {

            final int index = i;
            menu_Tv[i] = findViewById(menu_ID[i]);
            menu_Tv[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Context_Popup.this, Frag_Fame.class);
                    intent.putExtra("index", index);

                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            });
        }
    }
/*
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

 */

}
