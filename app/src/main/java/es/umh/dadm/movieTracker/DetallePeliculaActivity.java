package es.umh.dadm.movieTracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class DetallePeliculaActivity extends AppCompatActivity {
    private static final int CAPTURAR_IMAGEN = 1;
    private static final int CAPTURAR_IMAGEN_COMPLETA = 2;
    private static final int SELECCIONAR_IMAGEN_GALERIA = 3;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private EditText tituloView, duracionView, generoView, nombrePlataformaView;
    private RatingBar calificacionView;
    private ImageView caratulaView;
    private ImageButton buttonEditarPel;
    private String currentPhotoPath;
    private Context contexto;

    private String userIntent = "USER_ID";
    private String modeIntent = "MODE";
    private String idPeliculaIntent = "idPelicula";
    private int userID = -1;
    private int idPelicula = -1;
    private String prefijoFoto = "Imagen_";
    private String formatoFoto = ".png";
    private String directorioImagenes = "MisPelis74381147K/images";

    private boolean isEditable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_pelicula);

        tituloView = findViewById(R.id.editTextTituloPel);
        duracionView = findViewById(R.id.editTextDuracionPel);
        generoView = findViewById(R.id.editTextGeneroPel);
        calificacionView = findViewById(R.id.RatingBarCalificacion);
        nombrePlataformaView = findViewById(R.id.editTextTituloPel);
        caratulaView = findViewById(R.id.imageViewPelicula);
        buttonEditarPel = findViewById(R.id.buttonEditarPel);

        // Obteniendo los datos pasados a esta actividad
        Intent intent = getIntent();
        userID = intent.getIntExtra(userIntent, -1);
        idPelicula = intent.getIntExtra(idPeliculaIntent, -1);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Pelicula pelicula = dbHelper.getPeliculaById(idPelicula);

        String titulo = pelicula.getTitulo();
        String duracion = pelicula.getDuracion();
        String genero = pelicula.getGenero();
        float calificacion = pelicula.getCalificacion();
        String nombrePlataforma = pelicula.getNombrePlataforma();
        currentPhotoPath = pelicula.getCaratula();

        if (currentPhotoPath != null && !currentPhotoPath.isEmpty()) {
            caratulaView.setImageURI(Uri.parse(currentPhotoPath)); // Mostrar la imagen usando un Uri
        }

        tituloView.setText(titulo);
        duracionView.setText(duracion);
        generoView.setText(genero);
        calificacionView.setRating(calificacion);
        nombrePlataformaView.setText(nombrePlataforma);

        caratulaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditable) {
                    final CharSequence[] options = {getString(R.string.opcion_tomar_foto), getString(R.string.opcion_galeria), getString(R.string.opcion_cancelar)};
                    AlertDialog.Builder builder = new AlertDialog.Builder(DetallePeliculaActivity.this);
                    builder.setTitle(R.string.anadir_imagen);
                    builder.setItems(options, (dialog, item) -> {
                        if (options[item].equals(getString(R.string.opcion_tomar_foto))) { //Hacemos la foto
                            //abrir camara
                            if (ContextCompat.checkSelfPermission(DetallePeliculaActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(DetallePeliculaActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
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
            }
        });

        buttonEditarPel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEditable) {
                    tituloView.setFocusableInTouchMode(true);
                    tituloView.setClickable(true);
                    tituloView.requestFocus();

                    duracionView.setFocusableInTouchMode(true);
                    duracionView.setClickable(true);
                    duracionView.requestFocus();

                    generoView.setFocusableInTouchMode(true);
                    generoView.setClickable(true);
                    generoView.requestFocus();

                    calificacionView.setIsIndicator(false);

                    nombrePlataformaView.setFocusableInTouchMode(true);
                    nombrePlataformaView.setClickable(true);
                    nombrePlataformaView.requestFocus();

                    caratulaView.setClickable(true);

                    isEditable = true;
                    buttonEditarPel.setImageResource(R.drawable.done_edit_icon);
                } else {
                    tituloView.setFocusable(false);
                    tituloView.setClickable(false);

                    duracionView.setFocusable(false);
                    duracionView.setClickable(false);

                    generoView.setFocusable(false);
                    generoView.setClickable(false);

                    calificacionView.setIsIndicator(true);

                    nombrePlataformaView.setFocusable(false);
                    nombrePlataformaView.setClickable(false);

                    caratulaView.setClickable(false);

                    buttonEditarPel.setImageResource(R.drawable.edit_icon);

                    isEditable = false;

                    //Guardar los cambios en la base de datos
                    actualizarPelicula(idPelicula);
                }
            }
        });
    }
    public void actualizarPelicula(int peliculaId) {
        if(peliculaId != -1 && userID != -1){
            // Obtén la base de datos
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            // Crea un nuevo mapa de valores, donde los nombres de las columnas son las keys
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.getColumnPeliculasTitulo(), tituloView.getText().toString());
            values.put(DatabaseHelper.getColumnPeliculasDuracion(), duracionView.getText().toString());
            values.put(DatabaseHelper.getColumnPeliculasGenero(), generoView.getText().toString());
            values.put(DatabaseHelper.getColumnPeliculasCalificacion(), calificacionView.getRating());
            values.put(DatabaseHelper.getColumnPeliculasNombrePlataforma(), nombrePlataformaView.getText().toString());
            values.put(DatabaseHelper.getColumnPeliculasCaratula(), currentPhotoPath);

            // ¿Qué fila actualizamos? Especifica la selección por ID
            String selection = DatabaseHelper.getColumnPeliculasId() + " = ?";
            String[] selectionArgs = {String.valueOf(peliculaId)}; // Asegúrate de tener el ID de la película

            // Actualiza las filas
            int count = db.update(
                    DatabaseHelper.getTablePeliculas(),
                    values,
                    selection,
                    selectionArgs);

            db.close();
            Intent intent = new Intent(this, PeliculasActivity.class);
            intent.putExtra(userIntent, userID);
            intent.putExtra(modeIntent, 0);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, R.string.error_actualizar_pelicula, Toast.LENGTH_SHORT).show();
        }
    }

    public void volverPeliculas(View view) {
        Intent intent = new Intent(this, PeliculasActivity.class);
        intent.putExtra(userIntent, userID);
        startActivity(intent);
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
                ".png",         /* suffix */
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
                Toast.makeText(this, R.string.permiso_camara_denegado, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURAR_IMAGEN_COMPLETA) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
                caratulaView.setImageBitmap(bitmap);
                String imageName = new File(currentPhotoPath).getName();
                ImageHelper.guardarImagen(getApplicationContext(), bitmap, imageName);
            }
        } else if (requestCode == SELECCIONAR_IMAGEN_GALERIA){
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    caratulaView.setImageBitmap(bitmap);
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
