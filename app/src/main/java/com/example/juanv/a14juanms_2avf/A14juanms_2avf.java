package com.example.juanv.a14juanms_2avf;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

public class A14juanms_2avf extends Activity {
    EditText etName;
    EditText etPhone;
    SharedPreferences sharedpref;
    SharedPreferences.Editor editor;
    private baseDatos basedatos;
    Button btnAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a14juanms_2avf);
        etName = (EditText) findViewById(R.id.etName);
        etPhone = (EditText) findViewById(R.id.etPhone);
        sharedpref = getSharedPreferences("color", MODE_PRIVATE);
        aplicarpreferencias();

        //obtemos a base de datos e avisamos
        basedatos = new baseDatos(getApplicationContext());
        basedatos.getWritableDatabase();
        Toast.makeText(getApplicationContext(), R.string.bdCreada, Toast.LENGTH_LONG).show();



        //BTNLLAMAR
        Button btnCall = (Button) findViewById(R.id.btnCall);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etPhone.getText().toString().equals("")) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + etPhone.getText()));
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.callNO, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //BTNSALARY
        Button btnSalary = (Button) findViewById(R.id.btnSalary);
        btnSalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etName.getText().toString().equals("")) {
                    Intent intent = new Intent(getApplication(), salary.class);
                    Bundle feixe = new Bundle();
                    feixe.putString("nombre", etName.getText().toString());
                    intent.putExtras(feixe);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.meteName, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //BTNAUDIO
        btnAudio=(Button)findViewById(R.id.btnAudio);
        btnAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etName.getText().toString().equals("")) {
                    Intent intent = new Intent(getApplication(), audio.class);
                    Bundle feixe = new Bundle();
                    feixe.putString("nombre", etName.getText().toString());
                    intent.putExtras(feixe);
                    File faudio=new File(Environment.getExternalStorageDirectory(),"/AUDIO/a14juanms/");
                    if (!faudio.exists())faudio.mkdirs();
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.meteName, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.itemAzul) {
            item.setChecked(true);
            sharedpref = getSharedPreferences("color", MODE_PRIVATE);
            editor = sharedpref.edit();
            editor.putString("color", "azul");
            editor.commit();
            aplicarpreferencias();
            return true;
        }
        if (id == R.id.itemVerde) {
            item.setChecked(true);
            sharedpref = getSharedPreferences("color", MODE_PRIVATE);
            editor = sharedpref.edit();
            editor.putString("color", "verde");
            editor.commit();
            aplicarpreferencias();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void aplicarpreferencias() {
        sharedpref = getSharedPreferences("color", MODE_PRIVATE);
        String color = sharedpref.getString("color", "azul"); //azul por defecto
        //aplicamos colores definidos en un recurso XML
        if (color.equals("azul")) {
            etName.setTextColor(getResources().getColor(R.color.blue));
        } else {
            etName.setTextColor(getResources().getColor(R.color.green));
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        aplicarpreferencias();
    }





}
