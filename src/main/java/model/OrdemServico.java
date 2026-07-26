package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrdemServico {

    private int idOrdemServico;
    private LocalDate dataEmissao;
    private LocalDate dataPrevistaConclusao;
    private LocalDate dataConclusao;
    private double valorTotal;
    private Status status;
    private Veiculo veiculo;
    private Equipe equipe;
    private List<ItemServico> itensServico = new ArrayList<>();
    private List<ItemPeca> itensPeca = new ArrayList<>();

    public OrdemServico() {
        this.dataEmissao = LocalDate.now();
        this.status = Status.ABERTA;
    }

    public int getIdOrdemServico() {
        return idOrdemServico;
    }

    public void setIdOrdemServico(int idOrdemServico) {
        this.idOrdemServico = idOrdemServico;
    }

    public LocalDate getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(LocalDate dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public LocalDate getDataPrevistaConclusao() {
        return dataPrevistaConclusao;
    }

    public void setDataPrevistaConclusao(LocalDate dataPrevistaConclusao) {
        this.dataPrevistaConclusao = dataPrevistaConclusao;
    }

    public LocalDate getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDate dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public Equipe getEquipe() {
        return equipe;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }

    public List<ItemServico> getItensServico() {
        return itensServico;
    }

    public void setItensServico(List<ItemServico> itensServico) {
        this.itensServico = itensServico;
    }

    public List<ItemPeca> getItensPeca() {
        return itensPeca;
    }

    public void setItensPeca(List<ItemPeca> itensPeca) {
        this.itensPeca = itensPeca;
    }

    public void adicionarItemServico(ItemServico item) {
        this.itensServico.add(item);
        recalcularValorTotal();
    }

    public void adicionarItemPeca(ItemPeca item) {
        this.itensPeca.add(item);
        recalcularValorTotal();
    }

    public void removerItemServico(ItemServico item) {
        this.itensServico.remove(item);
        recalcularValorTotal();
    }

    public void removerItemPeca(ItemPeca item) {
        this.itensPeca.remove(item);
        recalcularValorTotal();
    }

    /**
     * Regra de negócio central da OS: o valor total é sempre a soma
     * de todos os itens de serviço e peças adicionados.
     */
    public void recalcularValorTotal() {
        double total = 0;
        for (ItemServico item : itensServico) {
            total += item.getValor();
        }
        for (ItemPeca item : itensPeca) {
            total += item.getValor();
        }
        this.valorTotal = total;
    }
}
