import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== BARBEIRO DORMINHOCO COM LOCKS ===\n");
        
        // Configurações
        int numCadeirasEspera = 3;
        int numClientes = 10;
        int intervaloClientes = 1500; // ms entre chegada de clientes
        
        // Cria a barbearia
        Barbearia barbearia = new Barbearia(numCadeirasEspera);
        
        // Cria o barbeiro
        Barbeiro barbeiro = new Barbeiro(barbearia);
        Thread barbeiroThread = new Thread(barbeiro, "Barbeiro");
        
        // Inicia o barbeiro
        barbeiroThread.start();
        
        // Cria pool de threads para clientes
        ExecutorService clientePool = Executors.newCachedThreadPool();
        
        // Simula a chegada de clientes
        for (int i = 1; i <= numClientes; i++) {
            Cliente cliente = new Cliente("Cliente-" + i, barbearia);
            clientePool.execute(cliente);
            
            try {
                // Intervalo aleatório entre chegada de clientes
                Thread.sleep((long) (Math.random() * intervaloClientes));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        // Finaliza o pool de clientes
        clientePool.shutdown();
        
        try {
            // Aguarda todos os clientes serem atendidos
            if (!clientePool.awaitTermination(30, TimeUnit.SECONDS)) {
                System.out.println("Tempo limite atingido - fechando barbearia");
            }
            
            // Interrompe o barbeiro
            barbeiroThread.interrupt();
            barbeiroThread.join(2000);
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("\n=== FIM DO EXPEDIENTE ===");
        System.out.println("Total de clientes atendidos: " + barbearia.getClientesAtendidos());
    }
}