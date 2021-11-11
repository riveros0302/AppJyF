package com.example.appjyf.Modelo;

import java.io.Serializable;

public class Usuarios implements Serializable{
    private Integer id_usu;
    private String nom_usu;
    private String ape_usu;
    private String emp_usu;
    private String correo_usu;
    private String fono_usu;


    public Usuarios() {
    }

    public Integer getId_usu() {
        return id_usu;
    }

    public void setId_usu(Integer id_usu) {
        this.id_usu = id_usu;
    }

    public String getNom_usu() {
        return nom_usu;
    }

    public void setNom_usu(String nom_usu) {
        this.nom_usu = nom_usu;
    }

    public String getApe_usu() {
        return ape_usu;
    }

    public void setApe_usu(String ape_usu) {
        this.ape_usu = ape_usu;
    }

    public String getEmp_usu() {
        return emp_usu;
    }

    public void setEmp_usu(String emp_usu) {
        this.emp_usu = emp_usu;
    }

    public String getCorreo_usu() {
        return correo_usu;
    }

    public void setCorreo_usu(String correo_usu) {
        this.correo_usu = correo_usu;
    }

    public String getFono_usu() {
        return fono_usu;
    }

    public void setFono_usu(String fono_usu) {
        this.fono_usu = fono_usu;
    }
}
