package com.company;

import java.io.Serializable;

public class Pessoa implements Serializable{
    private String nome;
    private String password;
    private String funcao;
    private String departamento;
    private int num_telefone;
    private String morada;
    private String num_cc;
    private String data_validade_cc;


    public Pessoa(String nome, String password, String funcao, String departamento, int num_telefone, String morada, String num_cc, String data_validade_cc) {
        this.nome = nome;
        this.password = password;
        this.funcao = funcao;
        this.departamento = departamento;
        this.num_telefone = num_telefone;
        this.morada = morada;
        this.num_cc = num_cc;
        this.data_validade_cc = data_validade_cc;
    }

    public Pessoa(){ this(null, null, null, null, -1, null, null, null);}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public int getNum_telefone() {
        return num_telefone;
    }

    public void setNum_telefone(int num_telefone) {
        this.num_telefone = num_telefone;
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public String getNum_cc() {
        return num_cc;
    }

    public void setNum_cc(String num_cc) {
        this.num_cc = num_cc;
    }

    public String getData_validade_cc() {
        return data_validade_cc;
    }

    public void setData_validade_cc(String data_validade_cc) {
        this.data_validade_cc = data_validade_cc;
    }

    @Override
    public String toString() {
        return "Pessoa{" +
                "nome='" + nome + '\'' +
                ", password='" + password + '\'' +
                ", funcao='" + funcao + '\'' +
                ", departamento='" + departamento + '\'' +
                ", num_telefone=" + num_telefone +
                ", morada='" + morada + '\'' +
                ", num_cc=" + num_cc +
                ", data_validade_cc=" + data_validade_cc +
                '}';
    }
}
