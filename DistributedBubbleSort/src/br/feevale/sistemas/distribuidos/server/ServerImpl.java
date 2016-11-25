package br.feevale.sistemas.distribuidos.server;

import br.feevale.sistemas.distribuidos.server.interfaces.Job;
import br.feevale.sistemas.distribuidos.server.interfaces.Server;
import br.feevale.sistemas.distribuidos.client.interfaces.Worker;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerImpl extends UnicastRemoteObject implements Server
{

    private static ArrayList<Integer> seedList;
    private static final Integer LIST_SIZE = 10000;
    private static final Integer JOB_LOAD = 10;

    private List<Job> availableJobs;
    private List<Job> concludedJobs;
    private List<Worker> workers;


    public ServerImpl() throws RemoteException
    {
        seedList = createSeedList(LIST_SIZE);
        availableJobs = new ArrayList<>();
        concludedJobs = new ArrayList<>();
        workers = new ArrayList<>();

        createJobs(JOB_LOAD);
    }

    @Override
    public void registerWorker(Worker worker) throws RemoteException
    {
        workers.add(worker);
    }

    @Override
    public void removeWorker(Worker worker) throws RemoteException
    {
        workers.remove(worker);
    }

    @Override
    public void delegateWork(Worker worker) throws RemoteException
    {
        worker.work(availableJobs.remove(0));
    }

    public void delegateWork(Worker worker, Job job) throws RemoteException
    {
        worker.work(job);
    }

    @Override
    public void concludeJob(Job job) throws RemoteException
    {
        concludedJobs.add(job);
    }

    public void delegateAllJobs() throws RemoteException
    {
        for (Job job : availableJobs)
        {
            delegateWork(findAvailableWorker(), job);
        }
    }

    private ArrayList<Integer> createSeedList(Integer size)
    {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++)
        {
            list.add(i);
        }
        return list;
    }

    private void createJobs(Integer quantity)
    {
        for (int i = 0; i < quantity; i++)
        {
            ArrayList<Integer> shuffledList = (ArrayList<Integer>) seedList.clone();
            Collections.shuffle(shuffledList);

            try
            {
                availableJobs.add(new JobImpl(shuffledList));
            } catch (RemoteException e)
            {
                e.printStackTrace();
            }
        }
    }

    private Worker findAvailableWorker() throws RemoteException
    {
        Worker worker = null;
        do
        {
            for (Worker w : workers)
            {
                if (!w.isBusy())
                {
                    worker = w;
                    System.out.println("Found worker");
                    break;
                }
            }
        } while (worker == null);

        return worker;
    }
}
