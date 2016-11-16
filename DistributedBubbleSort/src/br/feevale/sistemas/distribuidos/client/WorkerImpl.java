package br.feevale.sistemas.distribuidos.client;

import br.feevale.sistemas.distribuidos.server.interfaces.Job;
import br.feevale.sistemas.distribuidos.client.interfaces.Worker;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class WorkerImpl extends UnicastRemoteObject implements Worker
{
    private WorkerView view;

    public WorkerImpl(WorkerView view) throws RemoteException{
        super();
        this.view = view;
    }

    @Override
    public void work(Job job) throws RemoteException
    {
        if (job == null) {
            view.printText("Server has no job to send!");
        }
        job.setList(bubbleSort(job.getList()));
        view.logJob(job);
    }

    private List<Integer> bubbleSort(List<Integer> numbers)
    {
        Integer size = numbers.size();
        for(int i = 0; i < size; i++) {
            for (int j = 1; j < size - 1; j++) {
                if(numbers.get(j - 1) > numbers.get(j)) {
                    Integer temp = numbers.get(j - 1);
                    numbers.set(j - 1, numbers.get(j));
                    numbers.set(j, temp);
                }
            }
        }

        return numbers;
    }
}
