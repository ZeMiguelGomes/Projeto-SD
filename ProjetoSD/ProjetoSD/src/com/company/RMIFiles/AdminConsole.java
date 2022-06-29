package com.company.RMIFiles;

import com.company.Candidato;
import com.company.Eleicao;
import com.company.MulticastServer;
import com.company.Pessoa;

import java.rmi.ConnectException;
import java.rmi.ConnectIOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class AdminConsole extends UnicastRemoteObject implements AdminConsoleInterface{

	AdminConsole() throws RemoteException {
		super();
	}

	public RMInterface changeRMI() {
		try {
			RMInterface h = (RMInterface) LocateRegistry.getRegistry(7000).lookup("RMIConnect");
			h.print_on_server("Olá da admin console");

			System.out.println("Liguei-me ao secundário");
			return h;
		} catch (RemoteException | NotBoundException e) {
			return null;
		}
	}

	public void print_on_client(String s) throws RemoteException {
		System.out.println(s);
	}


	public static void displayFuncionalidade(){
		System.out.println("=========================");
		System.out.println("OPERAÇÕES A REALIZAR:");
		System.out.println("[1] -> Registar Pessoas");
		System.out.println("[2] -> Criar Eleição");
		System.out.println("[3] -> Gerir listas de candidatos a uma eleição");
		System.out.println("[4] -> Gerir mesas de voto");
		System.out.println("[5] -> Alterar propriedades de uma eleição");
		System.out.println("[6] -> Saber em que local votou cada eleitors");
		System.out.println("[7] -> Mostrar estado das mesas de voto");
		System.out.println("[8] -> Mostrar eleitores em tempo real");
		System.out.println("[9] -> Consultar resultados detalhados de eleições passadas");
		System.out.println("=========================");
	}

	public static void displayOpcoesEleicoes(){
		System.out.println("==-==-==-==-==-==-==-==-==-==-==-==-==-");
		System.out.println("OPERAÇÕES A REALIZAR:");
		System.out.println("[1] -> Listar Candidaturas");
		System.out.println("[2] -> Listar pessoas que possam ser adicionadas a uma candidatura");
		System.out.println("[3] -> Adicionar pessoa a uma candidatura");
		System.out.println("[4] -> Remover pessoa de uma candidatura");
		System.out.println("[5] -> Listar todas as candidaturas e seus elementos");
		System.out.println("[6] -> Criar candidatura");
		System.out.println("[7] -> SAIR");
		System.out.println("==-==-==-==-==-==-==-==-==-==-==-==-==-");
	}

	public static Pessoa registaPessoa() {
		String nome;
		String password;
		String funcao;
		String departamento;
		int num_telefone;
		String morada;
		String num_cc;
		String data_validade_cc;

		Scanner input;

		//nome
			while (true){
				System.out.printf("Nome: ");
				input = new Scanner(System.in);
				nome = input.nextLine();
				try {

					if (String.valueOf(nome).length() != 0 && !nome.isBlank()) {
						break;
					} else {
						System.out.println("Insira o seu nome");
					}

				}catch (InputMismatchException e){
					System.out.println("Insira o seu nome");
				}

			}

			//password
			while (true){
				System.out.printf("Password: ");
				input = new Scanner(System.in);
				password = input.nextLine();

				try {
					if (String.valueOf(password).length() != 0 && !password.isBlank()) {
						break;
					} else {
						System.out.println("Insira a sua password");
					}

				}catch (InputMismatchException e){
					System.out.println("Insira a sua password");
				}
			}

			//funcao
			while (true){
				try {
					System.out.printf("Funcao: ");
					//Estudante, Docente, Func
					input = new Scanner(System.in);
					funcao = input.nextLine();

					//if(!funcao.matches(".*\\d.*") && funcao.matches("[a-zA-Z]+")){ //O input não tem numeros
					if(funcao.equalsIgnoreCase("estudante")
							|| funcao.equalsIgnoreCase("docente")
							|| funcao.equalsIgnoreCase("funcionario")){
						break;
					}else{
						System.out.println("A categoria função não pode conter números");
					}

				}catch (NumberFormatException ex){
					System.out.println("A categoria função não pode conter números");
				}catch (InputMismatchException e){
					System.out.println("A categoria função não pode conter números");
				}
			}

			//departamento
			while (true){

				try {
					System.out.printf("Departamento: ");
					input = new Scanner(System.in);
					departamento = input.nextLine();

					if(!departamento.matches(".*\\d.*") && departamento.matches("[a-zA-Z]+")){ //O input não tem numeros
						break;
					}else{
						System.out.println("A categoria Departamento não pode conter números");
					}

				}catch (NumberFormatException ex){
					System.out.println("A categoria Departamento não pode conter números");
				}catch (InputMismatchException e){
					System.out.println("A categoria Departamento não pode conter números");
				}

			}

			//num telefone
			while(true){
				try{
					System.out.printf("Número de Telefone: ");
					input = new Scanner(System.in);
					num_telefone = Integer.parseInt(input.nextLine());
					if (String.valueOf(num_telefone).length() != 9){
						System.out.printf("Insira um número de telefone com 9 digitos: ");
						input = new Scanner(System.in);
						num_telefone = Integer.parseInt(input.nextLine());
					}
					break;
				}catch (NumberFormatException ex){
					System.out.println("Insira um número válido");
				}

			}

			//morada
			while (true){
				try {
					System.out.printf("Morada: ");
					input = new Scanner(System.in);
					morada = input.nextLine();

					if (String.valueOf(morada).length() != 0 && !morada.isBlank()) {
						break;
					} else {
						System.out.println("Insira a sua morada");
					}

				}catch (InputMismatchException e){
					System.out.println("Insira a sua morada");
				}

			}

			//num CC
			while (true){
				try {
					System.out.printf("Número do CC: ");
					input = new Scanner(System.in);
					num_cc = input.nextLine();
					int num_cc_error = Integer.parseInt(num_cc);
					if (String.valueOf(num_cc_error).length() != 8){
						System.out.printf("Insira um número de CC com 8 digitos: ");
						input = new Scanner(System.in);
						num_cc = input.nextLine();
					}
					break;
				}catch (NumberFormatException ex){
					System.out.println("Insira um número de CC válido");
				}
			}

			//data validade
			while (true){
				System.out.printf("Data de Validade do CC (MM/yyyy): ");
				input = new Scanner(System.in);
				String dataInput = "01/" + input.nextLine();

				data_validade_cc = dataInput;
				break;

			}

		Pessoa p = new Pessoa(nome, password, funcao, departamento, num_telefone, morada, num_cc, data_validade_cc);

		return p;
	}


	public static Eleicao criaEleicao(){

		String data_inicio;
		String data_fim;
		String titulo;
		String descricao;
		String tipoEleicao;
		String departamento;
		CopyOnWriteArrayList<String> listaDep = new CopyOnWriteArrayList<>();

		Scanner input;


		//data inicio
		while (true){
			try{
				System.out.printf("Data de Início da Eleicao (dd-MM-yyyy HH:mm):");
				input = new Scanner(System.in);
				String dataInput = input.nextLine();
				Date dataError = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(dataInput);
				data_inicio = dataInput;
				break;
			}catch (ParseException e) {
				System.out.println("Insira uma data válida");
			}
		}
		//data fim
		while (true){
			try{
				System.out.printf("Data de Fim da Eleicao (dd-MM-yyyy HH:mm):");
				input = new Scanner(System.in);
				String dataInput = input.nextLine();
				Date dataError = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(dataInput);
				data_fim = dataInput;
				break;
			}catch (ParseException e) {
				System.out.println("Insira uma data válida");
			}
		}

		//titulo eleicao
		while (true){
			System.out.printf("Titulo: ");
			input = new Scanner(System.in);
			titulo = input.nextLine();
			try {

				if (String.valueOf(titulo).length() != 0 && !titulo.isBlank()) {
					break;
				} else {
					System.out.println("Insira um título para a Eleição");
				}

			}catch (InputMismatchException e){
				System.out.println("Insira um título para a Eleição");
			}
		}

		//descricao eleicao
		while (true){
			System.out.printf("Descrição: ");
			input = new Scanner(System.in);
			descricao = input.nextLine();
			try {

				if (String.valueOf(descricao).length() != 0 && !descricao.isBlank()) {
					break;
				} else {
					System.out.println("Insira uma descrição para a Eleição");
				}

			}catch (InputMismatchException e){
				System.out.println("Insira uma descrição para a Eleição");
			}
		}

		//tipo eleicao
		while (true){
			System.out.printf("Tipo Eleição: ");
			input = new Scanner(System.in);
			tipoEleicao = input.nextLine();
			try {

				if (String.valueOf(tipoEleicao).length() != 0 && !tipoEleicao.isBlank() &&
						(tipoEleicao.equalsIgnoreCase("estudante")
						|| tipoEleicao.equalsIgnoreCase("docente")
						|| tipoEleicao.equalsIgnoreCase("funcionario"))) {
					break;
				} else {
					System.out.println("Insira um tipo de Eleição (Estudante/Docente/Funcionario)");
				}

			}catch (InputMismatchException e){
				System.out.println("Insira um tipo de Eleição (Estudante/Docente/Funcionario)");
			}
		}

		//local eleicao
		while (true){
			System.out.printf("Departamento: ");
			input = new Scanner(System.in);
			departamento = input.nextLine();
			try {
				if (String.valueOf(departamento).length() != 0 && !departamento.isBlank()) {
					listaDep.add(departamento);
					while (true){
						System.out.printf("Deseja Inserir mais algum departamento? (S/N)");
						input = new Scanner(System.in);
						String opcaoDep = input.nextLine();
						if (opcaoDep.equalsIgnoreCase("S")){
							System.out.printf("Departamento: ");
							input = new Scanner(System.in);
							departamento = input.nextLine();
							if (String.valueOf(departamento).length() != 0 && !departamento.isBlank()) {
								listaDep.add(departamento);
							}
						}else if (opcaoDep.equalsIgnoreCase("N")){
							break;
						}else System.out.println("Insira uma resposta válida");
					}
					break;
				} else {
					System.out.println("Insira um departamento para a realiação da Eleição");
				}

			}catch (InputMismatchException e){
				System.out.println("Insira um departamento para a realiação da Eleição");
			}
		}


		Eleicao e = new Eleicao(data_inicio, data_fim, titulo, descricao, tipoEleicao, listaDep, 0);

		return e;
		}

	@Override
	public void displayEleicoes(String id, String titulo, String tipo, String departamento, String data_inicio) throws RemoteException {
		System.out.println("ID ->" + id);
		System.out.println("Título -> " + titulo);
		System.out.println("Tipo -> " + tipo);
		System.out.println("Departamento -> " + departamento);
		System.out.println("Data de Início -> " + data_inicio);
		System.out.println(" - - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
	}

	@Override
	public void displayCandidatura(String id, String nomeCandidato, String categoria, String numEleicao, String titulo) throws RemoteException{
		System.out.println(" -_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-");
		System.out.println("\t\t\t\tDETALHES DA ELEIÇÃO");
		System.out.println("Número de Eleição -> " + numEleicao);
		System.out.println("Nome da Eleicao -> " + titulo);
		System.out.println("ID -> " + id); //ID do candidato
		System.out.println("Nome Candidato -> " + nomeCandidato);
		System.out.println("Categoria -> " + categoria);
		System.out.println(" -_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-");
	}

	@Override
	public void displayListaPessoasParaCandidatura(String num_cc, String nomeCandidato) throws RemoteException {
		System.out.println("NUM_CC -> " + num_cc);
		System.out.println("Nome -> " + nomeCandidato);
		System.out.println(" - - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
	}

	@Override
	public void displayListaElementosCandidatura(String num_cc, String nome, String nomeCandidato) throws RemoteException {
		System.out.println("########################");
		//System.out.println("Nome da Lista: " + nomeCandidato);
		System.out.println("Elementos: ");
		System.out.println("Nome: " + nome + "\tNum CC: " + num_cc);
	}

	@Override
	public void displayListaTudoEleicao(String num_cc, String nome, String nomeCandidato) throws RemoteException {
		System.out.println("-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/");
		System.out.println("Nome do Candidato: " + nomeCandidato);
		System.out.println("Num CC: " + num_cc);
		System.out.println("Nome: " + nome);
	}

	private static boolean checkopcaoEleicao(int opcaoEleicao, int[] listaEleicoes){
		int check = 0;
		for (int i = 0; i < listaEleicoes.length; i++){
			if(listaEleicoes[i] == opcaoEleicao){
				System.out.println("É igual");
				check++;
				break;
			}
		}
		if(check != 0){
			return true;
		}
		return false;
	}


	public static void gereCandidato(RMInterface h) throws RemoteException, SQLException {
		//TODO
		//Listar as eleições a decorrer - DONE
		//1-> Buscar a lista de candidaturas para uma determinada eleição. - DONE
		//2-> Para o tipo de eleicao mostrar a todas as pessoas que estão na tabela das pessoas - DONE
		//3-> Adicionar uma certa pessoa a uma determinada candidatura -DONE
		//4-> Remover uma pessoa de uma certa lista se ela lá estiver -DONE
		if(h.ListaEleicoesNaoComecadas() == false){
			System.out.println("Não existem eleições disponíveis para alterar a lista de candidatos");
		}
		else{
			int opcaoEleicao, flagOut = 0;
			int[] listaEleicaoNaoComecada = h.numEleicoesNaoComecadas();
			int opcaoEleicaoGlobal;

			Scanner input;

			//Eleição a ver
			while (flagOut != 1){
				try{
					System.out.printf("Indique a eleição a visualizar: ");
					input = new Scanner(System.in);
					String eleicao = input.nextLine();
					opcaoEleicao = Integer.parseInt(eleicao);
					opcaoEleicaoGlobal = opcaoEleicao;
					if(!(opcaoEleicao < 1 || checkopcaoEleicao(opcaoEleicao, listaEleicaoNaoComecada) == false)) { //Ele selecionou uma eleicao valida

						//TODO
						//Fazer um menu para as seguintes opções:
						//1-> Listar Candidaturas dessa eleicao - DONE
						//2-> Listar pessoas aptas para serem adicionadas a essa candidatura - DONE
						//3-> Adicionar pessoas a uma candidatura - DONE
						//4-> Remover pessoas de uma candidatura - DONE

						String[] listaNomeCandidatos = new String[10];  //Nome dos partidos dos candidatos
						String[] numCCListaCandidatos = new String[10];
						String idPartido = "";

						while (flagOut != 1){
							displayOpcoesEleicoes();
							String opcao = "";

							try{
								System.out.printf("OPÇÃO: ");
								Scanner inputOpcao = new Scanner(System.in);
								opcao = input.nextLine();
								Integer.parseInt(opcao);
								if (Integer.parseInt(opcao) <= 7 && Integer.parseInt(opcao) > 0){
									switch(opcao) {
										case "1":
											// Listar Candidaturas dessa eleicao

											System.out.println("\n");
											if((listaNomeCandidatos = h.ListaCandidaturas(opcaoEleicao)) == null){
												System.out.println("Não existem candidaturas para a eleição escolhida");
											}
											break;
										case "2":
											// Listar pessoas aptas para serem adicionadas a essa candidatura

											System.out.println("\t\t\tADICIONAR PESSOAS A UMA CANDIDATURA ");
											numCCListaCandidatos = h.ListaPessoasParaCandidatura();

											break;
										case "3":
											// Adicionar pessoas a uma candidatura -

											String num_cc = "";
											while (true){
												try {
													//Pedir o CC
													System.out.printf("Insira o número de cc da pessoa que deseja inserir: ");
													Scanner num_Cc = new Scanner(System.in);
													num_cc = num_Cc.nextLine();
													int num_cc_error = Integer.parseInt(num_cc);
													int valido = 0;
													if (String.valueOf(num_cc_error).length() != 8){ //Verifica se tem 8 digitos
														num_Cc = new Scanner(System.in);
														num_cc = num_Cc.nextLine();
													}
													//Verificar se é um cc válido
													for (int i = 0; i < numCCListaCandidatos.length; i++){ //Verifica se pode ser adicionado
														//System.out.println("\n\n\n" + numCCListaCandidatos.split("-")[i] + "\n\n\n");
														if (num_cc.equals(numCCListaCandidatos[i])){
															valido++;
														}
													}
													if (valido > 0){
														break;
													}else{
														System.out.printf("Insira o número de cc da pessoa ao qual se quer inserir: ");
														num_Cc = new Scanner(System.in);
														num_cc = num_Cc.nextLine();
													}
													break;
												}catch (NumberFormatException ex){
													System.out.println("Insira um número de CC válido");
												}
											}

											String sPartido = "";
											while (true){
												try {
													//Pedir o Partido a que se quer juntar
													System.out.printf("Insira o nome da lista ao qual se quer inserir: ");
													Scanner partido = new Scanner(System.in);
													sPartido = partido.nextLine();
													int valido = 0;
													idPartido = "";

													//System.out.println("CANDIDATOS: " + listaNomeCandidatos[0]);
													//Verificar se é um partido válido atraves do parsing do output
													//[1=Partido Chega][2=PS]

													for (int j = 0; j < listaNomeCandidatos.length; j++){
														if(listaNomeCandidatos[j] != null){
															if (sPartido.equals(listaNomeCandidatos[j].split("=")[1])){
																valido++;
																idPartido = listaNomeCandidatos[j].split("=")[0];
															}
														}

													}
													if (valido > 0){
														break;
													}else{
														System.out.printf("Insira o nome da lista ao qual se quer inserir: ");

													}
												}catch (NumberFormatException ex){
													System.out.println("Insira um nome de CC válido");
												}
											}

											//Já tenho um num_cc, partido agora vamos adicionar à BD
											h.AdicionaPessoaCandidatura(opcaoEleicao, num_cc, sPartido, idPartido);
											break;

										case "4":
											// Remover pessoas de uma candidatura
											//1-> Perguntar de que candidatura é que quer remover - DONE
											//2-> Listar os elementos dessa candidatura - DONE
											//3-> Perguntar que elementos deseja remover - DONE

											String nomeLista = "";
											while (true){
												try {
													//Pedir a Candidatura
													System.out.printf("Insira o nome da lista: ");
													Scanner Candidatura = new Scanner(System.in);
													nomeLista = Candidatura.nextLine();
													int valido = 0;


													//Verificar se é um partido válido atraves do parsing do output
													for (int j = 0; j < listaNomeCandidatos.length; j++){
														if(listaNomeCandidatos[j] != null){
															if (nomeLista.equals(listaNomeCandidatos[j].split("=")[1])){
																valido++;
																idPartido = listaNomeCandidatos[j].split("=")[0];
															}
														}

													}
													if (valido > 0){
														break;
													}else{
														System.out.println("Insira o nome da lista que pretende aceder: ");
													}break;
												}catch (NumberFormatException ex){
													System.out.println("Insira um número de CC válido");
												}
											}

											//Listar os elementos dessa candidatura
											System.out.println("########################");
											System.out.println("Nome da Lista: " + nomeLista);
											String dadosElementosCandidatura = h.ListaElementosCandidatura(opcaoEleicao, nomeLista, idPartido);
											if (dadosElementosCandidatura.length() == 0){
												System.out.println("A lista não tem elementos");

											}else{
												while (true){
													try {
														//Pedir a o num CC a remover
														System.out.printf("Insira numero de CC a remover: ");
														Scanner numCC = new Scanner(System.in);
														num_cc = numCC.nextLine();
														int valido = 0;

														//Verificar se é um num_cc  válido atraves do parsing do output
														for (int i = 0; i < dadosElementosCandidatura.split(" ").length; i++){
															if (num_cc.equals(dadosElementosCandidatura.split(" ")[i])){
																valido++;
															}
														}
														if (valido > 0){ //O numero que inseriu foi o correto agora vamos removê-lo
															h.RemovePessoaCandidatura(num_cc);
															break;
														}else{
															System.out.println("Insira numero de CC a remover válido: ");
														}

													}catch (NumberFormatException ex){
														System.out.println("Insira um número de CC válido");
													}
												}
											}
											break;
										case "5":
											// Listar as Candidaturas e seus representantes
											h.ListaTudoEleicao(opcaoEleicao);
											break;
										case "6":
											// Criar Candidatura
											//TODO verificar a hora da eleicao
											int maxCandidato = h.getMaxCandidato();
											String nomenovoCandidato;
											String tipoEleicao;

											Scanner inputCandidatura;

											//nome Candidato
											while (true){
												System.out.printf("Nome: ");
												inputCandidatura = new Scanner(System.in);
												nomenovoCandidato = inputCandidatura.nextLine();
												try {

													if (String.valueOf(nomenovoCandidato).length() != 0 && !nomenovoCandidato.isBlank()) {
														break;
													} else {
														System.out.println("Insira o nome do candidato");
													}

												}catch (InputMismatchException e){
													System.out.println("Insira o nome do candidato");
												}

											}

											//tipo eleicao
											while (true){
												System.out.printf("Tipo Eleição: ");
												inputCandidatura = new Scanner(System.in);
												tipoEleicao = inputCandidatura.nextLine();
												try {

													if (String.valueOf(tipoEleicao).length() != 0 && !tipoEleicao.isBlank() &&
															(tipoEleicao.equalsIgnoreCase("estudante")
																	|| tipoEleicao.equalsIgnoreCase("docente")
																	|| tipoEleicao.equalsIgnoreCase("funcionario"))) {
														break;
													} else {
														System.out.println("Insira um tipo de Candidatura (Estudante/Docente/Funcionario)");
													}

												}catch (InputMismatchException e){
													System.out.println("Insira um tipo de Candidatura (Estudante/Docente/Funcionario)");
												}
											}

											//Eleicao a participar
											while (true){
												//try {
													//System.out.printf("Escolha uma eleição a participar: ");
													//Scanner eleicaoNovaCandidatura = new Scanner(System.in);
													//String novaCandidatura = eleicaoNovaCandidatura.nextLine();
													//opcaoEleicao = Integer.parseInt(novaCandidatura);
													//if(!(opcaoEleicao < 1 || checkopcaoEleicao(opcaoEleicao, listaEleicaoNaoComecada) == false)) { //Verificar se o tipo de eleição é igual ao tipo da candidatura
													//	System.out.println("Entrei");
														Eleicao e = h.getEleicaoByID(opcaoEleicaoGlobal);
														if (e.getTipoEleicao().equals(tipoEleicao)){	//Pode adicionar
															System.out.println("entrei na eleicao");
															Candidato c = new Candidato(nomenovoCandidato, tipoEleicao, null);
															h.criaNovoCandidato(c, opcaoEleicao);
															break;
														}else{
															System.out.println("Insira uma eleição do tipo " + tipoEleicao);
														}

													//}else{
														System.out.println("Insira uma Eleição válida.");
													//}
												//}catch (NumberFormatException ex){
												//	System.out.println("Insira uma Eleição válida");
												//}
											}
											break;

										case "7":
											// SAIR
											flagOut++;
											break;
									}
								}else{ //Opção não incluída nas possíveis
									System.out.println("Escolha uma opção válida");
								}

							} catch(NumberFormatException ex){ // O que foi lido no input não foi um número
								System.out.println("Insira um número válido");
							}
						}
					}else{
						System.out.println("Insira uma Eleição válida");
					}
				}catch (NumberFormatException ex){
					System.out.println("Insira uma Eleição válida");
				}
			}
		}




	}


	@Override
	public void displayDetalhesEleicao(String titulo, String descricao, Timestamp data_inicio, Timestamp data_fim) throws RemoteException {
		System.out.println("Eleição: " + titulo);
		System.out.println("Descrição: " + descricao);
		System.out.println("Data de Início: " + data_inicio);
		System.out.println("Data de Fim: " + data_fim);

	}

	@Override
	public void displaylocalVotoEleitores(String local_voto, String hora_voto, String nome, String num_cc, String titulo) throws RemoteException {
		System.out.println("== == == == == == == == == == ==");
		System.out.println("Eleição: " + titulo);
		System.out.println("Nome: " + nome);
		System.out.println("Número de CC: " + num_cc);
		System.out.println("Local de Voto: " + local_voto);
		System.out.println("Hora de Voto: " + hora_voto);
	}

	@Override
	public void displayEleicoesPassadas1(Eleicao e,int  totalVotos, int votoBranco, int votoNulo, int flag, String nomeCandidato, int numVotos, float percentagem) throws RemoteException {
		if (flag == 1){
			System.out.println("====================");
			System.out.println("Eleição: " + e.getTitulo());
			if(!(totalVotos == 0)){
				System.out.println("Número de votos: " + totalVotos);
				System.out.println("Votos em branco: " + votoBranco + "\t(" + (votoBranco/totalVotos)*100 + ")%");
				System.out.println("Votos nulo: " + votoNulo + "\t(" + (votoNulo/totalVotos)*100 + "%)");
			}else{
				System.out.println(">Eleição sem votos");
			}

		}
		else{
			if (totalVotos != 0){
				System.out.println("----------------------");
				System.out.println("Nome do candidato: " + nomeCandidato);
				System.out.println("Votos: " + numVotos + "\t(" + percentagem + "%)");
			}
		}


	}

	@Override
	public void displaygereMesadeVoto(String titulo, String departameto) throws RemoteException {
		System.out.println("------------------");
		System.out.println("Nome: " + titulo);
		System.out.println("Departamento: " + departameto);
	}

	@Override
	public void displayEleitoresTempoReal(int numVotos, String local_voto) throws RemoteException {
		System.out.println("-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-");
		System.out.println("Local de voto: " + local_voto);
		System.out.println("Número de votos: " + numVotos);
	}

	public static void alteraPropriedadesEleicao(RMInterface h) throws RemoteException, SQLException {

		if(h.ListaEleicoesNaoComecadas() == false){
			System.out.println("Não existem eleições disponíveis para alterar a lista de candidatos");
		}
		else{
			int maxEleicoes, opcaoEleicao,flagOut, idEleicao = 0;
			maxEleicoes = h.maxEleicoes();
			int exitBreak = 0;

			Scanner input;
			String tituloAlteracao = "", descricaoAlteracao = "";
			Timestamp Data_inicio = null, Data_fim = null;

			String currTitulo = "", currDescricao = "";
			Timestamp currData_inicio = null, currData_fim = null;
			String detalhes; //String com os detalhes iniciais da eleicao



			while (true) {
				flagOut = 0; // Ver se faz update
				try {
					System.out.println("==============================");
					System.out.printf("(999)>Indique a eleição a visualizar: ");
					input = new Scanner(System.in);
					String eleicao = input.nextLine();
					opcaoEleicao = Integer.parseInt(eleicao);
					if (eleicao.equalsIgnoreCase("999")){
						exitBreak++;
						break;
					}
					if (!(opcaoEleicao < 1 || opcaoEleicao > maxEleicoes)) {
						idEleicao = opcaoEleicao;
						detalhes = h.getDetalhesEleicao(idEleicao);

						currTitulo = detalhes.split("#")[0];
						currDescricao = detalhes.split("#")[1];


						//Passar a data de String para TimeStamp

						currData_inicio = Timestamp.valueOf(detalhes.split("#")[2]);

						currData_fim = Timestamp.valueOf(detalhes.split("#")[3]);

						//VERIFICAR SE PODE FAZER ALTERAÇÂO NA ELEIÇÂO
						//comparar a data atual com a data da eleicao
						Timestamp checkhoraAtual = new Timestamp(System.currentTimeMillis());

						int checkAlteracao = currData_inicio.compareTo(checkhoraAtual);

						if (checkAlteracao < 0){ // >0 significa que a hora atual é superior e não se pode alterar
							System.out.println("A eleição já começou e não se pode alterar o seu valor");
							flagOut++;

							break;
						}else{
							while (true){
								System.out.println("-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|");
								System.out.printf("(999)Indique o campo a alterar: ");
								Scanner campo = new Scanner(System.in);
								String atributo = campo.nextLine();
								if (atributo.equalsIgnoreCase("999")) break;
								if (atributo.equalsIgnoreCase("Título")){
									System.out.println("Insira o novo título");
									Scanner alteração = new Scanner(System.in);
									tituloAlteracao = alteração.nextLine();
								}
								else if (atributo.equalsIgnoreCase("Descrição")){
									System.out.println("Insira a nova descrição");
									Scanner alteração = new Scanner(System.in);
									descricaoAlteracao = alteração.nextLine();
								}
								else if (atributo.equalsIgnoreCase("Data de Início")){

									//comparar a data atual com a data da eleicao
									SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyy HH:mm");
									Timestamp horaAtual = new Timestamp(System.currentTimeMillis());

									checkAlteracao = currData_inicio.compareTo(horaAtual);

									if (checkAlteracao < 0){ // >0 significa que a hora atual é superior e não se pode alterar
										System.out.println("A eleição já começou e não se pode alterar o seu valor");

										break;
									}
									else{ // A hora atual é inferior à hora da eleição e podemos alterar o seu campo
										while (true){
											try{
												System.out.printf("Nova Data de Início da Eleicao (dd-MM-yyyy HH:mm):");
												input = new Scanner(System.in);
												String dataInput = input.nextLine();
												Date dataError = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(dataInput);

												//TODO -> Verificar se a data inserida é superior à data que estava na eleicao

												DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm");
												LocalDateTime localTime = LocalDateTime.from(format.parse(dataInput));
												Data_inicio = Timestamp.valueOf(localTime);


												break;
											}catch (ParseException e) {
												System.out.println("Insira uma data válida");
											}
										}

									}
								}
								else if (atributo.equalsIgnoreCase("Data de Fim")){

									//comparar a data atual com a data da eleicao
									SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyy HH:mm");
									Timestamp horaAtual = new Timestamp(System.currentTimeMillis());

									checkAlteracao = currData_inicio.compareTo(horaAtual);

									if (checkAlteracao < 0){ // >0 significa que a hora atual é superior e não se pode alterar
										System.out.println("A eleição já começou e não se pode alterar o seu valor");
										break;
									}
									else{ // A hora atual é inferior à hora da eleição e podemos alterar o seu campo
										while (true){
											try{
												System.out.printf("Nova Data de Fim da Eleicao (dd-MM-yyyy HH:mm):");
												input = new Scanner(System.in);
												String dataInput = input.nextLine();
												Date dataError = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(dataInput);

												//TODO -> Verificar se a data inserida é superior à data que estava na eleicao

												DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm");
												LocalDateTime localTime = LocalDateTime.from(format.parse(dataInput));
												Data_fim = Timestamp.valueOf(localTime);
												break;
											}catch (ParseException e) {
												System.out.println("Insira uma data válida");
											}
										}

									}
								}
								else{
									System.out.println("Insira um campo válido");
								}

							}

						}

					} else {
						System.out.println("Insira uma Eleição entre 1 e " + maxEleicoes);
					}
				} catch (NumberFormatException ex) {
					System.out.println("Insira uma Eleição entre 1 e " + maxEleicoes);
				}

			}
			if (Data_inicio == null) Data_inicio = currData_inicio;
			if (Data_fim == null) Data_fim = currData_fim;
			if (descricaoAlteracao == "") descricaoAlteracao = currDescricao;
			if (tituloAlteracao == "") tituloAlteracao = currTitulo;

			if (flagOut == 0 && exitBreak == 0) h.UpdatePropriedadesEleicao(idEleicao, tituloAlteracao, descricaoAlteracao, Data_inicio, Data_fim);
		}


		//TODO fazer o update na BD - DONE
	}

	public static void consultaEleicoesPassadas(RMInterface h) throws RemoteException, SQLException {

		h.ListaEleicoesPassadas();

		int maxEleicoes, opcaoEleicao,flagOut, eleicaoID;
		maxEleicoes = h.maxEleicoes();

		Scanner input;

		while(true) {
			flagOut = 0; // Ver se faz update
			try {
				System.out.println("==============================");
				System.out.printf("(999)>Indique a eleição a visualizar: ");
				input = new Scanner(System.in);
				String eleicao = input.nextLine();
				opcaoEleicao = Integer.parseInt(eleicao);


				Timestamp currData_fim;
				String detalhes; //String com os detalhes iniciais da eleicao

				if (eleicao.equalsIgnoreCase("999")) break;
				if (!(opcaoEleicao < 1 || opcaoEleicao > maxEleicoes)) {
					eleicaoID = opcaoEleicao;
					detalhes = h.getDetalhesEleicao(eleicaoID);

					//Passar a data de String para TimeStamp

					currData_fim = Timestamp.valueOf(detalhes.split("#")[3]);

					//VERIFICAR SE PODE FAZER ALTERAÇÂO NA ELEIÇÂO
					//comparar a data atual com a data de fim da eleicao
					Timestamp checkhoraAtual = new Timestamp(System.currentTimeMillis());

					int checkAlteracao = currData_fim.compareTo(checkhoraAtual);

					if (checkAlteracao > 0) { // >0 significa que a hora atual é superior e não se pode alterar
						System.out.println("A eleição ainda não terminou e não pode consultar os resultados");
						flagOut++;
						//break;

					} else {
						h.consultaEleicoesPassadas(eleicaoID);

					}

				} else {
					System.out.println("Insira uma Eleição entre 1 e " + maxEleicoes);
				}
			}catch (NumberFormatException ex) {
				System.out.println("Insira uma Eleição entre 1 e " + maxEleicoes);
			}
		}

	}

	public static void localVotoEleitores(RMInterface h) throws RemoteException, SQLException {

		/*h.ListaEleicoes();

		int maxEleicoes, opcaoEleicao,flagOut, idEleicao = 0;
		Scanner input;
		maxEleicoes = h.maxEleicoes();

		while (true) {
			try {
				System.out.println("==============================");
				System.out.printf("(999)>Indique a eleição a visualizar: ");
				input = new Scanner(System.in);
				String eleicao = input.nextLine();
				opcaoEleicao = Integer.parseInt(eleicao);
				if (eleicao.equalsIgnoreCase("999")) break;
				if (!(opcaoEleicao < 1 || opcaoEleicao > maxEleicoes)) {
					idEleicao = opcaoEleicao;
					h.getlocalVotoEleitores(opcaoEleicao);


				} else {
					System.out.println("Insira uma Eleição entre 1 e " + maxEleicoes);
				}
			} catch (NumberFormatException ex) {
				System.out.println("Insira uma Eleição entre 1 e " + maxEleicoes);
			}
		}*/
		h.getlocalVotoEleitores();
		/*if(h.getlocalVotoEleitores() == false){
			System.out.println("Não há votos disponíveis.");
		}*/




	}

	private static void gereMesadeVoto(RMInterface h) throws RemoteException, SQLException {

		h.ListaEleicoesNaoComecadas();

		int maxEleicoes, opcaoEleicao,flagOut, eleicaoID;
		maxEleicoes = h.maxEleicoes();
		CopyOnWriteArrayList<String> listaDept = new CopyOnWriteArrayList<>();

		Scanner input;
		Scanner inputDep;
		String departamento;
		int update = 0;
		while (true){
			flagOut = 0; // Ver se faz update
			try {
				System.out.println("==============================");
				System.out.printf("(999)>Indique a eleição a visualizar: ");
				input = new Scanner(System.in);
				String eleicao = input.nextLine();
				opcaoEleicao = Integer.parseInt(eleicao);

				if (eleicao.equalsIgnoreCase("999")) break;
				if (!(opcaoEleicao < 1 || opcaoEleicao > maxEleicoes)) {
					eleicaoID = opcaoEleicao;
					listaDept = h.gereMesadeVoto(eleicaoID);

					for (int i = 0; i < listaDept.size(); i++) System.out.println("ATUAL: " + listaDept.get(i));

					//ADICIONAR DEP
					while (true){
						System.out.printf("Deseja Inserir algum departamento? (S/N)");
						inputDep = new Scanner(System.in);
						String opcaoDep = inputDep.nextLine();
						if (opcaoDep.equalsIgnoreCase("S")){
							update++;
							System.out.printf("Departamento: ");
							inputDep = new Scanner(System.in);
							departamento = inputDep.nextLine();
							if (String.valueOf(departamento).length() != 0 && !departamento.isBlank()) {
								listaDept.add(departamento);
							}
						}else if (opcaoDep.equalsIgnoreCase("N")){
							update++;
							break;
						}else System.out.println("Insira uma resposta válida");
					}
					//REMOVER DEP

					while (true){
						System.out.printf("Deseja Remover algum departamento? (S/N)");
						int valido = 0;
						inputDep = new Scanner(System.in);
						String opcaoDep = inputDep.nextLine();
						if (opcaoDep.equalsIgnoreCase("S")){
							update++;
							System.out.printf("Departamento: ");
							inputDep = new Scanner(System.in);
							departamento = inputDep.nextLine();
							if (String.valueOf(departamento).length() != 0 && !departamento.isBlank()) {
								for (int i = 0; i < listaDept.size(); i++){
									if (departamento.equalsIgnoreCase(listaDept.get(i))){
										valido++;
										listaDept.remove(i);
									}
								}
								if (valido > 0);
								else System.out.println("Insira um Departamento válido");

							}
						}else if (opcaoDep.equalsIgnoreCase("N")){
							update++;
							//System.out.println("VALOR: " + update);
							for(int i = 0; i < listaDept.size(); i++){
								System.out.println(i + "-> " + listaDept.get(i));
							}
							if (update != 2) h.updateListaDep(opcaoEleicao, listaDept);
							break;
						}else System.out.println("Insira uma resposta válida");
					}
					break;

				}else {
					System.out.println("Insira uma Eleição entre 1 e " + maxEleicoes);
				}

			}catch (NumberFormatException ex) {
				System.out.println("Insira uma Eleição entre 1 e " + maxEleicoes);
			}
		}

	}

	private static void eleitoresTempoReal(RMInterface h) throws RemoteException, SQLException {
		System.out.println(" - - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
		System.out.println("\t\t\t\tLISTA DE ELEIÇÕES");
		h.ListaEleicoes();

		int maxEleicoes, opcaoEleicao, flagOut = 0, idEleicao = 0;
		maxEleicoes = h.maxEleicoes();

		Scanner input;
		while (true) {

			try {
				System.out.printf("Indique a eleição a visualizar: ");
				input = new Scanner(System.in);
				String eleicao = input.nextLine();
				opcaoEleicao = Integer.parseInt(eleicao);
				if (!(opcaoEleicao < 1 || opcaoEleicao > maxEleicoes)) {
					//Depois de escolher uma eleicao a cada 2s vai mostrar os eleitores da eleição dos vários departamentos
					Scanner inputExit = new Scanner(System.in);
					idEleicao = opcaoEleicao;
					int exit = 0;

					while (exit != 5){
						h.getEleitoresTempoReal(idEleicao);
						Thread.sleep((2000));
						exit++;
					}
				}break;

			} catch (NumberFormatException | InterruptedException ex) {
				System.out.println("Insira uma Eleição entre 1 e " + maxEleicoes);
			}
		}
	}


		public static void main(String args[]) {

		try {
			RMInterface h = (RMInterface) LocateRegistry.getRegistry(7000).lookup("RMIConnect");
			AdminConsole admin = new AdminConsole();

			h.subscribe(admin);

			while(true){

				displayFuncionalidade();
				String opcao = "";
				try{
					System.out.printf("OPÇÃO: ");
					Scanner input = new Scanner(System.in);
					opcao = input.nextLine();
					Integer.parseInt(opcao);
					if (Integer.parseInt(opcao) <= 9){
						switch(opcao) {
							case "1":
								// Registar Pessoas
								while(true) {
									try {
										h.registaPessoa(registaPessoa());
										break;
									} catch (ConnectException | ConnectIOException ce) {
										RMInterface aux = admin.changeRMI();
										if(aux != null) {
											h = aux;
											h.subscribe(admin);
										}
									}
								}
								break;
							case "2":
								// Criar Eleição
								while(true) {
									try {
										h.criaEleicao(criaEleicao());
										break;
									} catch (ConnectException | ConnectIOException ce) {
										RMInterface aux = admin.changeRMI();
										if(aux != null) {
											h = aux;
											h.subscribe(admin);
										}
									}
								}
								break;
							case "3":
								// Gerir listas de candidatos a uma eleição
								System.out.println(" - - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
								System.out.println("\t\t\t\tLISTA DE ELEIÇÕES");
								while(true) {
									try {
										gereCandidato(h);
										break;
									} catch (ConnectException | ConnectIOException ce) {
										RMInterface aux = admin.changeRMI();
										if(aux != null) {
											h = aux;
											h.subscribe(admin);
										}
									}
								}
								break;
							case "4":
								// Gerir mesas de voto
								System.out.println(" - - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
								System.out.println("\t\t\t\tLISTA DE ELEIÇÕES");
								while(true) {
									try {

										gereMesadeVoto(h);
										break;
									} catch (ConnectException | ConnectIOException | NullPointerException ce) {
										RMInterface aux = admin.changeRMI();
										if(aux != null) {
											h = aux;
											h.subscribe(admin);
										}
									}
								}
								break;
							case "5":
								// Alterar propriedades de uma eleição
								System.out.println(" - - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
								System.out.println("\t\t\t\tLISTA DE ELEIÇÕES");
								while(true) {
									try {
										alteraPropriedadesEleicao(h);
										break;
									} catch (ConnectException | ConnectIOException ce) {
										RMInterface aux = admin.changeRMI();
										if(aux != null) {
											h = aux;
											h.subscribe(admin);
										}
									}
								}
								break;
							case "6":
								// Saber em que local votou cada eleitors
								System.out.println(" - - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
								System.out.println("\t\t\t\tCONSULTA DE VOTOS");
								while(true) {
									try {
										localVotoEleitores(h);
										break;
									} catch (ConnectException | ConnectIOException ce) {
										RMInterface aux = admin.changeRMI();
										if(aux != null) {
											h = aux;
											h.subscribe(admin);
										}
									}
								}
								break;
							case "7":
								// Mostrar estado das mesas de voto
								while(true) {
									try {
										h.saveDep("");
										break;
									} catch (ConnectException | ConnectIOException ce) {
										RMInterface aux = admin.changeRMI();
										if(aux != null) {
											h = aux;
											h.subscribe(admin);
										}
									}
								}
								break;
							case "8":
								// Mostrar eleitores em tempo real
								while(true) {
									try {
										eleitoresTempoReal(h);
										break;
									} catch (ConnectException | ConnectIOException ce) {
										RMInterface aux = admin.changeRMI();
										if(aux != null) {
											h = aux;
											h.subscribe(admin);
										}
									}
								}

								break;
							case "9":
								// Consultar resultados detalhados de eleições passadas
								System.out.println(" - - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
								System.out.println("\t\t\t\tLISTA DE ELEIÇÕES");
								while(true) {
									try {
										consultaEleicoesPassadas(h);
										break;
									} catch (ConnectException | ConnectIOException ce) {
										RMInterface aux = admin.changeRMI();
										if(aux != null) {
											h = aux;
											h.subscribe(admin);
										}
									}
								}
								break;

						}
					}else{ //Opção não incluída nas possíveis
						System.out.println("Escolha uma opção válida");
					}

				} catch(NumberFormatException ex){ // O que foi lido no input não foi um número
					System.out.println("Insira um número válido");
				}
			}

		} catch (Exception e) {
			System.out.println("Exception in main: " + e);
		}

	}

}


