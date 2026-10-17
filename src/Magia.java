public class Magia implements Habilidade {

    public static final int PODER_PADRAO = 10;
    public static final int CUSTO_PADRAO = 8;

    private final String nome;
    private final int poder;
    private final int custoMana;

    public Magia(String nome, int poder, int custoMana) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("a habilidade precisa de um nome");
        }
        if (poder <= 0) {
            throw new IllegalArgumentException("o poder da magia deve ser positivo");
        }
        if (custoMana < 0) {
            throw new IllegalArgumentException("o custo de mana não pode ser negativo");
        }
        this.nome = nome;
        this.poder = poder;
        this.custoMana = custoMana;
    }

    public Magia(String nome) {
        this(nome, PODER_PADRAO, CUSTO_PADRAO);
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
        int danoBruto = usuario.getMagiaTotal() + poder;
        int danoCausado = alvo.receberDano(danoBruto);
        return usuario.getNome() + " lança " + nome + " e causa " + danoCausado + " de dano";
    }
}
