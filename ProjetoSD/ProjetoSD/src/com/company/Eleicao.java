package com.company;

import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;

public class Eleicao implements Serializable{
    private String data_inicio;
    private String data_fim;
    private String titulo;
    private String descricao;
    private String tipoEleicao;
    private CopyOnWriteArrayList<String> departamento;
    private CopyOnWriteArrayList<Candidato> listaCandidatos;
    private int resultado;

    public Eleicao(String data_inicio, String data_fim, String titulo, String descricao, String tipoEleicao, CopyOnWriteArrayList<String> departamento, int resultado) {
        this.data_inicio = data_inicio;
        this.data_fim = data_fim;
        this.titulo = titulo;
        this.descricao = descricao;
        this.tipoEleicao = tipoEleicao;
        this.departamento = departamento;
        this.listaCandidatos = new CopyOnWriteArrayList<>();
        this.listaCandidatos.add(new Candidato("Branco", tipoEleicao, null));
        this.listaCandidatos.add(new Candidato("Nulo", tipoEleicao, null));
        this.resultado = resultado;
    }

    public Eleicao() {
        this(null, null, null, null, null, null, -1);
    }

    public String getData_inicio() {
        return data_inicio;
    }

    public void setData_inicio(String data_inicio) {
        this.data_inicio = data_inicio;
    }

    public String getData_fim() {
        return data_fim;
    }

    public void setData_fim(String data_fim) {
        this.data_fim = data_fim;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipoEleicao() {
        return tipoEleicao;
    }

    public void setTipoEleicao(String tipoEleicao) {
        this.tipoEleicao = tipoEleicao;
    }

    public CopyOnWriteArrayList<String> getDepartamento() {
        return departamento;
    }

    public void setDepartamento(CopyOnWriteArrayList<String> departamento) {
        this.departamento = departamento;
    }

    public CopyOnWriteArrayList<Candidato> getListaCandidatos() {
        return listaCandidatos;
    }

    public void setListaCandidatos(CopyOnWriteArrayList<Candidato> listaCandidatos) {
        this.listaCandidatos = listaCandidatos;
    }

    public int getResultado() {
        return resultado;
    }

    public void setResultado(int resultado) {
        this.resultado = resultado;
    }

    @Override
    public String toString() {
        return "Eleicao{" +
                "data_inicio=" + data_inicio +
                ", data_fim=" + data_fim +
                ", titulo='" + titulo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", tipoEleicao='" + tipoEleicao + '\'' +
                ", departamento='" + departamento + '\'' +
                ", listaCandidatos=" + listaCandidatos +
                ", resultado=" + resultado +
                '}';
    }
}