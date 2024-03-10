package org.example;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

public interface ChainAdder extends Remote {
    Integer result(UUID id) throws RemoteException;

    ChainAdder add(Integer value, UUID id) throws RemoteException;

    UUID getUUID() throws RemoteException;
    void close(UUID id) throws RemoteException;
}
