package br.feevale.sistemas.distribuidos.client;

import br.feevale.sistemas.distribuidos.client.interfaces.Worker;
import br.feevale.sistemas.distribuidos.server.interfaces.Job;
import br.feevale.sistemas.distribuidos.server.interfaces.Server;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;
import java.util.stream.Collectors;

public class WorkerView extends JFrame
{
    private JButton btnConnect;
    private JScrollPane jspScroll;
    private JTextField txtURI;
    private JTextArea txtOutput;
    private JButton btnRequestWork;

    private Worker worker;
    private Server server;

    public WorkerView()
    {
        initComponents();
    }

    public void printText(String text)
    {
        String originalText = txtOutput.getText() + "\n";
        txtOutput.setText(originalText + text);
    }

    public void logStartJog(Job job) throws RemoteException
    {
        List<Integer> numbers = job.getList();
        int size = numbers.size();
        printText("Worker has started a job: "
                + getItems(numbers, 0, 3) + " ... " + getItems(numbers, size - 4, size - 1) + " with size " + size);
    }

    private String getItems(List<Integer> numbers, int from, int to)
    {
        return numbers.subList(from, to).stream().map(Object::toString).collect(Collectors.joining(","));
    }

    public void logJob(Job job)
    {
        try
        {
            server.concludeJob(job);
            List<Integer> numbers = job.getList();
            int size = numbers.size();

            printText("Worker has concluded a job: "
                    + getItems(numbers, 0, 3) + " ... " + getItems(numbers, size - 4, size - 1) + " with size " + size);

        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    private void initComponents()
    {
        this.addWindowStateListener(
                new WindowAdapter()
                {
                    @Override
                    public void windowClosing(WindowEvent e)
                    {
                        try
                        {
                            server.removeWorker(worker);
                        } catch (RemoteException e1)
                        {
                            e1.printStackTrace();
                        }
                    }
                }
        );

        jspScroll = new JScrollPane();
        txtOutput = new JTextArea();
        txtURI = new JTextField();
        btnConnect = new JButton();
        btnRequestWork = new JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        txtOutput.setEditable(false);
        txtOutput.setColumns(20);
        txtOutput.setRows(5);
        jspScroll.setViewportView(txtOutput);

        txtURI.setText("rmi://localhost:8088/boss");

        btnConnect.setText("Connect");
        btnConnect.addActionListener(e -> btnConnectActionPerformed());

        btnRequestWork.setText("Request job");
        btnRequestWork.addActionListener(e -> btnRequestWorkActionPerformed());
        btnRequestWork.setEnabled(Boolean.FALSE);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jspScroll, GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(txtURI)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnConnect)
                                                .addComponent(btnRequestWork)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jspScroll, GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtURI, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnConnect)
                                        .addComponent(btnRequestWork))
                                .addContainerGap())
        );

        pack();
    }

    private void btnConnectActionPerformed()
    {
        try
        {
            server = (Server) Naming.lookup(txtURI.getText());
            worker = new WorkerImpl(this);
            server.registerWorker(worker);
            btnRequestWork.setEnabled(Boolean.TRUE);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void btnRequestWorkActionPerformed()
    {
        try
        {
            server.delegateWork(worker);
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        new WorkerView().setVisible(true);
    }
}
