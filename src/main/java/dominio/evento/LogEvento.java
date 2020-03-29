package dominio.evento;

import dominio.anotacoes.Plugin;

@Plugin
public interface LogEvento {

    void info(final String mensagem, Object... argumentos);

    void error(final String mensagem, Object... argumentos);

}
