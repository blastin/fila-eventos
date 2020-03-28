package evento;

final class GerenciamentoDeFilaDeEventosLog extends GerenciamentoDeFilaDeEventos {

    private final LogEvento log;

    GerenciamentoDeFilaDeEventosLog(final LogEvento log, final ExecutorTarefa executorTarefa, final Agenda agenda) {
        super(executorTarefa, agenda);
        this.log = log;
    }

    @Override
    public <T> GerenciamentoDeFilaDeEventos dispara(final T objeto, final Evento<T> evento) {
        log.info("Despachando evento %s para objeto -> %s", evento, objeto);
        super.dispara(objeto, evento);
        return this;
    }

    @Override
    protected <T> void eventoNaoExecutado(final T objeto, final Evento<T> evento) {
        log.error("Nao foi possivel finalizar execucao do evento %s com objeto : %s. Tentarei novamente", evento, objeto);
        super.eventoNaoExecutado(objeto, evento);
    }

}
