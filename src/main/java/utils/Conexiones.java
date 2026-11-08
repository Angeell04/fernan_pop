package utils;

import java.util.Properties;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class Conexiones {

    // ========================================================
    //  CONFIGURACIÓN SMTP Y APIS EXTERNAS (VÍA VARIABLES DE ENTORNO)
    // ========================================================
    private static final String CORREO_EMISOR = System.getenv("SMTP_EMAIL") != null
            ? System.getenv("SMTP_EMAIL")
            : "tu_correo@gmail.com";
    private static final String TOKEN_O_CLAVE = System.getenv("SMTP_TOKEN") != null
            ? System.getenv("SMTP_TOKEN")
            : "xxxx xxxx xxxx xxxx"; // Token de aplicación SMTP (16 caracteres)

    /**
     * Configura la sesión SMTP y envía el correo electrónico en una sola llamada.
     */
    public static boolean enviarCorreo(String destinatario, String asunto, String cuerpoHTML) {

        // 1. Configurar propiedades del servidor SMTP (Gmail)
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        // 2. Crear la sesión con autenticación automática
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(CORREO_EMISOR, TOKEN_O_CLAVE);
            }
        });

        try {
            // 3. Crear y configurar el mensaje de correo
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(CORREO_EMISOR));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto);
            message.setContent(cuerpoHTML, "text/html; charset=UTF-8");

            // 4. Enviar
            Transport.send(message);
            System.out.println("--> Correo enviado con éxito a: " + destinatario);
            return true;

        } catch (MessagingException e) {
            System.err.println("--> Error al enviar el correo: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
