package model;

public class DetalleGuia {
    private int nro;
    private String bienNormalizado;
    private String codigoBien;
    private String codigoProductoSunat;
    private String partidaArancelaria;
    private String codigoGtin;
    private String descripcion;
    private String unidadMedida;
    private String cantidad;

    public DetalleGuia() {}

    public DetalleGuia(int nro, String bienNormalizado, String codigoBien, String codigoProductoSunat,
                       String partidaArancelaria, String codigoGtin, String descripcion,
                       String unidadMedida, String cantidad) {
        this.nro = nro;
        this.bienNormalizado = bienNormalizado;
        this.codigoBien = codigoBien;
        this.codigoProductoSunat = codigoProductoSunat;
        this.partidaArancelaria = partidaArancelaria;
        this.codigoGtin = codigoGtin;
        this.descripcion = descripcion;
        this.unidadMedida = unidadMedida;
        this.cantidad = cantidad;
    }

    // getters / setters
    public int getNro() { return nro; }
    public void setNro(int nro) { this.nro = nro; }
    public String getBienNormalizado() { return bienNormalizado; }
    public void setBienNormalizado(String bienNormalizado) { this.bienNormalizado = bienNormalizado; }
    public String getCodigoBien() { return codigoBien; }
    public void setCodigoBien(String codigoBien) { this.codigoBien = codigoBien; }
    public String getCodigoProductoSunat() { return codigoProductoSunat; }
    public void setCodigoProductoSunat(String codigoProductoSunat) { this.codigoProductoSunat = codigoProductoSunat; }
    public String getPartidaArancelaria() { return partidaArancelaria; }
    public void setPartidaArancelaria(String partidaArancelaria) { this.partidaArancelaria = partidaArancelaria; }
    public String getCodigoGtin() { return codigoGtin; }
    public void setCodigoGtin(String codigoGtin) { this.codigoGtin = codigoGtin; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }
    public String getCantidad() { return cantidad; }
    public void setCantidad(String cantidad) { this.cantidad = cantidad; }
}