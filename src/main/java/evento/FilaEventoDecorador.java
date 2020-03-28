package evento;

import java.util.Map;

final class FilaEventoDecorador implements FilaDeEventosDecorado {

    private final FilaDeEventos filaDeEventos;

    FilaEventoDecorador(final FilaDeEventos filaDeEventos) {
        this.filaDeEventos = filaDeEventos;
    }

    @Override
    public <T> FilaDeEventos dispara(final T objeto, final Evento<T> evento) {
        return filaDeEventos.dispara(objeto, evento);
    }

    @Override
    public <T> FilaDeEventos disparaTodos(final Map<T, Evento<T>> eventos) {
        eventos.forEach(this::dispara);
        return this;
    }

}
