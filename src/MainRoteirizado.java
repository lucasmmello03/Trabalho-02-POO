import java.util.List;
import java.util.Random;

public class MainRoteirizado {

    public static final long SEMENTE = 42L;
    public static final int PERCENTUAL_PARA_BEBER_POCAO = 35;

    public static void main(String[] args) {
        System.out.println("=== ARENA: execução roteirizada (semente " + SEMENTE + ") ===");
        Random random = new Random(SEMENTE);
        Guerreiro jogador = new Guerreiro("Conan");
        List<Monstro> monstros = List.of(
                new Goblin(new EstrategiaPadrao(random)),
                new Orc(new EstrategiaPadrao(random)),
                new Dragao(new EstrategiaBrutal()));
        TabelaDeRecompensas recompensas = new TabelaDeRecompensas(List.of(
                new Arma("Adaga", 2),
                new Arma("Espada Longa", 4),
                new Armadura("Elmo de Ferro", 2, Slot.CABECA),
                new Armadura("Peitoral de Couro", 3, Slot.CORPO),
                new PocaoDeVida(),
                new PocaoDeMana()), random);
        Jogo jogo = new Jogo(jogador, monstros, recompensas);

        boolean primeiraBatalha = true;
        while (jogo.getEstado() == EstadoDoJogo.EM_ANDAMENTO) {
            Batalha batalha = jogo.iniciarProximaBatalha();
            System.out.println("\n>>> Batalha contra " + batalha.getMonstro().getNome());
            if (primeiraBatalha) {
                demonstrarRejeicoesNaBatalha(batalha, jogador);
                primeiraBatalha = false;
            }
            lutar(batalha, jogador);
            Item recompensa = jogo.concluirBatalha(batalha);
            if (recompensa != null) {
                aposVitoria(jogador, recompensa);
            }
        }

        System.out.println("\n=== Fim da campanha: " + (jogo.getEstado() == EstadoDoJogo.VENCIDO ? "VITÓRIA" : "DERROTA") + " ===");
        System.out.println(jogador.getNome() + ": vida " + jogador.getVida() + "/" + jogador.getVidaMaxima()
                + " | " + jogador.getAtributos());

        demonstrarLimiteDeAtributo();
    }

    private static void demonstrarRejeicoesNaBatalha(Batalha batalha, Personagem jogador) {
        Monstro monstro = batalha.getMonstro();
        System.out.println("[demonstração] Turno 1: forçando o ataque especial do monstro...");
        tentar(() -> batalha.executarTurno(new EscolhaDoJogador(AcaoDoJogador.ATACAR, null), AcaoDoMonstro.ATAQUE_ESPECIAL));
        System.out.println("[demonstração] Guerreiro tentando usar Cura...");
        tentar(() -> batalha.executarTurno(new EscolhaDoJogador(AcaoDoJogador.USAR_HABILIDADE, new Cura("Cura")),
                AcaoDoMonstro.ATAQUE_BASICO));
        System.out.println("[demonstração] Nada mudou: turno " + batalha.getTurnoAtual()
                + ", " + jogador.getNome() + " vida " + jogador.getVida()
                + ", " + monstro.getNome() + " vida " + monstro.getVida());
    }

    private static void lutar(Batalha batalha, Personagem jogador) {
        Monstro monstro = batalha.getMonstro();
        while (batalha.getEstado() == EstadoDaBatalha.EM_ANDAMENTO) {
            EscolhaDoJogador escolha = escolhaDoRoteiro(jogador);
            AcaoDoMonstro acao = monstro.decidirAcao(jogador, batalha.getTurnoAtual());
            ResultadoDoTurno resultado = batalha.executarTurno(escolha, acao);
            System.out.println("Turno " + resultado.numeroDoTurno() + ":");
            for (String evento : resultado.eventos()) {
                System.out.println("  " + evento);
            }
            System.out.println("  [" + jogador.getNome() + " " + jogador.getVida() + "/" + jogador.getVidaMaxima()
                    + " | mana " + jogador.getMana() + " | " + monstro.getNome() + " " + monstro.getVida() + "/" + monstro.getVidaMaxima() + "]");
        }
    }

    private static EscolhaDoJogador escolhaDoRoteiro(Personagem jogador) {
        boolean vidaBaixa = jogador.getVida() * 100 <= jogador.getVidaMaxima() * PERCENTUAL_PARA_BEBER_POCAO;
        if (vidaBaixa && jogador.getInventario().temPocaoDeVida()) {
            return new EscolhaDoJogador(AcaoDoJogador.USAR_POCAO_VIDA, null);
        }
        Habilidade golpe = jogador.getHabilidades().get(0);
        if (golpe.podeUsar(jogador)) {
            return new EscolhaDoJogador(AcaoDoJogador.USAR_HABILIDADE, golpe);
        }
        return new EscolhaDoJogador(AcaoDoJogador.ATACAR, null);
    }

    private static void aposVitoria(Personagem jogador, Item recompensa) {
        System.out.println("Vitória! Recompensa sorteada: " + recompensa.descricao());
        jogador.distribuirPontos(Atributo.FORCA, Jogo.PONTOS_POR_VITORIA);
        System.out.println("Pontos distribuídos em força: " + jogador.getAtributos());
        // Exceção permitida pelo enunciado: perguntar se o item é Equipavel para equipá-lo.
        if (recompensa instanceof Equipavel equipavel) {
            jogador.equipar(equipavel);
            System.out.println("Equipou " + recompensa.getNome() + " (bônus de dano " + jogador.getBonusDeDano()
                    + ", defesa total " + jogador.getDefesa() + ")");
        }
    }

    private static void demonstrarLimiteDeAtributo() {
        System.out.println("\n[demonstração] Limite por atributo (" + Atributos.LIMITE_POR_ATRIBUTO + ")");
        Guerreiro teste = new Guerreiro("Teste");
        int pontosAteOLimite = Atributos.LIMITE_POR_ATRIBUTO - teste.getAtributos().forca();
        teste.ganharPontos(pontosAteOLimite + 1);
        teste.distribuirPontos(Atributo.FORCA, pontosAteOLimite);
        System.out.println("Força levada a " + teste.getAtributos().forca() + "; pontos restantes: " + teste.getPontosDisponiveis());
        System.out.println("Tentando colocar mais 1 ponto em força...");
        tentar(() -> teste.distribuirPontos(Atributo.FORCA, 1));
        System.out.println("Pontos continuam disponíveis: " + teste.getPontosDisponiveis() + " | " + teste.getAtributos());
    }

    private static void tentar(Runnable acao) {
        try {
            acao.run();
            System.out.println("  (aceito)");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("  REJEITADO: " + e.getMessage());
        }
    }
}
