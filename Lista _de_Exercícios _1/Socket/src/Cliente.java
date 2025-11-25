import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    private static final String HOST = "localhost";
    private static final int PORTA = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORTA)) {
            System.out.println("Conectado ao servidor.");

            ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());

          
            Thread leitorServidor = new Thread(() -> {
                try {
                    Object obj;
                    while ((obj = entrada.readObject()) != null) {
                        System.out.println(">> " + obj);
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Conexão com servidor encerrada.");
                }
            });
            leitorServidor.start();

           
            Scanner scanner = new Scanner(System.in);
            System.out.print("Digite seu nome: ");
            String nome = scanner.nextLine();

            while (true) {
                String msg = scanner.nextLine();
                Pedido pedido = new Pedido(nome, msg);
                saida.writeObject(pedido);
                saida.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
