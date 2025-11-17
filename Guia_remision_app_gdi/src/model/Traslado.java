package model;

import java.sql.Timestamp;

public class Traslado {

    private String codigoTraslado;
    private String codigoGuia;
    private String placa;
    private String licencia;
    private Timestamp fechaInicio;
    private Timestamp fechaFin;
    private String estadoTraslado;
    private String observaciones;

    public Traslado() {}

    public Traslado(String codigoTraslado, String codigoGuia,
                    String placa, String licencia,
                    Timestamp fechaInicio, Timestamp fechaFin,
                    String estadoTraslado, String observaciones) {
        this.codigoTraslado = codigoTraslado;
        this.codigoGuia = codigoGuia;
        this.placa = placa;
        this.licencia = licencia;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estadoTraslado = estadoTraslado;
        this.observaciones = observaciones;
    }

    public String getCodigoTraslado() {
        return codigoTraslado;
    }

    public void setCodigoTraslado(String codigoTraslado) {
        this.codigoTraslado = codigoTraslado;
    }

    public String getCodigoGuia() {
        return codigoGuia;
    }

    public void setCodigoGuia(String codigoGuia) {
        this.codigoGuia = codigoGuia;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getLicencia() {
        return licencia;
    }

    public void setLicencia(String licencia) {
        this.licencia = licencia;
    }

    public Timestamp getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Timestamp fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Timestamp getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Timestamp fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getEstadoTraslado() {
        return estadoTraslado;
    }

    public void setEstadoTraslado(String estadoTraslado) {
        this.estadoTraslado = estadoTraslado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public String toString() {
        return codigoTraslado + " (" + codigoGuia + ")";
    }
}
