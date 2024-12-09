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
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class DetallePlataformaActivity extends AppCompatActivity {
    private static final int CAPTURAR_IMAGEN = 1;
    private static final int CAPTURAR_IMAGEN_COMPLETA = 2;
    private static final int SELECCIONAR_IMAGEN_GALERIA = 3;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private EditText nombreP, urlP, user, password;
    private ImageView imageViewPlataforma;
    private ImageButton buttonEditarPlat;
    private int idPlataforma;
    private String nombrePlataforma;
    private String currentPhotoPath;
    private boolean isEditable = false;
    private Context contexto;
    private String userIntent = "USER_ID";
    private String modeIntent = "MODE";
    private String nombreIntent = "nombrePlataforma";
    private String idPlataformaIntent = "ID_PLATAFORMA";
    private int userID = -1;
    private String prefijoFoto = "Imagen_";
    private String formatoFoto = ".png";
    private String directorioImagenes = "MisPelis74381147K/images";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_plataforma);

        Intent intent = getIntent();
        idPlataforma = intent.getIntExtra(idPlataformaIntent, -1);
        userID = intent.getIntExtra(userIntent, -1);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Plataforma plataforma = dbHelper.getPlataformaById(idPlataforma);

        enlazarVistas();

        nombrePlataforma = plataforma.getNombrePlataforma();

        nombreP.setText(nombrePlataforma);
        urlP.setText(plataforma.getUrlAcceso());
        user.setText(plataforma.getUsuarioAcceso());
        password.setText(plataforma.getPasswordAcceso());

        if (currentPhotoPath != null && !currentPhotoPath.isEmpty()) {
            imageViewPlataforma.setImageURI(Uri.parse(currentPhotoPath)); // Mostrar la imagen usando un Uri
        }

        // Codigo para editar la plataforma
        imageViewPlataforma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditable) {
                    final CharSequence[] options = {getString(R.string.opcion_tomar_foto), getString(R.string.opcion_galeria), getString(R.string.opcion_cancelar)};
                    AlertDialog.Builder builder = new AlertDialog.Builder(DetallePlataformaActivity.this);
                    builder.setTitle(R.string.anadir_imagen);
                    builder.setItems(options, (dialog, item) -> {
                        if (options[item].equals(getString(R.string.opcion_tomar_foto))) { //Hacemos la foto
                            //abrir camara
                            if (ContextCompat.checkSelfPermission(DetallePlataformaActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(DetallePlataformaActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
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

        buttonEditarPlat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEditable) {
                    // Habilitar edición
                    nombreP.setFocusableInTouchMode(true);
                    nombreP.setClickable(true);
                    nombreP.requestFocus();

                    urlP.setFocusableInTouchMode(true);
                    urlP.setClickable(true);
                    urlP.requestFocus();

                    user.setFocusableInTouchMode(true);
                    user.setClickable(true);
                    user.requestFocus();

                    password.setFocusableInTouchMode(true);
                    password.setClickable(true);
                    password.requestFocus();

                    imageViewPlataforma.setClickable(true);

                    // Cambiar el icono a "confirmar"
                    buttonEditarPlat.setImageResource(R.drawable.done_edit_icon);

                    isEditable = true;
                } else {
                    // Deshabilitar edición
                    nombreP.setFocusable(false);
                    nombreP.setClickable(false);

                    urlP.setFocusable(false);
                    urlP.setClickable(false);

                    user.setFocusable(false);
                    user.setClickable(false);

                    password.setFocusable(false);
                    password.setClickable(false);

                    imageViewPlataforma.setClickable(false);

                    // Cambiar el icono a "editar"
                    buttonEditarPlat.setImageResource(R.drawable.edit_icon);

                    isEditable = false;

                    // Guardar los cambios en la base de datos
                    actualizarPlataforma(idPlataforma);
                }
            }
        });

    }

    private void enlazarVistas(){
        nombreP = findViewById(R.id.editTextNombrePlat);
        urlP = findViewById(R.id.editTextUrlPlat);
        user = findViewById(R.id.editTextUsuarioPlat);
        password = findViewById(R.id.editTextPassword);
        imageViewPlataforma = findViewById(R.id.imageViewPlataforma);
        buttonEditarPlat = findViewById(R.id.buttonEditarPlat);
    }

    public void verPeliculasPlataforma(View view) {
        Intent intent = new Intent(this, PeliculasActivity.class);
        intent.putExtra(userIntent, userID);
        intent.putExtra(nombreIntent, nombrePlataforma);
        intent.putExtra(idPlataformaIntent, idPlataforma);
        intent.putExtra(modeIntent, 1);
        startActivity(intent);
    }

    public void actualizarPlataforma(int idPlataform) {
        if(idPlataform != -1 && userID != -1){
            // Obtén la base de datos
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            // Crea un nuevo mapa de valores, donde los nombres de las columnas son las keys
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.getColumnPlataformasNombre(), nombreP.getText().toString());
            values.put(DatabaseHelper.getColumnPlataformasUrl(), urlP.getText().toString());
            values.put(DatabaseHelper.getColumnPlataformasUsuario(), user.getText().toString());
            values.put(DatabaseHelper.getColumnPlataformasPassword(), password.getText().toString());
            values.put(DatabaseHelper.getColumnPlataformasImagen(), currentPhotoPath);

            // ¿Qué fila actualizamos? Especifica la selección por ID
            String selection = DatabaseHelper.getColumnPlataformasId() + " = ?";
            String[] selectionArgs = {String.valueOf(idPlataform)}; // Asegúrate de tener el ID de la plataforma

            // Actualiza las filas
            int count = db.update(
                    DatabaseHelper.getTablePlataformas(),
                    values,
                    selection,
                    selectionArgs);

            db.close();
            Intent intent = new Intent(this, PlataformaActivity.class);
            intent.putExtra(userIntent, userID);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, R.string.error_actualizar_plataforma, Toast.LENGTH_SHORT).show();
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
                imageViewPlataforma.setImageBitmap(bitmap);
                String imageName = new File(currentPhotoPath).getName();
                ImageHelper.guardarImagen(getApplicationContext(), bitmap, imageName);
            }
        } else if (requestCode == SELECCIONAR_IMAGEN_GALERIA){
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    imageViewPlataforma.setImageBitmap(bitmap);
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