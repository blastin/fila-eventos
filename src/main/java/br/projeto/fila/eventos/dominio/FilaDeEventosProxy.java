package br.projeto.fila.eventos.dominio;

final class FilaDeEventosProxy implements FilaDeEventos {

    private final GerenciamentoDeFilaDeEventos gerenciamentoDeFilaDeEventos;

    protected FilaDeEventosProxy(final GerenciamentoDeFilaDeEventos gerenciamentoDeFilaDeEventos) {
        this.gerenciamentoDeFilaDeEventos = gerenciamentoDeFilaDeEventos;
    }

    @Override
    public <T> FilaDeEventos disparar(final T objeto, final Evento<T> evento) {

        if (objeto == null || evento == null) {
            throw new EventoException("Objeto e/ou br.projeto.eventos.dominio.evento nulo");
        }

        return gerenciamentoDeFilaDeEventos.disparar(objeto, evento);

    }
}
