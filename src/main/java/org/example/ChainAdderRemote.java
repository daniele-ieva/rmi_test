package org.example;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ChainAdderRemote implements ChainAdder {
    public static final String name ="adder";
    @Override
    public Integer result(UUID id) throws RemoteException {
        synchronized (result) {
            if (!result.containsKey(id)) {
                return 0;
            }
            return result.get(id);
        }
    }

    @Override
    public synchronized UUID getUUID() {
        UUID client = UUID.randomUUID();
        synchronized (result) {
            connections++;
            System.out.println("connection started with " + client);
            System.out.printf("There are %d connections active\n", connections);
            result.put(client, 0);
        }
        return client;
    }

    @Override
    public void close(UUID id) throws RemoteException {
        System.out.println("Connection closed with " + id);
        synchronized (result) {
            connections--;
            System.out.printf("There are %d connections active\n", connections);
            result.remove(id);
        }
    }

    @Override
    public ChainAdder add(Integer value, UUID id) throws RemoteException {
        synchronized (result) {
            if (!result.containsKey(id)) {
                return this;
            }
            int cur = result.get(id);
            cur += value;
            result.put(id, cur);
            return this;
        }
    }

    public ChainAdderRemote() throws RemoteException {
        System.out.println("Starting server...");
        ChainAdder stub = (ChainAdder) UnicastRemoteObject.exportObject(this, 0);
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind(name, stub);
        System.out.println("Started!");
    }

    private final Map<UUID, Integer> result = new ConcurrentHashMap<>();
    private volatile int connections = 0;
}
