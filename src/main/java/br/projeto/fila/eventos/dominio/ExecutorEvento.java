package br.projeto.fila.eventos.dominio;

import br.projeto.fila.eventos.dominio.anotacoes.Plugin;

@Plugin
public interface ExecutorEvento {

    void executar(final Runnable runnable);

    void agendar(final Runnable runnable, final Agenda agenda);
}
