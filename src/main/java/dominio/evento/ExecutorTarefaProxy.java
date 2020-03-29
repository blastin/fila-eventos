package dominio.evento;

import dominio.anotacoes.Plugin;

@Plugin
final class ExecutorTarefaProxy implements ExecutorTarefa {

    private final ExecutorTarefa executorTarefa;

    ExecutorTarefaProxy(final ExecutorTarefa executorTarefa) {
        this.executorTarefa = executorTarefa;
    }

    @Override
    public void executar(final Runnable runnable) {
        executorTarefa.executar(runnable);
    }

    @Override
    public void agendar(final Runnable runnable, final Agenda agenda) {
        if (agenda == null) executar(runnable);
        else executorTarefa.agendar(runnable, agenda);
    }
}
