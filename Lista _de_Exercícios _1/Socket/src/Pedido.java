import java.io.Serializable;

public class Pedido implements Serializable {
    private static final long serialVersionUID = 1L;

    private String remetente;
    private String mensagem;

    public Pedido(String remetente, String mensagem) {
        this.remetente = remetente;
        this.mensagem = mensagem;
    }

    public String getRemetente() {
        return remetente;
    }

    public String getMensagem() {
        return mensagem;
    }

    @Override
    public String toString() {
        return remetente + ": " + mensagem;
    }
}
