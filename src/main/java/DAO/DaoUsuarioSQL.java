package DAO;

import models.Producto;
import models.Trato;
import models.Usuario;
import utils.Utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DaoUsuarioSQL implements DaoUsuario{
    @Override
    public boolean insertUser(Usuario usuario, String token, DaoManager dao) {
        String sentencia = "INSERT INTO usuario (nombre, apellido, clave, movil, email, rol, token, activo) VALUES (?,?,?,?,?,?,?,?);";
        try {
            dao.open();
            PreparedStatement pstmt = dao.getConn().prepareStatement(sentencia);
            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getApel());
            pstmt.setString(3, usuario.getClave());
            pstmt.setInt(4, usuario.getMovil());
            pstmt.setString(5, usuario.getMail());
            pstmt.setString(6, "User");
            pstmt.setString(7, token);
            pstmt.setBoolean(8, false); // 0 = Inactivo hasta que pulse la URL de su correo

            int filasAfectadas = pstmt.executeUpdate();
            dao.close();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteUser(Usuario usuario, DaoManager dao) {
        String sentencia;
        sentencia = "Delete from usuario where id = ?";
        try {
            dao.open();
            PreparedStatement pstmt = dao.getConn().prepareStatement(sentencia);
            pstmt.setInt(1,usuario.getId());

            pstmt.executeUpdate();
            dao.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean updateUser(Usuario usuario, DaoManager dao) {
        String sentencia;
        sentencia = "Update usuario " +
                "SET nombre = ?," +
                "apellido = ?,"+
                "clave = ?,"+
                "movil = ?,"+
                "email = ?"+
                "Where id = ?;";
        try {
            dao.open();
            PreparedStatement pstmt = dao.getConn().prepareStatement(sentencia);
            pstmt.setString(1,usuario.getNombre());
            pstmt.setString(2,usuario.getApel());
            pstmt.setString(3,usuario.getClave());
            pstmt.setInt(4,usuario.getMovil());
            pstmt.setString(5,usuario.getMail());
            pstmt.setInt(6,usuario.getId());

            pstmt.executeUpdate();
            dao.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public int countUsers(DaoManager dao) {
        int usuarios = 0;
        String sentencia;
        sentencia = "Select COUNT(id) from usuario;";
        try {
            dao.open();
            PreparedStatement pstmt = dao.getConn().prepareStatement(sentencia);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next())
                usuarios = rs.getInt("Count(id)");
            dao.close();
            return usuarios;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Usuario findMail(String email, DaoManager dao) {
        Usuario usuario = null;
        String sentencia;
        sentencia = "Select * from usuario where email = ?";
        try {
            dao.open();
            PreparedStatement pstmt = dao.getConn().prepareStatement(sentencia);
            pstmt.setString(1,email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next())
                usuario = new Usuario(
                        rs.getNString("clave"),
                        rs.getNString("apellido"),
                        rs.getInt("id"),
                        rs.getInt("movil"),
                        rs.getNString("nombre"),
                        rs.getNString("email")
                );
            dao.close();
            return usuario;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Usuario login(String email, String clave, DaoManager dao) {
        Usuario usuario = null;
        String sentencia;
        sentencia = "SELECT * FROM usuario WHERE email = ? AND clave = ? AND activo = true;";
        try {
            dao.open();
            PreparedStatement pstmt = dao.getConn().prepareStatement(sentencia);
            pstmt.setString(1,email);
            pstmt.setString(2,clave);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next())
                usuario = new Usuario(
                        rs.getNString("clave"),
                        rs.getNString("apellido"),
                        rs.getInt("id"),
                        rs.getInt("movil"),
                        rs.getNString("nombre"),
                        rs.getNString("email")
                );
            dao.close();
            return usuario;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ArrayList<Integer> getValoracionesPendientes(Usuario usuario, DaoManager dao) {
        ArrayList<Integer> valoracionesPendientes = new ArrayList<>();
        Trato trato = null;
        String sentencia;
        sentencia = "select id_trato from valoracionesPendientes where id_user = ?;";
        try {
            dao.open();
            PreparedStatement pstmt = dao.getConn().prepareStatement(sentencia);
            pstmt.setInt(1,usuario.getId());
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){

                valoracionesPendientes.add(rs.getInt("id_trato"));
            }

            dao.close();
            return valoracionesPendientes;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public Usuario findById(int id, DaoManager dao) {
        Usuario usuario = null;
        String sentencia;
        sentencia = "Select * from usuario where id = ?";
        try {
            dao.open();
            PreparedStatement pstmt = dao.getConn().prepareStatement(sentencia);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()){
                usuario = new Usuario(
                        rs.getNString("clave"),
                        rs.getNString("apellido"),
                        id,
                        rs.getInt("movil"),
                        rs.getNString("nombre"),
                        rs.getNString("email")
                );
            }
            dao.close();
            return usuario;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public ArrayList<Usuario> showAllUsers(DaoManager dao) {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        Usuario usuario = null;
        String sentencia;
        sentencia = "Select * from usuario;";
        try {
            dao.open();
            PreparedStatement pstmt = dao.getConn().prepareStatement(sentencia);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()){
                usuario = new Usuario(
                        rs.getNString("clave"),
                        rs.getNString("apellido"),
                        rs.getInt("id"),
                        rs.getInt("movil"),
                        rs.getNString("nombre"),
                        rs.getNString("email")
                );
                usuarios.add(usuario);
            }

            dao.close();
            return usuarios;
        } catch (SQLException e) {
            e.printStackTrace();
            return  null;
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }


    @Override
    public boolean loginAdmin(String email, String clave, DaoManager dao) {
        Usuario usuario = null;
        String sentencia;
        sentencia = "Select * from usuario where email = ? and clave = ? and rol = 'Admin';";
        try {
            dao.open();
            PreparedStatement pstmt = dao.getConn().prepareStatement(sentencia);
            pstmt.setString(1,email);
            pstmt.setString(2,clave);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                usuario = new Usuario(
                        rs.getNString("clave"),
                        rs.getNString("apellido"),
                        rs.getInt("id"),
                        rs.getInt("movil"),
                        rs.getNString("nombre"),
                        rs.getNString("email")
                );
                dao.close();
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean deleteValoracion(Usuario usuario, int id, DaoManager dao) {
        String sentencia;
        sentencia = "Delete from valoracionesPendientes where id_user = ? and id_trato = ?;";
        try {
            dao.open();
            PreparedStatement pstmt = dao.getConn().prepareStatement(sentencia);
            pstmt.setInt(1, usuario.getId());
            pstmt.setInt(2,id);
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
    public boolean insertValoracion(Usuario usuario, int idTrato, DaoManager dao) {
        String sentencia;
        sentencia = "Insert into valoracionesPendientes values (?,?);";
        try {
            dao.open();
            PreparedStatement pstmt = dao.getConn().prepareStatement(sentencia);
            pstmt.setInt(1,usuario.getId());
            pstmt.setInt(2,idTrato);
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
    public boolean deleteDatabase(DaoManager dao) {
        String sentencia;
        sentencia = "DELETE FROM usuario;";
        try {
            dao.open();
            PreparedStatement pstmt = dao.getConn().prepareStatement(sentencia);
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
    public boolean insertAdmin(Usuario usuario, DaoManager dao) {
        String sentencia;
        sentencia = "Insert into usuario Values (?,?,?,?,?,?,?);";
        try {
            dao.open();
            PreparedStatement pstmt = dao.getConn().prepareStatement(sentencia);
            pstmt.setInt(1,usuario.getId());
            pstmt.setString(2, usuario.getNombre());
            pstmt.setString(3, usuario.getApel());
            pstmt.setString(4,usuario.getClave());
            pstmt.setInt(5,usuario.getMovil());
            pstmt.setString(6, usuario.getMail());
            pstmt.setString(7, "Admin");

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
    public boolean validarUsuarioPorToken(String token, DaoManager dao) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }

        String sentencia = "UPDATE usuario SET activo = true, token = NULL WHERE token = ? AND activo = 0;";
        try {
            dao.open();
            PreparedStatement pstmt = dao.getConn().prepareStatement(sentencia);
            pstmt.setString(1, token.trim());

            int filasAfectadas = pstmt.executeUpdate();
            dao.close();

            return filasAfectadas > 0; // Devuelve true si encontró al usuario y cambió el estado a activo
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
