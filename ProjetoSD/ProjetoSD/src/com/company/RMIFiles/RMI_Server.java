package com.company.RMIFiles;

import com.company.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class RMI_Server extends UnicastRemoteObject implements RMInterface {
	static AdminConsoleInterface client;
	static MulticastServerInterface mClient;
	private static int MAIN_PORT = 7000;
	private int SEC_PORT = 5000;
	ArrayList<String> listaMulticast = new ArrayList<>();


	public RMI_Server() throws RemoteException {
		super();
	}

	@Override
	public void subscribe(AdminConsoleInterface c) throws RemoteException {
		client = c;
	}

	@Override
	public void subscribeMulticast(MulticastServerInterface c) throws RemoteException {
		mClient = c;
		mClient.print_on_client("Olá do server");
	}

	public int getIdPartido(String nomePartido) throws SQLException, RemoteException {
		PostgreSQLJDBC db = new PostgreSQLJDBC();
		db.connectDB();

		int idPartido;
		idPartido = db.getIdPartido(nomePartido);
		return idPartido;
	}

	@Override
	public void registaPessoa(Pessoa p) throws RemoteException, SQLClientInfoException {
		//Inserir na tabela das pessoas a pessoa recebida
		PostgreSQLJDBC db = new PostgreSQLJDBC();
		db.connectDB();
		db.InsertPessoas(p);

		//client.print_on_client("Server: Registo feito com sucesso");
		//return p;
	}

	@Override
	public void criaEleicao(Eleicao e) throws RemoteException, SQLClientInfoException {
		//Inserir na tabela das eleições a eleiçao recebida
		PostgreSQLJDBC db = new PostgreSQLJDBC();
		db.connectDB();
		int idEleicao = db.InsertElection(e);
		db.InsertElectionCandidatos(e);
		db.InsertDepartamentoEleicao(idEleicao, e);

		//client.print_on_client("Server: Eleição criada com sucesso");
		//return e;
	}

	@Override
	public void ListaEleicoes() throws RemoteException, SQLException {
		//Lista todas as eleicoes a decorrer
		PostgreSQLJDBC db = new PostgreSQLJDBC();
		db.connectDB();
		ResultSet rs = db.listaEleicoes(); //Retorna a lista de eleições
		int id;
		String titulo, tipo, departamento;
		Timestamp data_inicio;

		while (rs.next()) {
			id = rs.getInt(1);
			titulo = rs.getString("titulo");
			tipo = rs.getString("tipo");
			departamento = rs.getString("departamento");
			data_inicio = rs.getTimestamp("data_inicio");

			client.displayEleicoes(String.valueOf(id), titulo, tipo, departamento, data_inicio.toString());
		}

	}

	@Override
	public CopyOnWriteArrayList<Object> ListaEleicoesSpring() throws RemoteException, SQLException {
		//Lista todas as eleicoes a decorrer
		PostgreSQLJDBC db = new PostgreSQLJDBC();
		db.connectDB();
		ResultSet rs = db.listaEleicoes(); //Retorna a lista de eleições
		int id;
		String titulo, tipo, departamento;
		Timestamp data_inicio;

		CopyOnWriteArrayList<Object> listaEleicoes = new CopyOnWriteArrayList<>();
		boolean val = rs.next();

		if (val == false) return null;
		else{
			while (val) {
				CopyOnWriteArrayList<String> listaDep = new CopyOnWriteArrayList<>();

				id = rs.getInt(1);
				titulo = rs.getString("titulo");
				tipo = rs.getString("tipo");
				departamento = rs.getString("departamento");
				data_inicio = rs.getTimestamp("data_inicio");
				listaDep.add(departamento);

				Eleicao e = new Eleicao(data_inicio.toString(), null, titulo, null, tipo, listaDep, id);
				//O RESULTADO CORRESPONDE AO ID DA ELEIÇÃO JÁ QUE NAO SE SUA PARA MAIS NADA
				listaEleicoes.add(e);

				val = rs.next();
			}

		}
		return listaEleicoes;
	}



	@Override
	public boolean ListaEleicoesNaoComecadas() throws RemoteException, SQLException {
		//Lista todas as eleicoes a decorrer
		PostgreSQLJDBC db = new PostgreSQLJDBC();
		db.connectDB();
		ResultSet rs = db.listaEleicoesNaoComecadas(); //Retorna a lista de eleições
		int id;
		String titulo, tipo, departamento;
		Timestamp data_inicio;
		boolean val = rs.next();

		if (val == false) return false;
		else {
			while (val) {
				id = rs.getInt(1);
				titulo = rs.getString("titulo");
				tipo = rs.getString("tipo");
				departamento = rs.getString("departamento");
				data_inicio = rs.getTimestamp("data_inicio");

				val = rs.next();
				client.displayEleicoes(String.valueOf(id), titulo, tipo, departamento, data_inicio.toString());
			}
		}
		return true;
	}

	public CopyOnWriteArrayList<Object> getEleicoesparaVoto(String num_cc) throws SQLException, RemoteException {
		PostgreSQLJDBC db = new PostgreSQLJDBC();
		db.connectDB();

		Pessoa p = findPessoa(num_cc);
		CopyOnWriteArrayList<Object> listaEleicoesAVotar = new CopyOnWriteArrayList<>();


		//Saber quais eleicoes que pode votar
		ArrayList<Integer> idEleicaoDisponivel = db.getEleicoesparaVotoSpring(p);
		for (int i = 0; i < idEleicaoDisponivel.size(); i++){
			Eleicao e = getEleicaoByID(idEleicaoDisponivel.get(i));
			listaEleicoesAVotar.add(e);
		}
		return listaEleicoesAVotar;
	}

	@Override
	public CopyOnWriteArrayList<Object> ListaEleicoesNaoComecadasSpring() throws RemoteException, SQLException {
		//Lista todas as eleicoes que nao começaram
		PostgreSQLJDBC db = new PostgreSQLJDBC();
		db.connectDB();
		ArrayList<Integer> listaIDEleicao = db.listaEleicoesNaoComecadasSpring(); //Retorna a lista de eleições
		int id;
		String titulo, tipo, departamento;
		Timestamp data_inicio;

		CopyOnWriteArrayList<Object> listaEleicoesNaoComecadas = new CopyOnWriteArrayList<>();

		for (int i = 0; i < listaIDEleicao.size(); i++){
			CopyOnWriteArrayList <String> listaDepEleicao = new CopyOnWriteArrayList<>();

			Eleicao e = getEleicaoByID(listaIDEleicao.get(i));

			listaDepEleicao = db.getDepEleicao(listaIDEleicao.get(i));
			e.setDepartamento(listaDepEleicao);

			listaEleicoesNaoComecadas.add(e);
		}

		return listaEleicoesNaoComecadas;
	}

	@Override
	public void ListaEleicoesPassadas() throws RemoteException, SQLException {
		//Lista todas as eleicoes a decorrer
		PostgreSQLJDBC db = new PostgreSQLJDBC();
		db.connectDB();
		ResultSet rs = db.listaEleicoesPassadas(); //Retorna a lista de eleições passadas
		int id;
		String titulo, tipo, departamento;
		Timestamp data_inicio;

		while (rs.next()) {
			id = rs.getInt(1);
			titulo = rs.getString("titulo");
			tipo = rs.getString("tipo");
			departamento = rs.getString("departamento");
			data_inicio = rs.getTimestamp("data_inicio");

			client.displayEleicoes(String.valueOf(id), titulo, tipo, departamento, data_inicio.toString());
		}

	}


	@Override
	public CopyOnWriteArrayList<Object> ListaEleicoesPassadasSpring() throws RemoteException, SQLException {
		//Lista todas as eleicoes passadas
		PostgreSQLJDBC db = new PostgreSQLJDBC();
		db.connectDB();
		ResultSet rs = db.listaEleicoesPassadas(); //Retorna a lista de eleições passadas
		int id;
		String titulo, tipo, departamento;
		Timestamp data_inicio;

		CopyOnWriteArrayList<Object> listaEleicoesPassadas = new CopyOnWriteArrayList<>();


		boolean val = rs.next();

		if (val == false) return null;
		else{
			while (val) {
				CopyOnWriteArrayList<String> listaDep = new CopyOnWriteArrayList<>();
				id = rs.getInt(1);
				titulo = rs.getString("titulo");
				tipo = rs.getString("tipo");
				departamento = rs.getString("departamento");
				data_inicio = rs.getTimestamp("data_inicio");

				listaDep.add(departamento);
				Eleicao e = new Eleicao(data_inicio.toString(), null, titulo, null, tipo, listaDep, id);
				listaEleicoesPassadas.add(e);

				val = rs.next();

			}

		}
		return listaEleicoesPassadas;
	}

	@Override
	public CopyOnWriteArrayList<Object> ListaTodasCandidaturasSpring(String nomeEleicao) throws RemoteException, SQLException {
		//Lista todas as eleicoes passadas
		PostgreSQLJDBC db = new PostgreSQLJDBC();
		db.connectDB();
		CopyOnWriteArrayList<Candidato> listaCandidato = new CopyOnWriteArrayList<>();

		int idEleicao = getIdEleicao(nomeEleicao);


		listaCandidato = db.listaTodasCandidaturasSpring(idEleicao);


		for (int i = 0; i < listaCandidato.size(); i++){
			System.out.println("-.-.-.-.-.-.-.-.-.-.-.");
			System.out.println(listaCandidato.get(i).getNome());
			System.out.println(listaCandidato.get(i).getCategoria());
			if (listaCandidato.get(i).getLista_pessoas() != null){
				for (int j = 0; j < listaCandidato.get(i).getLista_pessoas().size(); j++){
					System.out.println(listaCandidato.get(i).getLista_pessoas().get(j).getNome());
					System.out.println(listaCandidato.get(i).getLista_pessoas().get(j).getNum_cc());
					System.out.println("= == == == == == == == == ");
				}
			}else{
				System.out.println("\t Este candidato " + listaCandidato.get(i).getNome() + "noa tem pessoas associadas");
			}
		}
		CopyOnWriteArrayList<Object> aux = new CopyOnWriteArrayList<>();
		for(int i = 0; i < listaCandidato.size(); i++) {
			aux.add(listaCandidato.get(i));
		}

		return aux;
	}



	@Override
	public int maxEleicoes() throws RemoteException, SQLException {
		//Vê o número max de eleicoes que existe
		PostgreSQLJDBC db = new PostgreSQLJDBC();
		db.connectDB();
		int max = db.maxEleicoes();

		return max;
	}

	@Override
	public int[] numEleicoesNaoComecadas() throws RemoteException, SQLException {
		//Vê o número max de eleicoes que existe
		PostgreSQLJDBC db = new PostgreSQLJDBC();
		db.connectDB();
		int[] idEleicao = db.numEleicoesNaoComecadas();
		return idEleicao;
	}


	@Override
	public String[] ListaCandidaturas(int opcaoEleicao) throws SQLException, RemoteException {
		PostgreSQLJDBC db = new PostgreSQLJDBC();
		db.connectDB();
		//String listaCandidatosArray = "";
		String[] returnCantidatos = new String[10];

		ResultSet rs = db.listaCandidaturas(opcaoEleicao); //Retorna a lista de candidaturas
		int id, i = 0;
		String nomeCandidato, categoria, numEleicao, titulo;
		boolean val = rs.next();
		System.out.println("VAL: " + val);
		if (val == false) return null;
		else {
			while (val) {
				String listaCandidatosArray = "";
				id = rs.getInt(1);
				nomeCandidato = rs.getString("nomecandidato");
				categoria = rs.getString("categoria");
				numEleicao = rs.getString("eleicao_id");
				titulo = rs.getString("titulo");

				//[1=Partido Chega][2=PS]
				listaCandidatosArray += id;
				listaCandidatosArray += "=";
				listaCandidatosArray += nomeCandidato;
				returnCantidatos[i] = listaCandidatosArray;
				i++;
				if (!(nomeCandidato.equals("Branco") || nomeCandidato.equals("Nulo"))) {
					client.displayCandidatura(String.valueOf(id), nomeCandidato, categoria, numEleicao, titulo);
				}
				System.out.println("NOME: " + nomeCandidato + "I: " + i);
				val = rs.next();
			}

			if(i == 2) return null; //significa que só tem os candidatos Branco e Nulo

			return returnCantidatos;
		}
	}

	@Override
	public String[] ListaPessoasParaCandidatura() throws SQLException, RemoteException {
		PostgreSQLJDBC db = new PostgreSQLJDBC();
		db.connectDB();
		String[] numCC_pessoas = new String[10];
		int i = 0;
		ResultSet rs = db.listaPessoasParaCandidatura(); //Retorna a lista de pessoas que podem ser adicionadas a uma certa candidatura

		String nomeCandidato, num_cc;

		while (rs.next()) {
			num_cc = rs.getString(1);
			nomeCandidato = rs.getString("nome");
			//numCC_pessoas += num_cc;
			numCC_pessoas[i] = num_cc;
			i++;

			//client.displayListaPessoasParaCandidatura(num_cc, nomeCandidato);
		}
		return numCC_pessoas;
	}


	@Override
	public void AdicionaPessoaCandidatura(int opcaoEleicao, String num_cc, String partido, String idPartido) throws SQLException, RemoteException {
		//Inserir na tabela das lista pessoas candidatos a pessoa recebida
		PostgreSQLJDBC db = new PostgreSQLJDBC();
		db.connectDB();
		db.InsertPessoasCandidatura(opcaoEleicao, num_cc, partido, Integer.parseInt(idPartido));
	}

	@Override
	public String ListaElementosCandidatura(int opcaoEleicao, String candidatura, String idPartido) throws RemoteException, SQLException {
		//Listar todas as pessoas de uma determinada candidatura
		PostgreSQLJDBC db = new PostgreSQLJDBC();
		db.connectDB();
		ResultSet rs = db.ListaElementosCandidatura(opcaoEleicao, candidatura, Integer.parseInt(idPartido));

		String dadosElementosCandidatura = "";

		String num_cc, nome, nomeCandidato;
		while (rs.next()) {
			num_cc = rs.getString(1);
			nome = rs.getString(2);
			nomeCandidato = rs.getString(3);

			dadosElementosCandidatura += num_cc;
			dadosElementosCandidatura += " ";

			//client.displayListaElementosCandidatura(num_cc, nome, nomeCandidato);
		}
		return dadosElementosCandidatura;
	}

	@Override
	public void RemovePessoaCandidatura(String num_cc) throws RemoteException, SQLException {
		//Remove na tabela das lista dos candidatos a pessoa recebida
		PostgreSQLJDBC db = new PostgreSQLJDBC();
		db.connectDB();
		db.RemovePessoaCandidatura(num_cc);
		//client.print_on_client("Pessoa removida com sucesso");
	}

	@Override
	public void ListaTudoEleicao(int opcaoEleicao) throws RemoteException, SQLException {
		//Listar todas as Candidaturas de uma determinada eleicao
		PostgreSQLJDBC db = new PostgreSQLJDBC();
		db.connectDB();
		ResultSet rs = db.listaTudoEleicao(opcaoEleicao);

		String num_cc, nome, nomeCandidato;
		while (rs.next()) {
			num_cc = rs.getString(1);
			nome = rs.getString(2);
			nomeCandidato = rs.getString(4);

			client.displayListaTudoEleicao(num_cc, nome, nomeCandidato);
		}
	}

	@Override
	public String getDetalhesEleicao(int opcaoEleicao) throws RemoteException, SQLException {
		//Obter propriedades de uma eleicao para as poder alterar
		PostgreSQLJDBC db = new PostgreSQLJDBC();
		db.connectDB();
		String detalhes = "";

		ResultSet rs = db.getDetalhesEleicao(opcaoEleicao);

		String titulo, descricao;
		Timestamp data_inicio, data_fim;
		while (rs.next()) {
			titulo = rs.getString("titulo");
			descricao = rs.getString("descricao");
			data_inicio = rs.getTimestamp("data_inicio");
			data_fim = rs.getTimestamp("data_fim");

			detalhes = titulo + "#" + descricao + "#" + data_inicio + "#" + data_fim;
			client.displayDetalhesEleicao(titulo, descricao, data_inicio, data_fim);
		}
		return detalhes;
	}

	@Override
	public void UpdatePropriedadesEleicao(int opcaoEleicao, String tituloAlteracao, String descricaoAlteracao, Timestamp data_inicio, Timestamp data_fim) throws RemoteException, SQLException {
		//DAr update às propriedades de uma eleicao
		PostgreSQLJDBC db = new PostgreSQLJDBC();
		db.connectDB();
		db.UpdatePropriedadesEleicao(opcaoEleicao, tituloAlteracao, descricaoAlteracao, data_inicio, data_fim);
		//client.print_on_client("Update com sucesso\n");
	}

	@Override
	public Pessoa findPessoa(String num_cc) throws RemoteException, SQLException {
		//Procurar a pessoa na DB
		PostgreSQLJDBC db = new PostgreSQLJDBC();
		db.connectDB();
		ResultSet rs = db.findPessoa(num_cc);

		boolean val = rs.next();
		String[] atributosPessoa = new String[8];
		Pessoa p = null;

		if (val == false) return null;
		else {
			while (val) {
				atributosPessoa[0] = rs.getString("num_cc");
				atributosPessoa[1] = rs.getString("nome");
				atributosPessoa[2] = rs.getString("password");
				atributosPessoa[3] = rs.getString("funcao");
				atributosPessoa[4] = rs.getString("departamento");
				atributosPessoa[5] = String.valueOf(rs.getInt("num_telefone"));
				atributosPessoa[6] = rs.getString("morada");
				atributosPessoa[7] = String.valueOf(rs.getTimestamp("data_validade"));
				val = rs.next();

				p = new Pessoa(atributosPessoa[1], atributosPessoa[2], atributosPessoa[3], atributosPessoa[4], Integer.parseInt(atributosPessoa[5]), atributosPessoa[6], atributosPessoa[0], atributosPessoa[7]);

			}
		}

		/*for (int i = 0; i < atributosPessoa.length; i++){
			System.out.println("-> " + atributosPessoa[i]);
		}*/

		return p;
	}

	public CopyOnWriteArrayList<Eleicao> getEleicao(String departamento) throws RemoteException, SQLException {
		//Procurar a eleicao na DB
		CopyOnWriteArrayList<Eleicao> listaEleicao = new CopyOnWriteArrayList<>();
		CopyOnWriteArrayList<String> listaDep = new CopyOnWriteArrayList<>();
		listaDep.add(departamento);

		PostgreSQLJDBC db = new PostgreSQLJDBC();
		db.connectDB();
		ResultSet rs = db.getEleicao(departamento);
		boolean val = rs.next();
		String[] atributosEleicao = new String[7];

		if (val == false) return null;
		else {

			while (val) {
				atributosEleicao[0] = rs.getString("id");
				atributosEleicao[1] = rs.getString("data_inicio");
				atributosEleicao[2] = rs.getString("data_fim");
				atributosEleicao[3] = rs.getString("titulo");
				atributosEleicao[4] = rs.getString("descricao");
				atributosEleicao[5] = rs.getString("tipo");
				atributosEleicao[6] = String.valueOf(rs.getInt("resultado"));


				Eleicao e = new Eleicao(atributosEleicao[1], atributosEleicao[2], atributosEleicao[3], atributosEleicao[4], atributosEleicao[5], listaDep, Integer.parseInt(atributosEleicao[6]));
				listaEleicao.add(e);
				val = rs.next();
			}
		}

		for (int i = 0; i < listaEleicao.size(); i++) {
			System.out.println("-> " + listaEleicao.get(i).getTitulo());
		}

		return listaEleicao;
	}

	public CopyOnWriteArrayList<Candidato> getListaCandidatos(int eleicaoID) throws RemoteException, SQLException {
		//Retorna os candidatos de uma eleicao
		CopyOnWriteArrayList<Candidato> listaCandidatos = new CopyOnWriteArrayList<>();
		PostgreSQLJDBC db = new PostgreSQLJDBC();
		db.connectDB();

		System.out.println("Eleição Id: " + eleicaoID);
		ResultSet rs = db.getListaCandidatos(eleicaoID);
		boolean val = rs.next();
		String[] atributosListaCandidatos = new String[4];

		System.out.println("VAL: " + val);
		if (val == false) return null;
		else {
			while (val) {
				atributosListaCandidatos[0] = rs.getString("id");
				atributosListaCandidatos[1] = rs.getString("nomecandidato");
				atributosListaCandidatos[2] = rs.getString("categoria");
				atributosListaCandidatos[3] = rs.getString("eleicao_id");

				Candidato c = new Candidato(atributosListaCandidatos[1], atributosListaCandidatos[2], null);
				listaCandidatos.add(c);
				val = rs.next();
			}
		}

		for (int i = 0; i < listaCandidatos.size(); i++) {
			System.out.println("-> " + listaCandidatos.get(i).getNome());
		}

		return listaCandidatos;
	}

	public int getIdVoto() throws RemoteException, SQLException {
		//Vê o qual o ID de voto que é a PK da tabela de voto
		PostgreSQLJDBC db = new PostgreSQLJDBC();
		db.connectDB();
		int idVoto = db.getIdVoto();

		return idVoto;
	}

	public int getMaxCandidato() throws RemoteException, SQLException {
		//Vê o qual o nºo max de candidatos
		PostgreSQLJDBC db = new PostgreSQLJDBC();
		db.connectDB();
		int maxCandidato = db.getMaxCandidato();

		return maxCandidato;
	}


	public int getIdEleicao(String nomeEleicao) throws RemoteException, SQLException {
		//Vê o qual o ID da eleicao dado um nome
		PostgreSQLJDBC db = new PostgreSQLJDBC();
		db.connectDB();
		int idEleicao = db.getIdEleicao(nomeEleicao);

		return idEleicao;
	}

	@Override
	public void recebeLocalVoto(String local, String num_cc, String nomeEleicao) throws RemoteException, SQLException {
		int idEleicao = getIdEleicao(nomeEleicao);
		int idVoto = getIdVoto();
		PostgreSQLJDBC db = new PostgreSQLJDBC();
		db.connectDB();
		db.criaVoto(idVoto + 1, local, idEleicao, num_cc);
	}

	@Override
	public void updateVotoPessoaData(Timestamp dataVoto, String num_cc, String nomeEleicao) throws RemoteException, SQLException {
		int idEleicao = getIdEleicao(nomeEleicao);
		PostgreSQLJDBC db = new PostgreSQLJDBC();
		db.connectDB();
		db.updateVotoPessoaData(dataVoto, num_cc, idEleicao);
	}


	@Override
	public void recebeVoto(String Voto, String nomeEleicao) throws RemoteException, SQLException {
		//A string Voto corresponde ao Candidato neste caso o seu nome
		//Esta função vai dar update ao número de votos de um candidato
		int idEleicao = getIdEleicao(nomeEleicao);
		PostgreSQLJDBC db = new PostgreSQLJDBC();
		db.connectDB();
		db.recebeVoto(Voto, idEleicao);
	}


	@Override
	public Eleicao getEleicaoByID(int opcaoEleicao) throws RemoteException, SQLException {
		//Procurar a eleicao na DB
		PostgreSQLJDBC db = new PostgreSQLJDBC();
		db.connectDB();
		ResultSet rs = db.getEleicaoByID(opcaoEleicao);

		CopyOnWriteArrayList<String> listaDep = new CopyOnWriteArrayList<>();
		listaDep = db.getDepEleicao(opcaoEleicao);

		boolean val = rs.next();
		String[] atributosEleicao = new String[7];
		Eleicao e = null;

		if (val == false) return null;
		else {

			while (val) {
				atributosEleicao[0] = rs.getString("id");
				atributosEleicao[1] = rs.getString("data_inicio");
				atributosEleicao[2] = rs.getString("data_fim");
				atributosEleicao[3] = rs.getString("titulo");
				atributosEleicao[4] = rs.getString("descricao");
				atributosEleicao[5] = rs.getString("tipo");
				atributosEleicao[6] = String.valueOf(rs.getInt("resultado"));


				e = new Eleicao(atributosEleicao[1], atributosEleicao[2], atributosEleicao[3], atributosEleicao[4], atributosEleicao[5], listaDep, Integer.parseInt(atributosEleicao[6]));
				val = rs.next();
			}
		}

		return e;
	}

	@Override
	public void criaNovoCandidato(Candidato c, int opcaoEleicao) throws RemoteException, SQLException {
		//Inserir na tabela dos candidatos o novo candidato
		PostgreSQLJDBC db = new PostgreSQLJDBC();
		db.connectDB();
		db.criaNovoCandidato(c, opcaoEleicao);
		System.out.println("Candidato criado com sucesso");
	}

	@Override
	public boolean getlocalVotoEleitores() throws RemoteException, SQLException {
		//Listar todos os votos de cada eleitor de uma certa eleicao
		PostgreSQLJDBC db = new PostgreSQLJDBC();
		db.connectDB();
		ResultSet rs = db.getListaVotos();

		String num_cc, nomeEleicao, local_voto, hora_voto, nomePessoa;
		boolean val = rs.next();
		if (val == false) return false;
		else{
			while (val) {
				local_voto = rs.getString(1);
				hora_voto = String.valueOf(rs.getTimestamp(2));
				num_cc = rs.getString(3);
				nomePessoa = rs.getString(4);
				nomeEleicao = rs.getString(5);

				client.displaylocalVotoEleitores(local_voto, hora_voto, nomePessoa, num_cc, nomeEleicao);
				val = rs.next();
			}

		}
		return true;
	}

	@Override
	public CopyOnWriteArrayList<Voto> getListaVotos() throws RemoteException, SQLException {
		//Retorna a tabela de votos
		CopyOnWriteArrayList<Voto> listaVotos = new CopyOnWriteArrayList<>();
		PostgreSQLJDBC db = new PostgreSQLJDBC();
		db.connectDB();

		ResultSet rs = db.getListaVotos();
		boolean val = rs.next();

		String[] atributosTabelaVotos = new String[5];

		System.out.println("VAL: " + val);
		if (val == false) return null;
		else {
			while (val) {
				atributosTabelaVotos[0] = rs.getString("id_voto");
				atributosTabelaVotos[1] = rs.getString("local_voto");
				atributosTabelaVotos[2] = rs.getString("hora_voto");
				atributosTabelaVotos[3] = rs.getString("eleicao_id");
				atributosTabelaVotos[4] = rs.getString("pessoa_num_cc");


				if (atributosTabelaVotos[2] == null) {
					Voto v = new Voto(atributosTabelaVotos[3], atributosTabelaVotos[4], atributosTabelaVotos[1], null);
					listaVotos.add(v);


				} else {
					Voto v = new Voto(atributosTabelaVotos[3], atributosTabelaVotos[4], atributosTabelaVotos[1], Timestamp.valueOf(atributosTabelaVotos[2]));
					listaVotos.add(v);
				}
				val = rs.next();
			}
		}

		for (int i = 0; i < listaVotos.size(); i++) {
			System.out.println("-> " + listaVotos.get(i).getNum_cc());
		}

		return listaVotos;
	}

	@Override
	public CopyOnWriteArrayList<Object> getListaVotosSpring() throws RemoteException, SQLException {
		//Retorna a tabela de votos
		CopyOnWriteArrayList<Object> listaVotos = new CopyOnWriteArrayList<>();
		PostgreSQLJDBC db = new PostgreSQLJDBC();
		db.connectDB();

		ResultSet rs = db.getListaVotos();
		boolean val = rs.next();

		String[] atributosTabelaVotos = new String[5];

		System.out.println("VAL votos: " + val);
		if (val == false) return null;
		else {
			while (val) {
				System.out.println("entrei");
				System.out.println("atributos");
				atributosTabelaVotos[0] = rs.getString("local_voto");
				atributosTabelaVotos[1] = rs.getString("hora_voto");
				atributosTabelaVotos[2] = rs.getString("pessoa_num_cc");
				atributosTabelaVotos[3] = rs.getString("eleicao_id");

				if (atributosTabelaVotos[1] == null) {
					Voto v = new Voto(atributosTabelaVotos[3], atributosTabelaVotos[2], atributosTabelaVotos[0], null);
					listaVotos.add(v);

				} else {
					Voto v = new Voto(atributosTabelaVotos[3], atributosTabelaVotos[2], atributosTabelaVotos[0], Timestamp.valueOf(atributosTabelaVotos[1]));
					listaVotos.add(v);
					System.out.println("entrei else");
				}
				val = rs.next();
				System.out.println("fim");
			}
		}
		Voto v = (Voto) listaVotos.get(0);
		System.out.println("aaa" + v.getNum_cc());
		return listaVotos;
	}

	@Override
	public void consultaEleicoesPassadas(int eleicaoID) throws RemoteException, SQLException {
		//Listar uma eleicao passada com o seu nº de votos
		PostgreSQLJDBC db = new PostgreSQLJDBC();
		db.connectDB();
		ResultSet rs = db.consultaEleicoesPassadas(eleicaoID);


		Eleicao e = getEleicaoByID(eleicaoID);
		String nomeCandidato, numVotos;
		int totalVotos = 0;
		int votoBranco = 0;
		int votoNulo = 0;

		while (rs.next()) {
			nomeCandidato = rs.getString(2);
			numVotos = rs.getString(5);
			totalVotos += Integer.parseInt(numVotos);
			if (nomeCandidato.equals("Branco")) votoBranco = Integer.parseInt(numVotos);
			if (nomeCandidato.equals("Nulo")) votoNulo = Integer.parseInt(numVotos);
		}

		client.displayEleicoesPassadas1(e, totalVotos, votoBranco, votoNulo, 1, null, 0, 0);


		rs = db.consultaEleicoesPassadas(eleicaoID);

		int votosVálidos = totalVotos - votoBranco - votoNulo;
		while (rs.next()) {
			nomeCandidato = rs.getString(2);
			numVotos = rs.getString(5);
			int votos = Integer.parseInt(numVotos);
			float percentagem = (float) (votos * 100.0 / votosVálidos);
			if (!(nomeCandidato.equals("Branco") || nomeCandidato.equals("Nulo"))) {
				//System.out.println("----------------------");
				//System.out.println("Nome do candidato: " + nomeCandidato);
				//System.out.println("Votos: " + numVotos + "\t(" + percentagem + "%)");
				client.displayEleicoesPassadas1(e, totalVotos, votoBranco, votoNulo, 2, nomeCandidato, votos, percentagem);

			}
		}
		//client.displayEleicoesPassadas(totalVotos, votoBranco, votoNulo, rs, e);
	}

	@Override
	public CopyOnWriteArrayList<String> gereMesadeVoto(int eleicaoID) throws RemoteException, SQLException {
		//Listar todas as Candidaturas de uma determinada eleicao
		PostgreSQLJDBC db = new PostgreSQLJDBC();
		db.connectDB();
		ResultSet rs = db.gereMesadeVoto(eleicaoID);
		CopyOnWriteArrayList<String> listaDep = new CopyOnWriteArrayList<>();
		int i = 0;
		String id, titulo = "", departamento;
		while (rs.next()) {
			id = rs.getString(1);
			titulo = rs.getString(2);
			departamento = rs.getString(3);
			listaDep.add(departamento);
			client.displaygereMesadeVoto(titulo, departamento);
		}
		return listaDep;
	}

	@Override
	public void updateListaDep(int opcaoEleicao, CopyOnWriteArrayList<String> listaDept) throws RemoteException, SQLException {
		PostgreSQLJDBC db = new PostgreSQLJDBC();
		db.connectDB();
		db.updateListaDep(opcaoEleicao, listaDept);
	}

	@Override
	public void getEleitoresTempoReal(int idEleicao) throws RemoteException, SQLException {
		//Listar o nº de votos de uma eleicao dos vários dep
		PostgreSQLJDBC db = new PostgreSQLJDBC();
		db.connectDB();
		ResultSet rs = db.getEleitoresTempoReal(idEleicao);

		String local_voto;
		int numVotos;


		while (rs.next()) {
			numVotos = rs.getInt(1);
			local_voto = rs.getString(2);

			client.displayEleitoresTempoReal(numVotos, local_voto);
		}
	}

	@Override
	public void saveDep(String name) throws RemoteException {

		//System.out.println("\t\t\tDEPARTAMENTO MULTICAST: " + name);
		//client.print_on_client("\n\t\t\t\t\t> " + name + " em funcionamento");
	}

	@Override
	public void print_on_server(String s) throws RemoteException {
		System.out.println("> " + s);
	}





	// =======================================================

	public static void main(String args[]) {
		DatagramSocket socket = null;

		while (true) {
			try {
				RMI_Server h = new RMI_Server();
				Registry r = LocateRegistry.createRegistry(MAIN_PORT);
				r.rebind("RMIConnect", h);

				PostgreSQLJDBC db = new PostgreSQLJDBC();
				db.connectDB();

				System.out.println("Hello Server ready.");
				break;

			} catch (ExportException e) {
				String s;

				try{
					socket = new DatagramSocket(6789);
					socket.setSoTimeout(9000);
					System.out.println("Socket Datagram à escuta no porto 6789");

					while(true){
						byte[] buffer = new byte[256];
						DatagramPacket heartBeat = new DatagramPacket(buffer, buffer.length);
						socket.receive(heartBeat);
						s = new String(heartBeat.getData(), 0, heartBeat.getLength());
						System.out.println("Servidor secundário recebeu: " + s);

						DatagramPacket reply = new DatagramPacket(heartBeat.getData(), heartBeat.getLength(), heartBeat.getAddress(), heartBeat.getPort());
						socket.send(reply);
					}

				} catch (SocketTimeoutException e1) {
					System.out.println("Servidor primário crashou");
				} catch (IOException e1) {
					e1.printStackTrace();
				} finally {
					socket.close();
				}

			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (SQLClientInfoException e) {
				e.printStackTrace();
			}
		}

		try {
			socket = new DatagramSocket();
			socket.setSoTimeout(13000);
			String heartBeat = "Servidor primário está vivo!";
			byte[] m = heartBeat.getBytes();

			while (true) {
				Thread.sleep((13000)); //A cada 3s manda uma mensagem ao  de uma resposta
				System.out.println(heartBeat);

				InetAddress host = InetAddress.getByName("localhost");
				int serverPort = 6789;
				DatagramPacket request = new DatagramPacket(m, m.length, host, serverPort);
				socket.send(request);

				byte[] buffer = new byte[256];
				DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
				try {
					socket.receive(reply);
					System.out.println("Recebeu: " + new String(reply.getData(), 0, reply.getLength()));
				} catch (SocketTimeoutException s) {
					//System.out.println("Não recebeu resposta...");
				}
			}

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		} finally {
			socket.close();
		}

	}
}