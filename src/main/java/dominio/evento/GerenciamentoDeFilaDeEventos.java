package dominio.evento;

abstract class GerenciamentoDeFilaDeEventos implements FilaDeEventos {

    private final ExecutorEvento executorEvento;

    private Agenda agendaParaEventoNaoSucedido;
    private Agenda agendaParaDispararEvento;

    protected GerenciamentoDeFilaDeEventos(final ExecutorEvento executorEvento) {
        this.executorEvento = executorEvento;
    }

    @Override
    public <T> FilaDeEventos disparar(final T objeto, final Evento<T> evento) {
        return executar(objeto, evento, agendaParaDispararEvento);
    }

    final void adicionarAgendaParaEventoNaoSucedido(final Agenda agendaParaEventoNaoSucedido) {
        this.agendaParaEventoNaoSucedido = agendaParaEventoNaoSucedido;
    }

    final void adicionarAgendaParaDispararEvento(final Agenda agendaParaDispararEvento) {
        this.agendaParaDispararEvento = agendaParaDispararEvento;
    }

    protected <T> void eventoNaoExecutado(final T objeto, final Evento<T> evento) {
        executar(objeto, evento, agendaParaEventoNaoSucedido);
    }

    private <T> FilaDeEventos executar(final T objetoRecebido, final Evento<T> evento, final Agenda agenda) {
        executorEvento
                .agendar(
                        () -> evento
                                .executar(
                                        objetoRecebido,
                                        objetoAtual -> eventoNaoExecutado(objetos(objetoRecebido, objetoAtual), evento)
                                ),
                        agenda
                );
        return this;
    }

    private static <T> T objetos(final T objetoRecebido, final T objetoAtual) {
        return objetoAtual == null ? objetoRecebido : objetoAtual;
    }

}
