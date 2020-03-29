import evento.Agenda;
import evento.ExecutorTarefa;
import evento.LogEvento;

import java.util.concurrent.ScheduledExecutorService;

final class ExecutorJava implements ExecutorTarefa {

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
