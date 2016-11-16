package br.feevale.sistemas.distribuidos.server.interfaces;

import br.feevale.sistemas.distribuidos.client.interfaces.Worker;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server extends Remote
{
    void registerWorker(Worker worker) throws RemoteException;
    void delegateWork(Worker worker) throws RemoteException;
    void concludeJob(Job job) throws RemoteException;
}
