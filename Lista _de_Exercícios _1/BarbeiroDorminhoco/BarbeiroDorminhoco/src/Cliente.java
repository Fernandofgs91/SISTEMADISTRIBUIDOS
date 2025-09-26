public class Cliente implements Runnable {
    private String nome;
    private Barbearia barbearia;

    public Cliente(String nome, Barbearia barbearia) {
        this.nome = nome;
        this.barbearia = barbearia;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public void run() {
        try {
            // Tenta entrar na barbearia
            if (barbearia.entrarBarbearia(this)) {
                // Se conseguiu entrar, espera o corte terminar
                barbearia.esperarCorteTerminar(this);
            }
        } catch (InterruptedException e) {
            System.out.println(nome + " foi interrompido");
            Thread.currentThread().interrupt();
        }
    }
}