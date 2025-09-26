import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ServidorSocketThread implements Runnable {

    private static final Set<DataOutputStream> clients = Collections.synchronizedSet(new HashSet<>());

    private Socket socketClient;
    private DataOutputStream saida;

    public ServidorSocketThread(Socket socketClient) {
        this.socketClient = socketClient;
    }

    @Override
    public void run() {
        try (DataInputStream entrada = new DataInputStream(socketClient.getInputStream())) {
            // registra o stream de saída deste cliente na lista global
            saida = new DataOutputStream(socketClient.getOutputStream());
            clients.add(saida);

            String linha;
            while (true) {
                try {
                    linha = entrada.readUTF();
                } catch (IOException e) {
                    break; // cliente desconectou
                }
                if (linha == null || linha.trim().isEmpty()) {
                    break;
                }
                // broadcast: enviar a mensagem para todos os clientes conectados
                broadcast("[" + socketClient.getRemoteSocketAddress() + "] " + linha);
            }
        } catch (IOException e) {
            System.out.println("Cliente desconectado: " + socketClient.getRemoteSocketAddress());
        } finally {
            if (saida != null) {
                clients.remove(saida);
                try { saida.close(); } catch (IOException ignored) {}
            }
            try { socketClient.close(); } catch (IOException ignored) {}
        }
    }

    private void broadcast(String mensagem) {
        synchronized (clients) {
            Iterator<DataOutputStream> it = clients.iterator();
            while (it.hasNext()) {
                DataOutputStream out = it.next();
                try {
                    out.writeUTF(mensagem);
                    out.flush();
                } catch (IOException e) {
                    System.out.println("Falha ao enviar para um cliente: " + e.getMessage());
                    it.remove(); // remove cliente problemático
                }
            }
        }
    }
}
