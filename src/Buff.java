public class Buff implements Habilidade {

    public static final int BONUS_PADRAO = 3;
    public static final int TURNOS_PADRAO = 3;
    public static final int CUSTO_PADRAO = 4;

    private final String nome;
    private final int bonusDefesa;
    private final int turnos;
    private final int custoMana;

    public Buff(String nome, int bonusDefesa, int turnos, int custoMana) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("a habilidade precisa de um nome");
        }
        if (bonusDefesa <= 0) {
            throw new IllegalArgumentException("o bônus de defesa deve ser positivo");
        }
        if (turnos <= 0) {
            throw new IllegalArgumentException("a duração do buff deve ser positiva");
        }
        if (custoMana < 0) {
            throw new IllegalArgumentException("o custo de mana não pode ser negativo");
        }
        this.nome = nome;
        this.bonusDefesa = bonusDefesa;
        this.turnos = turnos;
        this.custoMana = custoMana;
    }

    public Buff(String nome) {
        this(nome, BONUS_PADRAO, TURNOS_PADRAO, CUSTO_PADRAO);
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
        usuario.aplicarBuff(bonusDefesa, turnos);
        return usuario.getNome() + " usa " + nome + " (+" + bonusDefesa + " de defesa por " + turnos + " turnos)";
    }
}
