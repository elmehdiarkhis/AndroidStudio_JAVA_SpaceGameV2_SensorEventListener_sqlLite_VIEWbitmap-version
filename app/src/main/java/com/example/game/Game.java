package com.example.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

//probleme
//Sensor Manager AppCompatActivity
//we set X et Y in onDraw methode ???? how onSensor gonna trigger the onDraw ?
//getX Bitmap


public class Game extends View implements SensorEventListener {

    //variable globale ====================
    private Context ctx;

    private Bitmap background;
    private Bitmap spaceShip;

    private ArrayList<Astre> myArrayAsrte;
    private ArrayList<Bitmap> myArrayBitmap;

    private SensorManager mySensorManager;
    public static Sensor mySensor;


    int screenW;
    int screenH;

    private int spaceShip_PosX;
    private int spaceShip_PosY;

    private Canvas myCanvas;

    private boolean foundChange =false;




    public Game(Context _context) {
        super(_context);

        ctx = _context;


        mySensorManager = SendToGame.mySensorManager;
        mySensor = SendToGame.mySensor;
        mySensorManager.registerListener(this,mySensor,SensorManager.SENSOR_DELAY_GAME);


        //background===================================
        background = BitmapFactory.decodeResource(getResources(),R.drawable.back);
        //==============================================


        //Planets=============================================================================
        //DataBase
        mySqlDataBase myDataBase = new mySqlDataBase(_context);
        myDataBase.connectToDataBase();
        //Insert Astres into Database===
        myDataBase.insertAstre("nostra",1000,1000,R.drawable.nostra,-900,-100,1);
        myDataBase.insertAstre("sun",1000,1000,R.drawable.sun,0,350,0);
        myDataBase.insertAstre("mars",630,630,R.drawable.mars,0,-200,0);
        myDataBase.insertAstre("terre",450,450,R.drawable.terre,950,1800,1);
        myDataBase.insertAstre("moon",400,400,R.drawable.moon,650,1350,0);
        myDataBase.insertAstre("jupiter",500,500,R.drawable.jupiter,950,170,0);
        //Select All Astres
        myArrayAsrte = myDataBase.selectAllAstre();

        //creer une Array de Bitmap de tous les astre
        myArrayBitmap = new ArrayList<Bitmap>();
        for (Astre astre : myArrayAsrte) {
            Bitmap planet = BitmapFactory.decodeResource(getResources(),astre.getImage());
            myArrayBitmap.add(planet);
        }
        //======================================================================================

        //spaceShip=====================================
        spaceShip = BitmapFactory.decodeResource(getResources(),R.drawable.shipone);
        //==============================================

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        screenW = w;
        screenH = h;

        //spaceShip=====================================
        spaceShip_PosX=(int)(screenW*0.37);
        spaceShip_PosY=(int)(screenH*0.75);
        //==============================================
    }

    @Override
    protected void onDraw(Canvas _canvas) {
//        super.onDraw(canvas);


        //background===================================
        Bitmap ScaledBitmapBack = Bitmap.createScaledBitmap(background, _canvas.getWidth(),_canvas.getHeight(), true);
        _canvas.drawBitmap(ScaledBitmapBack,0,0,null);
        //==============================================


        //Planets=============================================================================
        int counter = 0;
        for (Bitmap bitmap : myArrayBitmap) {
            Bitmap ScaledBitmap = Bitmap.createScaledBitmap(bitmap,myArrayAsrte.get(counter).getWidth(),myArrayAsrte.get(counter).getHeight(), true);
            _canvas.drawBitmap(ScaledBitmap,myArrayAsrte.get(counter).getPositonIntialX(),myArrayAsrte.get(counter).getPositonIntialY(),null);
            counter++;
        }
        //======================================================================================

        //spaceShip=====================================
        Bitmap ScaledBitmapSpaceShip = Bitmap.createScaledBitmap(spaceShip, 330,330, true);
        _canvas.drawBitmap(ScaledBitmapSpaceShip,spaceShip_PosX,spaceShip_PosY,null);
        //==============================================

        myCanvas=_canvas;
    }

