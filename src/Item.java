public abstract class Item {

    private final String nome;

    protected Item(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("o item precisa de um nome");
        }
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public abstract String descricao();
}
