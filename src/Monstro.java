import java.util.List;

public abstract class Monstro extends Personagem {

    public static final int POCOES_INICIAIS = 1;
    public static final int MANA_DE_MONSTRO = 0;
    public static final int MAGIA_DE_MONSTRO = 0;
    public static final int TURNO_MINIMO_DO_ESPECIAL = 2;

    private final EstrategiaDoMonstro estrategia;

    protected Monstro(String nome, int vidaMaxima, int forca, int defesa, EstrategiaDoMonstro estrategia) {
        super(nome, vidaMaxima, MANA_DE_MONSTRO, new Atributos(forca, defesa, MAGIA_DE_MONSTRO),
                POCOES_INICIAIS, List.of());
        if (estrategia == null) {
            throw new IllegalArgumentException("o monstro precisa de uma estratégia");
        }
        this.estrategia = estrategia;
    }

    public abstract Habilidade getAtaqueEspecial();

    public AcaoDoMonstro decidirAcao(Personagem jogador, int turno) {
        return estrategia.decidir(this, jogador, turno);
    }

    @Override
    public int atacar(Combatente alvo) {
        if (alvo == null) {
            throw new IllegalArgumentException("o ataque precisa de um alvo");
        }
        return alvo.receberDano(getForcaTotal() + getBonusDeDano());
    }
}
