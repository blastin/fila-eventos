# Fila de eventos

**Fila de eventos** é um componente de software desenvolvido em **java 11**. 
Útil para projetos que precisam disparar eventos **assíncronos** ou **síncronos**. 
Facilmente plugável e adaptado.


Abaixo, uma tabela com as implementações necéssarias:

Plugin          |   Adaptador
---------       |    --------
Evento          |   Agenda
ExecutorEvento  | 
LogEvento       |

---

## Evento
Evento é a principal abstração do componente. Ele representa um comportamento de execução necessária para o sistema. 
Por ser uma interface genérica, será possível definir o tipo de objeto.

```java
import dominio.evento.Evento;
import dominio.evento.LogEvento;
import sql2o.*;

final class EventoMudarStatusDeClienteParaInativo implements Evento<Cliente> {
    
    private final LogEvento log;
    
    private final Sql2o sql2o;

    EventoMudarStatusDeClienteParaInativo(final LogEvento log, final Sql2o sql2o){
        this.log = log;
        this.sql2o = sql2o;
    }

    @Override
    public void executar(final Cliente cliente, final EventoMalSucedido<Cliente> eventoMalSucedido) throws EventoException {
            
        log.info("Alterando status de cliente com id = {}, para inativo", cliente.id());
        
         try (final Connection con = sql2o.beginTransaction()) {

            con
                .createQuery("update cliente c set c.status = 0 where c.id = :id")
                .addParameter("id", cliente.id())
                .executeUpdate();

            con.commit();

          } catch(ConnectionException e) {
            
            e.printStackTrace();

            log.error("Não foi possivel realizar a transação, Tentarei novamente");

            eventoMalSucedido.notificar(cliente);

          }
    }

}
```

A implementação de um evento segue o exemplo acima, sem necessidade de se preocupar 
em alto nível sobre os detalhes da forma como isso será executado. 

Evento por definição é uma interface funcional. Sua implementação é opcional. Caso seja de interesse da aplicação será possível implementa-la na propria chamada do método ou aproveitar alguma troca de mensagem de interesse da missão global.

