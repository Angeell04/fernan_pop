package DAO;

import models.Producto;
import models.Trato;
import models.Usuario;

import java.util.ArrayList;

public interface DaoProducto {
    public boolean insertProduct(Producto producto, Usuario user, DaoManager dao);

    public boolean deleteProduct(long id, DaoManager dao);

    public ArrayList<Producto> showAllProduct(DaoManager dao);

    public int countProducts(DaoManager dao);

    public Producto findProductById(long id, DaoManager dao);

    public ArrayList<Producto> findProductText(String titulo, DaoManager dao);

    public ArrayList<Producto> findProductsByIdUser(Usuario user, DaoManager dao);

    public ArrayList<Trato> historyCompras(Usuario usuario, DaoManager dao);

    public ArrayList<Trato> historyVentas(Usuario usuario, DaoManager dao);

    public boolean updateProduct(long id, DaoManager dao);
    public int findUserByIdProduct(long id, DaoManager dao);
}
