package br.projeto.fila.eventos.dominio;

@FunctionalInterface
public interface FilaDeEventos {

    <T> FilaDeEventos disparar(T objeto, Evento<T> evento);

}
