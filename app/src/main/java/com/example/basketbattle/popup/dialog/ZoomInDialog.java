package com.example.basketbattle.popup.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.basketbattle.R;

public class ZoomInDialog extends Dialog {

    private String mImage;

    private View.OnClickListener mZoomIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zoom_in_image);

        Button btn_Zoom_Close = findViewById(R.id.btn_Zoom_Close);
        ImageView iv_ZoomIn = findViewById(R.id.iv_ZoomIn);

        btn_Zoom_Close.setOnClickListener(mZoomIn);
        Glide.with(getContext()).load(mImage).transition(DrawableTransitionOptions.withCrossFade()).transform(new RoundedCorners(50)).into(iv_ZoomIn);
    }
    public ZoomInDialog(@NonNull Context context, String image_Url, View.OnClickListener zoomClose) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        mImage = image_Url;
        mZoomIn = zoomClose;
    }
}
