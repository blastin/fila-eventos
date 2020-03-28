package evento;

import anotacoes.Plugin;

@Plugin
public interface ExecutorTarefa {

    void executar(final Runnable runnable);

    void agendar(final Runnable runnable, final Agenda agenda);

}
