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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class CreatePlataformaActivity extends AppCompatActivity {
    private static final int CAPTURAR_IMAGEN_COMPLETA = 2;
    private static final int SELECCIONAR_IMAGEN_GALERIA = 3;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private EditText editTextPlataforma, editTextUrl, editTextUserPlat, editTextPasswordPlat;
    private ImageView imageViewPlataforma;
    private String currentPhotoPath;
    private Button buttonCancelarPlataforma;
    private Context contexto;
    private String userIntent = "USER_ID";
    private String idPlataformaIntent = "ID_PLATAFORMA";
    private int userID = -1;
    private String prefijoFoto = "Imagen_";
    private String formatoFoto = ".png";
    private String directorioImagenes = "MisPelis74381147K/images";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plataforma);
        enlazarVistas();

        Button buttonRegistrarPlataforma = findViewById(R.id.buttonRegistrarPlataforma);
        buttonRegistrarPlataforma.setOnClickListener(v -> {
            userID = getIntent().getIntExtra(userIntent, -1);
            if (userID != -1) {
                registrarPlataforma(userID, editTextPlataforma, editTextUrl, editTextUserPlat, editTextPasswordPlat, currentPhotoPath);
            }
        });

        buttonCancelarPlataforma.setOnClickListener(v -> {
            Intent intent = new Intent(this, PlataformaActivity.class);
            userID = getIntent().getIntExtra(userIntent, -1);
            if (userID != -1) {
                intent.putExtra(userIntent, userID);
                startActivity(intent);
            }
        });

    }
    private void enlazarVistas() {
        editTextPlataforma = findViewById(R.id.editTextPlataforma);
        editTextUrl = findViewById(R.id.editTextUrl);
        editTextUserPlat = findViewById(R.id.editTextUserPlat);
        editTextPasswordPlat = findViewById(R.id.editTextPasswordPlat);
        imageViewPlataforma = findViewById(R.id.imageViewPlataforma);
        buttonCancelarPlataforma = findViewById(R.id.buttonCancelarPlataforma);
    }

    private void registrarPlataforma(int userID, EditText editTextPlataforma, EditText editTextUrl, EditText editTextUserPlat, EditText editTextPasswordPlat, String imagen){
        String nombrePlataforma = editTextPlataforma.getText().toString().trim();
        String url = editTextUrl.getText().toString().trim();
        String userPlataforma = editTextUserPlat.getText().toString().trim();
        String passwordPlataforma = editTextPasswordPlat.getText().toString().trim();

        if (nombrePlataforma.isEmpty() || url.isEmpty() || userPlataforma.isEmpty() || passwordPlataforma.isEmpty()) {
            Toast.makeText(this, R.string.error_campos_requeridos, Toast.LENGTH_SHORT).show();
            return; // Detener la ejecución adicional del método
        }
        Plataforma nuevaPlataforma = new Plataforma(userID, imagen, nombrePlataforma, url, userPlataforma, passwordPlataforma);
        DatabaseHelper db = new DatabaseHelper(this);
        long idPlataforma = db.agregarPlataforma(nuevaPlataforma);
        if (idPlataforma == -1) {
            Toast.makeText(this, R.string.error_agregar_plataforma, Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, PlataformaActivity.class);
            intent.putExtra(userIntent, userID);
            startActivity(intent);
        }
    }

    public void addImagePlat(View view) {
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