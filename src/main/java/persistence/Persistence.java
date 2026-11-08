package persistence;

import controller.GestionAPP;
import models.Producto;
import models.Usuario;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Persistence {

    public static String leeRutaData() {
        try {
            Properties properties = new Properties();
            properties.load(new FileReader("src/main/Properties/Properties.properties"));
            String ruta = properties.getProperty("rutaUsuarios");
            return ruta;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static String leeRutaLogs(){
        try{
            Properties properties = new Properties();
            properties.load(new FileReader("src/main/Properties/Properties.properties"));
            String ruta = properties.getProperty("rutaLogs");
            return ruta;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void logs(String mensaje) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(leeRutaLogs(), true));
            writer.write("\n"+mensaje);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }







    public static String muestraConfig(ArrayList<Usuario> usuarios) {
        try {
            Properties properties = new Properties();
            properties.load(new FileReader("src/main/Properties/Properties.properties"));
            String config = "";
            config += "invitado=" + properties.getProperty("invitado")+ "\n";
            config += "rutaLog=" + properties.getProperty("rutaLogs")+ "\n";
            config += "rutaUsuarios=" + properties.getProperty("rutaUsuarios")+ "\n";
            for (Usuario usuario : usuarios){
                config += usuario.getMail()+ ": "+properties.getProperty(usuario.getNombre()+usuario.getId()+"LastAccess")+ "\n";
            }
            return config;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public static String generaResumenVenta(Usuario vendedor, Usuario comprador, Producto producto, double precio, int idTrato) {
        String ruta = "src/data/facturas/factura_" + idTrato + ".pdf";
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(ruta));
            writer.write("========== RESUMEN DE VENTA ==========");
            writer.newLine();
            writer.write("ID Trato: " + idTrato);
            writer.newLine();
            writer.write("Fecha: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            writer.newLine();
            writer.write("Vendedor: " + vendedor.getNombre() + " (" + vendedor.getMail() + ")");
            writer.newLine();
            writer.write("Comprador: " + comprador.getNombre() + " (" + comprador.getMail() + ")");
            writer.newLine();
            writer.write("Producto: " + producto.getTitulo());
            writer.newLine();
            writer.write("Precio final: " + precio + " €");
            writer.newLine();
            writer.write("======================================");
            writer.close();
            return ruta;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean copiaSeguridad(String ruta, ArrayList<Usuario> usuarios){
        try {
            for(Usuario usuario : usuarios) {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ruta+ "/usuario" + usuario.getId() + ".bin"));
                oos.writeObject(usuario);
                oos.close();
            }
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    public static String leeLogs(){

        File logs = new File(leeRutaLogs());
        String linea = "";
        String resultado = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(logs));
            while ((linea = reader.readLine()) != null) {
                resultado += linea + "\n";
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return resultado;

    }

    public static ArrayList<Usuario> leeUsuarios(String ruta) {
        ArrayList<Usuario> usuarios = new ArrayList();
        File directory = new File("src/data/"+ruta);

        try {
            for(String file : directory.list()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream("src/data/CopiasDeSeguridad/" + file));
                Usuario usuario = (Usuario)ois.readObject();
                ois.close();
                usuarios.add(usuario);
            }

            return usuarios;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
