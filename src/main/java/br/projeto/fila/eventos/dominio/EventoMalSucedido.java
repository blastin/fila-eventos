package br.projeto.fila.eventos.dominio;

@FunctionalInterface
public interface EventoMalSucedido<T> {

    void notificar(final T t);

}
