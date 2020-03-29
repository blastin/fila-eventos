package dominio.evento;

final class FilaDeEventosProxy implements FilaDeEventos {

    private final GerenciamentoDeFilaDeEventos gerenciamentoDeFilaDeEventos;

    protected FilaDeEventosProxy(final GerenciamentoDeFilaDeEventos gerenciamentoDeFilaDeEventos) {
        this.gerenciamentoDeFilaDeEventos = gerenciamentoDeFilaDeEventos;
    }

    @Override
    public <T> FilaDeEventos dispara(final T objeto, final Evento<T> evento) {

        if (objeto == null || evento == null) {
            throw new EventoException("Objeto e/ou dominio.evento nulo");
        }

        return gerenciamentoDeFilaDeEventos.dispara(objeto, evento);

    }
}
