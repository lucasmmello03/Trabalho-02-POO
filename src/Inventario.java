import java.util.ArrayList;
import java.util.List;


public class Inventario {

    public static final int CAPACIDADE_PADRAO = 10;

    private final int capacidade;
    private final List<Item> itens = new ArrayList<>();

    public Inventario(int capacidade) {
        if (capacidade <= 0) {
            throw new IllegalArgumentException("a capacidade deve ser positiva");
        }
        this.capacidade = capacidade;
    }

    public void adicionar(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("item não pode ser nulo");
        }
        if (itens.size() >= capacidade) {
            throw new IllegalStateException("inventário cheio (" + capacidade + " itens): " + item.getNome() + " não coube");
        }
        itens.add(item);
    }

    public void remover(Item item) {
        if (!itens.remove(item)) {
            throw new IllegalArgumentException("o item não está no inventário");
        }
    }

    public <T extends Item> T retirar(Class<T> tipo) {
        for (Item item : itens) {
            if (tipo.isInstance(item)) {
                itens.remove(item);
                return tipo.cast(item);
            }
        }
        throw new IllegalStateException("não há " + tipo.getSimpleName() + " no inventário");
    }

    
    public int contar(Class<? extends Item> tipo) {
        int quantidade = 0;
        for (Item item : itens) {
            if (tipo.isInstance(item)) {
                quantidade++;
            }
        }
        return quantidade;
    }

    public boolean temPocaoDeVida() {
        return contar(PocaoDeVida.class) > 0;
    }

    public boolean temPocaoDeMana() {
        return contar(PocaoDeMana.class) > 0;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public List<Item> getItens() {
        return List.copyOf(itens);
    }
}
