public class PocaoDeMana extends Consumivel {

    public static final int MANA_RESTAURADA = 15;

    public PocaoDeMana() {
        super("Poção de Mana");
    }

    @Override
    protected void aplicarEfeito(Personagem alvo) {
        alvo.recuperarMana(MANA_RESTAURADA);
    }

    @Override
    public String descricao() {
        return getNome() + " (recupera " + MANA_RESTAURADA + " de mana)";
    }
}
