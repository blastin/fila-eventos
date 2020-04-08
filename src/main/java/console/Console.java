package console;

import dominio.evento.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

final class Console {

    private static final LogEvento LOG_EVENTO = new LogTela();

    public static void main(String[] args) throws InterruptedException {

        final ScheduledExecutorService scheduledExecutorService =
                Executors
                        .newScheduledThreadPool(3);

        final ExecutorEvento executor = new ExecutorJava(scheduledExecutorService, LOG_EVENTO);

        final Agenda agendaParaCasoNaoSucesso = new AgendaParaConsole(8, TimeUnit.SECONDS);

        final Agenda agendaDisparoDeEvento = new AgendaParaConsole(4, TimeUnit.SECONDS);

        final FilaDeEventosFabrica filaDeEventosFabrica = FilaDeEventosFabricasImpl.criar(executor);

        final FilaDeEventos filaDeEventos =
                filaDeEventosFabrica
                        .log(LOG_EVENTO)
                        .agendaParaDispararEvento(agendaDisparoDeEvento)
                        .agendaParaEventoNaoSucedido(agendaParaCasoNaoSucesso)
                        .construir();

        new Console()
                .iniciar(filaDeEventos)
                .finalizar(scheduledExecutorService);

    }

    private Console iniciar(final FilaDeEventos filaDeEventos) {

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
                filaDeEventos.disparar("Nova tentativa", LOG_EVENTO::info);
            }

        };

        filaDeEventos
                .disparar("The Smiths", (objeto, vs) -> LOG_EVENTO.info(objeto, ""))
                .disparar("Legião Urbana", (objeto, eventoMalSucedido) -> {
                    LOG_EVENTO.info("Modo funcional");
                    LOG_EVENTO.info(objeto);
                })
                .disparar(2, escreverArquivo)
                .disparar("Doom", (objeto, eventoMalSucedido) -> System.out.println(objeto))
                .disparar("The Stones Roses", eventoTela)
                .disparar("Musica : I Wanna Be Adored", eventoTela);

        LOG_EVENTO.info("fim");

        return this;

    }

    private void finalizar(final ExecutorService executorService) throws InterruptedException {

        executorService.awaitTermination(20, TimeUnit.SECONDS);

        executorService.shutdown();

    }
}
