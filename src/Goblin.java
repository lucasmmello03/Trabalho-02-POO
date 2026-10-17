public class Goblin extends Monstro {

    public static final int VIDA = 40;
    public static final int FORCA = 6;
    public static final int DEFESA = 2;
    public static final int MULTIPLICADOR_DO_ESPECIAL = 2;
    public static final int CUSTO_DO_ESPECIAL = 0;

    private static final Habilidade FACADA_DUPLA =
            new AtaqueFisico("Facada Dupla", MULTIPLICADOR_DO_ESPECIAL, CUSTO_DO_ESPECIAL);

    public Goblin(EstrategiaDoMonstro estrategia) {
        super("Goblin", VIDA, FORCA, DEFESA, estrategia);
    }

    @Override
    public Habilidade getAtaqueEspecial() {
        return FACADA_DUPLA;
    }
}
