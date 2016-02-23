package com.example.nebir.paint;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.larswerkman.holocolorpicker.ColorPicker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements ColorPicker.OnColorChangedListener{

    private Vista vista;
    private int OPEN_IMAGE = 1, color = Color.BLACK;
    private Bitmap bitmap;
    private ColorPicker picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vista = (Vista) findViewById(R.id.view);
    }

    public void freeDraw (View v){
        vista.setFigura("libre");
    }
    public void line (View v){
        vista.setFigura("rect");
    }
    public void square (View v){
        vista.setFigura("cuad");
    }
    public void circle (View v){
        vista.setFigura("circ");
    }
    public void color (View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.color, null);
        builder.setView(dialogView);
        builder.show();
        picker = (ColorPicker) dialogView.findViewById(R.id.picker);
        picker.setOnColorChangedListener(this);
        picker.setShowOldCenterColor(false);
    }

    public void save (View v){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("¿Desea guardar los cambios?");
        alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Bitmap  bm = Bitmap.createBitmap( vista.getWidth(), vista.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bm);
                vista.draw(canvas);
                guardarCambio(bm);
                Toast.makeText(getApplicationContext(), "La foto ha sido guardada en myAppDir/imagen",
                        Toast.LENGTH_LONG).show();
            }
        });
        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        alert.show();
    }

    private boolean guardarCambio(Bitmap imageData) {
        //Obtener ruta de la memoria externa
        String ruta="/myAppDir/imagen/";
        String iconsStoragePath = Environment.getExternalStorageDirectory() + ruta;
        File sdIconStorageDir = new File(iconsStoragePath);
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-mm-yy");
        String fecha=simpleDateFormat.format(date);
        String filename=" "+fecha+".JPEG";

        //Crea el directorio si este no existe
        sdIconStorageDir.mkdirs();

        try {
            String filePath = sdIconStorageDir.toString() + filename;
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            imageData.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (FileNotFoundException e) {
        } catch (IOException e) {}

        return true;
    }
    public void delete(View v){
        vista.limpiar();
    }
    public void open (View v){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, OPEN_IMAGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == OPEN_IMAGE) {
            Uri uri = data.getData();
            if (uri != null) { //Obtiene la Uri de la imagen

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                vista.setMapaDeBits(bitmap);
            }
        }
    }

    @Override
    public void onColorChanged(int color) {
        vista.setColor(color);
    }


}
