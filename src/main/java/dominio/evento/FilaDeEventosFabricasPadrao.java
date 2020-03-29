package dominio.evento;

import java.util.Optional;

public final class FilaDeEventosFabricasPadrao implements FilaDeEventosFabrica {

    private final ExecutorTarefa executorTarefaProxy;
    private GerenciamentoDeFilaDeEventos gerenciamentoDeFilaDeEventos;
    private Agenda agendaParaEventoNaoSucedido;

    private FilaDeEventosFabricasPadrao(final ExecutorTarefa executorTarefa) {
        executorTarefaProxy = new ExecutorTarefaProxy(executorTarefa);
        gerenciamentoDeFilaDeEventos = new GerenciamentoDeFilaDeEventosWrapper(executorTarefaProxy);
    }

    public static FilaDeEventosFabrica criar(final ExecutorTarefa executorTarefa) {
        final ExecutorTarefa executorTarefaSeguro = Optional.ofNullable(executorTarefa).orElse(new ExecutorTarefaNulo());
        return new FilaDeEventosFabricasPadrao(executorTarefaSeguro);
    }

    @Override
    public FilaDeEventos construir() {
        gerenciamentoDeFilaDeEventos.adicionarAgenda(agendaParaEventoNaoSucedido);
        return new FilaDeEventosProxy(gerenciamentoDeFilaDeEventos);
    }

    @Override
    public FilaDeEventosFabrica log(final LogEvento logEvento) {
        final LogEvento logEventoSeguro = Optional.ofNullable(logEvento).orElse(new LogEventoNulo());
        gerenciamentoDeFilaDeEventos = new GerenciamentoDeFilaDeEventosLog(executorTarefaProxy, logEventoSeguro);
        return this;
    }

    @Override
    public FilaDeEventosFabrica agenda(final Agenda agendaParaEventoNaoSucedido) {
        this.agendaParaEventoNaoSucedido =
                Optional
                        .ofNullable(agendaParaEventoNaoSucedido)
                        .orElse(GerenciamentoDeFilaDeEventos.AGENDA_NULA);
        return this;
    }

    private static final class GerenciamentoDeFilaDeEventosWrapper extends GerenciamentoDeFilaDeEventos {
        GerenciamentoDeFilaDeEventosWrapper(final ExecutorTarefa executorTarefa) {
            super(executorTarefa);
        }
    }

    private static class LogEventoNulo implements LogEvento {

        @Override
        public void info(final String mensagem, final Object... argumentos) {
        }

        @Override
        public void error(final String mensagem, final Object... argumentos) {
        }

    }

    private static class ExecutorTarefaNulo implements ExecutorTarefa {

        @Override
        public void executar(final Runnable runnable) {
        }

        @Override
        public void agendar(final Runnable runnable, final Agenda agenda) {
        }

    }

}
