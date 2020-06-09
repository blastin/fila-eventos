package br.projeto.fila.eventos.console;

import br.projeto.fila.eventos.dominio.Agenda;
import br.projeto.fila.eventos.dominio.ExecutorEvento;
import br.projeto.fila.eventos.dominio.LogEvento;

import java.util.concurrent.ScheduledExecutorService;

final class ExecutorJava implements ExecutorEvento {

    private final ScheduledExecutorService executor;
    private final LogEvento logEvento;

    ExecutorJava(final ScheduledExecutorService executor, final LogEvento logEvento) {
        this.executor = executor;
        this.logEvento = logEvento;
    }

    @Override
    public void executar(final Runnable runnable) {
        logEvento.info("executando");
        executor.execute(runnable);
    }

    @Override
    public void agendar(final Runnable runnable, final Agenda agenda) {
        logEvento.info("agendando");
        executor.schedule(runnable, agenda.tempo(), agenda.unidadeTempo());
    }

}
