package br.projeto.fila.eventos.dominio;

import java.util.Objects;
import java.util.StringJoiner;

final class Gladiador {

    private final Integer id;

    private final Integer honra;

    private final Integer coragem;

    Gladiador(final Integer id, final Integer honra, final Integer coragem) {

        this.id = id;

        if (honra <= 0) {
            throw new RuntimeException("Um gladiador deve ter no minimo honra");
        }
        this.honra = honra;

        if (coragem <= 0) {
            throw new RuntimeException("Um gladiador deve ter no minimo coragem");
        }
        this.coragem = coragem;

    }

    Integer getId() {
        return id;
    }

    Integer getHonra() {
        return honra;
    }

    Integer getCoragem() {
        return coragem;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Gladiador gladiador = (Gladiador) o;
        return Objects.equals(getId(), gladiador.getId()) &&
               Objects.equals(getHonra(), gladiador.getHonra()) &&
               Objects.equals(getCoragem(), gladiador.getCoragem());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getHonra(), getCoragem());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Gladiador.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("honra=" + honra)
                .add("coragem=" + coragem)
                .toString();
    }
}

