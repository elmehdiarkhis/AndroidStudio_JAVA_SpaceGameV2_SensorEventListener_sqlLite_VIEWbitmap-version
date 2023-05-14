package com.example.game;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

public class BeforeGame extends View {

    private Context ctx;

    //===Logo===
    private Bitmap logo;
    private Bitmap Scaledlogo;


    //===btnTouch===
    private Bitmap btnTouch_Up;
    private Bitmap btnTouch_Down;
    private int posX_btnTouch_Up;
    private int posX_btnTouch_Down;
    private int posY_btnTouch;

    //===btnSensor===
    private Bitmap btnSensor_Up;
    private Bitmap btnSensor_Down;
    private int posX_btnSensor_Up;
    private int posX_btnSensor_Down;
    private int posY_btnSensor;

    //===
    private boolean btnTouch_State = false;
    private boolean btnSensor_State = false;

    //===
    private int screenW;
    private int screenH;

//    public static boolean btnTouchClicked = false;
//    public static boolean btnSensorClicked = false;


    public BeforeGame(Context _context) {
        super(_context);

        ctx = _context;
        logo = BitmapFactory.decodeResource(getResources(),R.drawable.logo);
        btnTouch_Up = BitmapFactory.decodeResource(getResources(),R.drawable.toucheup);
        btnTouch_Down = BitmapFactory.decodeResource(getResources(),R.drawable.touchedown);
        btnSensor_Up = BitmapFactory.decodeResource(getResources(),R.drawable.sensorup);
        btnSensor_Down = BitmapFactory.decodeResource(getResources(),R.drawable.sensordown);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenW = w;
        screenH = h;
    }

    @Override
    protected void onDraw(Canvas _canvas) {

        //logo
//        posX_logo = (screenW - logo.getWidth())/2;
//        posY_logo = (int) (screenH * 0.10);
        Scaledlogo = Bitmap.createScaledBitmap(logo, _canvas.getWidth(),_canvas.getHeight(), true);
        _canvas.drawBitmap(Scaledlogo,0,0,null);

        //btnTouch
        posX_btnTouch_Up = (screenW - btnTouch_Up.getWidth())/3;
        posX_btnTouch_Down = (screenW - btnTouch_Down.getWidth())/3;
        posY_btnTouch = (int) (screenH * 0.88);

        if(btnTouch_State==false){
            _canvas.drawBitmap(btnTouch_Up,posX_btnTouch_Up,posY_btnTouch,null);
        }else{
            _canvas.drawBitmap(btnTouch_Down,posX_btnTouch_Down,posY_btnTouch,null);
        }

        //btnSensor
        posX_btnSensor_Up = (screenW - btnSensor_Up.getWidth())/2+250;
        posX_btnSensor_Down = (screenW - btnSensor_Down.getWidth())/2+250;
        posY_btnSensor = (int) (screenH * 0.87);

        if(btnSensor_State==false){
            _canvas.drawBitmap(btnSensor_Up,posX_btnSensor_Up,posY_btnSensor,null);
        }else{
            _canvas.drawBitmap(btnSensor_Down,posX_btnSensor_Down,posY_btnSensor,null);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent _event) {

        if(_event.getAction() == MotionEvent.ACTION_DOWN){

            //t>touche
            Boolean t_lmtX_1 = _event.getX() > posX_btnTouch_Up;
            Boolean t_lmtX_2 = _event.getX() < posX_btnTouch_Up + btnTouch_Up.getWidth();
            Boolean t_lmtY_1 = _event.getY() > posY_btnTouch;
            Boolean t_lmtY_2 = _event.getY() < posY_btnTouch + btnTouch_Up.getHeight();

            if(t_lmtX_1 && t_lmtX_2 && t_lmtY_1 && t_lmtY_2){
                btnTouch_State=true;
            }

            //s>Sensor
            Boolean s_lmtX_1 = _event.getX() > posX_btnSensor_Up;
            Boolean s_lmtX_2 = _event.getX() < posX_btnSensor_Up + btnSensor_Up.getWidth();
            Boolean s_lmtY_1 = _event.getY() > posY_btnSensor;
            Boolean s_lmtY_2 = _event.getY() < posY_btnSensor + btnSensor_Up.getHeight();

            if(s_lmtX_1 && s_lmtX_2 && s_lmtY_1 && s_lmtY_2){
                btnSensor_State = true;
            }

        }


        if(_event.getAction() == MotionEvent.ACTION_UP){

            if(btnTouch_State==true){

                btnTouch_State=false;

                GlobalVar.btnTouchClicked = true;
                GlobalVar.btnSensorClicked = false;

                ctx.startActivity(new Intent(ctx,SendToGame.class));
            }

            if(btnSensor_State==true){

                btnSensor_State=false;

                GlobalVar.btnSensorClicked = true;
                GlobalVar.btnTouchClicked = false;

                ctx.startActivity(new Intent(ctx,SendToGame.class));
            }
        }


        invalidate();
        return true;
    }
}
