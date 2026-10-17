import java.util.List;
import java.util.Random;

public class TabelaDeRecompensas {

    private final List<Item> itens;
    private final Random random;

    public TabelaDeRecompensas(List<Item> itens, Random random) {
        if (itens == null || itens.isEmpty()) {
            throw new IllegalArgumentException("a tabela precisa de pelo menos um item");
        }
        if (random == null) {
            throw new IllegalArgumentException("a tabela precisa de um Random");
        }
        this.itens = List.copyOf(itens);
        this.random = random;
    }

    public Item sortear() {
        return itens.get(random.nextInt(itens.size()));
    }
}
