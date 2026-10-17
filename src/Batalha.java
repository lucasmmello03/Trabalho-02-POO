import java.util.ArrayList;
import java.util.List;

public class Batalha {

    public static final int PRIMEIRO_TURNO = 1;

    private final Personagem jogador;
    private final Monstro monstro;
    private int turnoAtual = PRIMEIRO_TURNO;
    private EstadoDaBatalha estado = EstadoDaBatalha.EM_ANDAMENTO;

    public Batalha(Personagem jogador, Monstro monstro) {
        if (jogador == null || monstro == null) {
            throw new IllegalArgumentException("a batalha precisa de um jogador e de um monstro");
        }
        if (!jogador.estaVivo() || !monstro.estaVivo()) {
            throw new IllegalArgumentException("os dois combatentes precisam estar vivos");
        }
        this.jogador = jogador;
        this.monstro = monstro;
    }

    public ResultadoDoTurno executarTurno(EscolhaDoJogador escolha, AcaoDoMonstro acaoDoMonstro) {
        validar(escolha, acaoDoMonstro);

        int numeroDoTurno = turnoAtual;
        List<String> eventos = new ArrayList<>();

        eventos.add(executarAcaoDoJogador(escolha));
        if (!monstro.estaVivo()) {
            estado = EstadoDaBatalha.VITORIA;
            eventos.add(monstro.getNome() + " foi derrotado!");
            return new ResultadoDoTurno(numeroDoTurno, eventos, estado);
        }

        eventos.add(executarAcaoDoMonstro(acaoDoMonstro));
        if (!jogador.estaVivo()) {
            estado = EstadoDaBatalha.DERROTA;
            eventos.add(jogador.getNome() + " foi derrotado!");
            return new ResultadoDoTurno(numeroDoTurno, eventos, estado);
        }

        jogador.encerrarTurno();
        monstro.encerrarTurno();
        turnoAtual++;
        return new ResultadoDoTurno(numeroDoTurno, eventos, estado);
    }

    private void validar(EscolhaDoJogador escolha, AcaoDoMonstro acaoDoMonstro) {
        if (estado != EstadoDaBatalha.EM_ANDAMENTO) {
            throw new IllegalStateException("a batalha já terminou em " + estado);
        }
        if (escolha == null || acaoDoMonstro == null) {
            throw new IllegalArgumentException("é preciso informar a escolha do jogador e a ação do monstro");
        }
        switch (escolha.acao()) {
            case ATACAR -> { }
            case USAR_HABILIDADE -> validarHabilidade(escolha.habilidade());
            case USAR_POCAO_VIDA -> {
                if (!jogador.getInventario().temPocaoDeVida()) {
                    throw new IllegalStateException(jogador.getNome() + " não tem poção de vida");
                }
            }
            case USAR_POCAO_MANA -> {
                if (!jogador.getInventario().temPocaoDeMana()) {
                    throw new IllegalStateException(jogador.getNome() + " não tem poção de mana");
                }
            }
        }
        switch (acaoDoMonstro) {
            case ATAQUE_BASICO -> { }
            case ATAQUE_ESPECIAL -> {
                if (turnoAtual < Monstro.TURNO_MINIMO_DO_ESPECIAL) {
                    throw new IllegalStateException("o ataque especial só pode ser usado a partir do turno "
                            + Monstro.TURNO_MINIMO_DO_ESPECIAL + " (turno atual: " + turnoAtual + ")");
                }
            }
            case USAR_POCAO -> {
                if (!monstro.getInventario().temPocaoDeVida()) {
                    throw new IllegalStateException(monstro.getNome() + " não tem poção de vida");
                }
            }
        }
    }

    private void validarHabilidade(Habilidade habilidade) {
        if (!jogador.getHabilidades().contains(habilidade)) {
            throw new IllegalArgumentException(jogador.getNome() + " não conhece a habilidade " + habilidade.getNome());
        }
        if (!habilidade.podeUsar(jogador)) {
            throw new IllegalStateException(jogador.getNome() + " não tem mana para usar " + habilidade.getNome());
        }
    }

    private String executarAcaoDoJogador(EscolhaDoJogador escolha) {
        return switch (escolha.acao()) {
            case ATACAR -> jogador.getNome() + " ataca " + monstro.getNome()
                    + " e causa " + jogador.atacar(monstro) + " de dano";
            case USAR_HABILIDADE -> {
                Habilidade habilidade = escolha.habilidade();
                Personagem alvo = habilidade.getAlvo() == Alvo.SI_MESMO ? jogador : monstro;
                yield jogador.usarHabilidade(habilidade, alvo);
            }
            case USAR_POCAO_VIDA -> {
                int vidaAntes = jogador.getVida();
                jogador.usarPocaoDeVida();
                yield jogador.getNome() + " bebe uma poção de vida e recupera "
                        + (jogador.getVida() - vidaAntes) + " de vida";
            }
            case USAR_POCAO_MANA -> {
                int manaAntes = jogador.getMana();
                jogador.usarPocaoDeMana();
                yield jogador.getNome() + " bebe uma poção de mana e recupera "
                        + (jogador.getMana() - manaAntes) + " de mana";
            }
        };
    }

    private String executarAcaoDoMonstro(AcaoDoMonstro acao) {
        return switch (acao) {
            case ATAQUE_BASICO -> monstro.getNome() + " ataca " + jogador.getNome()
                    + " e causa " + monstro.atacar(jogador) + " de dano";
            case ATAQUE_ESPECIAL -> monstro.getAtaqueEspecial().executar(monstro, jogador);
            case USAR_POCAO -> {
                int vidaAntes = monstro.getVida();
                monstro.usarPocaoDeVida();
                yield monstro.getNome() + " bebe uma poção de vida e recupera "
                        + (monstro.getVida() - vidaAntes) + " de vida";
            }
        };
    }

    public int getTurnoAtual() {
        return turnoAtual;
    }

    public EstadoDaBatalha getEstado() {
        return estado;
    }

    public Monstro getMonstro() {
        return monstro;
    }
}
