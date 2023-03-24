package entities;

import enums.CaminhaoEnum;

public class Caminhao {
    private String porte;
    private CaminhaoEnum caminhaoEnum;

    public Caminhao(String porte, CaminhaoEnum caminhaoEnum) {
        this.porte = porte;
        this.caminhaoEnum = caminhaoEnum;
    }

    public String getPorte() {
        return porte;
    }

    public CaminhaoEnum getCaminhaoEnum() {
        return caminhaoEnum;
    }

}
