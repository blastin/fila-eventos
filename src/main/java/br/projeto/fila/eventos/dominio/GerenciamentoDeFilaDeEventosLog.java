package br.projeto.fila.eventos.dominio;

import br.projeto.fila.eventos.dominio.anotacoes.Plugin;

@Plugin
final class GerenciamentoDeFilaDeEventosLog extends GerenciamentoDeFilaDeEventos {

    private final LogEvento log;

    GerenciamentoDeFilaDeEventosLog(final ExecutorEvento executorEvento, final LogEvento log) {
        super(executorEvento);
        this.log = log;
    }

    @Override
    public <T> FilaDeEventos disparar(final T objeto, final Evento<T> evento) {
        log.info("Despachando evento <{}> com objeto <{}>", evento, objeto);
        return super.disparar(objeto, evento);
    }

    @Override
    protected <T> void eventoNaoExecutado(final T objeto, final Evento<T> evento) {
        log.error("Nao foi possivel finalizar execucao do br.projeto.eventos.dominio.evento {} com objeto : {}. Tentarei novamente", evento, objeto);
        super.eventoNaoExecutado(objeto, evento);
    }

}
