package br.projeto.fila.eventos.dominio;

import br.projeto.fila.eventos.dominio.anotacoes.Plugin;

@Plugin
public interface Evento<T> {

    void executar(final T objeto, final EventoMalSucedido<T> eventoMalSucedido) throws EventoException;

}
