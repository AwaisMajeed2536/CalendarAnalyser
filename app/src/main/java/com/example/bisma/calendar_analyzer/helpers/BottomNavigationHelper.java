package com.example.bisma.calendar_analyzer.helpers;

import android.app.Activity;
import android.support.annotation.IntRange;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.example.bisma.calendar_analyzer.R;

import java.util.ArrayList;

public class BottomNavigationHelper {

    public static Builder with(Activity context) {
        return new Builder(context);
    }

    public static class Builder implements View.OnClickListener {
        Activity context;
        ArrayList<ImageView> imageButtons;
        ArrayList<LinearLayout> buttonWrappers;
        ImageView button1;
        ImageView button2;
        ImageView button3;
        ImageView button4;
        LinearLayout button1Wrapper;
        LinearLayout button2Wrapper;
        LinearLayout button3Wrapper;
        LinearLayout button4Wrapper;
        OnItemSelectedListener onItemSelectedListener;

        public Builder(Activity context) {
            this.context = context;
            this.imageButtons = new ArrayList<>();
            this.buttonWrappers = new ArrayList<>();
            button1 = context.findViewById(R.id.image_1);
            button1Wrapper = context.findViewById(R.id.btn1_wrapper);
            imageButtons.add(button1);
            buttonWrappers.add(button1Wrapper);
            button2 = context.findViewById(R.id.image_2);
            button2Wrapper = context.findViewById(R.id.btn2_wrapper);
            imageButtons.add(button2);
            buttonWrappers.add(button2Wrapper);
            button3 = context.findViewById(R.id.image_3);
            button3Wrapper = context.findViewById(R.id.btn3_wrapper);
            imageButtons.add(button2);
            buttonWrappers.add(button2Wrapper);
            button4 = context.findViewById(R.id.image_4);
            button4Wrapper = context.findViewById(R.id.btn4_wrapper);
            imageButtons.add(button2);
            buttonWrappers.add(button2Wrapper);
        }


        public Builder setCallBack(OnItemSelectedListener onItemSelectedListener) {
            this.button1Wrapper.setOnClickListener(this);
            this.button2Wrapper.setOnClickListener(this);
            this.button3Wrapper.setOnClickListener(this);
            this.button4Wrapper.setOnClickListener(this);
            this.onItemSelectedListener = onItemSelectedListener;
            return this;
        }

        public Builder setSelected(@IntRange(from = 0, to = 3) int position) {
            button1Wrapper.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
            button2Wrapper.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
            button3Wrapper.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
            button4Wrapper.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
            switch (position) {
                case 0:
                    button1Wrapper.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                    break;
                case 1:
                    button2Wrapper.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                    break;
                case 2:
                    button3Wrapper.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                    break;
                case 3:
                    button4Wrapper.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                    break;
            }
            if (onItemSelectedListener != null)
                onItemSelectedListener.onSelectItem(position);
            return this;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn1_wrapper:
                    onItemSelectedListener.onSelectItem(0);
                    setSelected(0);
                    break;
                case R.id.btn2_wrapper:
                    onItemSelectedListener.onSelectItem(1);
                    setSelected(1);
                    break;
                case R.id.btn3_wrapper:
                    onItemSelectedListener.onSelectItem(2);
                    setSelected(2);
                    break;
                case R.id.btn4_wrapper:
                    onItemSelectedListener.onSelectItem(3);
                    setSelected(3);
                    break;
            }
        }
    }


    public interface OnItemSelectedListener {
        public void onSelectItem(int position);
    }
}
