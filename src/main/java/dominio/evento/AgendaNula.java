package dominio.evento;

import java.util.concurrent.TimeUnit;

final class AgendaNula implements Agenda {

    @Override
    public long tempo() {
        return 0;
    }

    @Override
    public TimeUnit unidadeTempo() {
        return TimeUnit.NANOSECONDS;
    }

}
