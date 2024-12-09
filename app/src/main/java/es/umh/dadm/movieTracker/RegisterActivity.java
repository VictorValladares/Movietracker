package es.umh.dadm.movieTracker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.mindrot.jbcrypt.BCrypt;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

@SuppressLint("UseSwitchCompatOrMaterialCode")
public class RegisterActivity extends AppCompatActivity {
    private String date = "01/01/2024";
    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private EditText editTextNombre, editTextApellidos, editTextMail, editTextRespuestaSeguridad, editTextPassword, editTextOtroInteres;
    private Spinner spinnerRegister, spinnerIntereses;
    private CheckBox checkBoxAutenticacion;
    private Button buttonRegistrar;
    private final String formatoFecha = "dd/MM/yyyy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Enlazamos las vistas, inicializamos el DatePicker y configuramos los spinners
        enlazarVistas();
        initDatePicker();
        configurarSpinners();

        buttonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });

        spinnerIntereses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals(getString(R.string.otros))) {
                    editTextOtroInteres.setVisibility(View.VISIBLE);
                } else {
                    editTextOtroInteres.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // no hacer nada
            }
        });

    }

    private void enlazarVistas() {
        dateButton = findViewById(R.id.datePickerButton);
        editTextMail = findViewById(R.id.editTextMail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextApellidos = findViewById(R.id.editTextApellidos);
        editTextRespuestaSeguridad = findViewById(R.id.editTextRespuestaSeguridad);
        checkBoxAutenticacion = findViewById(R.id.checkBox);
        spinnerRegister = findViewById(R.id.spinner_register);
        spinnerIntereses = findViewById(R.id.spinner_intereses);
        editTextOtroInteres = findViewById(R.id.editTextOtroInteres);
        buttonRegistrar = findViewById(R.id.registerButton);
    }

    private void configurarSpinners(){

        ArrayList<String> preguntasSeguridad = new ArrayList<>();
        preguntasSeguridad.add(getString(R.string.animal_favorito));
        preguntasSeguridad.add(getString(R.string.ciudad_favorita));
        preguntasSeguridad.add(getString(R.string.cual_es_el_nombre_de_tu_mejor_amigo));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, preguntasSeguridad);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinnerRegister.setAdapter(adapter);

        ArrayList<String> interesesUser = new ArrayList<>();
        interesesUser.add(getString(R.string.tecnolog_a));
        interesesUser.add(getString(R.string.deportes));
        interesesUser.add(getString(R.string.redes_sociales));
        interesesUser.add(getString(R.string.cine));
        interesesUser.add(getString(R.string.otros));
        ArrayAdapter<String> adapterIntereses = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, interesesUser);
        adapterIntereses.setDropDownViewResource(android.R.layout.select_dialog_multichoice);
        spinnerIntereses.setAdapter(adapterIntereses);
    }

    private void registrarUsuario() {
        String nombre = editTextNombre.getText().toString().trim();
        String apellidos = editTextApellidos.getText().toString().trim();
        String mail = editTextMail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String respuestaSeguridad = editTextRespuestaSeguridad.getText().toString().trim();
        String preguntaSeguridad = spinnerRegister.getSelectedItem().toString();
        String intereses = spinnerIntereses.getSelectedItem().toString();
        String otroInteres = editTextOtroInteres.getText().toString().trim();
        boolean autenticacion = checkBoxAutenticacion.isChecked();

        if (intereses.equals(getString(R.string.otros))) {
            intereses = otroInteres;
        }

        if (mail.isEmpty()) {
            editTextMail.setError(getString(R.string.error_campo_vacio));
            editTextMail.requestFocus();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            editTextMail.setError(getString(R.string.error_formato_mail));
            editTextMail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError(getString(R.string.error_campo_vacio));
            editTextPassword.requestFocus();
            return;
        }

        if (nombre.isEmpty()) {
            editTextNombre.setError(getString(R.string.error_campo_vacio));
            editTextNombre.requestFocus();
            return;
        }

        if (apellidos.isEmpty()) {
            editTextApellidos.setError(getString(R.string.error_campo_vacio));
            editTextApellidos.requestFocus();
            return;
        }

        if (respuestaSeguridad.isEmpty()) {
            editTextRespuestaSeguridad.setError(getString(R.string.error_campo_vacio));
            editTextRespuestaSeguridad.requestFocus();
            return;
        }

        DatabaseHelper db = new DatabaseHelper(this);
        if(db.emailExiste(mail)){
            editTextMail.setError(getString(R.string.error_email_registrado));
            editTextMail.requestFocus();
            return;
        }

        //Hasheamos la contraseña
        String hash = BCrypt.hashpw(password, BCrypt.gensalt());
        Usuario nuevoUsuario = new Usuario(nombre, apellidos, mail, date, respuestaSeguridad, preguntaSeguridad, intereses, autenticacion);
        db.addUsuario(nuevoUsuario, hash);

        Toast.makeText(RegisterActivity.this, R.string.registro_exitoso, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();

    }

    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            SimpleDateFormat dateFormat = new SimpleDateFormat(formatoFecha, Locale.getDefault());
            date = dateFormat.format(calendar.getTime());
            dateButton.setText(date);
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);
    }

    public void openDatePicker(View view){
        datePickerDialog.show();
    }

    public void volverInicio(View view){
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}