package controller;

import DAO.*;
import models.Producto;
import models.Trato;
import models.Usuario;
import models.Oferta;
import persistence.Persistence;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

public class GestionAPP {
    //Atributos
    private Usuario usuario;
    private DaoManager dao;
    private DaoProductoSQL daoProductoSQL;
    private DaoUsuarioSQL daoUsuarioSQL;
    private DaoTratoSQL daoTratoSQL;
    private DaoOfertaSQL daoOfertaSQL;

    //Creamos al constructor

    public GestionAPP() {

        dao = DaoManager.getSingletonInstance();

        daoUsuarioSQL = new DaoUsuarioSQL();
        daoProductoSQL = new DaoProductoSQL();
        daoTratoSQL = new DaoTratoSQL();
        daoOfertaSQL = new DaoOfertaSQL();
    }
    //Creamos los getters and setters

    public ArrayList<Usuario> getUsuarios() {
        return daoUsuarioSQL.showAllUsers(dao);
    }

    public DaoManager getDao() {
        return dao;
    }

    //Metodo para contar los productos de cada usuario
    public int getTotalProductos(){
        return daoProductoSQL.countProducts(dao);
    }


    //Metodo para contar los usuarios generales que hay en la aplicación
    public int generalUser(){
        return daoUsuarioSQL.countUsers(dao);
    }

    //Metodo para buscar un email
    public Usuario buscaMail(String mail){
        return daoUsuarioSQL.findMail(mail,dao);
    }

    //Metodo para añadir productos a un usuario
    public boolean addProducto(Usuario usuario, Producto producto){
        if (usuario == null) return false;
        if (producto == null) return false;

        return daoProductoSQL.insertProduct(producto, usuario,dao);
    }

    public boolean buscaPorEmail(String email){
        return daoUsuarioSQL.findMail(email,dao) != null;
    }
    //Metodo para loguear un usuario
    public Usuario login (String email, String contrasenia){
        // Verificamos que los datos no estén vacíos
        if (email.isEmpty()) return null;
        if (contrasenia.isEmpty()) return null;


        Usuario user = daoUsuarioSQL.login(email, contrasenia, dao);

        if (user == null) return null;

        user.getEnVenta().addAll(daoProductoSQL.findProductsByIdUser(user, dao));

        user.getCompras().addAll(daoProductoSQL.historyCompras(user, dao));
        user.getVentas().addAll(daoProductoSQL.historyVentas(user, dao));

        for (Trato trato : user.getCompras()) {
            if (trato != null && trato.getProducto() == null) {
                // Recargamos el trato usando el método que sí inyecta el producto correctamente
                Trato tratoCargado = daoTratoSQL.findTratoById(trato.getId(), dao);
                if (tratoCargado != null) {
                    trato.setProducto(tratoCargado.getProducto());
                }
            }
        }

        for (Trato venta : user.getVentas()) {
            if (venta != null && venta.getProducto() == null) {
                // Recargamos la venta usando el método que sí inyecta el producto correctamente
                Trato ventaCargada = daoTratoSQL.findTratoById(venta.getId(), dao);
                if (ventaCargada != null) {
                    venta.setProducto(ventaCargada.getProducto());
                }
            }
        }

        // Rellenamos las valoraciones pendientes
        user.getValoracionesPendientes().addAll(daoUsuarioSQL.getValoracionesPendientes(user, dao));

        return user;
    }
    public ArrayList<Producto> verProductosUsuario(Usuario usuario){
        ArrayList<Producto> productos = new ArrayList<>();
        for (Producto producto: daoProductoSQL.findProductsByIdUser(usuario, dao)){
            if (producto.getVendido() != 1){
                productos.add(producto);
            }
        }
        return productos;
    }



    //Metodo para retornar todos los productos de la aplicacion
    public ArrayList<Producto> getAllProductos(){
        //Primero creamos el arrays aux que vamos a devolver

        return daoProductoSQL.showAllProduct(dao);
    }

    //Metodo para buscar productos por su id
    public Producto buscaProductoId(long id){
        if (id < 0) return null;

        return daoProductoSQL.findProductById(id, dao);
    }

