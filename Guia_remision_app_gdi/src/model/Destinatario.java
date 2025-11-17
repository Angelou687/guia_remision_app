package model;

public class Destinatario {

    private String ruc;
    private String nombre;
    private String numeroTelefono;
    private String calleDireccion;
    private String codigoUbigeo;
    private String gmail;

    public Destinatario() {}

    public Destinatario(String ruc, String nombre, String numeroTelefono,
                        String calleDireccion, String codigoUbigeo, String gmail) {
        this.ruc = ruc;
        this.nombre = nombre;
        this.numeroTelefono = numeroTelefono;
        this.calleDireccion = calleDireccion;
        this.codigoUbigeo = codigoUbigeo;
        this.gmail = gmail;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public String getCalleDireccion() {
        return calleDireccion;
    }

    public void setCalleDireccion(String calleDireccion) {
        this.calleDireccion = calleDireccion;
    }

    public String getCodigoUbigeo() {
        return codigoUbigeo;
    }

    public void setCodigoUbigeo(String codigoUbigeo) {
        this.codigoUbigeo = codigoUbigeo;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }
}
