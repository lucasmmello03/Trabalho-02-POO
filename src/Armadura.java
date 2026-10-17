public class Armadura extends Item implements Equipavel {

    private final int defesaExtra;
    private final Slot slot;

    public Armadura(String nome, int defesaExtra, Slot slot) {
        super(nome);
        if (defesaExtra <= 0) {
            throw new IllegalArgumentException("a defesa da armadura deve ser positiva");
        }
        if (slot == null) {
            throw new IllegalArgumentException("a armadura precisa de um slot");
        }
        if (slot == Slot.MAO) {
            throw new IllegalArgumentException("armadura não pode ser equipada na mão");
        }
        this.defesaExtra = defesaExtra;
        this.slot = slot;
    }

    @Override
    public Slot getSlot() {
        return slot;
    }

    @Override
    public int bonusDeDefesa() {
        return defesaExtra;
    }

    @Override
    public String descricao() {
        return getNome() + " (+" + defesaExtra + " de defesa, " + getSlot() + ")";
    }
}
