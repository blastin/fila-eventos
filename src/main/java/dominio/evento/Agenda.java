package dominio.evento;

import dominio.anotacoes.Adaptador;

import java.util.concurrent.TimeUnit;


@Adaptador
public interface Agenda {

    long tempo();

    /**
     * @return TimeUnit adaptado. Caso seja nulo, retornara TimeUNIT.NANOSeconds
     */
    TimeUnit unidadeTempo();

}
