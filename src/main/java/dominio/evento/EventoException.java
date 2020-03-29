package dominio.evento;

public final class EventoException extends RuntimeException {

    private static final long serialVersionUID = -2897288072515366523L;

    public EventoException(final String mensagem) {
        super(mensagem);
    }

}
