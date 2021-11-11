package com.example.appjyf.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.appjyf.Modelo.Productos;

import java.util.ArrayList;

public class DBProductos extends Database {

    Context context;
    Cursor cursor;

    public DBProductos(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public long insertarProductos(String nombre, String descripcion, String marca, int precio, String ruta, String unit){
        long id = 0;
        try {
            Database database = new Database(context);
            SQLiteDatabase dbLite = database.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("nom_prod", nombre);
            values.put("desc_prod", descripcion);
            values.put("marca_prod", marca);
            values.put("precio_prod", precio);
            values.put("ruta_img", ruta);
            values.put("unit_prod", unit);

             id = dbLite.insert(DBContract.TABLE_PRODUCTOS, null, values);
        }catch (Exception e){
            e.toString();
        }
        return id;
    }

    public ArrayList<Productos> mostrarProductos(){
        Database database = new Database(context);
        SQLiteDatabase dbLite = database.getWritableDatabase();
        ArrayList<Productos> list = new ArrayList<>();
        Productos productos = null;
        //Cursor cursor = null;

        cursor = dbLite.rawQuery("SELECT * FROM "+ DBContract.TABLE_PRODUCTOS, null);
        if (cursor.moveToFirst()){
            do {
                productos = new Productos();
                productos.setId_prod(cursor.getInt(0));
                productos.setNom_prod(cursor.getString(1));
                productos.setDesc_prod(cursor.getString(2));
                productos.setMarca_prod(cursor.getString(3));
                productos.setPrecio_prod(cursor.getInt(4));
                productos.setRuta_img(cursor.getString(5));
                productos.setUnit_prod(cursor.getString(6));
                list.add(productos);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}
