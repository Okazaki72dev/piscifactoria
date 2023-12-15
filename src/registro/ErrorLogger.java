package registro;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ErrorLogger {
    private static ErrorLogger instance;
    private BufferedWriter writer;

    private ErrorLogger() {
        iniciarErrorLogger();
    }

    public static synchronized ErrorLogger getInstance() {
        if (instance == null) {
            instance = new ErrorLogger();
        }
        return instance;
    }

    private void iniciarErrorLogger() {
        try {
            File errorsFolder = new File("src/registro/logs");
            if (!errorsFolder.exists()) {
                if (errorsFolder.mkdirs()) {
                    System.out.println("Carpeta 'errors' creada correctamente.");
                } else {
                    System.err.println("Error al crear la carpeta 'errors'.");
                }
            }

            writer = new BufferedWriter(new FileWriter("src/registro/logs/0_errors.log", true));
        } catch (IOException e) {
            e.printStackTrace(); // Manejo básico de errores
        }
    }

    public void registrarError(String mensaje) {
        try {
            String fechaActual = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]").format(new Date());
            writer.write(fechaActual + " " + mensaje);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace(); // Manejo básico de errores
        }
    }

    public void finalizarErrorLogger() {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace(); // Manejo básico de errores
        }
    }
}
