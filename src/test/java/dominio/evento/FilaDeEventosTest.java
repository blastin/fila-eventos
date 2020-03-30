package dominio.evento;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

final class FilaDeEventosTest {

    private final AgendaParaTeste agendaParaEventoNaoSucedido = new AgendaParaTeste(1, TimeUnit.MILLISECONDS);

    private final LogParaTeste logParaTeste = new LogParaTeste();

    @Test
    @DisplayName("Disparar evento gladiador com muita honra")
    public void dispararEventoParaGladiadorComMuitaHonra() throws InterruptedException {

        final ExecutorParaTeste executorTarefa = new ExecutorParaTeste();

        final FilaDeEventos filaDeEventos =
                FilaDeEventosFabricasPadrao
                        .criar(executorTarefa)
                        .agenda(agendaParaEventoNaoSucedido)
                        .log(logParaTeste)
                        .construir();

        final Gladiador gladiador = new Gladiador(1, 100, 100);

        final EventoChamadaGladiador eventoChamadaGladiador = new EventoChamadaGladiador(logParaTeste);

        filaDeEventos.disparar(gladiador, eventoChamadaGladiador);

        executorTarefa
                .esperarFinalizar();

        Assertions.assertTrue(eventoChamadaGladiador.chamadoRecebido(), "evento deve ter sido chamado");

        Assertions.assertTrue(executorTarefa.execucaoFoiRecebida());
        Assertions.assertFalse(executorTarefa.agendaFoiRecebida());

    }

    @Test
    @DisplayName("Disparar evento gladiador com pouca honra")
    public void dispararEventoGladiadorComPoucaHonra() throws InterruptedException {

        final ExecutorParaTeste executorTarefa = new ExecutorParaTeste();

        final FilaDeEventos filaDeEventos =
                FilaDeEventosFabricasPadrao
                        .criar(executorTarefa)
                        .agenda(agendaParaEventoNaoSucedido)
                        .log(logParaTeste)
                        .construir();

        final Gladiador gladiador = new Gladiador(1, 10, 100);

        final EventoChamadaGladiador eventoChamadaGladiador = new EventoChamadaGladiador(logParaTeste);

        filaDeEventos.disparar(gladiador, eventoChamadaGladiador);

        executorTarefa
                .esperarFinalizar();

        Assertions.assertTrue(eventoChamadaGladiador.chamadoRecebido(), "evento deve ter sido chamado");

        Assertions.assertTrue(executorTarefa.execucaoFoiRecebida());
        Assertions.assertTrue(executorTarefa.agendaFoiRecebida());

    }

    @Test
    @DisplayName("Disparar evento sem agendamento")
    public void dispararEventoSemAgendamento() throws InterruptedException {

        final ExecutorParaTeste executorTarefa = new ExecutorParaTeste();

        final FilaDeEventos filaDeEventos =
                FilaDeEventosFabricasPadrao
                        .criar(executorTarefa)
                        .log(logParaTeste)
                        .construir();

        final String mensagem = "Sem Agendamento";

        final EventoChecagem eventoChecagem = new EventoChecagem(logParaTeste);

        filaDeEventos
                .disparar(mensagem, eventoChecagem);

        executorTarefa.esperarFinalizar();

        Assertions.assertTrue(eventoChecagem.chamadoRecebido());
        Assertions.assertEquals(mensagem, eventoChecagem.objetoRecebido());


        Assertions.assertTrue(executorTarefa.execucaoFoiRecebida());
        Assertions.assertFalse(executorTarefa.agendaFoiRecebida());

    }

    @Test
    @DisplayName("Disparar evento sem log")
    public void dispararEventoSemLog() throws InterruptedException {

        final ExecutorParaTeste executorTarefa = new ExecutorParaTeste();

        final FilaDeEventos filaDeEventos =
                FilaDeEventosFabricasPadrao
                        .criar(executorTarefa)
                        .construir();

        final String mensagem = "Sem log";

        final EventoChecagem eventoChecagem = new EventoChecagem(logParaTeste);

        filaDeEventos
                .disparar(mensagem, eventoChecagem);

        executorTarefa.esperarFinalizar();

        Assertions.assertEquals(mensagem, eventoChecagem.objetoRecebido());
        Assertions.assertEquals(mensagem, eventoChecagem.objetoRecebido());

        Assertions.assertTrue(eventoChecagem.chamadoRecebido());

        Assertions.assertTrue(executorTarefa.execucaoFoiRecebida());
        Assertions.assertFalse(executorTarefa.agendaFoiRecebida());

    }

    @Test
    @DisplayName("Disparar evento com log nulo")
    public void dispararEventoComLogNulo() throws InterruptedException {

        final ExecutorParaTeste executorTarefa = new ExecutorParaTeste();

        final FilaDeEventos filaDeEventos =
                FilaDeEventosFabricasPadrao
                        .criar(executorTarefa)
                        .log(null)
                        .agenda(agendaParaEventoNaoSucedido)
                        .construir();

        final String mensagem = "";

        final EventoChecagem eventoChecagem = new EventoChecagem(logParaTeste);

        filaDeEventos
                .disparar(mensagem, eventoChecagem);

        executorTarefa.esperarFinalizar();

        Assertions.assertTrue(eventoChecagem.chamadoRecebido());
        Assertions.assertNotEquals(mensagem, eventoChecagem.objetoRecebido());

        Assertions.assertTrue(executorTarefa.execucaoFoiRecebida());
        Assertions.assertTrue(executorTarefa.agendaFoiRecebida());

    }

