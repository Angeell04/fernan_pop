package models;

import java.util.Calendar;

public class Trato {
    //Creamos los atributos
    private int id;
    private String tipo;
    private String emailOtroUser;
    private Producto producto;
    private Calendar fecha;
    private double precio;
    private String comentario;
    private int puntuacion;

    //Creamos el constructor y el constructor copia por si queremos hacer alguna copia

    public Trato(String comentario, String emailOtroUser, Calendar fecha, int id, double precio, Producto producto, int puntuacion, String tipo) {
        this.comentario = comentario;
        this.emailOtroUser = emailOtroUser;
        this.fecha = Calendar.getInstance();
        this.id = id;
        this.precio = precio;
        this.producto = producto;
        this.puntuacion = puntuacion;
        this.tipo = tipo;
    }

    public Trato(Producto producto, String emailOtroUser, double precio, int id){
        this.producto = producto;
        this.emailOtroUser = emailOtroUser;
        this.precio = precio;
        this.fecha = Calendar.getInstance();
        this.id = id;
    }

    public Trato (Trato t){
        comentario = t.comentario;
        emailOtroUser = t.emailOtroUser;
        fecha = t.fecha;
        id = t.id;
        precio = t.precio;
        producto = t.producto;
        puntuacion = t.puntuacion;
        tipo = t.tipo;
    }

    //Getters and setters

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Calendar getFecha() {
        return fecha;
    }

    public void setFecha(Calendar fecha) {
        this.fecha = fecha;
    }

    public String getEmailOtroUser() {
        return emailOtroUser;
    }

    public void setEmailOtroUser(String emailOtroUser) {
        this.emailOtroUser = emailOtroUser;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    @Override
    public String toString() {
        return "\n--- Detalle del Trato ---" +
                "\nID: " + id +
                "\nProducto: " + (producto != null ? producto.getTitulo() : "N/A") +
                "\nPrecio: " + precio + "€" +
                "\nFecha: " + (fecha != null ? fecha.getTime().toString() : "Sin fecha") +
                "\nComentario: " + (comentario != null ? comentario : "Sin valorar") +
                "\nPuntuación: " + (puntuacion > 0 ? puntuacion + "/5" : "Pendiente") +
                "\n-------------------------";
    }
}
