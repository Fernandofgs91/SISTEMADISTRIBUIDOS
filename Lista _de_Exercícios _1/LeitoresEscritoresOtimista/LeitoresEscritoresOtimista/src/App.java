import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

public class App {

    static class BancoDeDados {
        private int valor = 0;
        private final StampedLock lock = new StampedLock();

        // Escrita (exclusiva)
        public void escrever(int novoValor) {
            long stamp = lock.writeLock();
            try {
                System.out.printf("[%s] Escritor começando...%n", Thread.currentThread().getName());
                try { Thread.sleep(100); } catch (InterruptedException ignored) {}
                valor = novoValor;
                System.out.printf("[%s] Escritor atualizou valor para %d%n",
                        Thread.currentThread().getName(), valor);
            } finally {
                lock.unlockWrite(stamp);
            }
        }

        // Leitura otimista
        public int ler() {
            long stamp = lock.tryOptimisticRead();
            int v = valor;                      // leitura sem bloqueio
            try { Thread.sleep(50); } catch (InterruptedException ignored) {}

            if (!lock.validate(stamp)) {        // se houve escrita, validar falhou
                stamp = lock.readLock();        // cair para readLock garantido
                try {
                    v = valor;
                    System.out.printf("[%s] Leitor revalidou: valor=%d%n",
                            Thread.currentThread().getName(), v);
                } finally {
                    lock.unlockRead(stamp);
                }
            } else {
                System.out.printf("[%s] Leitor leu (otimista) valor=%d%n",
                        Thread.currentThread().getName(), v);
            }
            return v;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BancoDeDados bd = new BancoDeDados();
        ExecutorService pool = Executors.newFixedThreadPool(6);

        // leitores
        for (int i = 0; i < 4; i++) {
            pool.submit(() -> {
                for (int j = 0; j < 5; j++) {
                    bd.ler();
                    try { Thread.sleep(80); } catch (InterruptedException ignored) {}
                }
            });
        }

        // escritores
        for (int i = 0; i < 2; i++) {
            final int id = i;
            pool.submit(() -> {
                for (int j = 0; j < 3; j++) {
                    bd.escrever((id + 1) * 100 + j);
                    try { Thread.sleep(200); } catch (InterruptedException ignored) {}
                }
            });
        }

        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);
        System.out.println("Fim da simulação.");
    }
}
