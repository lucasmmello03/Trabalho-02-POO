public record Atributos(int forca, int defesa, int magia) {

    public static final int LIMITE_POR_ATRIBUTO = 20;

    public Atributos {
        validar(forca, "força");
        validar(defesa, "defesa");
        validar(magia, "magia");
    }

    private static void validar(int valor, String nome) {
        if (valor < 0 || valor > LIMITE_POR_ATRIBUTO) {
            throw new IllegalArgumentException(
                    nome + " deve estar entre 0 e " + LIMITE_POR_ATRIBUTO + ", mas foi " + valor);
        }
    }

    
    public Atributos somar(Atributo atributo, int pontos) {
        if (pontos <= 0) {
            throw new IllegalArgumentException("a quantidade de pontos deve ser positiva");
        }
        return switch (atributo) {
            case FORCA -> new Atributos(forca + pontos, defesa, magia);
            case DEFESA -> new Atributos(forca, defesa + pontos, magia);
            case MAGIA -> new Atributos(forca, defesa, magia + pontos);
        };
    }

    public int valorDe(Atributo atributo) {
        return switch (atributo) {
            case FORCA -> forca;
            case DEFESA -> defesa;
            case MAGIA -> magia;
        };
    }
}
