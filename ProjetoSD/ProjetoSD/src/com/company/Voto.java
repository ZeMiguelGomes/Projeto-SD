package com.company;

import java.io.Serializable;
import java.sql.Timestamp;

public class Voto implements Serializable {
    String eleicaoID;
    String num_cc;
    String localVoto;
    Timestamp horaVoto;


    public Voto(String eleicaoID, String num_cc, String localVoto, Timestamp horaVoto) {
        this.eleicaoID = eleicaoID;
        this.num_cc = num_cc;
        this.localVoto = localVoto;
        this.horaVoto = horaVoto;
    }

    public Voto() {this(null, null, null, null);}

    public String getEleicaoID() {
        return eleicaoID;
    }

    public void setEleicaoID(String eleicaoID) {
        this.eleicaoID = eleicaoID;
    }

    public String getNum_cc() {
        return num_cc;
    }

    public void setNum_cc(String num_cc) {
        this.num_cc = num_cc;
    }

    public String getLocalVoto() {
        return localVoto;
    }

    public void setLocalVoto(String localVoto) {
        this.localVoto = localVoto;
    }

    public Timestamp getHoraVoto() {
        return horaVoto;
    }

    public void setHoraVoto(Timestamp horaVoto) {
        this.horaVoto = horaVoto;
    }

    @Override
    public String toString() {
        return "Voto{" +
                "eleicaoID='" + eleicaoID + '\'' +
                ", num_cc='" + num_cc + '\'' +
                ", localVoto='" + localVoto + '\'' +
                ", horaVoto=" + horaVoto +
                '}';
    }
}