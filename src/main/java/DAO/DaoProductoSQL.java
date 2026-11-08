package DAO;

import models.Producto;
import models.Trato;
import models.Usuario;
import utils.Utils;

import java.rmi.MarshalledObject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class DaoProductoSQL implements DaoProducto{
    @Override
    public boolean insertProduct(Producto producto, Usuario user, DaoManager dao) {
        String sentencia;
        sentencia = "Insert into producto (id,titulo, descripcion, precio, estado, id_usuario, vendido)" +
                "values (?,?,?,?,?,?, ?);";
        try {

            dao.open();
            PreparedStatement pstmt = dao.getConn().prepareStatement(sentencia);
            pstmt.setLong(1, producto.getId());
            pstmt.setString(2, producto.getTitulo());
            pstmt.setString(3, producto.getDescripcion());
            pstmt.setDouble(4, producto.getPrecio());
            pstmt.setString(5, producto.getEstado());
            pstmt.setInt(6, user.getId());
            pstmt.setInt(7, producto.getVendido());

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
    public boolean deleteProduct(long id, DaoManager dao) {
        String sentencia;
        sentencia = "Delete from producto where id = ?";
        try {
            dao.open();
            PreparedStatement pstmt = dao.getConn().prepareStatement(sentencia);
            pstmt.setLong(1,id);

            pstmt.executeUpdate();
            dao.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    @Override
    public ArrayList<Producto> showAllProduct(DaoManager dao) {
        ArrayList<Producto> productos = new ArrayList<>();
        String sentencia;
        sentencia = "Select * from producto;";
        try {
            dao.open();
            PreparedStatement pstmt = dao.getConn().prepareStatement(sentencia);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                if (rs.getLong("vendido") != 1){
                    Producto producto = new Producto(
                            rs.getNString("descripcion"),
                            rs.getNString("estado"),
                            rs.getLong("id"),
                            rs.getDouble("precio"),
                            rs.getNString("titulo"),
                            rs.getInt("vendido")
                    );
                    productos.add(producto);
                }
            }
            dao.close();
            return productos;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public int countProducts(DaoManager dao) {
        int numProducts = 0;
        String sentencia;
        sentencia = "Select COUNT(id) from producto;";
        try {
            dao.open();
            PreparedStatement pstmt = dao.getConn().prepareStatement(sentencia);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()){
                numProducts = rs.getInt("COUNT(id)");
            }
            dao.close();
            return numProducts;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

    @Override
    public Producto findProductById(long id, DaoManager dao) {
        Producto producto = null;
        String sentencia;
        sentencia = "Select * from producto where id = ?;";

        try {
            dao.open();
            PreparedStatement pstmt = dao.getConn().prepareStatement(sentencia);
            pstmt.setLong(1,id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()){
                if (rs.getLong("vendido") != 1){
                    producto = new Producto(
                            rs.getNString("descripcion"),
                            rs.getNString("estado"),
                            rs.getLong("id"),
                            rs.getDouble("precio"),
                            rs.getNString("titulo"),
                            rs.getInt("vendido")
                    );
                }
            }
            dao.close();
            return producto;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public ArrayList<Producto> findProductText(String titulo, DaoManager dao) {
        ArrayList<Producto> productos = new ArrayList<>();
        String sentencia;
        sentencia = "Select * from producto where titulo like ?;";
        try {
            dao.open();
            PreparedStatement pstmt = dao.getConn().prepareStatement(sentencia);
            pstmt.setString(1, "%"+titulo+"%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                if (rs.getLong("vendido") != 1){
                    Producto producto = new Producto(
                            rs.getNString("descripcion"),
                            rs.getNString("estado"),
                            rs.getLong("id"),
                            rs.getDouble("precio"),
                            rs.getNString("titulo"),
                            rs.getInt("vendido")
                    );
                    productos.add(producto);
                }
            }

            dao.close();

            return productos;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public ArrayList<Producto> findProductsByIdUser(Usuario user, DaoManager dao) {
        ArrayList<Producto> productos = new ArrayList<>();

        String sentencia;
        sentencia = "Select * from producto where id_usuario = ?;";

        try {
            dao.open();
            PreparedStatement pstmt = dao.getConn().prepareStatement(sentencia);
            pstmt.setInt(1, user.getId());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){

                Producto producto = new Producto(
                        rs.getNString("descripcion"),
                        rs.getNString("estado"),
                        rs.getLong("id"),
                        rs.getDouble("precio"),
                        rs.getNString("titulo"),
                        rs.getInt("vendido")
                );
                productos.add(producto);


            }
            dao.close();
            return productos;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ArrayList<Trato> historyCompras(Usuario usuario, DaoManager dao) {
        ArrayList<Trato> tratos = new ArrayList<>();
        long id_producto = 0;
        Trato trato = null;
        HashMap<Trato, Long> productos= new HashMap<>();
        String sentencia;
        sentencia = "select t.* from trato t inner join compras c on t.id = c.id_trato where c.id_user = ?;";
        try {
            dao.open();
            PreparedStatement pstmt = dao.getConn().prepareStatement(sentencia);
            pstmt.setInt(1,usuario.getId());
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                id_producto = rs.getLong("id_producto");
                trato = new Trato(
                        rs.getString("comentario"),
                        rs.getNString("emailOtroUser"),
                        Utils.parseFechaStringCalendar(rs.getNString("fecha")),
                        rs.getInt("id"),
                        rs.getDouble("precio"),
                        null,
                        rs.getInt("puntuacion"),
                        rs.getNString("tipo")
                );
                tratos.add(trato);

                productos.put(trato, id_producto);
            }

            dao.close();

            long id_prodOriginal;
            for (Trato t: tratos){
                id_prodOriginal = productos.get(t);
                Producto productoOriginal = findProductByIdVentasCompras(id_prodOriginal, dao);

                t.setProducto(productoOriginal);
            }


            return tratos;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public ArrayList<Trato> historyVentas(Usuario usuario, DaoManager dao) {
        ArrayList<Trato> tratos = new ArrayList<>();
        ArrayList<Long> idsProductos = new ArrayList<>();

        String sentencia = "select t.* from trato t inner join ventas v on t.id = v.id_trato where v.id_user = ?;";

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            dao.open();
            pstmt = dao.getConn().prepareStatement(sentencia);
            pstmt.setInt(1, usuario.getId());
            rs = pstmt.executeQuery();

            while(rs.next()){
                Trato trato = new Trato(
                        rs.getString("comentario"),
                        rs.getNString("emailOtroUser"),
                        Utils.parseFechaStringCalendar(rs.getNString("fecha")),
                        rs.getInt("id"),
                        rs.getDouble("precio"),
                        null,
                        rs.getInt("puntuacion"),
                        rs.getNString("tipo")
                );

                tratos.add(trato);
                idsProductos.add(rs.getLong("id_producto"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                dao.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        for (int i = 0; i < tratos.size(); i++) {
            Long idProducto = idsProductos.get(i);
            Producto producto = findProductByIdVentasCompras(idProducto, dao);
            tratos.get(i).setProducto(producto);
        }

        return tratos;
    }
    public Producto findProductByIdVentasCompras(long id, DaoManager dao) {
        Producto producto = null;
        String sentencia;
        sentencia = "Select * from producto where id = ?;";

        try {
            dao.open();
            PreparedStatement pstmt = dao.getConn().prepareStatement(sentencia);
            pstmt.setLong(1,id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()){
                if (rs.getLong("vendido") == 1){
                    producto = new Producto(
                            rs.getNString("descripcion"),
                            rs.getNString("estado"),
                            rs.getLong("id"),
                            rs.getDouble("precio"),
                            rs.getNString("titulo"),
                            rs.getInt("Vendido")
                    );
                }
            }
            dao.close();
            return producto;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean updateProduct(long id, DaoManager dao) {
        String sentencia;
        sentencia = "Update producto set vendido = 1 where id = ?;";
        try {
            dao.open();
            PreparedStatement pstmt = dao.getConn().prepareStatement(sentencia);
            pstmt.setLong(1, id);
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
    public int findUserByIdProduct(long id, DaoManager dao) {
        int idUser = 0;

        String sentencia;
        sentencia = "Select id_usuario from producto where id = ?;";
        try {
            dao.open();
            PreparedStatement pstmt = dao.getConn().prepareStatement(sentencia);
            pstmt.setLong(1,id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()){
                idUser = rs.getInt("id_usuario");
            }
            dao.close();
            return idUser;
        } catch (SQLException e) {
            return idUser;
        } catch (Exception e) {
            return idUser;
        }

    }


}
