package com.example.appjyf.SQLite;

import android.graphics.Bitmap;
import android.provider.BaseColumns;

public class DBContract {


    public static final String DATABASE_NAME = "DBLite.db";
    public static final String TABLE_PRODUCTOS = "productos";
    public static final String TABLE_PEDIDOS = "pedidos";
    public static final String TABLE_USER = "usuarios";

    public static class productos implements BaseColumns {
        public static String ID = "id_prod";
        public static String NOMBRE = "nom_prod";
        public static String DESCRIPCION = "desc_prod";
        public static String MARCA = "marca_prod";
        public static String PRECIO = "precio_prod";
        public static String RUTA_IMG = "ruta_img";
        public static String UNIT = "cant_prod";
        //public static String DATO = "dato";
        //public static String IMAGEN = "img_prod";



    }

    public static class pedidos implements BaseColumns{
        public static String ID_PED = "id_ped";
        public static String CANT_PED = "cant_ped";
        public static String TOTAL_PED = "total_ped";
        public static String ID_PROD = "id_prod";
        public static String NOTA_PED = "nota_ped";
    }

    public static class usuarios implements BaseColumns{
        public static String ID_USU = "id_usu";
        public static String NOM_USU = "nom_usu";
        public static String APE_USU = "ape_usu";
        public static String EMP_USU = "emp_usu";
        public static String CORREO_USU = "correo_usu";
        public static String FONO_USU = "fono_usu";
        public static String COM_USU = "comuna_usu";
    }
}
