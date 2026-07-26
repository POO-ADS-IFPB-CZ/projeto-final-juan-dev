package model;
/**
 * Representa os possíveis estados de uma Ordem de Serviço.
 * Usar enum em vez de String evita erros de digitação e deixa
 * o código mais seguro (o compilador garante que só esses valores existem).
 */
public enum Status {
    ABERTA("Aberta"),
    EM_ANDAMENTO("Em andamento"),
    CONCLUIDA("Concluída"),
    CANCELADA("Cancelada");

    private final String descricao;

    Status(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
