import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.LinkedList;
import java.util.Queue;

public class Barbearia {
    private final int NUM_CADEIRAS_ESPERA;
    private final Lock lock;
    private final Condition barbeiroDisponivel;
    private final Condition clienteDisponivel;
    private final Condition corteTerminado;
    
    private boolean barbeiroDormindo;
    private boolean corteEmAndamento;
    private Queue<Cliente> cadeirasEspera;
    private int clientesAtendidos;

    public Barbearia(int numCadeirasEspera) {
        this.NUM_CADEIRAS_ESPERA = numCadeirasEspera;
        this.lock = new ReentrantLock();
        this.barbeiroDisponivel = lock.newCondition();
        this.clienteDisponivel = lock.newCondition();
        this.corteTerminado = lock.newCondition();
        
        this.barbeiroDormindo = true;
        this.corteEmAndamento = false;
        this.cadeirasEspera = new LinkedList<>();
        this.clientesAtendidos = 0;
    }

    public boolean entrarBarbearia(Cliente cliente) {
        lock.lock();
        try {
            System.out.println(cliente.getNome() + " chegou na barbearia");
            
            // Se há cadeiras de espera disponíveis
            if (cadeirasEspera.size() < NUM_CADEIRAS_ESPERA) {
                cadeirasEspera.offer(cliente);
                System.out.println(cliente.getNome() + " sentou na cadeira de espera. " + 
                                 cadeirasEspera.size() + "/" + NUM_CADEIRAS_ESPERA + " cadeiras ocupadas");
                
                // Se o barbeiro está dormindo, acorda ele
                if (barbeiroDormindo) {
                    System.out.println(cliente.getNome() + " acordou o barbeiro");
                    barbeiroDormindo = false;
                    clienteDisponivel.signal();
                }
                return true;
            } else {
                // Barbearia cheia - cliente vai embora
                System.out.println(cliente.getNome() + " foi embora - barbearia cheia!");
                return false;
            }
        } finally {
            lock.unlock();
        }
    }

    public Cliente proximoCliente() throws InterruptedException {
        lock.lock();
        try {
            // Se não há clientes, barbeiro dorme
            while (cadeirasEspera.isEmpty()) {
                System.out.println("Barbeiro foi dormir - nenhum cliente");
                barbeiroDormindo = true;
                clienteDisponivel.await();
            }
            
            // Pega o próximo cliente da fila
            Cliente cliente = cadeirasEspera.poll();
            corteEmAndamento = true;
            System.out.println("Barbeiro chamou " + cliente.getNome() + " para corte");
            
            return cliente;
        } finally {
            lock.unlock();
        }
    }

    public void cortarCabelo(Cliente cliente) throws InterruptedException {
        lock.lock();
        try {
            // Simula o tempo de corte
            System.out.println("Barbeiro cortando cabelo de " + cliente.getNome());
            Thread.sleep(2000); // 2 segundos para cortar
            
            // Notifica que o corte terminou
            corteTerminado.signal();
            corteEmAndamento = false;
            clientesAtendidos++;
            
            System.out.println("Barbeiro terminou corte de " + cliente.getNome() + 
                             ". Total atendidos: " + clientesAtendidos);
        } finally {
            lock.unlock();
        }
    }

    public void esperarCorteTerminar(Cliente cliente) throws InterruptedException {
        lock.lock();
        try {
            while (corteEmAndamento) {
                corteTerminado.await();
            }
            System.out.println(cliente.getNome() + " saiu satisfeito da barbearia");
        } finally {
            lock.unlock();
        }
    }

    public int getClientesAtendidos() {
        return clientesAtendidos;
    }
}