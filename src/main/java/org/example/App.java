package org.example;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class App {
    public static void main(String[] args) throws RemoteException {
        assert args.length > 0;

        switch (args[0]) {
            case "server":
                int port = 42069;
                if (args.length > 1) {
                    try {
                        port = Integer.parseInt(args[1]);
                    } catch (NumberFormatException ignored) {
                    }
                }
                spawn_server(port);
                break;
            case "test":
                int iter;
                if (args.length < 2) {
                    iter = 10;
                }
                else {
                    try {
                        iter = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        iter = 10;
                    }
                }
                spawn_test(iter);
                break;
            case "client":
                if (args.length > 1) {
                    spawn_client(args[1]);
                    break;
                }
                spawn_client();
                break;
            default:
                System.out.println("Unknown arguments");
        }
    }

    private static void spawn_server(int port) throws RemoteException {
        new ChainAdderRemote(port);
    }

    private static void spawn_test(int count) {
        for (var i = 0; i < count; i++) {
            new Thread(() -> {
                try {
                    Client c = new Client();
                    int bound = ThreadLocalRandom.current().nextInt(100000);
                    for (var j = 0; j < bound; j++) {
                        c.add(10)
                                .add(25)
                                .add(33);
                    }
                    System.out.println(c.result());
                    c.close();
                } catch (RemoteException | NotBoundException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }

    private static void spawn_client() {
        boolean running = true;
        Scanner in = new Scanner(System.in);
        try (Client client = new Client()) {
                mainLoop(running, in, client);
        } catch (RemoteException | NotBoundException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void spawn_client(String ip) {
        boolean running = true;
        Scanner in = new Scanner(System.in);
        try (Client client = new Client(ip)) {
            mainLoop(running, in, client);
        } catch (RemoteException | NotBoundException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private static void mainLoop(boolean running, Scanner in, Client client) throws RemoteException {
        String input;
        int val;
        while (running) {
            input = in.nextLine();
            if (input.contains("close")) {
                running = false;
                continue;
            }
            if (input.contains("result")) {
                System.out.println(client.result());
                continue;
            }
            if (input.contains("add")) {
                try {
                    val = Integer.parseInt(input.split(" ")[1]);
                    client.add(val);
                } catch (NumberFormatException e) {
                    continue;
                }
                continue;
            }
            System.out.println("Unknown Command!");
        }
    }
}
