
import evento.*;

import java.util.Map;
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

        final ExecutorTarefa executor = new ExecutorJava(scheduledExecutorService, LOG_EVENTO);

        final Agenda agendaParaCasoNaoSucesso = new AgendaParaCasoNaoSucesso(10, TimeUnit.SECONDS);

        final FilaDeEventosDecorado filaDeEventos =
                FilaDeEventosFabrica
                        .construir(LOG_EVENTO, executor, agendaParaCasoNaoSucesso);

        new Main()
                .iniciar(filaDeEventos)
                .finalizar(scheduledExecutorService);

    }

    public Main iniciar(final FilaDeEventosDecorado filaDeEventos) {

        final EscreverArquivo escreverArquivo = new EscreverArquivo(LOG_EVENTO);

        final AtomicInteger atomicInteger = new AtomicInteger(0);

        final Evento<String> eventoTela = (objeto, eventoMalSucedido) -> {

            LOG_EVENTO.info("INICIANDO");

            IntStream
                    .range(0, 3)
                    .forEach(value -> LOG_EVENTO.info(objeto));

            final int i = atomicInteger.getAndAdd(1);

            if (i <= 3) {
                LOG_EVENTO.info("entrei uma vez pois %d", i);
                eventoMalSucedido.notificar(objeto);
                filaDeEventos.dispara("Tentanto novamente", LOG_EVENTO::info);
            }

        };

        filaDeEventos
                .disparaTodos(
                        Map.of(
                                "Oasis", eventoTela,
                                "The Smiths", eventoTela,
                                "Legiao Urbana", eventoTela))
                .dispara(2, escreverArquivo)
                .dispara("Doom", eventoTela);

        LOG_EVENTO.info("fim");

        return this;

    }

    public void finalizar(final ExecutorService executorService) throws InterruptedException {

        executorService.awaitTermination(35, TimeUnit.SECONDS);

        executorService.shutdown();

    }
}
