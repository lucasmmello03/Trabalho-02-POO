# Trabalho II: Arena, um RPG de combate por turnos

- **Aluno:** Lucas Melo De Mello
- **Turma:** Terceiro Semestre
- **Disciplina:** Programação Orientada a Objetos (TADS)
- **Java:** 17 ou superior (desenvolvido e testado com OpenJDK 21)
- **Vídeo:** _[colar o link aqui]_

Mini-RPG de console em Java puro, sem bibliotecas externas. O jogador escolhe Guerreiro, Arqueiro ou Mago
e enfrenta, em sequência, um Goblin, um Orc e um Dragão.

## Como compilar e executar

Todas as classes estão em `src/`, no pacote padrão (sem pacotes).

```bash
# compilar
javac -d out src/*.java

# jogo interativo
java -cp out Main

# execução roteirizada (saída sempre igual)
java -cp out MainRoteirizado
```

No Windows (cmd/PowerShell), os comandos são os mesmos, trocando `/` por `\` se necessário:
`javac -d out src\*.java`. Se os acentos aparecerem errados no terminal, rode `chcp 65001` antes, ou
execute com `java -Dstdout.encoding=UTF-8 -cp out MainRoteirizado`.

A saída do `MainRoteirizado` está em [`saida-main-roteirizado.txt`](saida-main-roteirizado.txt).

## Estrutura

| Grupo | Classes |
|---|---|
| Personagens | `Combatente` (interface), `Personagem` (abstrata), `LutadorFisico` (abstrata), `Guerreiro`, `Arqueiro`, `Mago` |
| Monstros | `Monstro` (abstrata), `Goblin`, `Orc`, `Dragao`, `EstrategiaDoMonstro` (interface), `EstrategiaPadrao`, `EstrategiaBrutal` |
| Habilidades | `Habilidade` (interface), `AtaqueFisico`, `Magia`, `Cura`, `Buff` |
| Itens | `Item` (abstrata), `Equipavel` e `Usavel` (interfaces), `Arma`, `Armadura`, `Consumivel` (abstrata), `PocaoDeVida`, `PocaoDeMana`, `Inventario` |
| Batalha e jogo | `Batalha`, `Jogo`, `TabelaDeRecompensas` |
| Records e enums | `Atributos`, `EscolhaDoJogador`, `ResultadoDoTurno`; `Slot`, `Atributo`, `Alvo`, `AcaoDoJogador`, `AcaoDoMonstro`, `EstadoDaBatalha`, `EstadoDoJogo` |
| Entrada e saída | `Main`, `MainRoteirizado` (as únicas classes que usam `System.out`/`Scanner`) |

Diagrama de classes final: [`DIAGRAMA.md`](DIAGRAMA.md) (imagem em [`diagrama-de-classes.svg`](diagrama-de-classes.svg)).
Diagrama original do enunciado: [`DIAGRAMA-ENUNCIADO.md`](DIAGRAMA-ENUNCIADO.md).

## Itens opcionais implementados

Nenhum.

## Decisões de design

O código segue o diagrama do enunciado **sem classes a mais, sem classes a menos e com as mesmas assinaturas**.

1. **Único método acrescentado: `Inventario.retirar(Class)`.** Para beber uma poção, o `Personagem` precisa
   pegar uma poção da lista do inventário. Sem esse método, ele teria que percorrer os itens perguntando
   `instanceof PocaoDeVida`, o que o enunciado proíbe fora do `Inventario`. O `retirar` usa a mesma técnica do
   `contar(Class)` (permitido pelo enunciado), dentro do próprio `Inventario`.

2. **Getters.** Os campos privados do diagrama têm getters quando alguém de fora precisa ler o valor (por
   exemplo `getMana()`, `getVidaMaxima()`, `getInventario()`, `Batalha.getMonstro()`). Eles não aparecem no
   diagrama, como é costume.

3. **O `Arqueiro` sobrescreve `atacar()` sem chamar `super.atacar()`.** O `atacar()` herdado do
   `LutadorFisico` já aplica o dano no alvo; se o Arqueiro chamasse `super.atacar()`, o dano já teria sido
   aplicado e não haveria como somar o bônus de precisão. Então ele calcula
   `força + bônus dos equipamentos + 3` e aplica de uma vez. A sobrescrita com `super` aparece no
   `Guerreiro.receberDano()`.

4. **Itens equipados continuam no inventário.** O item ganho na batalha vai para o inventário; ao equipá-lo,
   ele continua lá. Equipar em um slot ocupado substitui o antigo, que deixa de estar equipado e continua no
   inventário como item comum ("volta ao inventário"). Motivo: `Equipavel` é uma interface e não é um `Item`;
   tirar e devolver o item exigiria um cast. Consequência: itens equipados ocupam espaço no inventário.

5. **Recompensa com inventário cheio.** O `Inventario` rejeita o item com exceção. O `Jogo` não guarda o item,
   mas o devolve mesmo assim, e o `Main` avisa o jogador que ele foi descartado. Nunca some em silêncio.

6. **`Jogo.concluirBatalha()` devolve `null` na derrota**, porque não há item a devolver.

7. **Ataque especial do monstro.** Ele não fica na lista de habilidades do monstro (não custa mana). A
   `Batalha` o executa com `monstro.getAtaqueEspecial().executar(monstro, jogador)`. A regra "só a partir do
   2º turno" usa a constante `Monstro.TURNO_MINIMO_DO_ESPECIAL`: as estratégias a usam para decidir e a
   `Batalha` para validar.

8. **Sem pacotes.** Todas as classes ficam em `src/`, no pacote padrão.

## Onde as restrições do enunciado são atendidas

- **`instanceof` / verificação de tipo:** só nas duas exceções permitidas.
  - `Inventario.contar(Class)` e `Inventario.retirar(Class)` usam `tipo.isInstance(item)`, porque o
    inventário guarda `Item` genérico e precisa responder "quantas poções de vida eu tenho?" e entregar uma.
  - `Main` e `MainRoteirizado` perguntam `recompensa instanceof Equipavel` para oferecer equipar o item.
- **I/O:** `System.out`, `Scanner` e `System.in` só em `Main` e `MainRoteirizado`.
- **`Random`:** criado só nos dois `Main`. `EstrategiaPadrao` e `TabelaDeRecompensas` o recebem pelo construtor.
- **`static`:** só constantes `static final` (e métodos estáticos sem estado).
- **Coleções:** nenhuma lista interna sai sem cópia (`List.copyOf`).
- **Números de regra:** em constantes nomeadas (`Guerreiro.VIDA_INICIAL`,
  `EstrategiaPadrao.CHANCE_DE_USAR_POCAO_PERCENTUAL`, `Monstro.TURNO_MINIMO_DO_ESPECIAL`...). Os valores dos
  itens da tabela de recompensas (dano da Adaga etc.) são conteúdo, montado no `Main`.
- **Validar antes de alterar:** `Batalha` confere tudo (método privado `validar`) antes do turno. `Personagem.distribuirPontos()`
  calcula o novo `Atributos` (que pode lançar exceção) antes de descontar os pontos.
