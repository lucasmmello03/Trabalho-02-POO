public interface Habilidade {

    String getNome();

    int getCustoMana();

    Alvo getAlvo();

    String executar(Personagem usuario, Personagem alvo);

    default boolean podeUsar(Personagem usuario) {
        return usuario.getMana() >= getCustoMana();
    }
}
