package br.projeto.fila.eventos.dominio;

import br.projeto.fila.eventos.dominio.anotacoes.Plugin;

@Plugin
final class ExecutorEventoProxy implements ExecutorEvento {

    private final ExecutorEvento executorEvento;

    ExecutorEventoProxy(final ExecutorEvento executorEvento) {
        this.executorEvento = executorEvento;
    }

    @Override
    public void executar(final Runnable runnable) {
        executorEvento.executar(runnable);
    }

    @Override
    public void agendar(final Runnable runnable, final Agenda agenda) {
        if (agenda == null) executar(runnable);
        else executorEvento.agendar(runnable, agenda);
    }
}
