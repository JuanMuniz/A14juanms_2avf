package com.example.juanv.a14juanms_2avf;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class salary extends Activity {
    File ruta;
    String nombre;
    TextView txtSalario;
    private baseDatos base;
    Button btnXML,btnShow,btnSTF;
    private final String archivoDescargar = "http://manuais.iessanclemente.net/images/5/53/Salaries.xml";
    private File rutaArquivo;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary);
        //traigo el nombre de la activity principal pero realmente no me vale para nada
        Intent intent = getIntent();
        nombre = intent.getExtras().getString("nombre");
        txtSalario = (TextView) findViewById(R.id.txtSalario);
        base = new baseDatos(this);
        //método getReadable para meter datos ojito!!!! xa vale para despois de descargar
        base.sqlLiteDB = base.getReadableDatabase();
        //base.engadirSalario(new Salario("Febrero", 2000));
        //Log.i("Engadido", "YES!");

        // Boton descargar archivo
        btnXML = (Button) findViewById(R.id.btnXMLpro);
        btnXML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thread = new Thread() {

                    @Override
                    public void run() {
                        descargarArquivo();
                    }
                };
                thread.start();
                try {
                    thread.join();
                    Toast.makeText(getApplicationContext(),"DESCARGA TERMINADA",Toast.LENGTH_SHORT).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                procesarArquivo();
            }
        });

        //Boton enseñar Salarios
        btnShow=(Button)findViewById(R.id.btnShowSalary);
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarSalario();
            }
        });

        btnSTF=(Button)findViewById(R.id.btnPasarArchivo);
        btnSTF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pasarSalarioArchivo();
            }
        });
    }


   //Método que descarga o arquivo
    private void descargarArquivo() {
        URL url = null;
        try {
            url = new URL(archivoDescargar);
        } catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return;
        }

        HttpURLConnection conn = null;
        String nomeArquivo = Uri.parse(archivoDescargar).getLastPathSegment();
        rutaArquivo = new File(Environment.getExternalStorageDirectory(),"/SALARIO/"+nomeArquivo);
        ruta=new File(Environment.getExternalStorageDirectory(),"/SALARIO/");
        if(!ruta.exists())ruta.mkdirs();
        try {

            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);/* milliseconds */
            conn.setConnectTimeout(15000);/* milliseconds */
            conn.setRequestMethod("POST");
            conn.setDoInput(true);/* Indicamos que a conexión vai recibir datos */

            conn.connect();

            int response = conn.getResponseCode();
            if (response != HttpURLConnection.HTTP_OK) {
                return;
            }
            OutputStream os = new FileOutputStream(rutaArquivo);
            InputStream in = conn.getInputStream();
            byte data[] = new byte[1024];// Buffer a utilizar
            int count;
            while ((count = in.read(data)) != -1) {
                os.write(data, 0, count);
            }
            os.flush();
            os.close();
            in.close();
            conn.disconnect();
            Log.i("COMUNICACION", "ACABO");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            Log.e("COMUNICACION", e.getMessage());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("COMUNICACION", e.getMessage());
        }

    }

    //Vamos a leer e procesar o arquivo
    private ArrayList<Salario> contactosalarios = new ArrayList<Salario>();
    private void procesarArquivo(){

        try {
            lerArquivo();
            Log.i("Archivo","Leido con éxito");

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, "ERRO:"+ e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, "ERRO:"+ e.getMessage(), Toast.LENGTH_LONG).show();
        }



    }

    //leer Archivo y meter los campos a la base de datos
    private void lerArquivo() throws IOException, XmlPullParserException {

        InputStream is = new FileInputStream(rutaArquivo);

        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(is, "UTF-8");

        int evento = parser.nextTag();
        Salario s = null;

        while(evento != XmlPullParser.END_DOCUMENT) {
            if(evento == XmlPullParser.START_TAG) {
                if (parser.getName().equals("salary")) {      // Un novo Salario
                    s = new Salario();
                    evento = parser.nextTag();      // Pasamos a <month>
                    s.setMonth(parser.nextText());
                    evento = parser.nextTag();      // Pasamos a <amount>
                    s.setTotal_salary(Integer.parseInt(parser.nextText()));
                    evento = parser.nextTag();      // Pasamos a <complement>
                    s.setTotal_salary(s.getTotal_salary()+Integer.parseInt(parser.nextText()));
                    evento = parser.nextTag();      // Pasamos a <complement>
                    s.setTotal_salary(s.getTotal_salary()+Integer.parseInt(parser.nextText()));
                }
            }
            if(evento == XmlPullParser.END_TAG) {
                if (parser.getName().equals("salary")) {      // Un novo salario

                    try {
                        base.engadirSalario(s);
                        Log.i("Engadido", s.toString());
                    }catch(SQLiteConstraintException e){
                        e.getMessage();
                    }

                }
            }

            evento = parser.next();
        }

        is.close();
    }

    private void mostrarSalario(){
        txtSalario.setText("");
        txtSalario.setText("Total Salary    Month. \n");
        contactosalarios=base.obterSalario();
        for (Salario s:contactosalarios) {
            if (!txtSalario.getText().toString().contains(s.getMonth())) {
                txtSalario.append(s.getTotal_salary() + "                " + s.getMonth() + "\n");
            }
        }
    }

    private void pasarSalarioArchivo(){
        for (Salario s:contactosalarios)
            Log.i("Salario",s.getMonth());
        File arquivo = new File(ruta, "salaries.txt");
        try {
            FileOutputStream fos_sd = new FileOutputStream(arquivo,false);
            OutputStreamWriter iswriter_sd = new OutputStreamWriter(fos_sd);
            iswriter_sd.write(txtSalario.getText().toString());
            iswriter_sd.flush();

            iswriter_sd.close();
            fos_sd.close();

            Toast.makeText(getApplicationContext(), "Texto gardado en " +arquivo.getAbsolutePath(), Toast.LENGTH_LONG).show();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
