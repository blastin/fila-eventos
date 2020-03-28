import evento.LogEvento;

final class LogTela implements LogEvento {

    @Override
    public void info(final String mensagem, final Object... argumentos) {

        Thread currentThread = Thread.currentThread();

        System.out.println("thread [ " + currentThread.getId() + "] " + String.format(mensagem, argumentos));

    }

    @Override
    public void error(final String mensagem, final Object... argumentos) {

        Thread currentThread = Thread.currentThread();

        System.err.println("thread [ " + currentThread.getId() + "] " + String.format(mensagem, argumentos));

    }

}
