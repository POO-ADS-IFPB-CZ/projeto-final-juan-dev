package model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Servico {
    private int idServico;
    private String descricao;
    private double valorMaoDeObra;

    public Servico(String descricao, double valorMaoDeObra) {
        this.descricao = descricao;
        this.valorMaoDeObra = valorMaoDeObra;
    }

    @Override
    public String toString() {
        return descricao + " (R$ " + String.format("%.2f", valorMaoDeObra) + ")";
    }
}
