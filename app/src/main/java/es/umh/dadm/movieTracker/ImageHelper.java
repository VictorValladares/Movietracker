package es.umh.dadm.movieTracker;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageHelper {

    public static String guardarImagen(Context c, Bitmap bitmap, String imageName){
        File dir = new File(c.getExternalFilesDir(null), "MisPelis74381147K");

        // Crear subdirectorio "images"
        File imagesDir = new File(dir, "images");

        // Guardar la imagen en el subdirectorio "images"
        File imageFile = new File(imagesDir, imageName);
        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            // Compress bitmap to png format
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (IOException e) {
            Log.e("Guardar imagen", "Error al guardar imagen", e);
        }
        return imageFile.getAbsolutePath();
    }
}

