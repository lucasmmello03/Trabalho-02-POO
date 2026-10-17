import java.util.EnumMap;
import java.util.List;
import java.util.Map;


public abstract class Personagem implements Combatente {

    public static final int DANO_MINIMO = 1;

    private final String nome;
    private final int vidaMaxima;
    private int vida;
    private final int manaMaxima;
    private int mana;
    private Atributos atributos;
    private int pontosDisponiveis;
    private int bonusTemporarioDeDefesa;
    private int turnosDeBuff;

    private final Inventario inventario = new Inventario(Inventario.CAPACIDADE_PADRAO);
    private final Map<Slot, Equipavel> equipamentos = new EnumMap<>(Slot.class);
    private final List<Habilidade> habilidades;

    
    protected Personagem(String nome, int vidaMaxima, int manaMaxima, Atributos atributos,
                         int pocoesDeVidaIniciais, List<Habilidade> habilidades) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("o personagem precisa de um nome");
        }
        if (vidaMaxima <= 0) {
            throw new IllegalArgumentException("a vida máxima deve ser positiva");
        }
        if (manaMaxima < 0) {
            throw new IllegalArgumentException("a mana máxima não pode ser negativa");
        }
        if (atributos == null) {
            throw new IllegalArgumentException("o personagem precisa de atributos");
        }
        if (pocoesDeVidaIniciais < 0) {
            throw new IllegalArgumentException("a quantidade de poções não pode ser negativa");
        }
        this.nome = nome;
        this.vidaMaxima = vidaMaxima;
        this.vida = vidaMaxima;
        this.manaMaxima = manaMaxima;
        this.mana = manaMaxima;
        this.atributos = atributos;
        this.habilidades = List.copyOf(habilidades);
        for (int i = 0; i < pocoesDeVidaIniciais; i++) {
            inventario.adicionar(new PocaoDeVida());
        }
    }

    
    @Override
    public abstract int atacar(Combatente alvo);

    
    @Override
    public int receberDano(int danoBruto) {
        if (danoBruto < 0) {
            throw new IllegalArgumentException("o dano não pode ser negativo");
        }
        int danoEfetivo = Math.max(DANO_MINIMO, danoBruto - getDefesa());
        vida = Math.max(0, vida - danoEfetivo);
        return danoEfetivo;
    }

    @Override
    public boolean estaVivo() {
        return vida > 0;
    }


    public void curar(int quantidade) {
        if (quantidade < 0) {
            throw new IllegalArgumentException("a cura não pode ser negativa");
        }
        if (!estaVivo()) {
            throw new IllegalStateException(nome + " está morto e não pode ser curado");
        }
        vida = Math.min(vidaMaxima, vida + quantidade);
    }

    public void recuperarMana(int quantidade) {
        if (quantidade < 0) {
            throw new IllegalArgumentException("a quantidade de mana não pode ser negativa");
        }
        mana = Math.min(manaMaxima, mana + quantidade);
    }

    public void gastarMana(int quantidade) {
        if (quantidade < 0) {
            throw new IllegalArgumentException("a quantidade de mana não pode ser negativa");
        }
        if (quantidade > mana) {
            throw new IllegalStateException(nome + " não tem mana suficiente (" + mana + "/" + quantidade + ")");
        }
        mana -= quantidade;
    }

    public String usarHabilidade(Habilidade habilidade, Personagem alvo) {
        if (habilidade == null) {
            throw new IllegalArgumentException("habilidade não pode ser nula");
        }
        if (!habilidades.contains(habilidade)) {
            throw new IllegalArgumentException(nome + " não conhece a habilidade " + habilidade.getNome());
        }
        if (!habilidade.podeUsar(this)) {
            throw new IllegalStateException(nome + " não tem mana para usar " + habilidade.getNome());
        }
        gastarMana(habilidade.getCustoMana());
        return habilidade.executar(this, alvo);
    }

   
    public void usarPocaoDeVida() {
        if (!inventario.temPocaoDeVida()) {
            throw new IllegalStateException(nome + " não tem poção de vida");
        }
        Usavel pocao = inventario.retirar(PocaoDeVida.class);
        pocao.usar(this);
    }

    public void usarPocaoDeMana() {
        if (!inventario.temPocaoDeMana()) {
            throw new IllegalStateException(nome + " não tem poção de mana");
        }
        Usavel pocao = inventario.retirar(PocaoDeMana.class);
        pocao.usar(this);
    }

    public void equipar(Equipavel equipamento) {
        if (equipamento == null) {
            throw new IllegalArgumentException("equipamento não pode ser nulo");
        }
        equipamentos.put(equipamento.getSlot(), equipamento);
    }

    public int getBonusDeDano() {
        int total = 0;
        for (Equipavel equipamento : equipamentos.values()) {
            total += equipamento.bonusDeDano();
        }
        return total;
    }


    public void ganharPontos(int pontos) {
        if (pontos <= 0) {
            throw new IllegalArgumentException("a quantidade de pontos deve ser positiva");
        }
        pontosDisponiveis += pontos;
    }

   
    public void distribuirPontos(Atributo atributo, int pontos) {
        if (pontos <= 0) {
            throw new IllegalArgumentException("a quantidade de pontos deve ser positiva");
        }
        if (pontos > pontosDisponiveis) {
            throw new IllegalArgumentException("pontos insuficientes: há " + pontosDisponiveis + " disponíveis");
        }
        Atributos novos = atributos.somar(atributo, pontos);
        atributos = novos;
        pontosDisponiveis -= pontos;
    }

    public void aplicarBuff(int bonus, int turnos) {
        if (bonus <= 0 || turnos <= 0) {
            throw new IllegalArgumentException("bônus e turnos do buff devem ser positivos");
        }
        bonusTemporarioDeDefesa = bonus;
        turnosDeBuff = turnos;
    }

    public void encerrarTurno() {
        if (turnosDeBuff > 0) {
            turnosDeBuff--;
            if (turnosDeBuff == 0) {
                bonusTemporarioDeDefesa = 0;
            }
        }
    }

    public int getForcaTotal() {
        return atributos.forca();
    }

    public int getMagiaTotal() {
        return atributos.magia();
    }

    @Override
    public int getDefesa() {
        int bonusDosEquipamentos = 0;
        for (Equipavel equipamento : equipamentos.values()) {
            bonusDosEquipamentos += equipamento.bonusDeDefesa();
        }
        return atributos.defesa() + bonusDosEquipamentos + bonusTemporarioDeDefesa;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public int getVida() {
        return vida;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getMana() {
        return mana;
    }

    public int getManaMaxima() {
        return manaMaxima;
    }

    public Atributos getAtributos() {
        return atributos;
    }

    public int getPontosDisponiveis() {
        return pontosDisponiveis;
    }

    public List<Habilidade> getHabilidades() {
        return habilidades;
    }

    public Inventario getInventario() {
        return inventario;
    }
}