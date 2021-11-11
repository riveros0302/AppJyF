package com.example.appjyf.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.appjyf.DetailsProdFragment;
import com.example.appjyf.Modelo.Pedidos;
import com.example.appjyf.Modelo.Productos;
import com.example.appjyf.ui.notifications.NotificationsFragment;

import java.util.ArrayList;

public class DBPedidos extends Database {

    Context context;
    String position;

    public DBPedidos(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public long insertarPedidos(int cant, int total, int id_prod, String nota_ped){
        long id = 0;
        try {
            Database database = new Database(context);
            SQLiteDatabase dbLite = database.getWritableDatabase();

            ContentValues values = new ContentValues();
           // values.put("id_ped", id);
            values.put("cant_ped", cant);
            values.put("total_ped", total);
            values.put("id_prod", id_prod);
            values.put("nota_ped", nota_ped);

             id = dbLite.insert(DBContract.TABLE_PEDIDOS, null, values);
        }catch (Exception e){
            e.toString();
        }
        return id;
    }

    public ArrayList<Pedidos> mostrarPedidos(){
        Database database = new Database(context);
        SQLiteDatabase dbLite = database.getWritableDatabase();
        ArrayList<Pedidos> list = new ArrayList<>();
        Pedidos pedidos = null;
        Cursor cursor = null;
        Cursor cursor1 = null;

       // cursor1 = dbLite.rawQuery("SELECT * FROM "+DBContract.TABLE_PRODUCTOS, null);

        cursor = dbLite.rawQuery("SELECT * FROM "+ DBContract.TABLE_PEDIDOS , null);
        if (cursor.moveToFirst()){
            Integer i = cursor.getInt(3);
            do {
                pedidos = new Pedidos();
                pedidos.setId_ped(cursor.getInt(0));
                pedidos.setCant_ped(cursor.getInt(1));
                pedidos.setTotal_ped(cursor.getInt(2));
                pedidos.setProd(cursor.getInt(3));
                pedidos.setNota_ped(cursor.getString(4));
                //pedidos.setDesc_prod(cursor1.getString(2));

                list.add(pedidos);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

  /*  public boolean EliminarPedido(Integer id){
        boolean correcto = false;
        Database database = new Database(context);
        SQLiteDatabase db = database.getWritableDatabase();

        try {
            db.execSQL("DELETE FROM "+DBContract.TABLE_PEDIDOS+" WHERE "+DBContract.pedidos.ID_PED+" = '"+id+"'");
            correcto = true;
        }catch (Exception e){
            e.toString();
            correcto = false;

        }finally {
            db.close();
        }


        return correcto;
    }*/

    public void EliminarPedido(Integer id){
        Database database = new Database(context);
        SQLiteDatabase dbLite = database.getWritableDatabase();
        String[] parametro = {id.toString()};

        dbLite.delete(DBContract.TABLE_PEDIDOS,DBContract.pedidos.ID_PED+"=? ", parametro);
        Toast.makeText(context, "ID ="+id+" Eliminado", Toast.LENGTH_SHORT).show();
        dbLite.close();
    }

    public void EliminarTodo(){
        Database database = new Database(context);
        SQLiteDatabase dbLite = database.getWritableDatabase();
        dbLite.execSQL("DELETE FROM "+DBContract.TABLE_PEDIDOS);
        dbLite.close();
    }
}
