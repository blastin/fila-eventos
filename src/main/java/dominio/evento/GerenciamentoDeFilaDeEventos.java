package dominio.evento;

import java.util.Optional;

abstract class GerenciamentoDeFilaDeEventos implements FilaDeEventos {

    private final ExecutorTarefa executorTarefa;

    private Agenda agendaParaEventoNaoSucedido;

    static final AgendaNula AGENDA_NULA = new AgendaNula();

    protected GerenciamentoDeFilaDeEventos(final ExecutorTarefa executorTarefa) {
        this.executorTarefa = executorTarefa;
        agendaParaEventoNaoSucedido = AGENDA_NULA;
    }

    @Override
    public <T> FilaDeEventos dispara(final T objeto, final Evento<T> evento) {
        return executar(objeto, evento, null);
    }

    final void adicionarAgenda(final Agenda agendaParaEventoNaoSucedido) {
        this.agendaParaEventoNaoSucedido = agendaParaEventoNaoSucedido;
    }

    protected <T> void eventoNaoExecutado(final T objeto, final Evento<T> evento) {
        executar(objeto, evento, agendaParaEventoNaoSucedido);
    }

    private <T> FilaDeEventos executar(final T objeto, final Evento<T> evento, final Agenda agenda) {
        executorTarefa
                .agendar(
                        () -> evento
                                .executar(
                                        objeto,
                                        o -> eventoNaoExecutado(Optional.ofNullable(o).orElse(objeto), evento)
                                ),
                        agenda
                );
        return this;
    }

}
