package entities;

public class Produto {
    private String nome;
    private Integer quantidade;
    private double peso;

    public Produto(String nome, Integer quantidade, double peso) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.peso = peso;
    }

    public String getNome() {
        return nome;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public double getPeso() {
        return peso;
    }

}
