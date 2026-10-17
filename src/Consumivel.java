public abstract class Consumivel extends Item implements Usavel {

    protected Consumivel(String nome) {
        super(nome);
    }

    @Override
    public final void usar(Personagem alvo) {
        if (alvo == null) {
            throw new IllegalArgumentException("o consumível precisa de um alvo");
        }
        if (!alvo.estaVivo()) {
            throw new IllegalStateException(alvo.getNome() + " está morto e não pode usar " + getNome());
        }
        aplicarEfeito(alvo);
    }

    protected abstract void aplicarEfeito(Personagem alvo);
}
