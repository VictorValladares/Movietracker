package es.umh.dadm.movieTracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CreatePeliculaActivity extends AppCompatActivity {
    // Constantes y campos de clase
    private static final int CAPTURAR_IMAGEN = 1;
    private static final int CAPTURAR_IMAGEN_COMPLETA = 2;
    private static final int SELECCIONAR_IMAGEN_GALERIA = 3;
    private static final int REQUEST_CAMERA_PERMISSION = 1;

    private EditText editTextTituloPelicula, editTextDuracion, editTextGenero;
    private RatingBar ratingBarCalificacion;
    private ImageView imageViewPelicula;
    private String currentPhotoPath;
    private Spinner spinnerPlataformas;

    private String userIntent = "USER_ID";
    private String modeIntent = "MODE";
    private int userID = -1;
    private String prefijoFoto = "Imagen_";
    private String formatoFoto = ".png";
    private String directorioImagenes = "MisPelis74381147K/images";

    private Context contexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pelicula);

        userID = getIntent().getIntExtra(userIntent, -1);
        enlazarVistas();
        poblarSpinnerPlataformas(userID);
        configurarListeners(userID);
    }

    private void enlazarVistas() {
        editTextTituloPelicula = findViewById(R.id.editTextTituloPelicula);
        editTextDuracion = findViewById(R.id.editTextDuracion);
        editTextGenero = findViewById(R.id.editTextGenero);
        ratingBarCalificacion = findViewById(R.id.ratingBarCalificacion);
        imageViewPelicula = findViewById(R.id.imageViewPelicula);
        spinnerPlataformas = findViewById(R.id.spinnerPlataformas);
    }

    private void configurarListeners(int userID) {
        Button buttonRegistrarPelicula = findViewById(R.id.buttonRegistrarPelicula);
        buttonRegistrarPelicula.setOnClickListener(v -> registrarPelicula(userID));

        Button buttonCancelarPelicula = findViewById(R.id.buttonCancelarPelicula);
        buttonCancelarPelicula.setOnClickListener(v -> finish());
    }

    private void poblarSpinnerPlataformas(int idUsuario) {
        DatabaseHelper db = new DatabaseHelper(this);
        List<Plataforma> plataformas = db.obtenerPlataformasUsuario(idUsuario);

        ArrayAdapter<Plataforma> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, plataformas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlataformas.setAdapter(adapter);
    }

    private void registrarPelicula(int userID) {
        String titulo = editTextTituloPelicula.getText().toString().trim();
        String duracion = editTextDuracion.getText().toString().trim();
        String genero = editTextGenero.getText().toString().trim();
        float rating = ratingBarCalificacion.getRating();
        Plataforma plataformaSeleccionada = (Plataforma) spinnerPlataformas.getSelectedItem();

        if (validarEntradas(titulo, duracion, genero, plataformaSeleccionada)) {
            Pelicula nuevaPelicula = new Pelicula(userID, plataformaSeleccionada.getId(), currentPhotoPath, titulo, duracion, genero, rating, plataformaSeleccionada.getNombrePlataforma());
            DatabaseHelper db = new DatabaseHelper(this);
            long resultado = db.agregarPelicula(nuevaPelicula);
            if (resultado == -1) {
                Toast.makeText(this, R.string.error_al_agregar_la_pelicula, Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(this, PeliculasActivity.class);
                intent.putExtra(userIntent, userID);
                intent.putExtra(modeIntent, 0);
                startActivity(intent);
            }
        }
    }

    private boolean validarEntradas(String titulo, String duracion, String genero, Plataforma plataforma) {
        if (titulo.isEmpty() || duracion.isEmpty() || genero.isEmpty() || plataforma == null) {
            Toast.makeText(this, R.string.error_campos_requeridos, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    public void addImagePeli(View view){
        final CharSequence[] options = {getString(R.string.opcion_tomar_foto), getString(R.string.opcion_galeria), getString(R.string.opcion_cancelar)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.anadir_imagen);
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals(getString(R.string.opcion_tomar_foto))) { //Hacemos la foto
                //abrir camara
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                } else {
                    abrirCamara();
                }

            } else if (options[item].equals(getString(R.string.opcion_galeria))) { //Elegimos foto de galeria
                Intent cInt = new Intent();
                cInt.setType("image/*");
                cInt.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(cInt, getString(R.string.seleccionar_imagen)), SELECCIONAR_IMAGEN_GALERIA);
            } else if (options[item].equals(getString(R.string.opcion_cancelar))) { // Cancelamos
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void abrirCamara(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = crearImagenCompleta();
        } catch (Exception ex) {
            // Error occurred while creating the File
            ex.printStackTrace();
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            android.net.Uri photoURI = FileProvider.getUriForFile(contexto,getString(R.string.provider), photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, CAPTURAR_IMAGEN_COMPLETA);
        }
    }

    private File crearImagenCompleta() throws IOException {
        String imageName = prefijoFoto + System.currentTimeMillis() + formatoFoto;

        // Usar el mismo directorio que ImageHelper
        File storageDir = new File(getApplicationContext().getExternalFilesDir(null), directorioImagenes);

        if (!storageDir.exists()) {
            if (!storageDir.mkdirs()) {
                return null;  // Retorna null si no se pudo crear el directorio
            }
        }

        // Crear el archivo de imagen
        File image = File.createTempFile(
                imageName,  /* prefix */
                formatoFoto,         /* suffix */
                storageDir      /* directory */
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                abrirCamara();
            } else {
                Toast.makeText(this, R.string.error_permiso_camara, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURAR_IMAGEN_COMPLETA) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
                imageViewPelicula.setImageBitmap(bitmap);
                String imageName = new File(currentPhotoPath).getName();
                ImageHelper.guardarImagen(getApplicationContext(), bitmap, imageName);
            }
        } else if (requestCode == SELECCIONAR_IMAGEN_GALERIA){
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    imageViewPelicula.setImageBitmap(bitmap);

                    // Usar ImageHelper para guardar la imagen
                    String imageName = prefijoFoto + System.currentTimeMillis() + formatoFoto;
                    String savedImagePath = ImageHelper.guardarImagen(getApplicationContext(), bitmap, imageName);
                    currentPhotoPath = savedImagePath;

                } catch (IOException e) {
                    Toast.makeText(this, R.string.error_cargar_imagen, Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}