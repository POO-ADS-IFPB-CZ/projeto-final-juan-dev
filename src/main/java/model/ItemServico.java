package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemServico {
    private int idItemServico;
    private Servico servico;
    private int quantidade;
    private double valor;

    public ItemServico(Servico servico, int quantidade) {
        this.servico = servico;
        this.quantidade = quantidade;
        this.valor = servico.getValorMaoDeObra() * quantidade;
    }
}
