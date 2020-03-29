package dominio.evento;

import dominio.anotacoes.Plugin;

@Plugin
final class GerenciamentoDeFilaDeEventosLog extends GerenciamentoDeFilaDeEventos {

    private final LogEvento log;

    GerenciamentoDeFilaDeEventosLog(final ExecutorTarefa executorTarefa, final LogEvento log) {
        super(executorTarefa);
        this.log = log;
    }

    @Override
    public <T> GerenciamentoDeFilaDeEventos dispara(final T objeto, final Evento<T> evento) {
        log.info("Despachando dominio.evento %s para objeto -> %s", evento, objeto);
        super.dispara(objeto, evento);
        return this;
    }

    @Override
    public <T> void eventoNaoExecutado(final T objeto, final Evento<T> evento) {
        log.error("Nao foi possivel finalizar execucao do dominio.evento %s com objeto : %s. Tentarei novamente", evento, objeto);
        super.eventoNaoExecutado(objeto, evento);
    }

}