    @Test
    @DisplayName("Disparar evento sem executor")
    public void dispararEventoSemExecutor() throws InterruptedException {

        final FilaDeEventos filaDeEventos =
                FilaDeEventosFabricasPadrao
                        .criar(null)
                        .log(logParaTeste)
                        .construir();

        final String mensagem = "Sem executor";

        final EventoChecagem eventoChecagem = new EventoChecagem(logParaTeste);

        filaDeEventos
                .disparar(mensagem, eventoChecagem);

        TimeUnit.MILLISECONDS.sleep(50);

        Assertions.assertFalse(eventoChecagem.chamadoRecebido());
        Assertions.assertNull(eventoChecagem.objetoRecebido());

    }

    @Test
    @DisplayName("Disparar evento sem executor e com agenda")
    public void dispararEventoSemExecutorComAgenda() throws InterruptedException {

        final FilaDeEventos filaDeEventos =
                FilaDeEventosFabricasPadrao
                        .criar(null)
                        .agenda(agendaParaEventoNaoSucedido)
                        .log(logParaTeste)
                        .construir();

        final EventoChecagem eventoChecagem = new EventoChecagem(logParaTeste);

        final String mensagem = "";

        filaDeEventos
                .disparar(mensagem, eventoChecagem);

        TimeUnit.MILLISECONDS.sleep(50);

        Assertions.assertFalse(eventoChecagem.chamadoRecebido());
        Assertions.assertNull(eventoChecagem.objetoRecebido());

    }

    @Test
    @DisplayName("Disparar evento com executor e sem agenda")
    public void dispararEventoComExecutorSemAgenda() throws InterruptedException {

        final ExecutorParaTeste executorTarefa = new ExecutorParaTeste();

        final FilaDeEventos filaDeEventos =
                FilaDeEventosFabricasPadrao
                        .criar(executorTarefa)
                        .log(logParaTeste)
                        .construir();

        final EventoChecagem eventoChecagem = new EventoChecagem(logParaTeste);

        final String mensagem = "";

        filaDeEventos
                .disparar(mensagem, eventoChecagem);

        executorTarefa.esperarFinalizar();

        Assertions.assertTrue(eventoChecagem.chamadoRecebido());
        Assertions.assertNotEquals(mensagem, eventoChecagem.objetoRecebido());

        Assertions.assertTrue(executorTarefa.execucaoFoiRecebida());
        Assertions.assertTrue(executorTarefa.agendaFoiRecebida());

    }

    @Test
    @DisplayName("Disparar evento com executor e agenda nula")
    public void dispararEventoComExecutorAgendaNula() throws InterruptedException {

        final ExecutorParaTeste executorTarefa = new ExecutorParaTeste();

        final FilaDeEventos filaDeEventos =
                FilaDeEventosFabricasPadrao
                        .criar(executorTarefa)
                        .log(logParaTeste)
                        .agenda(null)
                        .construir();

        final EventoChecagem eventoChecagem = new EventoChecagem(logParaTeste);

        final String mensagem = "";

        filaDeEventos
                .disparar(mensagem, eventoChecagem);

        executorTarefa.esperarFinalizar();

        Assertions.assertTrue(eventoChecagem.chamadoRecebido());
        Assertions.assertNotEquals(mensagem, eventoChecagem.objetoRecebido());

        Assertions.assertTrue(executorTarefa.execucaoFoiRecebida());
        Assertions.assertTrue(executorTarefa.agendaFoiRecebida());

    }

    @Test
    @DisplayName("Disparar evento com objeto nulo")
    public void dispararEventoComObjetoNulo() {

        final FilaDeEventos filaDeEventos =
                FilaDeEventosFabricasPadrao
                        .criar(null)
                        .agenda(agendaParaEventoNaoSucedido)
                        .log(logParaTeste)
                        .construir();

        final EventoChecagem eventoChecagem = new EventoChecagem(logParaTeste);

        Assertions.assertThrows(EventoException.class, () -> filaDeEventos
                .disparar(null, eventoChecagem));

    }

    @Test
    @DisplayName("Disparar evento com evento nulo")
    public void dispararEventoComEventoNulo() {

        final FilaDeEventos filaDeEventos =
                FilaDeEventosFabricasPadrao
                        .criar(null)
                        .agenda(agendaParaEventoNaoSucedido)
                        .log(logParaTeste)
                        .construir();

        Assertions.assertThrows(EventoException.class, () -> filaDeEventos
                .disparar("evento nulo", null));

    }

    @Test
    @DisplayName("Chamada de executor de eventos nulo")
    public void realizarChamadaDeExecucaoEAgendamentoEmExecutorNulo() {

        AtomicBoolean executar = new AtomicBoolean();

        AtomicBoolean agendar = new AtomicBoolean();

        final ExecutorEvento executorNulo = new ExecutorEventoNulo() {
            @Override
            public void executar(final Runnable runnable) {
                executar.set(true);
                super.executar(runnable);
                runnable.run();
            }

            @Override
            public void agendar(final Runnable runnable, final Agenda agenda) {
                agendar.set(true);
                super.agendar(runnable, agenda);
                runnable.run();
            }
        };

        final FilaDeEventos filaDeEventos =
                FilaDeEventosFabricasPadrao
                        .criar(executorNulo)
                        .agenda(agendaParaEventoNaoSucedido)
                        .log(logParaTeste)
                        .construir();

        final EventoChecagem eventoChecagem = new EventoChecagem(logParaTeste);

        final String mensagem = "";

        filaDeEventos
                .disparar(mensagem, eventoChecagem);

        Assertions.assertTrue(eventoChecagem.chamadoRecebido());

        Assertions.assertNotEquals(mensagem, eventoChecagem.objetoRecebido());

        Assertions.assertTrue(executar.get());
        Assertions.assertTrue(agendar.get());

    }
}
