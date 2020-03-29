package dominio.evento;

import dominio.anotacoes.Plugin;

@Plugin
public interface Evento<T> {

    void executar(final T objeto, final EventoMalSucedido<T> eventoMalSucedido) throws EventoException;

}
