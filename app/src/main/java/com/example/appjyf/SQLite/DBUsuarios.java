package com.example.appjyf.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.appjyf.Modelo.Productos;
import com.example.appjyf.Modelo.Usuarios;

import java.util.ArrayList;

public class DBUsuarios extends Database {

    Context context;
    Cursor cursor;


    public DBUsuarios(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public long insertarUsuario(String nom, String ape, String emp, String correo, String fono, String comuna){
        long id = 0;
        try {
            Database database = new Database(context);
            SQLiteDatabase dbLite = database.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("nom_usu", nom);
            values.put("ape_usu", ape);
            values.put("emp_usu", emp);
            values.put("correo_usu", correo);
            values.put("fono_usu", fono);
            values.put("comuna_usu", comuna);

            id = dbLite.insert(DBContract.TABLE_USER, null, values);
        }catch (Exception e){
            e.toString();
        }
        return id;
    }


    public void updateUsuario(String nom, String ape, String emp, String correo, String fono, String comuna, int ID){
        try {
            Database database = new Database(context);
            SQLiteDatabase dbLite = database.getWritableDatabase();
            String[] parametros = {String.valueOf(ID)};

            ContentValues values = new ContentValues();
            values.put("nom_usu", nom);
            values.put("ape_usu", ape);
            values.put("emp_usu", emp);
            values.put("correo_usu", correo);
            values.put("fono_usu", fono);
            values.put("comuna_usu",comuna);

            dbLite.update(DBContract.TABLE_USER, values, DBContract.usuarios.ID_USU+"=?",parametros);

            dbLite.close();
        }catch (Exception e){
            e.toString();
        }
    }


    public void mostrarUsuario(EditText nom, EditText ape, EditText emp, EditText mail, EditText fono, EditText comuna, int ID){

        Database database = new Database(context);
        SQLiteDatabase dbLite = database.getReadableDatabase();

        Cursor c = dbLite.rawQuery("SELECT * FROM "+DBContract.TABLE_USER+" WHERE "+DBContract.usuarios.ID_USU+"="+ID, null);
    if (c != null) {
            c.moveToFirst();
            do {
                nom.setText(c.getString(1));
                ape.setText(c.getString(2));
                emp.setText(c.getString(3));
                mail.setText(c.getString(4));
                fono.setText(c.getString(5));
                comuna.setText(c.getString(6));

            } while (c.moveToNext());
        }

        //Cerramos el cursor y la conexion con la base de datos
        c.close();
        dbLite.close();
    }


    public ArrayList<Usuarios> mostrarUsuarios(){
        Database database = new Database(context);
        SQLiteDatabase dbLite = database.getWritableDatabase();
        ArrayList<Usuarios> list = new ArrayList<>();
        Usuarios usuarios = null;
        //Cursor cursor = null;

        cursor = dbLite.rawQuery("SELECT * FROM "+ DBContract.TABLE_USER, null);
        if (cursor.moveToFirst()){
            do {
                usuarios = new Usuarios();
                usuarios.setId_usu(cursor.getInt(0));
                usuarios.setNom_usu(cursor.getString(1));
                usuarios.setApe_usu(cursor.getString(2));
                usuarios.setEmp_usu(cursor.getString(3));
                usuarios.setCorreo_usu(cursor.getString(4));
                usuarios.setFono_usu(cursor.getString(5));
                list.add(usuarios);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public void EliminarUsuario(){
        Database database = new Database(context);
        SQLiteDatabase dbLite = database.getWritableDatabase();
        dbLite.execSQL("DELETE FROM "+DBContract.TABLE_USER);
        dbLite.close();
    }

}
