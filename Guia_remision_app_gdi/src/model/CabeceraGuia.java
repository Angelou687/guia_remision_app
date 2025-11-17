package model;

import java.sql.Date;
import java.sql.Time;

public class CabeceraGuia {

    private String codigoGuia;
    private String serie;
    private String numero;
    private String codOrden;
    private String rucRemitente;
    private Date   fechaEmision;
    private Time   horaEmision;
    private String estadoGuia;

    public CabeceraGuia() {}

    public CabeceraGuia(String codigoGuia, String serie, String numero,
                        String codOrden, String rucRemitente,
                        Date fechaEmision, Time horaEmision,
                        String estadoGuia) {
        this.codigoGuia = codigoGuia;
        this.serie = serie;
        this.numero = numero;
        this.codOrden = codOrden;
        this.rucRemitente = rucRemitente;
        this.fechaEmision = fechaEmision;
        this.horaEmision = horaEmision;
        this.estadoGuia = estadoGuia;
    }

    public String getCodigoGuia() {
        return codigoGuia;
    }

    public void setCodigoGuia(String codigoGuia) {
        this.codigoGuia = codigoGuia;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCodOrden() {
        return codOrden;
    }

    public void setCodOrden(String codOrden) {
        this.codOrden = codOrden;
    }

    public String getRucRemitente() {
        return rucRemitente;
    }

    public void setRucRemitente(String rucRemitente) {
        this.rucRemitente = rucRemitente;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Time getHoraEmision() {
        return horaEmision;
    }

    public void setHoraEmision(Time horaEmision) {
        this.horaEmision = horaEmision;
    }

    public String getEstadoGuia() {
        return estadoGuia;
    }

    public void setEstadoGuia(String estadoGuia) {
        this.estadoGuia = estadoGuia;
    }

    @Override
    public String toString() {
        return codigoGuia + " - " + estadoGuia;
    }
}
