package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Veiculo {
    private int idVeiculo;
    private String placa;
    private String modelo;
    private int ano;
    private String cor;
    private Cliente cliente;

    public Veiculo(String placa, String modelo, int ano, String cor, Cliente cliente) {
        this.placa = placa;
        this.modelo = modelo;
        this.ano = ano;
        this.cor = cor;
        this.cliente = cliente;
    }
}
