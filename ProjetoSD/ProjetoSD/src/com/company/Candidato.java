package com.company;

import java.io.Serializable;
import java.util.ArrayList;

public class Candidato implements Serializable{
    private String nome;
    private String categoria;
    private ArrayList<Pessoa> lista_pessoas;
    private int numVotos;


    public Candidato(String nome, String categoria, ArrayList<Pessoa> lista_pessoas) {
        this.nome = nome;
        this.categoria = categoria;
        this.lista_pessoas = lista_pessoas;
        this.numVotos = 0;
    }

    public Candidato() { this(null, null, null); }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public ArrayList<Pessoa> getLista_pessoas() {
        return lista_pessoas;
    }

    public void setLista_pessoas(ArrayList<Pessoa> lista_pessoas) {
        this.lista_pessoas = lista_pessoas;
    }

    public int getNumVotos() {
        return numVotos;
    }

    public void setNumVotos(int numVotos) {
        this.numVotos = numVotos;
    }

    @Override
    public String toString() {
        return "Candidato{" +
                "nome='" + nome + '\'' +
                ", categoria='" + categoria + '\'' +
                ", lista_pessoas=" + lista_pessoas +
                '}';
    }
}
