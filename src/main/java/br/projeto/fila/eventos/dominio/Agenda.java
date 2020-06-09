package br.projeto.fila.eventos.dominio;

import br.projeto.fila.eventos.dominio.anotacoes.Adaptador;

import java.util.concurrent.TimeUnit;


@Adaptador
public interface Agenda {

    long tempo();

    /**
     * @return TimeUnit adaptado. Caso seja nulo, retornara TimeUNIT.NANOSECONDS
     */
    TimeUnit unidadeTempo();

}
