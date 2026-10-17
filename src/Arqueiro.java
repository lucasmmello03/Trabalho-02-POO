import java.util.List;

public class Arqueiro extends LutadorFisico {

    public static final int VIDA_INICIAL = 80;
    public static final int MANA_INICIAL = 15;
    public static final int FORCA_INICIAL = 7;
    public static final int DEFESA_INICIAL = 3;
    public static final int MAGIA_INICIAL = 2;
    public static final int BONUS_DE_PRECISAO = 3;

    public Arqueiro(String nome) {
        super(nome, VIDA_INICIAL, MANA_INICIAL,
                new Atributos(FORCA_INICIAL, DEFESA_INICIAL, MAGIA_INICIAL),
                List.of(new AtaqueFisico("Tiro Certeiro")));
    }

    /**
     * Sobrescreve o ataque herdado do LutadorFisico somando o bônus de precisão.
     * Não chama {@code super.atacar()} porque ele já aplica o dano no alvo: o bônus precisa entrar
     * no dano bruto ANTES de o alvo recebê-lo.
     */
    @Override
    public int atacar(Combatente alvo) {
        if (alvo == null) {
            throw new IllegalArgumentException("o ataque precisa de um alvo");
        }
        return alvo.receberDano(getForcaTotal() + getBonusDeDano() + BONUS_DE_PRECISAO);
    }
}