    //Metodo para buscar productos por texto
    public ArrayList<Producto> buscaProductoTexto(String nombre) {
        // 1. Creamos la lista que vamos a devolver

        ArrayList<Producto> productos = daoProductoSQL.findProductText(nombre, dao);

        //Los ordenamos de forma manual
        for (int i = 0; i < productos.size(); i++) {
            for (int j = 0; j < productos.size() - 1 - i; j++) {

                // Si el precio de j es MENOR que el de j+1, los intercambiamos
                // Así los más grandes (caros) se quedan al principio.
                if (productos.get(j).getPrecio() > productos.get(j + 1).getPrecio()) {

                    // Intercambio con variable auxiliar
                    Producto aux = productos.get(j);
                    productos.set(j, productos.get(j + 1));
                    productos.set(j + 1, aux);
                }
            }
        }

        return productos;
    }

    //Metodo para borrar un usuario
    public boolean borrarUsuario(Usuario usuario){
        //Primero nos protegemos de que el usuario no sea null
        if (usuario == null) return false;

        return daoUsuarioSQL.deleteUser(usuario, dao);
    }

    //Metodo para buscar el trato por su id
    public Trato buscaTratoId (int id){
        //Nos protegemos de que la id no sea menor que 0
        if (id <= 0) return null;

        //Buscamos el trato en los usuarios


        return daoTratoSQL.findTratoById(id, dao);
    }

    //metodo para obtener todos los productos pendientes de valoracion
    public ArrayList<Trato> getValoracionesPendientes(Usuario usuario){
        // Primero nos defendemos de que el usuario no sea nulo
        if (usuario == null) return null;

        // Luego creamos el arrays auxiliar para guardar los tratos
        ArrayList<Trato> tratos = new ArrayList<>();

        // Creamos el arrays de valoraciones pendientes del usuario
        ArrayList<Integer> valoracionesPendientes = daoUsuarioSQL.getValoracionesPendientes(usuario, dao);

        // Ahora vamos a recorrer las valoraciones pendientes
        for (int idBuscado : valoracionesPendientes){
            // Usamos el metodo busca trato por id
            Trato t = daoTratoSQL.findTratoById(idBuscado, dao);

            // Si el trato existe pero el producto se quedó null debido al cruce de conexiones
            if (t != null && t.getProducto() == null) {
                // Buscamos el ID del producto que va ligado de forma directa en el DAO
                long idProducto = daoTratoSQL.findIdProductoByTratoId(idBuscado, dao);
                if (idProducto != 0) {
                    // Inyectamos el producto real rescatándolo directamente de la BBDD
                    t.setProducto(daoProductoSQL.findProductById(idProducto, dao));
                }
            }
            // ------------------------------------------

            // Si el trato es diferente de null lo añadimos a nuestro arrays aux
            if (t != null) {
                tratos.add(t);
            }
        }

        // Algoritmo de ordenación Burbuja por Fecha (Impecable)
        for (int i = 0; i < tratos.size(); i++) {
            for (int j = 0; j < tratos.size() - 1 - i; j++) {
                // Comparamos el trato actual (j) con el siguiente (j + 1)
                if (tratos.get(j).getFecha().compareTo(tratos.get(j + 1).getFecha()) > 0) {
                    // Si están desordenados, los intercambiamos usando un auxiliar
                    Trato aux = tratos.get(j);
                    tratos.set(j, tratos.get(j + 1));
                    tratos.set(j + 1, aux);
                }
            }
        }
        return tratos;
    }

    //Metodo para borrar una valoracion pendiente
    public boolean borrarValoracionPendiente (Usuario usuario, int id){
        //Nos protegemos de que el usuario no sea null y que el id no sea menor que 0
        if (usuario == null || id < 0) return false;
        //guardamos el arraylist entero anterior para guardar el id en la misma posicion exactamente
        //Usamos el metodo que ya teniamos antes en la clase usuario
        return daoUsuarioSQL.deleteValoracion(usuario, id, dao);
    }





    //Vamos a crear un metodo para registrarnos y crear usuarios
    public boolean registro(String nombre, String clave, String apellido , int movil, String email, String token){
        Usuario nuevoUsuario = new Usuario(clave, apellido, movil, nombre, email);
        return daoUsuarioSQL.insertUser(nuevoUsuario, token, dao);
    }



    public boolean cambiaMail(Usuario usuario, String email) {
        //SI el usuario es nulo no podemos cambiar el email
        if (usuario == null) return false;
        usuario.setMail(email);
        return daoUsuarioSQL.updateUser(usuario,dao);
    }

