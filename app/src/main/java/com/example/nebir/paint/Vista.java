package com.example.nebir.paint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by nebir on 02/02/2016.
 */
public class Vista extends View {

    private int ancho, alto;
    private float x0, y0, xf, yf;
    private Bitmap mapaDeBits;
    private Canvas lienzoFondo;
    private Paint pincel = new Paint();
    private Path rectaPoligonal = new Path();
    private String figura = "libre";
    private int color = Color.BLACK;
    private boolean pinta = true;


    public Vista(Context context, AttributeSet attrs) {
        super(context, attrs);
        pincel.setColor(color);
        pincel.setAntiAlias(true);
        pincel.setStrokeWidth(8);
        pincel.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas lienzo) {
        super.onDraw(lienzo);
        lienzo.drawBitmap(mapaDeBits, 0, 0, null);
        if (pinta) {
            switch (figura) {
                case "rect":
                    lienzo.drawLine(x0, y0, xf, yf, pincel);
                    break;
                case "circ":

                    lienzo.drawCircle(x0, y0, (float) Math.sqrt(Math.pow(x0 - xf, 2) + Math.pow(y0 - yf, 2)), pincel);
                    break;
                case "cuad":
                    lienzo.drawRect(x0, y0, xf, yf, pincel);
                    break;
                case "libre":
                    pincel.setStyle(Paint.Style.STROKE);
                    lienzo.drawPath(rectaPoligonal, pincel);
                    break;
            }
        } else {
            pinta = true;
        }

    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mapaDeBits = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        lienzoFondo = new Canvas(mapaDeBits);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x0 = xf = x;
                y0 = yf = y;
                rectaPoligonal.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                xf = x;
                yf = y;
                rectaPoligonal.lineTo(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                xf = x;
                yf = y;
                dibujar();
                rectaPoligonal.reset();
                break;
        }
        return true;
    }

    public void dibujar() {
        switch (figura) {
            case "rect":
                lienzoFondo.drawLine(x0, y0, xf, yf, pincel);
                break;
            case "libre":
                lienzoFondo.drawPath(rectaPoligonal, pincel);
                break;
            case "circ":
                lienzoFondo.drawCircle(x0, y0, (float) Math.sqrt(Math.pow(x0 - xf, 2) + Math.pow(y0 - yf, 2)),
                        pincel);
                break;
            case "cuad":
                lienzoFondo.drawRect(x0, y0, xf, yf, pincel);
                break;
        }
    }

    public Bitmap getMapaDeBits() {
        return mapaDeBits;
    }

    public void setMapaDeBits(Bitmap mapaDeBits) {
        limpiar();
        this.mapaDeBits = mapaDeBits;
    }

    public void limpiar() {
        pinta = false;
        lienzoFondo.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    public String getFigura() {
        return figura;
    }

    public void setFigura(String figura) {
        pincel.setStyle(Paint.Style.FILL);
        this.figura = figura;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        pincel.setColor(this.color);
    }
}
