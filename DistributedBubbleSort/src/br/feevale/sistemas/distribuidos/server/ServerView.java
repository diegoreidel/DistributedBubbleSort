package br.feevale.sistemas.distribuidos.server;


import javax.swing.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerView extends JFrame
{
    private ServerImpl server;

    private JButton btnCreateServer;
    private JButton btnDelegateAllJobs;
    private JScrollPane jspScroll;
    private JTextField txtURI;
    private JTextArea txtOutput;

    public ServerView()
    {
        initComponents();
    }

    private void initComponents()
    {

        jspScroll = new JScrollPane();
        txtOutput = new JTextArea();
        txtURI = new JTextField();
        btnCreateServer = new JButton();
        btnDelegateAllJobs = new JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        txtOutput.setEditable(false);
        txtOutput.setColumns(20);
        txtOutput.setRows(5);
        jspScroll.setViewportView(txtOutput);

        txtURI.setText("rmi://localhost:8088/boss");

        btnCreateServer.setText("Connect");
        btnCreateServer.addActionListener(e -> btnConnectActionPerformed());

        btnDelegateAllJobs.setText("Delegate jobs");
        btnDelegateAllJobs.setEnabled(Boolean.FALSE);
        btnDelegateAllJobs.addActionListener(e -> btnDelegateAllJobsActionPerformed());

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
                                                .addComponent(btnCreateServer)
                                                .addComponent(btnDelegateAllJobs)))
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
                                        .addComponent(btnCreateServer)
                                        .addComponent(btnDelegateAllJobs))
                                .addContainerGap())
        );

        pack();
    }

    private void btnConnectActionPerformed()
    {
        try
        {
            server = new ServerImpl(this);
            Registry r = LocateRegistry.createRegistry(8088);
            String uri = txtURI.getText();
            Naming.rebind(uri, server);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        btnCreateServer.setEnabled(Boolean.FALSE);
        btnDelegateAllJobs.setEnabled(Boolean.TRUE);
    }

    private void btnDelegateAllJobsActionPerformed()
    {
        try
        {
            server.delegateAllJobs();
            log("Finished delegating jobs.");
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    public void log(String log)
    {
        String originalText = txtOutput.getText() + "\n";
        txtOutput.setText(originalText + log);
    }

    public static void main(String[] args)
    {
        new ServerView().setVisible(Boolean.TRUE);
    }
}