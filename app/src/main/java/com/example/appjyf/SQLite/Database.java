package com.example.appjyf.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import com.android.volley.toolbox.StringRequest;

public class Database extends SQLiteOpenHelper {

    public static int VERSION = 18;

    public Database(@Nullable Context context) {
        super(context, DBContract.DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

       db.execSQL("CREATE TABLE "+DBContract.TABLE_PRODUCTOS+"("+DBContract.productos.ID+" INTEGER PRIMARY KEY,"+DBContract.productos.NOMBRE+" TEXT," +
                ""+DBContract.productos.DESCRIPCION+" TEXT, "+DBContract.productos.MARCA+" TEXT," +
                ""+DBContract.productos.PRECIO+" INTEGER,"+DBContract.productos.RUTA_IMG+" TEXT, "+DBContract.productos.UNIT+" TEXT "+")");

       CrearPedido(db);
       CrearUsuario(db);
    }

    private void CrearPedido(SQLiteDatabase db){

        db.execSQL("CREATE TABLE "+DBContract.TABLE_PEDIDOS+"("+DBContract.pedidos.ID_PED+" INTEGER PRIMARY KEY AUTOINCREMENT,"+DBContract.pedidos.CANT_PED+" INTEGER," +
                ""+DBContract.pedidos.TOTAL_PED+" INTEGER, "+DBContract.pedidos.ID_PROD+" INTEGER, "+DBContract.pedidos.NOTA_PED+" TEXT "+")");
    }

    private void CrearUsuario(SQLiteDatabase db){
        db.execSQL("CREATE TABLE "+DBContract.TABLE_USER+"("+DBContract.usuarios.ID_USU+" INTEGER PRIMARY KEY, "+DBContract.usuarios.NOM_USU+" TEXT,"+
                ""+DBContract.usuarios.APE_USU+" TEXT, "+DBContract.usuarios.EMP_USU+" TEXT," +
                ""+DBContract.usuarios.CORREO_USU+" TEXT, "+DBContract.usuarios.FONO_USU+" TEXT, "+DBContract.usuarios.COM_USU+" TEXT "+")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

      DeleteProductos(db);
    }

    public void DeleteProductos (SQLiteDatabase db){
        db.execSQL("DROP TABLE "+DBContract.TABLE_PRODUCTOS);
        db.execSQL("DROP TABLE "+DBContract.TABLE_PEDIDOS);
        db.execSQL("DROP TABLE "+DBContract.TABLE_USER);
        onCreate(db);
    }



    public void savedProductos(int id, String nombre, String descripcion, String marca, int precio, String ruta, String unit, SQLiteDatabase db){

        ContentValues values = new ContentValues();
        values.put(DBContract.productos.ID, id);
        values.put(DBContract.productos.NOMBRE, nombre);
        values.put(DBContract.productos.DESCRIPCION, descripcion);
        values.put(DBContract.productos.MARCA, marca);
        values.put(DBContract.productos.PRECIO, precio);
        values.put(DBContract.productos.RUTA_IMG, ruta);
        values.put(DBContract.productos.UNIT, unit);
       // values.put(DBContract.productos.IMAGEN, img);

        db.insert(DBContract.TABLE_PRODUCTOS,null, values);
    }

    public void savedPedidos (int id, int cant, int total, int id_prod){
        ContentValues values = new ContentValues();
        values.put(DBContract.pedidos.ID_PED, id);
        values.put(DBContract.pedidos.CANT_PED, cant);
        values.put(DBContract.pedidos.TOTAL_PED, total);
        values.put(DBContract.pedidos.ID_PROD, id_prod);
    }

    public Cursor readProductos(SQLiteDatabase db){
        return (db.query(DBContract.TABLE_PRODUCTOS, null, null, null, null, null, null));
    }

    public Cursor readPedidos(SQLiteDatabase db){
        return (db.query(DBContract.TABLE_PEDIDOS, null, null, null, null, null, null));
    }

    public Cursor readUsuarios(SQLiteDatabase db){
        return (db.query(DBContract.TABLE_USER, null, null, null, null, null, null));
    }
}
