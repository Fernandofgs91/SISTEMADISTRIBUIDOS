import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Servidor {
    private static final int PORTA = 12345;
   
    private static final Set<ServidorSocketThread> clientes =
            Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) {
        try (ServerSocket servidor = new ServerSocket(PORTA)) {
            System.out.println("Servidor iniciado na porta " + PORTA);

            while (true) {
                Socket socket = servidor.accept();
                System.out.println("Novo cliente conectado: " + socket);

                ServidorSocketThread clienteThread = new ServidorSocketThread(socket, clientes);
                clientes.add(clienteThread);

                new Thread(clienteThread).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
