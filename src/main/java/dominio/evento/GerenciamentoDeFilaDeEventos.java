package dominio.evento;

abstract class GerenciamentoDeFilaDeEventos implements FilaDeEventos {

    private final ExecutorTarefa executorTarefa;
    private Agenda agendaParaEventoNaoSucedido;

    protected GerenciamentoDeFilaDeEventos(final ExecutorTarefa executorTarefa) {
        this.executorTarefa = executorTarefa;
    }

    @Override
    public <T> FilaDeEventos disparar(final T objeto, final Evento<T> evento) {
        return executar(objeto, evento, null);
    }

    final void adicionarAgenda(final Agenda agendaParaEventoNaoSucedido) {
        this.agendaParaEventoNaoSucedido = agendaParaEventoNaoSucedido;
    }

    protected <T> void eventoNaoExecutado(final T objeto, final Evento<T> evento) {
        executar(objeto, evento, agendaParaEventoNaoSucedido);
    }

    private <T> FilaDeEventos executar(final T objetoRecebido, final Evento<T> evento, final Agenda agenda) {
        executorTarefa
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
