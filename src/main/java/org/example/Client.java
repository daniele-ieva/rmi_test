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
    }

    public Client add(int value) throws RemoteException {
        server.add(value, id);
        return this;
    }

    public int result() throws RemoteException {
        return server.result(id);
    }

    private final ChainAdder server;
    private final UUID id;
}
