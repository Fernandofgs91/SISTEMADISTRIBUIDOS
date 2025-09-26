public class Barbeiro implements Runnable {
    private Barbearia barbearia;

    public Barbeiro(Barbearia barbearia) {
        this.barbearia = barbearia;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Espera por um cliente
                Cliente cliente = barbearia.proximoCliente();
                
                // Corta o cabelo do cliente
                barbearia.cortarCabelo(cliente);
                
                // Pequena pausa entre clientes
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            System.out.println("Barbeiro interrompido - hora de fechar!");
            Thread.currentThread().interrupt();
        }
    }
}