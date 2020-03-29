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
        return new FilaDeEventosFabricasPadrao(executorTarefa);
    }

    @Override
    public FilaDeEventos construir() {
        gerenciamentoDeFilaDeEventos.adicionarAgenda(agendaParaEventoNaoSucedido);
        return new FilaDeEventosProxy(gerenciamentoDeFilaDeEventos);
    }

    @Override
    public FilaDeEventosFabrica log(final LogEvento logEvento) {
        gerenciamentoDeFilaDeEventos = new GerenciamentoDeFilaDeEventosLog(executorTarefaProxy, logEvento);
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

}
