package dominio.evento;

import dominio.anotacoes.ClasseAberta;

import java.util.concurrent.TimeUnit;

@ClasseAberta
public class FilaDeEventosFabricasImplementacao implements FilaDeEventosFabrica {

    private final ExecutorEvento executorEventoProxy;
    private GerenciamentoDeFilaDeEventos gerenciamentoDeFilaDeEventos;
    private Agenda agendaParaEventoNaoSucedido;
    private Agenda agendaParaDispararEvento;

    private FilaDeEventosFabricasImplementacao(final ExecutorEvento executorEvento) {
        executorEventoProxy = new ExecutorEventoProxy(executorEvento);
        gerenciamentoDeFilaDeEventos = new GerenciamentoDeFilaDeEventosWrapper(executorEventoProxy);
        agendaParaEventoNaoSucedido = new AgendaNulo();
        agendaParaDispararEvento = null;
    }

    public static FilaDeEventosFabrica criar(final ExecutorEvento executorEvento) {
        final ExecutorEvento executorEventoSeguro = executorEvento == null ? new ExecutorEventoNulo() : executorEvento;
        return new FilaDeEventosFabricasImplementacao(executorEventoSeguro);
    }

    @Override
    public FilaDeEventos construir() {

        gerenciamentoDeFilaDeEventos.adicionarAgendaParaEventoNaoSucedido(new AgendaProxy(agendaParaEventoNaoSucedido));

        gerenciamentoDeFilaDeEventos.adicionarAgendaParaDispararEvento(agendaParaDispararEvento);

        return new FilaDeEventosProxy(gerenciamentoDeFilaDeEventos);

    }

    @Override
    public FilaDeEventosFabrica log(final LogEvento logEvento) {
        final LogEvento logEventoSeguro = logEvento == null ? new LogEventoNulo() : logEvento;
        gerenciamentoDeFilaDeEventos = new GerenciamentoDeFilaDeEventosLog(executorEventoProxy, logEventoSeguro);
        return this;
    }

    @Override
    public FilaDeEventosFabrica agendaParaEventoNaoSucedido(final Agenda agendaParaEventoNaoSucedido) {
        this.agendaParaEventoNaoSucedido = agendaParaEventoNaoSucedido == null ? new AgendaNulo() : new AgendaNulo();
        return this;
    }

    @Override
    public FilaDeEventosFabrica agendaParaDispararEvento(final Agenda agendaParaDispararEvento) {
        if (agendaParaDispararEvento != null) {
            this.agendaParaDispararEvento = new AgendaProxy(agendaParaDispararEvento);
        }
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
