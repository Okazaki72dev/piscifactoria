package registro;

import commons.Coins;
import commons.Simul;
import propiedades.AlmacenPropiedades;
import propiedades.CriaTipo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Transcripcion {
    private static Transcripcion instance = null;
    String fileName = "src/registro/transcripciones/" + Simul.getGameName() + ".tr";
    String gameName = Simul.getGameName();
    private BufferedWriter writer;

    private Transcripcion() {
        iniciarTranscripcion(fileName, gameName);
    }

    public static Transcripcion getInstance() {
        if (instance == null) {
            instance = new Transcripcion();
        }
        return instance;
    }

    private void iniciarTranscripcion(String fileName, String gamename) {
        try {
            File file = getFile(fileName);

            writer = new BufferedWriter(new FileWriter(file));

            escribirTranscripcion("Inicio de la simulacion " + gamename +
                    "\nDinero: " + Coins.getInstance() + " monedas" +
                    "\nPeces implementados:");
            printFishType();
        } catch (IOException e) {
            ErrorLogger.getInstance().registrarError("Error al iniciar la transcripción: " + e.getMessage());
        }
    }

    private static File getFile(String fileName) throws IOException {
        File file = new File(fileName);

        // Verificar si el directorio de transcripciones existe, si no, crearlo
        File directory = file.getParentFile();
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Error al crear el directorio de transcripciones.");
        }

        // Crear el archivo de transcripción
        if (!file.exists() && !file.createNewFile()) {
            throw new IOException("Error al crear el archivo de transcripción.");
        }
        return file;
    }

    public void finalizarTranscripcion() {
        try {
            escribirTranscripcion("-------------------------");
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            ErrorLogger.getInstance().registrarError("Error al finalizar la transcripción");
        }
    }

    public void escribirTranscripcion(String mensaje) {
        try {
            writer.write(mensaje);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            ErrorLogger.getInstance().registrarError("Error al escribir en la transcripción");
        }
    }

    public void printFishType() {
        ArrayList<String> toPrintRio = new ArrayList<String>();
        ArrayList<String> toPrintMar = new ArrayList<String>();

        for (String pez : Simul.getPeces()) {
            CriaTipo tipo = AlmacenPropiedades.getPropByName(pez).getPiscifactoria();
            if (tipo == CriaTipo.RIO) {
                toPrintRio.add("-" + pez);
            } else {
                toPrintMar.add("-" + pez);
            }
        }

        escribirTranscripcion("Río:");
        for (String tipo : toPrintRio) {
            escribirTranscripcion(tipo);
        }

        escribirTranscripcion("Mar:");
        for (String tipo : toPrintMar) {
            escribirTranscripcion(tipo);
        }
    }
}
