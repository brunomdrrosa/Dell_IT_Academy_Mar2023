package entities;

import java.util.List;

public class Viagem {
    private String nomesCidades;
    private int distanciaTotal;
    private String nomesProdutos;
    private List<Integer> quantidadePortesCaminhoes;
    private double valorTotalViagem;
    private double valorUnitarioMedio;

    public Viagem(String nomesCidades, int distanciaTotal, String nomesProdutos, List<Integer> quantidadePortesCaminhoes,
                  double valorTotalViagem, double valorUnitarioMedio) {
        this.nomesCidades = nomesCidades;
        this.distanciaTotal = distanciaTotal;
        this.nomesProdutos = nomesProdutos;
        this.quantidadePortesCaminhoes = quantidadePortesCaminhoes;
        this.valorTotalViagem = valorTotalViagem;
        this.valorUnitarioMedio = valorUnitarioMedio;
    }

    public String getNomesCidades() {
        return nomesCidades;
    }

    public int getDistanciaTotal() {
        return distanciaTotal;
    }

    public String getNomesProdutos() {
        return nomesProdutos;
    }

    public List<Integer> getQuantidadePortesCaminhoes() {
        return quantidadePortesCaminhoes;
    }

    public double getValorTotalViagem() {
        return valorTotalViagem;
    }

    public double getValorUnitarioMedio() {
        return valorUnitarioMedio;
    }

}