    public boolean cambiaNombre(Usuario usuario, String nombre) {
        //SI el usuario es nulo no podemos cambiar el email
        if (usuario == null) return false;
        usuario.setNombre(nombre);
        return daoUsuarioSQL.updateUser(usuario,dao);
    }

    public boolean cambiaApellidos(Usuario usuario, String apellido) {
        //SI el usuario es nulo no podemos cambiar el email
        if (usuario == null) return false;
        usuario.setApel(apellido);
        return daoUsuarioSQL.updateUser(usuario,dao);

    }


    public boolean cambiaMovil(Usuario usuario, int movil) {
        //SI el usuario es nulo no podemos cambiar el email
        if (usuario == null) return false;

        usuario.setMovil(movil);
        return daoUsuarioSQL.updateUser(usuario,dao);
    }

    public boolean cambiaClave(Usuario usuario, String clave) {
        //SI el usuario es nulo no podemos cambiar el email
        if (usuario == null) return false;

        usuario.setClave(clave);
        return daoUsuarioSQL.updateUser(usuario,dao);
    }

    public ArrayList<Trato> historialCompra(Usuario usuario) {
        return daoProductoSQL.historyCompras(usuario,dao);
    }
    public ArrayList<Trato> historialVentas(Usuario usuario) {
        return daoProductoSQL.historyVentas(usuario,dao);
    }


    public int addTrato(Usuario usuario, long idProducto, String emailOtroUser, double precio) {
        // 1. Generamos la ID del nuevo trato
        int idTrato = generaIdTrato();

        // 2. Buscamos el producto usando una conexión limpia
        Producto producto = daoProductoSQL.findProductById(idProducto,dao);

        // 3. Verificamos que el producto exista para evitar NullPointerException
        if (producto == null) {
            System.err.println("Error: No se encontró el producto con ID: " + idProducto);
            return 0;
        }

        // 4. Creamos el objeto Trato con el producto correctamente cargado
        Trato trato = new Trato(producto, emailOtroUser, precio, idTrato);

        // 5. Insertamos el trato en la BD con su propia conexión
        int idResultado = daoTratoSQL.insertTrato(trato, dao);

        // 6. Si la inserción fue exitosa, actualizamos el estado del producto
        if (idResultado > 0) {
            // Cambiamos el estado del producto a VENDIDO o RESERVADO si tu modelo lo requiere
            producto.setEstado("VENDIDO");
            daoProductoSQL.updateProduct(producto.getId(), dao);
        }

        return idTrato;
    }
    public int generaIdTrato() {
        int idGenerado;

        do {
            idGenerado = (int) (Math.random() * 100000 + 1);

        } while (daoTratoSQL.findTratoById(idGenerado,dao) != null); // Si se marcó como repetido, vuelve a generar otro número

        return idGenerado;
    }

    public int addTratoVenta(Usuario usuario, int idTrato,long id, String email, double precio) {
        Trato trato = new Trato(daoProductoSQL.findProductById(id,dao), email, precio, idTrato);
        if (daoTratoSQL.addVenta(trato, usuario, dao)) return trato.getId();
        return 0;
    }

    public boolean addTratoCompra(Usuario usuarioCompra, Trato trato) {
        if (usuarioCompra  == null) return false;
        if (trato  == null) return false;
        return daoTratoSQL.addCompra(trato,usuarioCompra,dao);
    }

    public boolean borraProductoPorId(Usuario user, long id) {
        return daoProductoSQL.deleteProduct(id, dao);
    }

    public void logInicioSesion(Usuario usuario) {
        String mensaje = "";
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        mensaje += "Inicio de sesión: "+usuario.getMail()+ " ";
        mensaje += "Fecha y hora: ["+ ahora.format(formato)+ "]";
        Persistence.logs(mensaje);
    }

    public void logInvitado() {
        String mensaje = "";
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        mensaje += "Inicio de sesión: Invitado ";
        mensaje += "Fecha y hora: ["+ ahora.format(formato)+ "]";
        Persistence.logs(mensaje);
    }

    public void logCierreSesion(Usuario usuario) {
        String mensaje = "";
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        mensaje += "Cierre de sesión: "+usuario.getMail()+ " ";
        mensaje += "Fecha y hora: ["+ ahora.format(formato)+ "]";
        Persistence.logs(mensaje);
    }