    @Override
    public boolean onTouchEvent(MotionEvent _event) {
//        return super.onTouchEvent(event);

        if(GlobalVar.btnTouchClicked==true){

            if(_event.getAction() == MotionEvent.ACTION_MOVE){

                //resete position du spaceship relative to touche
                spaceShip_PosX = (int) _event.getX()-100;
                spaceShip_PosY = (int) _event.getY()-100;




                //Faire bouger les Planet dans le sens contraire du spaceShip=====
//                for (Astre astre : myArrayAsrte) {
//                    astre.setPositonIntialX(astre.getPositonIntialX()-200);
//                    astre.setPositonIntialY(astre.getPositonIntialY()-200);
//                }
                //============================================================

//                //===========================!!!!!!!!!!!======================//
//
                //change l'image de la planet to Green if on est pret d'une planet habite
                int counter_n = 0;
                for (Bitmap bitmap : myArrayBitmap) {
                    if(_event.getX() > myArrayAsrte.get(counter_n).getPositonIntialX()-300 && _event.getX() < myArrayAsrte.get(counter_n).getPositonIntialX()+300 && _event.getY()> myArrayAsrte.get(counter_n).getPositonIntialY()-300 && _event.getY()< myArrayAsrte.get(counter_n).getPositonIntialY()+300 && myArrayAsrte.get(counter_n).getHabite()==1){
                        myArrayBitmap.set(counter_n,BitmapFactory.decodeResource(getResources(),R.drawable.green));
                    }else{
                        myArrayBitmap.set(counter_n,BitmapFactory.decodeResource(getResources(),myArrayAsrte.get(counter_n).getImage()));
                    }
                    counter_n++;
                }
                //===========================!!!!!!!!!!!======================//
            }


        }

        invalidate();
        return true;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        if(GlobalVar.btnSensorClicked==true){


            //Calculer la distance du vecteur OM = Racine(X2+Y2+Z2)
            float vectorLength = (float) Math.sqrt(
                    Math.pow( (float)(event.values[0]),2)
                            + Math.pow( (float)(event.values[1]-9.81),2));


            //Bouger le spaceShip + bloquer le spaceShip dans le cadre_X====
            if(spaceShip_PosX>15 && spaceShip_PosX<1100) {
                spaceShip_PosX = (int) (spaceShip_PosX - (event.values[0]*vectorLength));
            }else{
                if(spaceShip_PosX>500){
                    spaceShip_PosX = spaceShip_PosX-3;
                }else{
                    spaceShip_PosX = spaceShip_PosX+3;
                }
            }
            //===
            if(spaceShip_PosY>0 && spaceShip_PosY<2040) {
                spaceShip_PosY  = (int) (spaceShip_PosY  - (event.values[2]*vectorLength) );
            }else{
                if(spaceShip_PosY>500){
                    spaceShip_PosY = spaceShip_PosY -3;
                }else{
                    spaceShip_PosY = spaceShip_PosY +3;
                }
            }
            //============================================================


//            //Faire bouger les Planet dans le sens contraire du spaceShip=====
//            int counter = 0;
//            for (Bitmap bitmap : myArrayBitmap) {
//                myArrayAsrte.get(counter).setPositonIntialX( (int) (myArrayAsrte.get(counter).getPositonIntialX() + (event.values[0]*vectorLength/4)) );
//                myArrayAsrte.get(counter).setPositonIntialY( (int) (myArrayAsrte.get(counter).getPositonIntialY() + (event.values[2]*vectorLength/4)) );
//                counter++;
//            }
//            //============================================================



            //===========================!!!!!!!!!!!======================//
            //reset images if loin
//            int cx=0;
//            for (Astre astre : myArrayAsrte) {
//                if(astre.isChanged()==true){
//                    foundChange=true;
//                }
//                cx++;
//            }
//
//            if(foundChange==true){
//                int c = 0;
//                for (Astre astre : myArrayAsrte) {
//                    if(astre.isChanged()==true){
//                        myArrayBitmap.set(c,BitmapFactory.decodeResource(getResources(),myArrayAsrte.get(c).getImage()));
//                        astre.setChanged(false);
//                    }
//                    c++;
//                }
//                foundChange=false;
//            }


            //======================================================================================
            //change l'image de la planet to Green if on est pret d'une planet habite
            int counter_n = 0;
            for (Bitmap bitmap : myArrayBitmap) {
                if(spaceShip_PosX > myArrayAsrte.get(counter_n).getPositonIntialX()-300 && spaceShip_PosX < myArrayAsrte.get(counter_n).getPositonIntialX()+300 && spaceShip_PosY> myArrayAsrte.get(counter_n).getPositonIntialY()-300 && spaceShip_PosY< myArrayAsrte.get(counter_n).getPositonIntialY()+300 && myArrayAsrte.get(counter_n).getHabite()==1){
                    if(myArrayAsrte.get(counter_n).isChanged()==false){
                        myArrayBitmap.set(counter_n,BitmapFactory.decodeResource(getResources(),R.drawable.green));
                        myArrayAsrte.get(counter_n).setChanged(true);
                    }
                }
                counter_n++;
            }
            //===========================!!!!!!!!!!!======================//



            invalidate();
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
