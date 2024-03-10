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
                spawn_server();
                break;
            case "test":
                spawn_test(10);
                break;
            case "client":
                spawn_client();
                break;
            default:
                System.out.println("Unknown arguments");
        }
    }

    private static void spawn_server() throws RemoteException {
        new ChainAdderRemote();
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
            String input;
            int val;
            try (Client client = new Client()) {
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
            } catch (RemoteException | NotBoundException | InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
}
