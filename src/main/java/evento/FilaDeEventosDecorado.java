package evento;

import java.util.Map;

public interface FilaDeEventosDecorado extends FilaDeEventos {

    <T> FilaDeEventos disparaTodos(final Map<T, Evento<T>> eventos);

}
