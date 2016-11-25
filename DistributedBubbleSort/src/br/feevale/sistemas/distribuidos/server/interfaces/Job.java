package br.feevale.sistemas.distribuidos.server.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Job extends Remote
{
    List<Integer> getList() throws RemoteException;

    void setList(List<Integer> list) throws RemoteException;

    String getId() throws RemoteException;
}