    public void logVentaCerrada(String mailVenta, String mailCompra) {
        String mensaje = "";
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        mensaje += "Venta cerrada email del vendedor:  "+mailVenta+ ", email comprador: "+mailCompra;
        mensaje += "Fecha y hora: ["+ ahora.format(formato)+ "]";
        Persistence.logs(mensaje);
    }



    public boolean compruebaAdmin(Usuario usuario) {
        return daoUsuarioSQL.loginAdmin(usuario.getMail(), usuario.getClave(),dao);
    }


    public String leeConfig() {
        return Persistence.muestraConfig(daoUsuarioSQL.showAllUsers(dao));

    }

    public boolean addValoracionPendiente(Usuario usuarioCompra, int idTrato) {
        return daoUsuarioSQL.insertValoracion(usuarioCompra,idTrato,dao);
    }

    public void valoraCompra(int id, String comentario, int puntuacion) {
        daoTratoSQL.updateTrato(id,comentario,puntuacion,dao);
    }

    public boolean copiaDeSeguridad(String ruta) {
        ArrayList<Usuario> usuarios = daoUsuarioSQL.showAllUsers(dao);

        // Bucle para reconstruir todos los usuarios
        for (Usuario usuario : usuarios) {
            usuario.getEnVenta().addAll(daoProductoSQL.findProductsByIdUser(usuario, dao));

            usuario.getCompras().addAll(daoProductoSQL.historyCompras(usuario, dao));
            usuario.getVentas().addAll(daoProductoSQL.historyVentas(usuario, dao));

            for (Trato trato : usuario.getCompras()) {
                if (trato != null && trato.getProducto() == null) {
                    Trato tratoCargado = daoTratoSQL.findTratoById(trato.getId(), dao);
                    if (tratoCargado != null) {
                        trato.setProducto(tratoCargado.getProducto());
                    }
                }
            }


            for (Trato venta : usuario.getVentas()) {
                if (venta != null && venta.getProducto() == null) {
                    Trato ventaCargada = daoTratoSQL.findTratoById(venta.getId(), dao);
                    if (ventaCargada != null) {
                        venta.setProducto(ventaCargada.getProducto());
                    }
                }
            }

            // Rellenamos valoraciones pendientes
            usuario.getValoracionesPendientes().addAll(daoUsuarioSQL.getValoracionesPendientes(usuario, dao));
        }

        //Si se escribe el archivo binario lo borramos


        return Persistence.copiaSeguridad(ruta, usuarios);
    }

//    public boolean retornaCopiaABaseDatos(String ruta) {
//        ArrayList<Usuario> usuarios = Persistence.leeUsuarios(ruta);
//        if (usuarios == null || usuarios.isEmpty()) {
//            return false;
//        }else {
//            if (daoUsuarioSQL.deleteDatabase(dao)){
//                // Primero insertamos todos los usuarios
//                for (Usuario usuario : usuarios) {
//                    boolean operacionOk;
//                    if (usuario.getMail().equalsIgnoreCase("Admin") && usuario.getClave().equalsIgnoreCase("Admin")) {
//                        operacionOk = daoUsuarioSQL.insertAdmin(usuario, dao);
//                    } else {
//                        operacionOk = daoUsuarioSQL.insertUser(usuario, dao);
//                    }
//
//                    // Si falla la inserción de un solo usuario, paramos el proceso
//                    if (!operacionOk) {
//                        System.err.println("No se pudo restaurar el usuario " + usuario.getNombre());
//                        return false;
//                    }
//                }
//
//                // Luego cada producto de cada usuario
//                for (Usuario usuario : usuarios) {
//                    for (Producto producto : usuario.getEnVenta()) {
//                        if (!daoProductoSQL.insertProduct(producto, usuario, dao)) {
//                            System.err.println("Fallo al insertar producto ID " + producto.getId());
//                            return false;
//                        }
//                    }
//                }
//
//                // Ahora metemos los tratos, las compras, las ventas y sus valoracionesPendientes
//                for (Usuario usuario : usuarios) {
//
//                    // Procesamos sus compras
//                    for (Trato trato : usuario.getCompras()) {
//                        if (daoTratoSQL.findTratoById(trato.getId(), dao) == null) {
//                            if (daoTratoSQL.insertTrato(trato, dao) == 0) {
//                                System.err.println("Fallo al insertar trato padre ID " + trato.getId());
//                                return false;
//                            }
//                        }
//                        if (!daoTratoSQL.addCompra(trato, usuario, dao)) {
//                            System.err.println("Fallo al asociar compra del trato ID " + trato.getId());
//                            return false;
//                        }
//                    }
//
//                    // Procesamos sus ventas
//                    for (Trato venta : usuario.getVentas()) {
//                        if (daoTratoSQL.findTratoById(venta.getId(), dao) == null) {
//                            if (daoTratoSQL.insertTrato(venta, dao) == 0) {
//                                System.err.println("Error: Fallo al insertar trato padre ID " + venta.getId());
//                                return false;
//                            }
//                        }
//                        if (!daoTratoSQL.addVenta(venta, usuario, dao)) {
//                            System.err.println("Error: Fallo al asociar venta del trato ID " + venta.getId());
//                            return false;
//                        }
//                    }
//
//                    // Procesamos sus valoraciones pendientes
//                    for (Integer valoracion : usuario.getValoracionesPendientes()) {
//                        if (!daoUsuarioSQL.insertValoracion(usuario, valoracion, dao)) {
//                            System.err.println("Error: Fallo al insertar valoración pendiente para el trato ID " + valoracion);
//                            return false;
//                        }
//                    }
//                }
//            }
//        }
//        return true;
//    }

