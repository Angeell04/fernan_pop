package models;

public class Oferta {
    private Usuario comprador;
    private Usuario vendedor;
    private float precio;
    Producto producto;

    public Oferta(Usuario comprador, float precio, Usuario vendedor, Producto producto) {
        this.comprador = comprador;
        this.precio = precio;
        this.vendedor = vendedor;
        this.producto = producto;
    }

    public Usuario getComprador() {
        return comprador;
    }

    public void setComprador(Usuario comprador) {
        this.comprador = comprador;
    }

    public Usuario getVendedor() {
        return vendedor;
    }

    public void setVendedor(Usuario vendedor) {
        this.vendedor = vendedor;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
