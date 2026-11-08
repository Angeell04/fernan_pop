package models;

public class Producto {
    //Atributos
    private long id;
    private String titulo;
    private String descripcion;
    private double precio;
    private String estado;
    private int vendido;

    //Creamos el constructor

    public Producto(String descripcion, String estado, long id, double precio, String titulo, int vendido) {
        this.descripcion = descripcion;
        this.estado = estado;
        this.id = id;
        this.precio = precio;
        this.titulo = titulo;
        this.vendido = vendido;
    }

    //creamos el constructor copia por si queremos hacer una copia de producto
    public Producto (Producto p){
        descripcion = p.descripcion;
        estado = p.estado;
        id = p.id;
        precio = p.precio;
        titulo = p.titulo;
        vendido = p.vendido;
    }

    public int getVendido() {
        return vendido;
    }

    public void setVendido(int vendido) {
        this.vendido = vendido;
    }
//Creamos los getters and setters

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    //Creamos el to string

    @Override
    public String toString() {
        return "********************************************" + "\n" +
                "Información del producto. id: " + id + "\n" +
                "Título: " + titulo + "\n" +
                "Descripción: " + descripcion + "\n" +
                "Precio: " + precio + "\n" +
                "Estado: " + estado ;
    }

}
