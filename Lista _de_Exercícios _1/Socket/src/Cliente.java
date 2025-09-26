import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Cliente {

    public static void main(String args[]) throws Exception {
        try (Socket socket = new Socket("127.0.0.1", 50000);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

            System.out.println("Conectado ao servidor em 127.0.0.1:50000");

            // Thread para ler mensagens do servidor (broadcasts)
            Thread reader = new Thread(() -> {
                try {
                    String msg;
                    while ((msg = input.readUTF()) != null) {
                        System.out.println(msg);
                    }
                } catch (IOException e) {
                    System.out.println("Conexão encerrada pelo servidor.");
                }
            });
            reader.setDaemon(true);
            reader.start();

            // Leitura do teclado e envio para o servidor
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
            String linha;
            while ((linha = teclado.readLine()) != null) {
                // se o usuário digitar "sair" fechamos a conexão de forma elegante
                if ("sair".equalsIgnoreCase(linha.trim())) {
                    // envia uma linha vazia (ou apenas fecha) para sinalizar
                    output.writeUTF("");
                    output.flush();
                    break;
                }
                output.writeUTF(linha);
                output.flush();
            }

            System.out.println("Cliente finalizando...");
        }
    }
}