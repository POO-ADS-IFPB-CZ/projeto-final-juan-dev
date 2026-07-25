package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPeca {
    private int idItemPeca;
    private Peca peca;
    private int quantidade;
    private double valor;

    public ItemPeca(Peca peca, int quantidade) {
        this.peca = peca;
        this.quantidade = quantidade;
        this.valor = peca.getValorUnitario() * quantidade;
    }
}
