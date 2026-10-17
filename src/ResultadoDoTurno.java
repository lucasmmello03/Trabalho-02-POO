import java.util.List;

public record ResultadoDoTurno(int numeroDoTurno, List<String> eventos, EstadoDaBatalha estado) {

    public ResultadoDoTurno {
        if (numeroDoTurno <= 0) {
            throw new IllegalArgumentException("o número do turno deve ser positivo");
        }
        if (estado == null) {
            throw new IllegalArgumentException("o resultado precisa de um estado");
        }
        eventos = List.copyOf(eventos);
    }
}
