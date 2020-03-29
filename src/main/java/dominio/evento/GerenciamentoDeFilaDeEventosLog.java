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
    public <T> FilaDeEventos dispara(final T objeto, final Evento<T> evento) {
        log.info("Despachando evento <{}> com objeto <{}>", evento, objeto);
        return super.dispara(objeto, evento);
    }

    @Override
    protected <T> void eventoNaoExecutado(final T objeto, final Evento<T> evento) {
        log.error("Nao foi possivel finalizar execucao do dominio.evento {} com objeto : {}. Tentarei novamente", evento, objeto);
        super.eventoNaoExecutado(objeto, evento);
    }

}
