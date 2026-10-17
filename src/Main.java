import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {

    private static final int OPCAO_ATACAR = 1;
    private static final int OPCAO_HABILIDADE = 2;
    private static final int OPCAO_POCAO_VIDA = 3;
    private static final int OPCAO_POCAO_MANA = 4;

    private final Scanner entrada = new Scanner(System.in);

    public static void main(String[] args) {
        new Main().jogar();
    }

    private void jogar() {
        System.out.println("=== ARENA ===");
        System.out.print("Nome do personagem: ");
        String nome = entrada.nextLine().trim();
        if (nome.isEmpty()) {
            nome = "Herói";
        }
        Personagem jogador = escolherClasse(nome);

        Random random = new Random();
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

        while (jogo.getEstado() == EstadoDoJogo.EM_ANDAMENTO) {
            Batalha batalha = jogo.iniciarProximaBatalha();
            System.out.println("\n>>> Um " + batalha.getMonstro().getNome() + " aparece!");
            lutar(batalha, jogador);
            boolean inventarioCheio = jogador.getInventario().getItens().size() >= jogador.getInventario().getCapacidade();
            Item recompensa = jogo.concluirBatalha(batalha);
            if (batalha.getEstado() == EstadoDaBatalha.VITORIA) {
                aposVitoria(jogador, recompensa, inventarioCheio);
            }
        }

        System.out.println();
        if (jogo.getEstado() == EstadoDoJogo.VENCIDO) {
            System.out.println("*** VITÓRIA! ***");
        } else {
            System.out.println("*** DERROTA ***");
        }
        System.out.println(status(jogador));
    }

    private Personagem escolherClasse(String nome) {
        System.out.println("Escolha a classe: 1) Guerreiro  2) Arqueiro  3) Mago");
        return switch (lerInteiro(1, 3)) {
            case 1 -> new Guerreiro(nome);
            case 2 -> new Arqueiro(nome);
            default -> new Mago(nome);
        };
    }

    private void lutar(Batalha batalha, Personagem jogador) {
        Monstro monstro = batalha.getMonstro();
        while (batalha.getEstado() == EstadoDaBatalha.EM_ANDAMENTO) {
            System.out.println("\n--- Turno " + batalha.getTurnoAtual() + " ---");
            System.out.println(status(jogador));
            System.out.println(monstro.getNome() + ": vida " + monstro.getVida() + "/" + monstro.getVidaMaxima());
            EscolhaDoJogador escolha = lerEscolha(jogador);
            AcaoDoMonstro acao = monstro.decidirAcao(jogador, batalha.getTurnoAtual());
            try {
                ResultadoDoTurno resultado = batalha.executarTurno(escolha, acao);
                for (String evento : resultado.eventos()) {
                    System.out.println("  " + evento);
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("  Ação inválida: " + e.getMessage() + " (nada aconteceu, escolha de novo)");
            }
        }
    }

    private EscolhaDoJogador lerEscolha(Personagem jogador) {
        System.out.println("1) Atacar  2) Habilidade  3) Poção de vida ("
                + jogador.getInventario().contar(PocaoDeVida.class) + ")  4) Poção de mana ("
                + jogador.getInventario().contar(PocaoDeMana.class) + ")");
        int opcao = lerInteiro(OPCAO_ATACAR, OPCAO_POCAO_MANA);
        if (opcao == OPCAO_HABILIDADE) {
            List<Habilidade> habilidades = jogador.getHabilidades();
            for (int i = 0; i < habilidades.size(); i++) {
                Habilidade h = habilidades.get(i);
                System.out.println("  " + (i + 1) + ") " + h.getNome() + " (custo " + h.getCustoMana() + " de mana)");
            }
            Habilidade escolhida = habilidades.get(lerInteiro(1, habilidades.size()) - 1);
            return new EscolhaDoJogador(AcaoDoJogador.USAR_HABILIDADE, escolhida);
        }
        if (opcao == OPCAO_POCAO_VIDA) {
            return new EscolhaDoJogador(AcaoDoJogador.USAR_POCAO_VIDA, null);
        }
        if (opcao == OPCAO_POCAO_MANA) {
            return new EscolhaDoJogador(AcaoDoJogador.USAR_POCAO_MANA, null);
        }
        return new EscolhaDoJogador(AcaoDoJogador.ATACAR, null);
    }

    private void aposVitoria(Personagem jogador, Item recompensa, boolean inventarioCheio) {
        System.out.println("\nVitória! Você recuperou vida e mana e ganhou " + Jogo.PONTOS_POR_VITORIA + " pontos.");
        if (inventarioCheio) {
            System.out.println("Recompensa: " + recompensa.descricao() + ", mas o inventário está cheio e ela foi descartada.");
            distribuirPontos(jogador);
            return;
        }
        System.out.println("Recompensa: " + recompensa.descricao() + " (guardada no inventário)");
        distribuirPontos(jogador);

        if (recompensa instanceof Equipavel equipavel) {
            System.out.println("Deseja equipar " + recompensa.getNome() + "? 1) Sim  2) Não");
            if (lerInteiro(1, 2) == 1) {
                jogador.equipar(equipavel);
                System.out.println(recompensa.getNome() + " equipado.");
            }
        }
    }

    private void distribuirPontos(Personagem jogador) {
        while (jogador.getPontosDisponiveis() > 0) {
            System.out.println("Pontos disponíveis: " + jogador.getPontosDisponiveis() + " | " + jogador.getAtributos());
            System.out.println("Onde colocar? 1) Força  2) Defesa  3) Magia  0) Guardar para depois");
            int opcao = lerInteiro(0, 3);
            if (opcao == 0) {
                return;
            }
            Atributo atributo = Atributo.values()[opcao - 1];
            System.out.print("Quantos pontos? ");
            int pontos = lerInteiro(1, jogador.getPontosDisponiveis());
            try {
                jogador.distribuirPontos(atributo, pontos);
            } catch (IllegalArgumentException e) {
                System.out.println("Rejeitado: " + e.getMessage() + " (nenhum ponto foi gasto)");
            }
        }
    }

    private String status(Personagem p) {
        return p.getNome() + ": vida " + p.getVida() + "/" + p.getVidaMaxima()
                + " | mana " + p.getMana() + "/" + p.getManaMaxima()
                + " | defesa " + p.getDefesa();
    }

    private int lerInteiro(int minimo, int maximo) {
        while (true) {
            String linha = entrada.nextLine().trim();
            try {
                int valor = Integer.parseInt(linha);
                if (valor >= minimo && valor <= maximo) {
                    return valor;
                }
            } catch (NumberFormatException e) {
            }
            System.out.print("Digite um número entre " + minimo + " e " + maximo + ": ");
        }
    }
}
