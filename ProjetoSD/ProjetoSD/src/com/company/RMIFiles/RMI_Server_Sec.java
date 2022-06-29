package com.company.RMIFiles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLClientInfoException;

//FUNCIONA COMO CLIENTE UDP

class RMI_Server_sec {
    public RMI_Server_sec() throws RemoteException {
        super();
    }

    public static void main(String args[]) {

        try {
            int TIMEOUT = 9000; //9s de timeout caso nao obtenha uma resposta

            RMI_Server hSec = new RMI_Server();
            Registry r = LocateRegistry.createRegistry(5000);
            r.rebind("RMIConnect", hSec);

            System.out.println("Hello Server SEC ready.");

            PostgreSQLJDBC db = new PostgreSQLJDBC();
            db.connectDB();

            //Ligação RMI Secundário <-> RMI Principal
            DatagramSocket aSocketRMI_sec = null;
            try {
                aSocketRMI_sec = new DatagramSocket();
                aSocketRMI_sec.setSoTimeout(TIMEOUT);

                String heartBeat = "Estou vivo! ";
                InputStreamReader input = new InputStreamReader(System.in);
                BufferedReader reader = new BufferedReader(input);
                while(true){
                    Thread.sleep((3000)); //A cada 3s manda uma mensagem ao servidor RMI primário à espera de uma resposta
                    System.out.println(heartBeat);

                    byte [] m = heartBeat.getBytes();

                    InetAddress aHost = InetAddress.getByName("localhost");
                    int serverPort = 6789;
                    DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);
                    aSocketRMI_sec.send(request);

                    byte[] buffer = new byte[1000];
                    DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                    aSocketRMI_sec.receive(reply);
                    System.out.println("Recebeu: " + new String(reply.getData(), 0, reply.getLength()));
                } // while


            }catch (SocketTimeoutException s) {
                System.out.println("Não recebi nada durante 15s.");

            }catch (SocketException e) {
                System.out.println("Não recebi nada durante 15s");
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        } catch (RemoteException re) {
            System.out.println("Exception in HelloImpl.main: " + re);
        } catch (SQLClientInfoException throwables) {
            throwables.printStackTrace();
        }

    }
}
