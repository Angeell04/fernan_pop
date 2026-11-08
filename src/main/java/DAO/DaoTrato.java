package DAO;

import models.Trato;
import models.Usuario;

import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;

public interface DaoTrato {
    public int insertTrato(Trato trato, DaoManager dao);
    public boolean updateTrato(int id, String comentario, int puntuacion, DaoManager dao);
    public Trato findTratoById(int id, DaoManager dao);
    public boolean addCompra (Trato trato, Usuario user, DaoManager dao);
    public boolean addVenta (Trato trato, Usuario user, DaoManager dao);
    public long findIdProductoByTratoId(int idTrato, DaoManager dao);


}
