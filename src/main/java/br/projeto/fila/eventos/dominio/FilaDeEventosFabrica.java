package br.projeto.fila.eventos.dominio;

public interface FilaDeEventosFabrica {

    FilaDeEventos construir();

    FilaDeEventosFabrica log(final LogEvento logEvento);

    FilaDeEventosFabrica agendaParaEventoNaoSucedido(final Agenda agendaParaEventoNaoSucedido);

    FilaDeEventosFabrica agendaParaDispararEvento(final Agenda agendaParaEventoNormal);

}
