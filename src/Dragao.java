public class Dragao extends Monstro {

    public static final int VIDA = 150;
    public static final int FORCA = 12;
    public static final int DEFESA = 6;
    public static final int PODER_DO_ESPECIAL = 15;
    public static final int CUSTO_DO_ESPECIAL = 0;

    private static final Habilidade SOPRO_DE_FOGO =
            new Magia("Sopro de Fogo", PODER_DO_ESPECIAL, CUSTO_DO_ESPECIAL);

    public Dragao(EstrategiaDoMonstro estrategia) {
        super("Dragão", VIDA, FORCA, DEFESA, estrategia);
    }

    @Override
    public Habilidade getAtaqueEspecial() {
        return SOPRO_DE_FOGO;
    }
}
