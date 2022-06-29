package com.company;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MulticastServerInterface extends Remote {
    public void print_on_client(String s) throws RemoteException;

}
