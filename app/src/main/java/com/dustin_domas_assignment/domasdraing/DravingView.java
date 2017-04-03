package com.dustin_domas_assignment.domasdraing;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Bundle;

import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;

import android.util.TypedValue;


import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;




public class DravingView extends View {



    //drawing path
    private Path drawPath;

    //drawing and canvas paint
    private Paint drawPaint, canvasPaint;

    //initial color
    private int paintColor = 0xff000000;

    //Canvas
    private Canvas drawCanvas;

    //Canvas bitmap
    private Bitmap canvasBitmap;

    private float brushSize, lastBrushSize;


    //checks if eraser is on or now
    private boolean erase = false;
    private boolean rectFlag = false;
    private Uri imageURI;

    private Bitmap imageBitmap;


    // this constructor used when creating view through XML
    public DravingView(Context context, AttributeSet attrs) {

        super(context, attrs);


        setupDrawing();
    }



    private void setupDrawing() {


        //drawPaint and drawPath object from Paint class
        drawPath = new Path();
        drawPaint = new Paint();

        //set brushSize and lastBrushSize
        brushSize = getResources().getInteger(R.integer.mediun_size);
        lastBrushSize = brushSize;


        drawPaint.setStrokeWidth(brushSize);


        //set initial color
        drawPaint.setColor(paintColor);


        //setting path properties
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(20);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        //declare canvasPaint Object
        canvasPaint = new Paint(Paint.DITHER_FLAG);

    }//end of setupDrawing



    //Method will make Custom view function as Drawing View
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        canvasBitmap = Bitmap.createBitmap(w, h ,Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);

    }// end of onSizeChanged

    //also need to use this method for custom drawing view
    @Override
    protected void onDraw(Canvas canvas) {


        canvas.drawBitmap(canvasBitmap, 0 , 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);

        /*if(){
            canvas.drawRect();
        }
*/
    }//end of onDraw

    //used to register what is touched on screen
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;
            default:
                return false;
        }

        //calling invalidate() will cause onDraw method to activate
        invalidate();
        return true;


    }//end of onTouchEvent

    public void setColor (int newColor){
        invalidate();
        paintColor = newColor;
        //paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);

    }//end of setColor




    //to set brush size
    public void setBrushSize(float newSize){

        //update brush size with passed value
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());
        brushSize=pixelAmount;
        drawPaint.setStrokeWidth(brushSize);


    }//end of setBrushSize



    //methods below will get and set the other sizes for brush from dimensions
    public void setLastBrushSize(float lastSize){
        lastBrushSize = lastSize;

    }// end of get lastBrushSize

    public float getLastBrushSize () {

        return lastBrushSize;
    }// end of float getLastBrush





    //clear canvas for new drawing
    public void startNew(){
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();



    }//end of startNew

    public void setErase (boolean isErase){
        erase = isErase;

        //change Paint object to erase or switch back to drawing
        //PorterDuffXfermode gives clear color for erased
        if (erase) {
            drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }
        else {
            drawPaint.setXfermode(null);
        }

    }// end of setErase

    public void setRectangle(){

    }



    //suposse
    public void setImageURI(Uri imageURI) {
        this.imageURI = imageURI;

    }

    public void setImageBitmap(Bitmap imageBitmap) {
       // this.imageBitmap = imageBitmap;

        canvasBitmap = imageBitmap;
        drawCanvas = new Canvas(canvasBitmap);
        invalidate();
    }


/********
    public void setCanvasBitmap(Bitmap bmp) {
        canvasBitmap = bmp;
        drawCanvas = new Canvas(canvasBitmap);
        invalidate();
    }
 *******/

}//end of Drawingview

