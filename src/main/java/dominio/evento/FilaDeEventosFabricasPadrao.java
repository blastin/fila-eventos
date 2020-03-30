package dominio.evento;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public final class FilaDeEventosFabricasPadrao implements FilaDeEventosFabrica {

    private final ExecutorEvento executorEventoProxy;
    private GerenciamentoDeFilaDeEventos gerenciamentoDeFilaDeEventos;
    private Agenda agendaParaEventoNaoSucedido;

    private FilaDeEventosFabricasPadrao(final ExecutorEvento executorEvento) {
        executorEventoProxy = new ExecutorEventoProxy(executorEvento);
        gerenciamentoDeFilaDeEventos = new GerenciamentoDeFilaDeEventosWrapper(executorEventoProxy);
        agendaParaEventoNaoSucedido = new AgendaNulo();
    }

    public static FilaDeEventosFabrica criar(final ExecutorEvento executorEvento) {
        final ExecutorEvento executorEventoSeguro = Optional.ofNullable(executorEvento).orElse(new ExecutorEventoNulo());
        return new FilaDeEventosFabricasPadrao(executorEventoSeguro);
    }

    @Override
    public FilaDeEventos construir() {
        gerenciamentoDeFilaDeEventos.adicionarAgenda(new AgendaProxy(agendaParaEventoNaoSucedido));
        return new FilaDeEventosProxy(gerenciamentoDeFilaDeEventos);
    }

    @Override
    public FilaDeEventosFabrica log(final LogEvento logEvento) {
        final LogEvento logEventoSeguro = Optional.ofNullable(logEvento).orElse(new LogEventoNulo());
        gerenciamentoDeFilaDeEventos = new GerenciamentoDeFilaDeEventosLog(executorEventoProxy, logEventoSeguro);
        return this;
    }

    @Override
    public FilaDeEventosFabrica agenda(final Agenda agendaParaEventoNaoSucedido) {
        this.agendaParaEventoNaoSucedido = Optional.ofNullable(agendaParaEventoNaoSucedido).orElse(new AgendaNulo());
        return this;
    }

    private static class GerenciamentoDeFilaDeEventosWrapper extends GerenciamentoDeFilaDeEventos {
        GerenciamentoDeFilaDeEventosWrapper(final ExecutorEvento executorEvento) {
            super(executorEvento);
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

    private static class AgendaNulo implements Agenda {

        @Override
        public long tempo() {
            return 0;
        }

        @Override
        public TimeUnit unidadeTempo() {
            return null;
        }
    }
}
