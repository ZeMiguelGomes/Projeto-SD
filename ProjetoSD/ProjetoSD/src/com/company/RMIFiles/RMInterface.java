package com.company.RMIFiles;

import com.company.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.concurrent.CopyOnWriteArrayList;

public interface RMInterface extends Remote {

    void registaPessoa(Pessoa p) throws RemoteException, SQLClientInfoException;

    void criaEleicao(Eleicao e) throws RemoteException, SQLClientInfoException;

    void print_on_server(String s) throws RemoteException;

    void subscribe(AdminConsoleInterface c) throws RemoteException;

    void subscribeMulticast(MulticastServerInterface c) throws RemoteException;

    void ListaEleicoes() throws RemoteException, SQLException;

    int maxEleicoes() throws RemoteException, SQLException;

    String[] ListaCandidaturas(int opcaoEleicao) throws RemoteException, SQLException;

    String[] ListaPessoasParaCandidatura() throws RemoteException, SQLException;

    void AdicionaPessoaCandidatura(int opcaoEleicao, String num_cc, String partido, String idPartido) throws RemoteException, SQLException;

    String ListaElementosCandidatura(int opcaoEleicao, String candidatura, String idPartido) throws RemoteException, SQLException;

    void RemovePessoaCandidatura(String num_cc) throws RemoteException, SQLException;

    void ListaTudoEleicao(int opcaoEleicao) throws RemoteException, SQLException;

    String getDetalhesEleicao(int opcaoEleicao) throws RemoteException, SQLException;

    void UpdatePropriedadesEleicao(int opcaoEleicao, String tituloAlteracao, String descricaoAlteracao, Timestamp data_inicio, Timestamp data_fim) throws RemoteException, SQLException;

    Pessoa findPessoa(String num_cc)  throws RemoteException, SQLException;

    CopyOnWriteArrayList<Eleicao> getEleicao(String departamento) throws RemoteException, SQLException;

    CopyOnWriteArrayList<Candidato> getListaCandidatos(int eleicaoID) throws RemoteException, SQLException;

    int getIdVoto() throws RemoteException, SQLException;

    int getIdEleicao(String nomeEleicao) throws RemoteException, SQLException;

    boolean getlocalVotoEleitores() throws RemoteException, SQLException;

    int getMaxCandidato() throws RemoteException, SQLException;

    Eleicao getEleicaoByID(int opcaoEleicao) throws RemoteException, SQLException;

    void criaNovoCandidato(Candidato c, int opcaoEleicao) throws RemoteException, SQLException;

    void recebeLocalVoto(String local, String num_cc, String nomeEleicao)throws RemoteException, SQLException;

    void updateVotoPessoaData(Timestamp dataVoto,String num_cc, String nomeEleicao) throws RemoteException, SQLException;

    void recebeVoto(String Voto, String nomeEleicao)	throws RemoteException, SQLException;

    CopyOnWriteArrayList<Voto> getListaVotos() throws RemoteException, SQLException;

    CopyOnWriteArrayList<Object> getListaVotosSpring() throws RemoteException, SQLException;

    void consultaEleicoesPassadas(int eleicaoID) throws RemoteException, SQLException;

    CopyOnWriteArrayList<String> gereMesadeVoto(int eleicaoID) throws RemoteException, SQLException;

    void updateListaDep(int opcaoEleicao, CopyOnWriteArrayList<String> listaDept) throws RemoteException, SQLException;

    void ListaEleicoesPassadas() throws RemoteException, SQLException;

    boolean ListaEleicoesNaoComecadas() throws RemoteException, SQLException;

    void getEleitoresTempoReal(int idEleicao) throws RemoteException, SQLException;

    void saveDep(String name) throws RemoteException;

    int[] numEleicoesNaoComecadas() throws RemoteException, SQLException;

    CopyOnWriteArrayList<Object> ListaEleicoesSpring() throws RemoteException, SQLException;

    CopyOnWriteArrayList<Object> ListaEleicoesNaoComecadasSpring() throws RemoteException, SQLException;

    CopyOnWriteArrayList<Object> ListaEleicoesPassadasSpring() throws RemoteException, SQLException;

    CopyOnWriteArrayList<Object> ListaTodasCandidaturasSpring(String nomeEleicao) throws RemoteException, SQLException;

    int getIdPartido(String nomePartido) throws SQLException, RemoteException;

    CopyOnWriteArrayList<Object> getEleicoesparaVoto(String num_cc) throws SQLException, RemoteException;
}
