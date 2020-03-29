package dominio.evento;

@FunctionalInterface
public interface FilaDeEventos {

    <T> FilaDeEventos dispara(T objeto, Evento<T> evento);

}
