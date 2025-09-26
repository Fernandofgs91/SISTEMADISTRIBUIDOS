import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args) throws Exception {
        try (ServerSocket s = new ServerSocket(50000, 50)) {
            System.out.println("Servidor iniciado na porta 50000");
            while (true) {
                Socket con = s.accept();
                System.out.println("Conexão estabelecida de " + con.getRemoteSocketAddress());
                ServidorSocketThread socketThread = new ServidorSocketThread(con);
                new Thread(socketThread).start();
            }
        }
    }
}