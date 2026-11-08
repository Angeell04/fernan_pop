package DAO;

import controller.GestionAPP;
import models.Oferta;
import models.Producto;
import models.Usuario;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class DaoOfertaSQL implements DaoOferta{

    @Override
    public boolean haceOferta(int comprador, int vendedor, float precio, Producto producto, DaoManager dao) {
        String sentencia = "Insert into oferta values (?,?,?,?);";
        try {
            dao.open();
            PreparedStatement pstmt = dao.getConn().prepareStatement(sentencia);
            pstmt.setLong(1,producto.getId());
            pstmt.setInt(2,comprador);
            pstmt.setInt(3,vendedor);
            pstmt.setFloat(4,precio);
            pstmt.executeUpdate();
            dao.close();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean deleteOferta(Usuario comprador, float precio, Producto producto, Usuario vendedor ,DaoManager dao) {
        String sentencia;
        sentencia = "Delete from oferta where idProducto = ? and idComprador = ? and idvendedor = ? and precio = ?;";

        try {
            dao.open();
            PreparedStatement pstmt = dao.getConn().prepareStatement(sentencia);
            pstmt.setLong(1, producto.getId());
            pstmt.setInt(2, comprador.getId());
            pstmt.setInt(3, vendedor.getId());
            pstmt.setFloat(4, precio);
            pstmt.executeUpdate();
            dao.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public ArrayList<Oferta> getAllOferta(Usuario vendedor, GestionAPP gestionAPP, DaoManager dao) {
        String sentencia;
        ArrayList<Oferta> ofertas = new ArrayList<>();
        HashMap<Oferta , Integer> compradores = new HashMap<>();
        HashMap<Oferta, Long> productos = new HashMap<>();
        sentencia = "Select * from oferta where idvendedor = ?;";

        try {
            dao.open();
            PreparedStatement pstmt = dao.getConn().prepareStatement(sentencia);
            pstmt.setInt(1, vendedor.getId());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                Oferta ofertaTmp = new Oferta(
                        null,
                        rs.getFloat("precio"),
                        vendedor,
                        null
                );
                ofertas.add(ofertaTmp);
                compradores.put(ofertaTmp,(rs.getInt("idComprador")));
                productos.put(ofertaTmp,(rs.getLong("idProducto")));
            }
            dao.close();

            int idComprador = 0;
            for (Oferta oferta : ofertas) {
                idComprador = compradores.get(oferta);
                oferta.setComprador(gestionAPP.buscaUsuarioPorId(idComprador));
            }

            long idProducto = 0;
            for (Oferta oferta : ofertas){
                idProducto = productos.get(oferta);
                oferta.setProducto(gestionAPP.buscaProductoId(idProducto));
            }
        } catch (SQLException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
        return ofertas;
    }

    @Override
    public boolean comprado(long idProducto, DaoManager dao) {
        String sentencia;
        sentencia = "Delete from oferta where idProducto = ?;";

        try {
            dao.open();
            PreparedStatement pstmt = dao.getConn().prepareStatement(sentencia);
            pstmt.setLong(1, idProducto);
            pstmt.executeUpdate();
            dao.close();
        } catch (SQLException e) {
           return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public int buscaComprador(long idProducto, int idVendedor, float precio, DaoManager dao) {
        String sentencia;
        sentencia = "Select idComprador from oferta where idProducto = ? and idvendedor = ? and precio = ?;";
        int idComprador = 0;
        try {
            dao.open();
            PreparedStatement pstmt = dao.getConn().prepareStatement(sentencia);
            pstmt.setLong(1,idProducto);
            pstmt.setInt(2,idVendedor);
            pstmt.setFloat(3,precio);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                idComprador = rs.getInt("idComprador");
            }
            dao.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return idComprador;
    }
}
