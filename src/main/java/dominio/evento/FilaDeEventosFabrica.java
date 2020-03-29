package dominio.evento;


public interface FilaDeEventosFabrica {

    FilaDeEventos construir();

    FilaDeEventosFabrica log(LogEvento logEvento);

    FilaDeEventosFabrica agenda(Agenda agendaParaEventoNaoSucedido);

}
