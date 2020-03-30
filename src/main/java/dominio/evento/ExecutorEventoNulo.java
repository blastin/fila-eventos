package dominio.evento;

class ExecutorEventoNulo implements ExecutorEvento {

    @Override
    public void executar(final Runnable runnable) {
    }

    @Override
    public void agendar(final Runnable runnable, final Agenda agenda) {
    }

}
