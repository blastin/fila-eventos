package dominio.evento;

@FunctionalInterface
public interface EventoMalSucedido<T> {

    void notificar(final T t);

}
