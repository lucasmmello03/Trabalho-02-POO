import java.util.List;

public class Jogo {

    public static final int PONTOS_POR_VITORIA = 2;
    public static final int PERCENTUAL_DE_VIDA_RECUPERADA = 50;

    private final Personagem jogador;
    private final List<Monstro> monstros;
    private final TabelaDeRecompensas tabelaDeRecompensas;
    private int indiceDoMonstro;
    private EstadoDoJogo estado = EstadoDoJogo.EM_ANDAMENTO;
    private Batalha batalhaAtual; 

    public Jogo(Personagem jogador, List<Monstro> monstros, TabelaDeRecompensas tabelaDeRecompensas) {
        if (jogador == null || tabelaDeRecompensas == null) {
            throw new IllegalArgumentException("o jogo precisa de um jogador e de uma tabela de recompensas");
        }
        if (monstros == null || monstros.isEmpty()) {
            throw new IllegalArgumentException("o jogo precisa de pelo menos um monstro");
        }
        this.jogador = jogador;
        this.monstros = List.copyOf(monstros);
        this.tabelaDeRecompensas = tabelaDeRecompensas;
    }

    public Batalha iniciarProximaBatalha() {
        if (estado != EstadoDoJogo.EM_ANDAMENTO) {
            throw new IllegalStateException("o jogo já terminou: " + estado);
        }
        if (batalhaAtual != null) {
            throw new IllegalStateException("já existe uma batalha em andamento");
        }
        batalhaAtual = new Batalha(jogador, monstros.get(indiceDoMonstro));
        return batalhaAtual;
    }

    
    public Item concluirBatalha(Batalha batalha) {
        if (batalha == null || batalha != batalhaAtual) {
            throw new IllegalArgumentException("esta não é a batalha atual do jogo");
        }
        if (batalha.getEstado() == EstadoDaBatalha.EM_ANDAMENTO) {
            throw new IllegalStateException("a batalha ainda não terminou");
        }
        batalhaAtual = null;

        if (batalha.getEstado() == EstadoDaBatalha.DERROTA) {
            estado = EstadoDoJogo.PERDIDO;
            return null;
        }

        jogador.curar(jogador.getVidaMaxima() * PERCENTUAL_DE_VIDA_RECUPERADA / 100);
        jogador.recuperarMana(jogador.getManaMaxima());
        jogador.ganharPontos(PONTOS_POR_VITORIA);

        Item recompensa = tabelaDeRecompensas.sortear();
        try {
            jogador.getInventario().adicionar(recompensa);
        } catch (IllegalStateException inventarioCheio) {

        }

        indiceDoMonstro++;
        if (indiceDoMonstro == monstros.size()) {
            estado = EstadoDoJogo.VENCIDO;
        }
        return recompensa;
    }

    public EstadoDoJogo getEstado() {
        return estado;
    }
}