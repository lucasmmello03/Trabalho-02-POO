import java.util.List;

public abstract class LutadorFisico extends Personagem {

    public static final int POCOES_INICIAIS = 3;

    protected LutadorFisico(String nome, int vidaMaxima, int manaMaxima, Atributos atributos,
                            List<Habilidade> habilidades) {
        super(nome, vidaMaxima, manaMaxima, atributos, POCOES_INICIAIS, habilidades);
    }

    @Override
    public int atacar(Combatente alvo) {
        if (alvo == null) {
            throw new IllegalArgumentException("o ataque precisa de um alvo");
        }
        return alvo.receberDano(getForcaTotal() + getBonusDeDano());
    }
}
