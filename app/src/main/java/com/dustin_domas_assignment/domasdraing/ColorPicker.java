package com.dustin_domas_assignment.domasdraing;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;

/**
 * Created by dustinlobato on 3/27/17.
 */

public class ColorPicker extends Dialog implements View.OnClickListener{
    private ImageButton currPaint;
    private int color;
    private DravingView drawView2;

    public ColorPicker(Context context) {
        super(context);
    }
    public ColorPicker(Context context, DravingView d) {
        super(context);
        this.drawView2 = d;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.color_picker);
         //LinearLayout paintLayout = (LinearLayout) findViewById(R.id.paint_colors);

        //currPaint = (ImageButton) paintLayout.getChildAt(0);

        currPaint.setOnClickListener(this);
       drawView2 = (DravingView)findViewById(R.id.drawing);
    }
/*
    public void paintClicked (View view){

        drawView2.setErase(false);


        //set brush size to previous size
        drawView2.setBrushSize(drawView2.getLastBrushSize());

        //checking if user have clicked anything
        if(view != currPaint) {
            ImageButton imgView = (ImageButton)view;
            String color  = view.getTag().toString();

            drawView2.setColor(color);


        }//end if


    }//end of paintClicked

*/

    @Override
    public void onClick(View view) {
    //if(view.getId()==R.id.color1){
        ImageButton imgView = (ImageButton)view;
        String color  = view.getTag().toString();
       // drawView2.setColor(color);
   // }

    }
}
