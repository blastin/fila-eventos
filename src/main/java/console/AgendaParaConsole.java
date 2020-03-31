package console;

import dominio.evento.Agenda;

import java.util.concurrent.TimeUnit;

final class AgendaParaConsole implements Agenda {

    private final long valorTempoParaIniciar;

    private final TimeUnit unidade;

    AgendaParaConsole(final long valorTempoParaIniciar, final TimeUnit unidade) {
        this.valorTempoParaIniciar = valorTempoParaIniciar;
        this.unidade = unidade;
    }

    @Override
    public long tempo() {
        return valorTempoParaIniciar;
    }

    @Override
    public TimeUnit unidadeTempo() {
        return unidade;
    }

}
