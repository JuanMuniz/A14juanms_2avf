package com.example.juanv.a14juanms_2avf;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class audio extends Activity {

    public MediaPlayer mediaplayer;
    private boolean pause;  // Indica se o mediaplayer estaba tocando cando cambiamos de aplicación
    Spinner spCanciones;
    Button btnRep, btnParar, btnGrabar;
    ArrayList<String> canciones;
    String cancionSeleccionada;
    File faudio;
    public MediaRecorder mediaRecorder = new MediaRecorder();
    ArrayAdapter<String> adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        mediaplayer = new MediaPlayer();
        spCanciones = (Spinner) findViewById(R.id.spCanciones);
        cargarSpinner();

        //BTNREPRODUCIR
        btnRep = (Button) findViewById(R.id.btnReproducir);
        btnRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String path = Environment.getExternalStorageDirectory() + "/AUDIO/a14juanms/" + cancionSeleccionada;
                    mediaplayer.reset();
                    mediaplayer.setDataSource(path);
                    mediaplayer.prepare();
                    mediaplayer.start();
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });


        //BTNPARAR
        btnParar = (Button) findViewById(R.id.btnParar);
        btnParar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mediaplayer.isPlaying())
                    mediaplayer.stop();
                pause = false;
            }
        });

        //BTNGRABAR
        btnGrabar = (Button) findViewById(R.id.btnGrabar);
        btnGrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timeStamp = DateFormat.getDateTimeInstance().format(
                        new Date()).replaceAll(":", "").replaceAll("/", "_")
                        .replaceAll(" ", "_");

                mediaRecorder = new MediaRecorder();
                File arquivoGravar = new File(Environment.getExternalStorageDirectory() + "/AUDIO/a14juanms/" + timeStamp + ".3gp");
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mediaRecorder.setMaxDuration(15000);
                mediaRecorder.setAudioEncodingBitRate(32768);
                mediaRecorder.setAudioSamplingRate(8000);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                mediaRecorder.setOutputFile(arquivoGravar.toString());
                try {
                    mediaRecorder.prepare();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    mediaRecorder.reset();
                }
                mediaRecorder.start();
                abrirDialogo();
                adaptador.notifyDataSetChanged();
                cargarSpinner();
            }

        });

    }

    private void cargarSpinner() {
        faudio = new File(Environment.getExternalStorageDirectory(), "/AUDIO/a14juanms/");
        canciones = new ArrayList<String>();
        String[] audios = faudio.list();
        for (int i = 0; i < audios.length; i++) canciones.add(audios[i]);
        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, canciones);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spCanciones.setAdapter(adaptador);

        spCanciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                cancionSeleccionada = canciones.get(pos).toString();
                Log.i("cancion", cancionSeleccionada);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        }); // Fin da clase anónima


    }


    //CADRO DE DIALOGO PARA GRABAR
    private void abrirDialogo() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setMessage("GRAVANDO").setPositiveButton(
                        "PREME PARA PARAR",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                mediaRecorder.stop();
                                mediaRecorder.release();
                                mediaRecorder = null;
                            }
                        });
        dialog.show();

    }


    @Override
    protected void onPause() {
        super.onPause();

        if (mediaplayer.isPlaying()) {
            mediaplayer.pause();
            pause = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (pause) {
            mediaplayer.start();
            pause = false;
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle estado) {
        estado.putBoolean("MEDIAPLAYER_PAUSE", pause);
        super.onSaveInstanceState(estado);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("MEDIAPLAYER_PAUSE", false);
        pause = savedInstanceState.getBoolean("MEDIAPLAYER_PAUSE");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mediaplayer.isPlaying()) mediaplayer.stop();

        if (mediaplayer != null) mediaplayer.release();
        mediaplayer = null;

    }
}
