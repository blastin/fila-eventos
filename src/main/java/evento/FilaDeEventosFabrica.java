package evento;

public final class FilaDeEventosFabrica {

    private FilaDeEventosFabrica() {
    }

    public static FilaDeEventosDecorado construir(
            final LogEvento logEvento,
            final ExecutorTarefa executorTarefa,
            final Agenda agendaParaEventoNaoSucedido) {

        final ExecutorTarefa executorProxy = new ExecutorTarefaProxy(executorTarefa);

        final GerenciamentoDeFilaDeEventosLog log = new GerenciamentoDeFilaDeEventosLog(logEvento, executorProxy, agendaParaEventoNaoSucedido);

        final FilaDeEventosProxy filaDeEventosProxy = new FilaDeEventosProxy(log);

        return new FilaEventoDecorador(filaDeEventosProxy);

    }

}
