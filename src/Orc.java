public class Orc extends Monstro {

    public static final int VIDA = 70;
    public static final int FORCA = 9;
    public static final int DEFESA = 4;
    public static final int MULTIPLICADOR_DO_ESPECIAL = 2;
    public static final int CUSTO_DO_ESPECIAL = 0;

    private static final Habilidade GOLPE_ESMAGADOR =
            new AtaqueFisico("Golpe Esmagador", MULTIPLICADOR_DO_ESPECIAL, CUSTO_DO_ESPECIAL);

    public Orc(EstrategiaDoMonstro estrategia) {
        super("Orc", VIDA, FORCA, DEFESA, estrategia);
    }

    @Override
    public Habilidade getAtaqueEspecial() {
        return GOLPE_ESMAGADOR;
    }
}
