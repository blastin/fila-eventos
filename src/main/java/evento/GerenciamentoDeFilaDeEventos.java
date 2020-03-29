package evento;

import java.util.Optional;

abstract class GerenciamentoDeFilaDeEventos implements FilaDeEventos {

    private final ExecutorTarefa executorTarefa;

    private final Agenda agendaParaEventoNaoSucedido;

    protected GerenciamentoDeFilaDeEventos(final ExecutorTarefa executorTarefa, final Agenda agendaParaEventoNaoSucedido) {
        this.executorTarefa = executorTarefa;
        this.agendaParaEventoNaoSucedido = agendaParaEventoNaoSucedido;
    }

    @Override
    public <T> FilaDeEventos dispara(final T objeto, final Evento<T> evento) {
        return executar(objeto, evento, null);
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

    protected <T> void eventoNaoExecutado(final T objeto, final Evento<T> evento) {
        executar(objeto, evento, agendaParaEventoNaoSucedido);
    }

}
