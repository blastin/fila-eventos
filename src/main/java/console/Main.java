package console;

import dominio.evento.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Main {

    private final static LogEvento LOG_EVENTO = new LogTela();

    public static void main(String[] args) throws InterruptedException {

        final ScheduledExecutorService scheduledExecutorService =
                Executors
                        .newScheduledThreadPool(3);

        final ExecutorEvento executor = new ExecutorJava(scheduledExecutorService, LOG_EVENTO);

        final Agenda agendaParaCasoNaoSucesso = new AgendaParaCasoNaoSucesso(2, TimeUnit.SECONDS);

        final FilaDeEventosFabrica filaDeEventosFabrica = FilaDeEventosFabricasPadrao.criar(executor);

        final FilaDeEventos filaDeEventos =
                filaDeEventosFabrica
                        .log(LOG_EVENTO)
                        .agenda(agendaParaCasoNaoSucesso)
                        .construir();

        new Main()
                .iniciar(filaDeEventos)
                .finalizar(scheduledExecutorService);

    }

    private Main iniciar(final FilaDeEventos filaDeEventos) {

        final EscreverArquivo escreverArquivo = new EscreverArquivo(LOG_EVENTO);

        final AtomicInteger atomicInteger = new AtomicInteger(0);

        final Evento<String> eventoTela = (objeto, eventoMalSucedido) -> {

            LOG_EVENTO.info("INICIANDO");

            IntStream
                    .range(0, 3)
                    .forEach(value -> LOG_EVENTO.info(objeto));

            final int i = atomicInteger.getAndAdd(1);

            if (i <= 3) {
                LOG_EVENTO.info("entrei uma vez pois {}", i);
                eventoMalSucedido.notificar(objeto.concat(String.valueOf(i)));
                filaDeEventos.disparar("Tentanto novamente", LOG_EVENTO::info);
            }

        };

        filaDeEventos
                .disparar("The Smiths", eventoTela)
                .disparar("Legiao Urbana", eventoTela)
                .disparar(2, escreverArquivo)
                .disparar("Doom", eventoTela)
                .disparar("The Stones Roses", eventoTela)
                .disparar("Musica : I Wanna Be Adored", eventoTela);

        LOG_EVENTO.info("fim");

        return this;

    }

    private void finalizar(final ExecutorService executorService) throws InterruptedException {

        executorService.awaitTermination(15, TimeUnit.SECONDS);

        executorService.shutdown();

    }
}
