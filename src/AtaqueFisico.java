public class AtaqueFisico implements Habilidade {

    public static final int MULTIPLICADOR_PADRAO = 2;
    public static final int CUSTO_PADRAO = 5;

    private final String nome;
    private final int multiplicador;
    private final int custoMana;

    public AtaqueFisico(String nome, int multiplicador, int custoMana) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("a habilidade precisa de um nome");
        }
        if (multiplicador <= 0) {
            throw new IllegalArgumentException("o multiplicador deve ser positivo");
        }
        if (custoMana < 0) {
            throw new IllegalArgumentException("o custo de mana não pode ser negativo");
        }
        this.nome = nome;
        this.multiplicador = multiplicador;
        this.custoMana = custoMana;
    }

    public AtaqueFisico(String nome) {
        this(nome, MULTIPLICADOR_PADRAO, CUSTO_PADRAO);
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public int getCustoMana() {
        return custoMana;
    }

    @Override
    public Alvo getAlvo() {
        return Alvo.INIMIGO;
    }

    @Override
    public String executar(Personagem usuario, Personagem alvo) {
        int danoBruto = (usuario.getForcaTotal() + usuario.getBonusDeDano()) * multiplicador;
        int danoCausado = alvo.receberDano(danoBruto);
        return usuario.getNome() + " usa " + nome + " e causa " + danoCausado + " de dano";
    }
}
