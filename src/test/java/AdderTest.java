import org.example.ChainAdder;
import org.example.ChainAdderRemote;
import org.example.Client;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.ThreadLocalRandom;

public class AdderTest {
    private final ChainAdder server = new ChainAdderRemote();

    public AdderTest() throws RemoteException {
    }

    @Test
    public void testClientInitializer() {
        try (Client client = new Client()){
            Assertions.assertEquals(client.result(), 0);
        } catch (NotBoundException | InterruptedException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testClientAdd() {
        try (Client client = new Client()){
            int expected = ThreadLocalRandom.current().nextInt();
            client.add(expected);
            Assertions.assertEquals(client.result(), expected);
        } catch (NotBoundException | InterruptedException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testMultipleAdd() {
        try (Client client = new Client()){
            int sum = 0;
            int rand;
            for (int i = 0; i < 100; i++) {
                // Number chosen to avoid overflow
                rand = ThreadLocalRandom.current().nextInt(100_000);
                sum += rand;
                client.add(rand);
            }
            Assertions.assertEquals(sum, client.result());
        } catch (NotBoundException | InterruptedException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
