package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Mecanico {
    private int idMecanico;
    private String nome;
    private String endereco;
    private String especialidade;
    private Equipe equipe;

    public Mecanico(String nome, String endereco, String especialidade, Equipe equipe) {
        this.nome = nome;
        this.endereco = endereco;
        this.especialidade = especialidade;
        this.equipe = equipe;
    }
}
