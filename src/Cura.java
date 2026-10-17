public class Cura implements Habilidade {

    public static final int QUANTIDADE_PADRAO = 15;
    public static final int CUSTO_PADRAO = 6;

    private final String nome;
    private final int quantidade;
    private final int custoMana;

    public Cura(String nome, int quantidade, int custoMana) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("a habilidade precisa de um nome");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("a quantidade de cura deve ser positiva");
        }
        if (custoMana < 0) {
            throw new IllegalArgumentException("o custo de mana não pode ser negativo");
        }
        this.nome = nome;
        this.quantidade = quantidade;
        this.custoMana = custoMana;
    }

    public Cura(String nome) {
        this(nome, QUANTIDADE_PADRAO, CUSTO_PADRAO);
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
        return Alvo.SI_MESMO;
    }

    @Override
    public String executar(Personagem usuario, Personagem alvo) {
        int vidaAntes = usuario.getVida();
        usuario.curar(quantidade + usuario.getMagiaTotal());
        int vidaRecuperada = usuario.getVida() - vidaAntes;
        return usuario.getNome() + " usa " + nome + " e recupera " + vidaRecuperada + " de vida";
    }
}
