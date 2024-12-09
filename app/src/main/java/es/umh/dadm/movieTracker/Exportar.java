package es.umh.dadm.movieTracker;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Exportar {

    private Context context;
    private DatabaseHelper db;

    private String nombre_archivo_plataformas = "plataformas_usuario_";
    private String nombre_archivo_peliculas = "peliculas_usuario_";
    private String formato_archivo = ".txt";


    public Exportar(Context context) {
        this.context = context;
        this.db = new DatabaseHelper(context);
    }

    public void exportarPlataformas(int idUsuario) {
        List<Plataforma> plataformas = db.obtenerPlataformasUsuario(idUsuario);
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), nombre_archivo_plataformas + idUsuario + formato_archivo);
        try (FileWriter writer = new FileWriter(file)) {
            for (Plataforma plataforma : plataformas) {
                writer.write(plataforma.platformtoString() + "\n");  // Asegúrate de que Plataforma tiene un método toString adecuado
            }
            Toast.makeText(context, context.getString(R.string.exportar_plataforma_exito) + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(context, context.getString(R.string.error_exportar_plataformas) + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void exportarPeliculas(int idUsuario) {
        List<Pelicula> peliculas = db.obtenerPeliculasUsuario(idUsuario);
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), nombre_archivo_peliculas + idUsuario + formato_archivo);
        try (FileWriter writer = new FileWriter(file)) {
            for (Pelicula pelicula : peliculas) {
                writer.write(pelicula.peliculatoString() + "\n");
            }
            Toast.makeText(context, context.getString(R.string.exportar_pelicula_exito) + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(context, context.getString(R.string.error_exportar_peliculas) + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}

