package com.dustin_domas_assignment.domasdraing;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener  {


    //create variale to access DravingView
    private DravingView drawView;
    private ImageButton currPaint;
    private ImageButton drawbtn;
    ColorPicker picker;

    private float smallBrush, mediumBrush, largeBrush;
    Button btn_select;
    private ImageButton  drawBtn;
    private ImageButton eraseBtn;
    private ImageButton newBtn;
    private ImageButton saveBtn;
    private ImageButton paintPicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        drawView = (DravingView)findViewById(R.id.drawing);

        //need to retreive layout in order to get colors
        //LinearLayout paintLayout = (LinearLayout) findViewById(R.id.paint_colors);

        //possition of the color in  Layour
        //currPaint = (ImageButton) paintLayout.getChildAt(0);


        //this will give a darker color around selected paint
//       currPaint.setImageDrawable(getResources().getDrawable((R.drawable.paint_pressed)));


        //declare brush sizes
        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.mediun_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        paintPicker = (ImageButton)findViewById(R.id.paintColor) ;
        paintPicker.setOnClickListener(this);
        //declare drawBtn
        drawbtn = (ImageButton) findViewById(R.id.draw_button);

        drawbtn.setOnClickListener(this);


        //declace new button
        newBtn = (ImageButton)findViewById(R.id.new_button);
        newBtn.setOnClickListener(this);


        //declare save_button
        saveBtn = (ImageButton)findViewById(R.id.save_button);
        saveBtn.setOnClickListener(this);

        //declare eraase_button
        eraseBtn = (ImageButton) findViewById(R.id.eraser_button);
        eraseBtn.setOnClickListener(this);



    }//end of onCreate

    //will allow to use color of my choice

    /*public void paintClicked (View view){

        drawView.setErase(false);


        //set brush size to previous size
        drawView.setBrushSize(drawView.getLastBrushSize());

        //checking if user have clicked anything
        if(view != currPaint) {
            ImageButton imgView = (ImageButton)view;
            String color  = view.getTag().toString();

            drawView.setColor(color);

            //updating UI to refelct the new chose color
            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint=(ImageButton)view;

        }//end if


    }//end of paintClicked
*/

    @Override
    public void onClick(View view) {

        if(view.getId()== R.id.paintColor){
            final Dialog paintDialog = new Dialog(this);
            paintDialog.setTitle("Color Wheel");

            paintDialog.setContentView(R.layout.color_picker);


            ImageView colorPalette = (ImageView)paintDialog.findViewById(R.id.colorWheel);
            colorPalette.setImageResource(R.drawable.colorwheel);
            btn_select = (Button) paintDialog.findViewById(R.id.btn_select);
            colorPalette.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    TextView colorSwatch = (TextView)paintDialog.findViewById(R.id.colorSwatch);
                    float cordinate_x = event.getX();
                    float  cordinate_y = event.getY();

                    float [] xy = new float[] {cordinate_x,cordinate_y};
                    Matrix matrix = new Matrix();
                    ((ImageView)v).getImageMatrix().invert(matrix);

                    matrix.mapPoints(xy);
                    int x = Integer.valueOf((int)xy[0]);
                    int y = Integer.valueOf((int)xy[1]);

                    Drawable imgDrawable = ((ImageView)v).getDrawable();
                    Bitmap bitmap = ((BitmapDrawable)imgDrawable).getBitmap();
                    int bitW = bitmap.getWidth()-1;
                    int bitH = bitmap.getHeight()-1;
                    if(x < 0){
                        x = 0;
                    }else if(x > bitW ){
                        x = bitW ;
                    }

                    if(y < 0){
                        y = 0;
                    }else if(y > bitH ){
                        y = bitH ;
                    }
                    int pixel = bitmap.getPixel(x,y);

                    //pixel = bitsCanvas.getPixel(cordinate_x,cordinate_y);

                    int r = Color.red(pixel);
                    int g = Color.green(pixel);
                    int b = Color.blue(pixel);

                    int color = Color.rgb(r,g,b);
                    // btn_select.setBackgroundColor(color);
                    btn_select.setTextColor(color);

                    colorSwatch.setBackgroundColor(color);

                    btn_select.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            int c = btn_select.getCurrentTextColor();
                            Log.i("+++++++++++++++++",""+c);
                            drawView.setColor(c);
                            paintDialog.dismiss();
                        }
                    });

                    return true;

                }

            });
            paintDialog.show();

        }

        //set dialog box for new drawing
        if(view.getId() == R.id.new_button){

            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
            newDialog.setTitle("New Sketch");
            newDialog.setMessage("Start New Sketch?");
            newDialog.setPositiveButton("YES", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    drawView.startNew();
                    dialog.dismiss();
                }
            });
            newDialog.setNegativeButton("NO", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            newDialog.show();

        }//end of IF
        else if(view.getId()==R.id.save_button){
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Save Sketch");
            saveDialog.setMessage("Save Sketch To Divice Gallery?");
            saveDialog.setPositiveButton("YES", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    //save drawing

                    drawView.setDrawingCacheEnabled(true);

                    Bitmap bitmap = drawView.getDrawingCache();


                    //To Complete this action targetSDK  must be 22
                    String imgSaved = MediaStore.Images.Media.insertImage(
                            getContentResolver(), drawView.getDrawingCache(),
                            UUID.randomUUID().toString()+".png", "drawing");


                    try {
                        FileOutputStream out = new FileOutputStream(imgSaved);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90 , out);
                        Log.i("afsdfsfdsfsd", "asfsdfs");
                        out.flush();
                        out.close();
                    } catch (Exception e) {

                    }



                }
            });
            saveDialog.setNegativeButton("NO", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            saveDialog.show();




        }// end of IF ELSE


        //check for the click on draw_button
        if(view.getId() == R.id.draw_button){


            //making bursh Dialog Box
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Brush size:");

            brushDialog.setContentView(R.layout.brush_chooser);

            //declare small_button and create logo
            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawView.setBrushSize(smallBrush);
                    drawView.setLastBrushSize(smallBrush);

                    //will set eraser back to false
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });


            //declare medium_button and create logo
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawView.setBrushSize(mediumBrush);
                    drawView.setLastBrushSize(mediumBrush);

                    //will set eraser back to false
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });

            //declare large_button and create logo
            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawView.setBrushSize(largeBrush);
                    drawView.setLastBrushSize(largeBrush);

                    //will set eraser back to false
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });



            brushDialog.show();
        }//end of if


        //Condition for erase_button
        else if (view.getId() == R.id.eraser_button){

            final Dialog brushDialog = new Dialog (this);
            brushDialog.setTitle("Eraser size: ");
            brushDialog.setContentView(R.layout.brush_chooser);

            ImageButton erase_small = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            erase_small.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawView.setErase(true);
                    drawView.setBrushSize(smallBrush);


                    brushDialog.dismiss();
                }
            });


            ImageButton erase_medium = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            erase_medium.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawView.setErase(true);
                    drawView.setBrushSize(mediumBrush);

                    brushDialog.dismiss();

                }
            });






            ImageButton erase_large = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            erase_large.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawView.setErase(true);
                    drawView.setBrushSize(largeBrush);


                    brushDialog.dismiss();
                }
            });


            brushDialog.show();

        }// end of if Else



    }//end of OnClick




}// end of MainActivit