    public Usuario muestraPerfilUsuario(Usuario user) {
        user.getEnVenta().clear();
        user.getCompras().clear();
        user.getVentas().clear();
        user.getValoracionesPendientes().clear();

        for (Producto producto : daoProductoSQL.findProductsByIdUser(user, dao)) {
            if (producto.getVendido() != 1) {
                user.getEnVenta().add(producto);
            }
        }



        user.getCompras().addAll(daoProductoSQL.historyCompras(user, dao));
        user.getVentas().addAll(daoProductoSQL.historyVentas(user, dao));

        for (Trato trato : user.getCompras()) {
            if (trato != null && trato.getProducto() == null) {
                // Recargamos el trato usando el método que sí inyecta el producto correctamente
                Trato tratoCargado = daoTratoSQL.findTratoById(trato.getId(), dao);
                if (tratoCargado != null) {
                    trato.setProducto(tratoCargado.getProducto());
                }
            }
        }

        for (Trato venta : user.getVentas()) {
            if (venta != null && venta.getProducto() == null) {
                // Recargamos la venta usando el método que sí inyecta el producto correctamente
                Trato ventaCargada = daoTratoSQL.findTratoById(venta.getId(), dao);
                if (ventaCargada != null) {
                    venta.setProducto(ventaCargada.getProducto());
                }
            }
        }

        // Rellenamos las valoraciones pendientes
        user.getValoracionesPendientes().addAll(daoUsuarioSQL.getValoracionesPendientes(user, dao));

        return user;
    }

    public boolean hacerOferta(int comprador, int vendedor, float precio, long productoId) {
        Producto producto = buscaProductoId(productoId);
        return daoOfertaSQL.haceOferta(comprador,vendedor, precio, producto,dao);
    }

    public long generaIdProducto() {
        long id = 0;
        do {
            id = (long) (Math.random() * 1000000) + 1;
        }while(daoProductoSQL.findProductById(id, dao) != null);
        return id;
    }

    public int buscaIdUsuarioProducto(long id){
        return daoProductoSQL.findUserByIdProduct(id, dao);
    }

    public Usuario buscaUsuarioPorId(int id){
        return daoUsuarioSQL.findById(id, dao);
    }

    public ArrayList<Oferta> showAllOfertByUser(Usuario user){
        return daoOfertaSQL.getAllOferta(user,this,dao);
    }

    public boolean aceptarOferta(long idProductoParam) {
        return daoOfertaSQL.comprado(idProductoParam,dao);

    }

    public boolean rechazarOferta(long idProducto, int id, float precio) {
        Usuario vendedor = buscaUsuarioPorId(id);
        Usuario comprador = buscaUsuarioPorId(daoOfertaSQL.buscaComprador(idProducto,id,precio,dao));
        return daoOfertaSQL.deleteOferta(comprador, precio, buscaProductoId(idProducto),vendedor,dao);

    }

    public boolean updateProducto(long idProducto){
        return daoProductoSQL.updateProduct(idProducto, dao);
    }

    public boolean validarUsuarioPorToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }

        return daoUsuarioSQL.validarUsuarioPorToken(token, dao);
    }
    }


