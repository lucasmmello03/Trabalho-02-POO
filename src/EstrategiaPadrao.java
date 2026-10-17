import java.util.Random;

public class EstrategiaPadrao implements EstrategiaDoMonstro {

    public static final int PERCENTUAL_DE_VIDA_BAIXA = 30;
    public static final int CHANCE_DE_USAR_POCAO_PERCENTUAL = 25;
    public static final int CHANCE_DE_ATAQUE_ESPECIAL_PERCENTUAL = 40;
    private static final int CEM_POR_CENTO = 100;

    private final Random random;

    public EstrategiaPadrao(Random random) {
        if (random == null) {
            throw new IllegalArgumentException("a estratégia precisa de um Random");
        }
        this.random = random;
    }

    @Override
    public AcaoDoMonstro decidir(Monstro monstro, Personagem jogador, int turno) {
        boolean vidaBaixa = monstro.getVida() * CEM_POR_CENTO <= monstro.getVidaMaxima() * PERCENTUAL_DE_VIDA_BAIXA;
        if (vidaBaixa && monstro.getInventario().temPocaoDeVida()
                && random.nextInt(CEM_POR_CENTO) < CHANCE_DE_USAR_POCAO_PERCENTUAL) {
            return AcaoDoMonstro.USAR_POCAO;
        }
        if (turno >= Monstro.TURNO_MINIMO_DO_ESPECIAL
                && random.nextInt(CEM_POR_CENTO) < CHANCE_DE_ATAQUE_ESPECIAL_PERCENTUAL) {
            return AcaoDoMonstro.ATAQUE_ESPECIAL;
        }
        return AcaoDoMonstro.ATAQUE_BASICO;
    }
}
