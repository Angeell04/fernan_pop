package models;

import java.util.ArrayList;

public class Usuario {
    //Atributos
    private int id;
    private String nombre;
    private String apel;
    private String clave;
    private int movil;
    private String mail;
    private ArrayList<Producto> enVenta;
    private ArrayList<Trato> ventas;
    private ArrayList<Trato> compras;
    private ArrayList<Integer> valoracionesPendientes;

    //Creamos el constructor


    public Usuario(String clave, String apel, int id, int movil, String nombre, String mail) {
        this.clave = clave;
        this.apel = apel;
        this.compras = new ArrayList<>();
        this.enVenta = new ArrayList<>();
        this.id = id;
        this.movil = movil;
        this.nombre = nombre;
        this.valoracionesPendientes = new ArrayList<>();
        this.ventas = new ArrayList<>();
        this.mail = mail;
    }

    public Usuario(String clave, String apel, int movil, String nombre, String mail) {
        this.clave = clave;
        this.apel = apel;
        this.compras = new ArrayList<>();
        this.enVenta = new ArrayList<>();
        this.movil = movil;
        this.nombre = nombre;
        this.valoracionesPendientes = new ArrayList<>();
        this.ventas = new ArrayList<>();
        this.mail = mail;
    }
    //getters and setters


    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getApel() {
        return apel;
    }

    public void setApel(String apel) {
        this.apel = apel;
    }

    public ArrayList<Trato> getCompras() {
        return compras;
    }

    public void setCompras(ArrayList<Trato> compras) {
        this.compras = compras;
    }

    public ArrayList<Producto> getEnVenta() {
        return enVenta;
    }

    public void setEnVenta(ArrayList<Producto> enVenta) {
        this.enVenta = enVenta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMovil() {
        return movil;
    }

    public void setMovil(int movil) {
        this.movil = movil;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Integer> getValoracionesPendientes() {
        return valoracionesPendientes;
    }

    public void setValoracionesPendientes(ArrayList<Integer> valoracionesPendientes) {
        this.valoracionesPendientes = valoracionesPendientes;
    }

    public ArrayList<Trato> getVentas() {
        return ventas;
    }

    public void setVentas(ArrayList<Trato> ventas) {
        this.ventas = ventas;
    }


    @Override
    public String toString() {
        String texto = "***********************************\n"
                + "Usuario : " + nombre + "\n" +
                "Apellidos: " + apel + "\n" +
                "Id: " + id + "\n" +
                "Móvil: " + movil + "\n" +
                "***********************************\n"
                + "Historial de productos: \n";

        if (enVenta.isEmpty()) texto += "Aún no tienes productos en venta";
        else {
            for (Producto p : enVenta) {
                texto += p.toString();
            }
        }
        texto += "\n***********************************";
        texto += "\nVentas realizadas: \n";
        if (ventas.isEmpty()) texto += "Aún no tienes ventas realizadas";
        else {
            for (Trato t : ventas) {
                texto += t.toString();
            }
        }

        texto += "\n***********************************";
        texto += "\nCompras realizadas: \n";
        if (compras.isEmpty()) texto += "Aún no has realizado ninguna compra";
        else {
            for (Trato t : compras) {
                texto += t.toString();
            }
        }
        texto += "\n***********************************";
        texto += "\nNumero de valoraciones pendientes: " + valoracionesPendientes.size();

        return texto;
    }
}
