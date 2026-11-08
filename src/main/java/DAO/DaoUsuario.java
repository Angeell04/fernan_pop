package DAO;

import models.Producto;
import models.Trato;
import models.Usuario;

import java.util.ArrayList;

public interface DaoUsuario {
    public boolean insertUser(Usuario usuario, String tokrn,DaoManager dao);

    public boolean deleteUser(Usuario usuario, DaoManager dao);

    public boolean updateUser(Usuario usuario, DaoManager dao);

    public int countUsers(DaoManager dao);

    public Usuario findMail(String email, DaoManager dao);

    public Usuario login(String email, String clave, DaoManager dao);

    public ArrayList<Integer> getValoracionesPendientes(Usuario usuario, DaoManager dao);

    public Usuario findById(int id, DaoManager dao);

    public ArrayList<Usuario> showAllUsers(DaoManager dao);

    public boolean loginAdmin(String email, String clave, DaoManager dao);

    public boolean deleteValoracion(Usuario usuario, int id, DaoManager dao);

    public boolean insertValoracion(Usuario usuario, int idTrato, DaoManager dao);

    public boolean deleteDatabase(DaoManager dao);

    public boolean insertAdmin(Usuario usuario, DaoManager dao);
    public boolean validarUsuarioPorToken(String token, DaoManager dao);
}
