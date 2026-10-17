public class Arma extends Item implements Equipavel {

    private final int dano;

    public Arma(String nome, int dano) {
        super(nome);
        if (dano <= 0) {
            throw new IllegalArgumentException("o dano da arma deve ser positivo");
        }
        this.dano = dano;
    }

    @Override
    public Slot getSlot() {
        return Slot.MAO;
    }

    @Override
    public int bonusDeDano() {
        return dano;
    }

    @Override
    public String descricao() {
        return getNome() + " (+" + dano + " de dano, " + getSlot() + ")";
    }
}
