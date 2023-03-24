package enums;

public enum CaminhaoEnum {
    PEQUENO_PORTE(4.87),
    MEDIO_PORTE(11.92),
    GRANDE_PORTE(27.44);

    private final double valor;

    CaminhaoEnum(double valor) {
        this.valor = valor;
    }

    public double getValor() {
        return valor;
    }
}
