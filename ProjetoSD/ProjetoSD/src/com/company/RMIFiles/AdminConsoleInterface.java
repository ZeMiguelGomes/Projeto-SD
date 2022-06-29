package com.company.RMIFiles;

import com.company.Eleicao;

import java.rmi.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public interface AdminConsoleInterface extends Remote{
    void print_on_client(String s) throws RemoteException;

    void displayEleicoes(String id, String titulo, String tipo, String departamento, String data_inicio) throws RemoteException;

    void displayCandidatura(String id, String nomeCandidato, String categoria, String numEleicao, String titulo) throws RemoteException;

    void displayListaPessoasParaCandidatura(String num_cc, String nomeCandidato) throws RemoteException;

    void displayListaElementosCandidatura(String num_cc, String nome, String nomeCandidato) throws RemoteException;

    void displayListaTudoEleicao(String num_cc, String nome, String nomeCandidato) throws RemoteException;

    void displayDetalhesEleicao(String titulo, String descricao, Timestamp data_inicio, Timestamp data_fim) throws RemoteException;

    void displaylocalVotoEleitores(String local_voto, String hora_voto, String nome, String num_cc, String titulo) throws RemoteException;

    void displayEleicoesPassadas1(Eleicao e, int totalVotos, int votoBranco, int votoNulo, int flag, String nomeCandidato, int numVotos, float percentagem) throws RemoteException;

    void displaygereMesadeVoto(String titulo, String departameto) throws RemoteException;

    void displayEleitoresTempoReal(int numVotos, String local_voto) throws RemoteException;
}
