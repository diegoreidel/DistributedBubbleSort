package br.feevale.sistemas.distribuidos.server;

import br.feevale.sistemas.distribuidos.server.interfaces.Job;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class JobImpl extends UnicastRemoteObject implements Job
{

    private ArrayList<Integer> numbers;

    public JobImpl(ArrayList<Integer> numbers) throws RemoteException
    {
        super();
        this.numbers = numbers;
    }

    @Override
    public List<Integer> getList() throws RemoteException
    {
        return numbers;
    }

    @Override
    public void setList(List<Integer> list) throws RemoteException
    {
        this.numbers = new ArrayList<>(list);
    }
}
