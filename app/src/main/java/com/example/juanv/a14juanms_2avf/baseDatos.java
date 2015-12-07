package com.example.juanv.a14juanms_2avf;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class baseDatos extends SQLiteOpenHelper {

    public SQLiteDatabase sqlLiteDB;

    public final static String NOME_BD = "salaries.db";
    public final static int VERSION_BD = 1;

    private final String CONSULTAR_SALARIO = "SELECT month,total_salary FROM salary order by month";
    private final String TABOA_SALARY = "salary";


    private String CREAR_TABOA_SALARY = "CREATE TABLE salary ( " +
            "month  VARCHAR(50) PRIMARY KEY ," +
            "total_salary REAL  NOT NULL)";


    public baseDatos(Context context) {
        super(context, NOME_BD, null, VERSION_BD);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(CREAR_TABOA_SALARY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS salary");
        onCreate(db);
    }


    public void engadirSalario(Salario s) {
        int salario=s.getTotal_salary();
        String mes=s.getMonth();
        sqlLiteDB.execSQL("INSERT INTO salary (month,total_salary) VALUES ('"+s.getMonth()+"','"+s.getTotal_salary()+"')");

    }

    public ArrayList<Salario> obterSalario() {
        ArrayList<Salario> salarios_devolver = new ArrayList<Salario>();

        Cursor datosConsulta = sqlLiteDB.rawQuery(CONSULTAR_SALARIO, null);
        if (datosConsulta.moveToFirst()) {
            Salario s;
            while (!datosConsulta.isAfterLast()) {
                s = new Salario(datosConsulta.getString(0),
                        datosConsulta.getInt(1));
                salarios_devolver.add(s);
                datosConsulta.moveToNext();
            }
        }

        return salarios_devolver;
    }


}


