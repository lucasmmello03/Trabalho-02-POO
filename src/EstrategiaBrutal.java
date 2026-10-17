public class EstrategiaBrutal implements EstrategiaDoMonstro {

    @Override
    public AcaoDoMonstro decidir(Monstro monstro, Personagem jogador, int turno) {
        if (turno >= Monstro.TURNO_MINIMO_DO_ESPECIAL) {
            return AcaoDoMonstro.ATAQUE_ESPECIAL;
        }
        return AcaoDoMonstro.ATAQUE_BASICO;
    }
}
