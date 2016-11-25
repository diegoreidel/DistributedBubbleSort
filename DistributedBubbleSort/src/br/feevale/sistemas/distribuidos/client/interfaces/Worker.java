package br.feevale.sistemas.distribuidos.client.interfaces;

import br.feevale.sistemas.distribuidos.server.interfaces.Job;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Worker extends Remote
{
    void work(Job job) throws RemoteException;

    boolean isBusy() throws RemoteException;

    String getUid() throws RemoteException;
}
