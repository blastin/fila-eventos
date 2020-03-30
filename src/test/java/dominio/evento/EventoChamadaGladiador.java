package dominio.evento;

final class EventoChamadaGladiador implements Evento<Gladiador> {

    private boolean recebiChamado;

    private final LogEvento logEvento;

    EventoChamadaGladiador(final LogEvento logEvento) {
        this.logEvento = logEvento;
        recebiChamado = false;
    }

    @Override
    public void executar(final Gladiador gladiador, final EventoMalSucedido<Gladiador> eventoMalSucedido) throws EventoException {

        recebiChamado = true;

        logEvento.info("Recebi chamada de um gladiador : {}", gladiador);

        if (gladiador.getHonra() < 50) {
            logEvento.info("Você receberá mais honra");
            eventoMalSucedido.notificar(new Gladiador(gladiador.getId(), gladiador.getHonra() * 2, gladiador.getCoragem()));
        } else {
            logEvento.info("Você tem honra gladiador : {}", gladiador);
        }

    }

    public boolean chamadoRecebido() {
        return recebiChamado;
    }

}
