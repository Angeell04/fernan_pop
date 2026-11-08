package DAO;

import models.Producto;
import models.Trato;
import models.Usuario;
import utils.Utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DaoTratoSQL implements DaoTrato{
    DaoProductoSQL daoProductoSQL = new DaoProductoSQL();


    @Override
    public int insertTrato(Trato trato, DaoManager dao) {
        String sentencia;
        sentencia = "Insert into trato (id,tipo, emailOtroUser, id_producto, fecha, precio)" +
                "values (?,?,?,?,?,?);";
        try{
            dao.open();
            PreparedStatement pstmt = dao.getConn().prepareStatement(sentencia);
            pstmt.setInt(1, trato.getId());
            pstmt.setString(2, "Venta");
            pstmt.setString(3, trato.getEmailOtroUser());

            if (trato.getProducto() != null) {
                pstmt.setLong(4, trato.getProducto().getId());
            } else {

                System.err.println("Advertencia: El trato ID " + trato.getId() + " venía con producto null en el binario.");

                throw new NullPointerException("No se puede migrar el trato " + trato.getId() + " porque su producto es inexistente (N/A).");
            }

            pstmt.setString(5, Utils.calendarToString(trato.getFecha()));
            pstmt.setDouble(6, trato.getPrecio());
            pstmt.executeUpdate();

            dao.close();
            return trato.getId();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public boolean updateTrato(int id, String comentario, int puntuacion, DaoManager dao) {
        String sentencia;
        sentencia = "Update trato SET comentario = ?, puntuacion = ? where id = ?;";
        try{
            dao.open();
            PreparedStatement pstmt = dao.getConn().prepareStatement(sentencia);
            pstmt.setString(1, comentario);
            pstmt.setInt(2, puntuacion);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
            dao.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Trato findTratoById(int id, DaoManager dao) {
        String sentencia = "SELECT * FROM trato WHERE id = ?;";
        long id_producto = 0;
        Trato trato = null;

        try {
            dao.open();
            PreparedStatement pstmt = dao.getConn().prepareStatement(sentencia);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                id_producto = rs.getLong("id_producto");
                trato = new Trato(
                        rs.getString("comentario"),
                        rs.getString("emailOtroUser"),
                        Utils.parseFechaStringCalendar(rs.getString("fecha")),
                        rs.getInt("id"),
                        rs.getDouble("precio"),
                        null, // Dejamos el producto en null temporalmente
                        rs.getInt("puntuacion"),
                        rs.getString("tipo")
                );
            }
            dao.close(); // <--- CERRAR la primera conexión primero

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        // Ahora que dao está cerrado, buscamos el producto de forma limpia
        if (trato != null && id_producto > 0) {
            trato.setProducto(daoProductoSQL.findProductById(id_producto, dao));
        }

        return trato;
    }

    @Override
    public boolean addCompra(Trato trato, Usuario user, DaoManager dao) {
        String sentencia;
        sentencia = "Insert into compras (id_user, id_trato ) values (?,?);";

        try{
            dao.open();
            PreparedStatement pstmt = dao.getConn().prepareStatement(sentencia);
            pstmt.setInt(1, user.getId());
            pstmt.setInt(2,trato.getId());

            pstmt.executeUpdate();
            dao.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addVenta(Trato trato, Usuario user, DaoManager dao) {
        String sentencia;
        sentencia = "Insert into ventas (id_user, id_trato ) values (?,?);";

        try{
            dao.open();
            PreparedStatement pstmt = dao.getConn().prepareStatement(sentencia);
            pstmt.setInt(1, user.getId());
            pstmt.setInt(2,trato.getId());

            pstmt.executeUpdate();
            dao.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public long findIdProductoByTratoId(int idTrato, DaoManager dao) {
        String sentencia = "SELECT id_producto FROM trato WHERE id = ?;";
        try {
            dao.open();
            PreparedStatement pstmt = dao.getConn().prepareStatement(sentencia);
            pstmt.setInt(1, idTrato);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                long idProd = rs.getLong("id_producto");
                dao.close();
                return idProd;
            }
            dao.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
