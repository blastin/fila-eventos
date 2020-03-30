package dominio.evento;

final class ExecutorParaTeste implements ExecutorEvento {

    private Thread thread;

    private boolean execucaoRecebida;

    private boolean agendaRecebida;

    @Override
    public void executar(final Runnable runnable) {
        execucaoRecebida = true;
        thread = new Thread(runnable);
        thread.start();
    }

    @Override
    public void agendar(final Runnable runnable, final Agenda agenda) {
        try {
            agendaRecebida = true;
            agenda.unidadeTempo().sleep(agenda.tempo());
            executar(runnable);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    void esperarFinalizar() throws InterruptedException {
        thread.join();
    }

    boolean agendaFoiRecebida() {
        return agendaRecebida;
    }

    boolean execucaoFoiRecebida() {
        return execucaoRecebida;
    }

}
