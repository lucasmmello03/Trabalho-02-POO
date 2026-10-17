public record EscolhaDoJogador(AcaoDoJogador acao, Habilidade habilidade) {

    public EscolhaDoJogador {
        if (acao == null) {
            throw new IllegalArgumentException("a escolha precisa de uma ação");
        }
        if (acao == AcaoDoJogador.USAR_HABILIDADE && habilidade == null) {
            throw new IllegalArgumentException("USAR_HABILIDADE exige uma habilidade");
        }
        if (acao != AcaoDoJogador.USAR_HABILIDADE && habilidade != null) {
            throw new IllegalArgumentException(acao + " não recebe habilidade");
        }
    }
}
