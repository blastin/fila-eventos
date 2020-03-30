package dominio.evento;

import java.util.concurrent.TimeUnit;

final class AgendaParaTeste implements Agenda {

    private final long valorTempo;
    private final TimeUnit unidade;

    AgendaParaTeste(final long valorTempo, final TimeUnit unidade) {
        this.valorTempo = valorTempo;
        this.unidade = unidade;
    }

    @Override
    public long tempo() {
        return valorTempo;
    }

    @Override
    public TimeUnit unidadeTempo() {
        return unidade;
    }
}
