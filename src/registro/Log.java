package registro;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
    private static Log instance;
    private BufferedWriter writer;

    private Log() {
        // Constructor privado para asegurar la singularidad del Singleton
    }

    public static Log getInstance() {
        if (instance == null) {
            instance = new Log();
        }
        return instance;
    }

    public void iniciarLog(String nombrePartida) {
        try {
            // Inicializar el BufferedWriter con el nombre del archivo
            writer = new BufferedWriter(new FileWriter("logs/" + nombrePartida + ".log"));
            escribirLog("[aaaa-MM-dd HH:mm:ss] Inicio de la simulación " + nombrePartida);
        } catch (IOException e) {
            registrarError("Error al iniciar el log");
        }
    }

    public void finalizarLog() {
        try {
            // Cerrar el BufferedWriter al finalizar
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            registrarError("Error al finalizar el log");
        }
    }

    public void escribirLog(String mensaje) {
        try {
            // Escribir en el archivo de log con formato de fecha
            String fechaActual = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]").format(new Date());
            writer.write(fechaActual + " " + mensaje);
            writer.newLine();
            writer.flush(); // Limpiar el buffer
        } catch (IOException e) {
            registrarError("Error al escribir en el log");
        }
    }

    public void registrarError(String mensaje) {
        try {
            // Registrar errores en un archivo compartido para todos los sistemas
            BufferedWriter errorWriter = new BufferedWriter(new FileWriter("logs/0_errors.log", true));
            String fechaActual = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]").format(new Date());
            errorWriter.write(fechaActual + " " + mensaje);
            errorWriter.newLine();
            errorWriter.flush();
            errorWriter.close();
        } catch (IOException e) {
            e.printStackTrace(); // Manejo básico de errores
        }
    }

    // Resto de métodos para gestionar el log
}
