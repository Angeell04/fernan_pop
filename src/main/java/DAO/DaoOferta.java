package DAO;

import controller.GestionAPP;
import models.Oferta;
import models.Producto;
import models.Usuario;

import java.util.ArrayList;

public interface DaoOferta {
    public boolean haceOferta(int comprador, int vendedor, float precio, Producto producto, DaoManager dao);
    public boolean deleteOferta(Usuario comprador, float precio, Producto producto, Usuario vendedor, DaoManager dao);
    public ArrayList<Oferta> getAllOferta(Usuario vendedor, GestionAPP gestionAPP, DaoManager dao);
    public boolean comprado(long idProducto, DaoManager dao);
    public int buscaComprador(long idProducto, int idVendedor, float precio, DaoManager dao);
}