A  exceção RuntimeException::**EventoException**, implica em algo inesperado e seu controle deverá ser feito pelo componente utilizável, caso necessário.
Como por exemplo, uma instância [**SQL2o**](https://www.sql2o.org/) nula.  

---

## Executor Evento

**ExecutorEvento** é um wrapper necessário para encapsular a tecnologia que será utilizada para executar um evento.
No projeto pode-se encontrar exemplos de executores em src/**test**/java ou src/main/java/**console**. 

Threads podem ser utilizadas no caso de projetos que não precisam de um controle de execução de concorrência. Entretanto, caso seja necessário manter ativo uma **pool** de threads disponíveis, pode-se utilizar Executor do java.concorrent. 


```java
import java.util.concurrent.ScheduledExecutorService;
import dominio.evento.LogEvento;

final class ExecutorThread implements ExecutorEvento {

    private final ScheduledExecutorService executor;
    private final LogEvento logEvento;

    ExecutorJava(final ScheduledExecutorService executor, final LogEvento logEvento) {
        this.executor = executor;
        this.logEvento = logEvento;
    }

    @Override
    public void executar(final Runnable runnable) {
        logEvento.info("executando");
        executor.execute(runnable);
    }

    @Override
    public void agendar(final Runnable runnable, final Agenda agenda) {
        logEvento.info("agendando");
        executor.schedule(runnable, agenda.tempo(), agenda.unidadeTempo());
    }

}
```

No exemplo acima, foi utilizado o Executor::ScheduledExecutorService para implementar o comportamento de execução e de agendamento.

---

## Log Evento

Log Evento é plugin opcional do componente **fila de eventos**. 
Sua implementação pode enviar logs informativos da fila de evento para qualquer dispositivo IO.

```java

import dominio.evento.LogEvento;

final class LogConsole implements LogEvento {

 @Override
    public void info(final String mensagem, final Object... argumentos) {

        Thread currentThread = Thread.currentThread();

        String mensagemProcessada = processar(mensagem, argumentos);

        System.out.println(mensagemPadrao(currentThread) + mensagemProcessada);

    }

    @Override
    public void error(final String mensagem, final Object... argumentos) {

        Thread currentThread = Thread.currentThread();

        String mensagemProcessada = processar(mensagem, argumentos);

        System.err.println(mensagemPadrao(currentThread) + mensagemProcessada);

    }

    private String processar(final String mensagem, final Object[] argumentos) {

        final String mensagemCopia = mensagem.replaceAll(Pattern.quote("{}"), "%s");

        return String.format(mensagemCopia, argumentos);

    }

    private String mensagemPadrao(final Thread currentThread) {

        final String className = Thread.currentThread().getStackTrace()[3].getClassName();

        return className + "::Thread<" + currentThread.getId() + "> -> ";

    }

}
```

No exemplo acima utilizamos **System::println*** para encaminhar log para o console. 
Uma implementação interessante seria criar um **Wrapper** de uma biblioteca existente, como por exemplo, **sl4j**.

---

## Agenda

Agenda é uma interface **Adaptador** necessária para identificar o tempo de espera que cada evento será executado.
Existem dois momentos que agenda pode ser utilizada :

* No disparo inicial de um evento
* Próximo tentativa de disparo após receber uma notificação de evento não sucedido

É obrigatório implementar dois métodos :
 
 * Valor do tempo encapsulado em um tipo primitivo long
 * Unidade definida pela enumeração [**TimeUnit**](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/TimeUnit.html)

```java

import dominio.evento.Agenda;

final class AgendaComum implements  Agenda {

    private final long valorTempo;
    private final TimeUnit unidade;

    AgendaComum(final long valorTempo, final TimeUnit unidade) {
        this.valorTempo = valorTempo;
        this.unidade = unidade;
    }

    @Override
    public long tempo() {
        return valorTempo;
    }

    @Override
    public TimeUnit unidadeTempo() {
        return unidade;
    }

}

```

Caso TimeUnit passado como parâmetro para o construtor de Agenda::**AgendaComum** seja **nulo**, 
não há porque se preocupar em termos de falha sistêmica. 
O componente saberá lidar com esse caso e substituirá-lo por **TimeUNIT.NANO_SECONDS**

---

## Fila de eventos fábrica

Agora que os plugins e adaptadores foram apresentados, precisamos construir uma instância de fila de eventos.

```java

package dominio.evento;

public interface FilaDeEventosFabrica {

    FilaDeEventos construir();

    FilaDeEventosFabrica log(final LogEvento logEvento);

    FilaDeEventosFabrica agendaParaEventoNaoSucedido(final Agenda agendaParaEventoNaoSucedido);

    FilaDeEventosFabrica agendaParaDispararEvento(final Agenda agendaParaEventoNormal);

}

```

A interface acima representa o contrato de como construir uma fila de eventos. Não será necessário implementa-la, 
o componente disponibiliza FilaDeEventosFabrica::**FilaDeEventosFabricasImplementacao**

Abaixo um exemplo de construção de uma fila de evento e disparo.

```java

import dominio.evento.Evento;
import dominio.evento.FilaDeEventos;
import dominio.evento.LogEvento;
import dominio.evento.ExecutorEvento;

import java.time.LocalTime;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

final class Console {

    public static void main(String[] args){
      
        final LogEvento logEvento = new LogConsole();

        final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);
        
        final ExecutorEvento executor = new ExecutorThread(scheduledExecutorService, logEvento);
    
        final Agenda agendaParaCasoNaoSucesso = new AgendaComum(15, TimeUnit.MINUTES);
    
        final Agenda agendaDisparoDeEvento = new AgendaComum(5, TimeUnit.SECONDS);
    
        final FilaDeEventosFabrica filaDeEventosFabrica = FilaDeEventosFabricasImplementacao.criar(executor);
    
        final FilaDeEventos filaDeEventos =
    
                filaDeEventosFabrica
    
                        .log(logEvento)
    
                        .agendaParaDispararEvento(agendaDisparoDeEvento)
    
                        .agendaParaEventoNaoSucedido(agendaParaCasoNaoSucesso)
    
                        .construir();
       
        final Evento<String> evento = new EventoAlmoco(filaDeEventos, logEvento);

        filaDeEventos

            .disparar("Gladiador é o melhor filme do gênero", (objeto, __) -> logEvento.info(objeto, ""))
            
            .disparar("Estou com muita fome", evento);

    }
    
    private static class EventoAlmoco implements Evento<String> {
    
        private static final LocalTime ALMOCO_INICIO = LocalTime.of(12, 30);
        private static final LocalTime ALMOCO_FIM = LocalTime.of(14, 30);

        private final FilaDeEventos filaDeEventos;
    
        private final LogEvento logEvento;

        EventoAlmoco(final FilaDeEventos filaDeEventos, final LogEvento logEvento){
            this.filaDeEventos = filaDeEventos;
            this.logEvento = logEvento;
        }

        @Override
        public void executar(
                final String objeto, 
                final EventoMalSucedido<String> eventoMalSucedido) throws EventoException {
        
            final LocalTime localTime = LocalTime.now();

            if(localTime.isAfter(ALMOCO_INICIO) && localTime.isBefore(ALMOCO_FIM)){

                eventoMalSucedido.notificar(objeto);

            }else{

                logEvento.info(objeto);

                filaDeEventos.disparar("Almoço chega em meia hora", (objeto, __) -> logEvento.info(objeto, ""));

            }
        }
    }
}
```


