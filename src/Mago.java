import java.util.List;

public class Mago extends Personagem {

    public static final int VIDA_INICIAL = 60;
    public static final int MANA_INICIAL = 30;
    public static final int FORCA_INICIAL = 2;
    public static final int DEFESA_INICIAL = 2;
    public static final int MAGIA_INICIAL = 9;
    public static final int POCOES_INICIAIS = 1;

    public Mago(String nome) {
        super(nome, VIDA_INICIAL, MANA_INICIAL,
                new Atributos(FORCA_INICIAL, DEFESA_INICIAL, MAGIA_INICIAL),
                POCOES_INICIAIS,
                List.of(new Magia("Bola de Fogo"), new Cura("Cura")));
    }

    @Override
    public int atacar(Combatente alvo) {
        if (alvo == null) {
            throw new IllegalArgumentException("o ataque precisa de um alvo");
        }
        return alvo.receberDano(getMagiaTotal() + getBonusDeDano());
    }
}