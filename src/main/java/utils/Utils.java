package utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class Utils {
    public static void pulsaParaContinuar(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Pulsa alguna tecla para continuar");
        sc.nextLine();
    }

    public static void limpiaPantallas(){
        for (int i = 0; i < 100; i++) {
            System.out.println();
        }
    }

    public static Calendar parseFechaStringCalendar(String fecha) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date fechaCalendar = sdf.parse(fecha);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fechaCalendar);

            return calendar;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String calendarToString(Calendar calendar) {
        if (calendar == null) {
            return null;
        }

        // Definimos el formato compatible con la base de datos
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Extraemos la fecha del calendar y la formateamos
        return sdf.format(calendar.getTime());
    }
}
