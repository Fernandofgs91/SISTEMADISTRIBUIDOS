import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Set;

public class ServidorSocketThread implements Runnable {
    private Socket socket;
    private ObjectOutputStream saida;
    private ObjectInputStream entrada;
    private Set<ServidorSocketThread> clientes;

    public ServidorSocketThread(Socket socket, Set<ServidorSocketThread> clientes) {
        this.socket = socket;
        this.clientes = clientes;
        try {
            this.saida = new ObjectOutputStream(socket.getOutputStream());
            this.entrada = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Envia objeto para este cliente
    public void enviarMensagem(Object obj) {
        try {
            saida.writeObject(obj);
            saida.flush();
        } catch (IOException e) {
            System.out.println("Erro ao enviar para cliente: " + socket);
        }
    }

    // Envia mensagem para todos os clientes conectados
    private void broadcast(Pedido pedido) {
        synchronized (clientes) {
            for (ServidorSocketThread cliente : clientes) {
                if (cliente != this) {
                    cliente.enviarMensagem(pedido);
                }
            }
        }
    }

    @Override
    public void run() {
        try {
            Object obj;
            while ((obj = entrada.readObject()) != null) {
                if (obj instanceof Pedido pedido) {
                    System.out.println("Recebido: " + pedido);

                    if (pedido.getMensagem().contains("<todos>")) {
                        broadcast(pedido);
                    } else {
                        enviarMensagem("Servidor recebeu seu pedido: " + pedido.getMensagem());
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Cliente desconectado: " + socket);
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {}
            clientes.remove(this);
        }
    }
}
    
