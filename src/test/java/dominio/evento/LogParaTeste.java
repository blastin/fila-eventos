package dominio.evento;

import java.util.regex.Pattern;

final class LogParaTeste implements LogEvento {

    @Override
    public void info(final String mensagem, final Object... argumentos) {

        Thread currentThread = Thread.currentThread();

        String construcao = construir(mensagem, argumentos);

        System.out.println(mensagemPadrao(currentThread) + construcao);

    }

    @Override
    public void error(final String mensagem, final Object... argumentos) {

        Thread currentThread = Thread.currentThread();

        String construcao = construir(mensagem, argumentos);

        System.err.println(mensagemPadrao(currentThread) + construcao);

    }


    private String construir(final String mensagem, final Object[] argumentos) {

        final String mensagemCopia = mensagem.replaceAll(Pattern.quote("{}"), "%s");

        return String.format(mensagemCopia, argumentos);

    }

    private String mensagemPadrao(final Thread currentThread) {

        final String className = Thread.currentThread().getStackTrace()[3].getClassName();

        return className + "::Thread<" + currentThread.getId() + "> -> ";

    }

}
