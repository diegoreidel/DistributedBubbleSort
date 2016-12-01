package br.feevale.sistemas.distribuidos.client;

import br.feevale.sistemas.distribuidos.server.interfaces.Job;
import br.feevale.sistemas.distribuidos.client.interfaces.Worker;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.UUID;

public class WorkerImpl extends UnicastRemoteObject implements Worker
{
    private WorkerView view;
    private boolean busy;
    private String uid;

    public WorkerImpl(WorkerView view) throws RemoteException
    {
        super();
        this.view = view;
        this.uid = UUID.randomUUID().toString();
    }

    @Override
    public void work(Job job) throws RemoteException
    {
        if (job == null)
        {
            view.printText("Server has no job to send!");
        }
        this.busy = true;
        view.logStartJog(job);
        job.setList(bubbleSort(job.getList()));
        view.logJob(job);
        this.busy = false;
    }

    @Override
    public boolean isBusy() throws RemoteException
    {
        return this.busy;
    }

    @Override
    public String getUid() throws RemoteException
    {
        return this.uid;
    }

    @Override
    public void setBusy(boolean busy)
    {
        this.busy = busy;
    }

    private List<Integer> bubbleSort(List<Integer> numbers)
    {
        Integer size = numbers.size();
        for (int i = 0; i < size; i++)
        {
            for (int j = 1; j < size - 1; j++)
            {
                if (numbers.get(j - 1) > numbers.get(j))
                {
                    Integer temp = numbers.get(j - 1);
                    numbers.set(j - 1, numbers.get(j));
                    numbers.set(j, temp);
                }
            }
        }
        return numbers;
    }
}
