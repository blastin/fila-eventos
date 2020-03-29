package console;

import dominio.evento.LogEvento;

final class LogTela implements LogEvento {

    public LogTela() {
    }

    @Override
    public void info(final String mensagem, final Object... argumentos) {

        Thread currentThread = Thread.currentThread();

        System.out.println(mensagemPadrao(currentThread) + String.format(mensagem, argumentos));

    }

    @Override
    public void error(final String mensagem, final Object... argumentos) {

        Thread currentThread = Thread.currentThread();

        System.err.println(mensagemPadrao(currentThread) + String.format(mensagem, argumentos));

    }

    private String mensagemPadrao(final Thread currentThread) {

        final String className = Thread.currentThread().getStackTrace()[3].getClassName();

        return className + "::Thread<" + currentThread.getId() + "> -> ";
    }

}
