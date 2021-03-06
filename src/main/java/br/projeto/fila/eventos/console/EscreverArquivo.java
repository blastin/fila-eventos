package br.projeto.fila.eventos.console;

import br.projeto.fila.eventos.dominio.Evento;
import br.projeto.fila.eventos.dominio.EventoException;
import br.projeto.fila.eventos.dominio.EventoMalSucedido;
import br.projeto.fila.eventos.dominio.LogEvento;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.PrimitiveIterator;
import java.util.stream.IntStream;

final class EscreverArquivo implements Evento<Integer> {

    private final LogEvento logEvento;

    EscreverArquivo(final LogEvento logEvento) {
        this.logEvento = logEvento;
    }

    @Override
    public void executar(final Integer objeto, final EventoMalSucedido<Integer> eventoMalSucedido) throws EventoException {

        logEvento.info("salvando");

        try (final FileOutputStream fos = new FileOutputStream("/tmp/output.txt");
             final DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(fos))) {

            final PrimitiveIterator.OfInt iterator = IntStream.range(1, 200).iterator();

            while (iterator.hasNext()) {
                final int valor = objeto + iterator.next();
                outStream.write(String.valueOf(valor).getBytes(Charset.defaultCharset()));
            }

        } catch (IOException e) {

            logEvento.error("Não foi possível escrever informações no arquivo");

            eventoMalSucedido.notificar(objeto);

        }

    }
}
