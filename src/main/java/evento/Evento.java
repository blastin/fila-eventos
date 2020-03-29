package evento;

import anotacoes.Plugin;

@Plugin
public interface Evento<T> {

    void executar(final T objeto, final EventoMalSucedido<T> eventoMalSucedido) throws EventoException;

}
