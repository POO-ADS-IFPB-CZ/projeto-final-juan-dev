package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Equipe {
    private int idEquipe;
    private String nomeEquipe;

    public Equipe(String nomeEquipe) {
        this.nomeEquipe = nomeEquipe;
    }
}
