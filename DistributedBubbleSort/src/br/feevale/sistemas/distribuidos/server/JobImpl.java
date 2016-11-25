package br.feevale.sistemas.distribuidos.server;

import br.feevale.sistemas.distribuidos.server.interfaces.Job;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JobImpl extends UnicastRemoteObject implements Job
{

    private ArrayList<Integer> numbers;
    private String id;

    public JobImpl(ArrayList<Integer> numbers) throws RemoteException
    {
        super();
        this.numbers = numbers;
        this.id = UUID.randomUUID().toString();
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

    @Override
    public String getId()
    {
        return this.id;
    }
}
