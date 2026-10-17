import java.util.List;

public class Guerreiro extends LutadorFisico {

    public static final int VIDA_INICIAL = 100;
    public static final int MANA_INICIAL = 10;
    public static final int FORCA_INICIAL = 8;
    public static final int DEFESA_INICIAL = 5;
    public static final int MAGIA_INICIAL = 1;
    public static final int REDUCAO_DA_ARMADURA_NATURAL_PERCENTUAL = 20;

    public Guerreiro(String nome) {
        super(nome, VIDA_INICIAL, MANA_INICIAL,
                new Atributos(FORCA_INICIAL, DEFESA_INICIAL, MAGIA_INICIAL),
                List.of(new AtaqueFisico("Golpe Poderoso"), new Buff("Postura Defensiva")));
    }

    @Override
    public int receberDano(int danoBruto) {
        int danoReduzido = danoBruto * (100 - REDUCAO_DA_ARMADURA_NATURAL_PERCENTUAL) / 100;
        return super.receberDano(danoReduzido);
    }
}
