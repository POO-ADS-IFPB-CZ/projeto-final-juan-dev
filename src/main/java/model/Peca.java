package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Peca {
    private int idPeca;
    private String descricao;
    private double valorUnitario;
    private int estoque;

    public Peca(String descricao, double valorUnitario, int estoque) {
        this.descricao = descricao;
        this.valorUnitario = valorUnitario;
        this.estoque = estoque;
    }

    @Override
    public String toString() {
        return descricao + " (R$ " + String.format("%.2f", valorUnitario) + ") - Estoque: " + estoque;
    }
}
