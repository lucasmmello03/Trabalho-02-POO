public class PocaoDeVida extends Consumivel {

    public static final int VIDA_RESTAURADA = 30;

    public PocaoDeVida() {
        super("Poção de Vida");
    }

    @Override
    protected void aplicarEfeito(Personagem alvo) {
        alvo.curar(VIDA_RESTAURADA);
    }

    @Override
    public String descricao() {
        return getNome() + " (recupera " + VIDA_RESTAURADA + " de vida)";
    }
}
