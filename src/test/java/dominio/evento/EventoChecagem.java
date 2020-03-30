package dominio.evento;

final class EventoChecagem implements Evento<String> {

    private boolean recebiChamado;
    private String mensagem;
    private final LogEvento logEvento;

    EventoChecagem(final LogEvento logEvento) {
        this.logEvento = logEvento;
    }

    @Override
    public void executar(final String objeto, final EventoMalSucedido<String> eventoMalSucedido) throws EventoException {
        recebiChamado = true;
        if (objeto.isEmpty()) {
            logEvento.info("Vou reagenda");
            eventoMalSucedido.notificar("objeto não pode ser vazio");
        } else {
            logEvento.info("Evento concluido");
            mensagem = objeto;
        }
    }

    boolean chamadoRecebido() {
        return recebiChamado;
    }

    String objetoRecebido() {
        return mensagem;
    }

}
