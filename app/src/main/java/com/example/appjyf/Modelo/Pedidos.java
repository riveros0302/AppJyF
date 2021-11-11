package com.example.appjyf.Modelo;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Pedidos extends Productos implements Serializable{
    private Integer id_ped;
    private Integer cant_ped;
    private Integer total_ped;
    private Integer prod;
    private String nota_ped;


    public Pedidos() {
    }

    public Pedidos(Integer id_prod, Integer id_ped, Integer cant_ped, Integer total_ped, String nota_ped) {
        super(id_prod);
        this.id_ped = id_ped;
        this.cant_ped = cant_ped;
        this.total_ped = total_ped;
        this.nota_ped = nota_ped;
    }



    public Pedidos(String desc_prod, Integer id_ped, Integer cant_ped, Integer total_ped) {
        super(desc_prod);
        this.id_ped = id_ped;
        this.cant_ped = cant_ped;
        this.total_ped = total_ped;
    }

    public Integer getId_ped() {
        return id_ped;
    }

    public void setId_ped(Integer id_ped) {
        this.id_ped = id_ped;
    }

    public Integer getCant_ped() {
        return cant_ped;
    }

    public void setCant_ped(Integer cant_ped) {
        this.cant_ped = cant_ped;
    }

    public Integer getTotal_ped() {
        return total_ped;
    }

    public void setTotal_ped(Integer total_ped) {
        this.total_ped = total_ped;
    }

    public Integer getProd() {
        return prod;
    }

    public void setProd(Integer prod) {
        this.prod = prod;
    }

    public String getNota_ped() {
        return nota_ped;
    }

    public void setNota_ped(String nota_ped) {
        this.nota_ped = nota_ped;
    }
}
