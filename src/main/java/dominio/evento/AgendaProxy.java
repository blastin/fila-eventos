package dominio.evento;

import java.util.concurrent.TimeUnit;

final class AgendaProxy implements Agenda {

    private final Agenda agenda;

    AgendaProxy(final Agenda agenda) {
        this.agenda = agenda;
    }

    @Override
    public long tempo() {
        return agenda.tempo();
    }

    @Override
    public TimeUnit unidadeTempo() {
        final TimeUnit unidade = agenda.unidadeTempo();
        return unidade == null ? TimeUnit.NANOSECONDS : unidade;
    }

}
