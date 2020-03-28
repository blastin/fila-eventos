package evento;

import anotacoes.Adaptador;

import java.util.concurrent.TimeUnit;

@Adaptador
public interface Agenda {

    long tempo();

    TimeUnit unidadeTempo();

}
