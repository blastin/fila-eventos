package dominio.evento;

import dominio.anotacoes.ClasseAberta;

@ClasseAberta
class ExecutorEventoNulo implements ExecutorEvento {

    @Override
    public void executar(final Runnable runnable) {
        // Comportamento para executar nulo
    }

    @Override
    public void agendar(final Runnable runnable, final Agenda agenda) {
        // Comportamento para agendar nulo
    }

}
