package dominio.evento;

@FunctionalInterface
public interface FilaDeEventos {

    <T> FilaDeEventos disparar(T objeto, Evento<T> evento);

}
