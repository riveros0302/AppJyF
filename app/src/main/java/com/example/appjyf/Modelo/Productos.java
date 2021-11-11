package com.example.appjyf.Modelo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.Serializable;

public class Productos implements Serializable{
    private Integer id_prod;
    private String nom_prod;
    private String desc_prod;
    private String marca_prod;
    private Integer precio_prod;
    private String ruta_img;
    private String unit_prod;
   // private String dato;
    // private Bitmap img_prod;


    public Productos() {
    }

    public Productos(Integer id_prod) {
        this.id_prod = id_prod;
    }

    public Productos(String desc_prod) {
        this.desc_prod = desc_prod;
    }

    public Productos(Integer id_prod, String nom_prod, String desc_prod, String marca_prod, Integer precio_prod, String ruta_img, String unit_prod) {
        this.id_prod = id_prod;
        this.nom_prod = nom_prod;
        this.desc_prod = desc_prod;
        this.marca_prod = marca_prod;
        this.precio_prod = precio_prod;
        this.ruta_img = ruta_img;
        this.unit_prod = unit_prod;
    }

    public Integer getId_prod() {
        return id_prod;
    }

    public void setId_prod(Integer id_prod) {
        this.id_prod = id_prod;
    }

    public String getNom_prod() {
        return nom_prod;
    }

    public void setNom_prod(String nom_prod) {
        this.nom_prod = nom_prod;
    }

    public String getDesc_prod() {
        return desc_prod;
    }

    public void setDesc_prod(String desc_prod) {
        this.desc_prod = desc_prod;
    }

    public String getMarca_prod() {
        return marca_prod;
    }

    public void setMarca_prod(String marca_prod) {
        this.marca_prod = marca_prod;
    }

    public Integer getPrecio_prod() {
        return precio_prod;
    }

    public void setPrecio_prod(Integer precio_prod) {
        this.precio_prod = precio_prod;
    }

    public String getRuta_img() {
        return ruta_img;
    }

    public void setRuta_img(String ruta_img) {
        this.ruta_img = ruta_img;
    }

    public String getUnit_prod() {
        return unit_prod;
    }

    public void setUnit_prod(String unit_prod) {
        this.unit_prod = unit_prod;
    }

    /* public String getDato() { return dato; }


    public void setDato(String dato) {
        this.dato = dato;

        try{
            byte[] bytesCode = Base64.decode(dato, Base64.DEFAULT);
            this.img_prod = BitmapFactory.decodeByteArray(bytesCode,0,bytesCode.length);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Bitmap getImg_prod() {
        return img_prod;
    }

    public void setImg_prod(Bitmap img_prod) {
        this.img_prod = img_prod;
    }*/

    @Override
    public String toString() {
        return desc_prod;
    }
}
