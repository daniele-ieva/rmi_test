package org.example;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.UUID;

public class Client implements AutoCloseable {
    @Override
    public void close() {
        try {
            server.close(id);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public Client() throws RemoteException, NotBoundException, InterruptedException {
        Registry registry = LocateRegistry.getRegistry();
        server = (ChainAdder) registry.lookup(ChainAdderRemote.name);
        id = server.getUUID();
        while (id == null) {
            Thread.sleep(1000);
            id = server.getUUID();
        }
        addShutdownHook();
    }

    public Client(String ip) throws RemoteException, NotBoundException, InterruptedException {
        Registry registry = LocateRegistry.getRegistry(ip, 1099);
        server = (ChainAdder) registry.lookup(ChainAdderRemote.name);
        id = server.getUUID();
        while (id == null) {
            Thread.sleep(1000);
            id = server.getUUID();
        }
        addShutdownHook();
    }

    public Client add(int value) throws RemoteException {
        server.add(value, id);
        try {Thread.sleep(10);
        } catch (InterruptedException e) {return this;}
        return this;
    }

    public int result() throws RemoteException {
        return server.result(id);
    }

    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(
                this::close
        ));
    }

    private final ChainAdder server;
    private UUID id;
}
