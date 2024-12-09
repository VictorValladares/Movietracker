package es.umh.dadm.movieTracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private String userIntent = "USER_ID";
    private int userID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.mail);
        passwordEditText = findViewById(R.id.password);
    }

    public void login(View view){
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        DatabaseHelper db = new DatabaseHelper(this);

        userID = db.checkUsuario(email, password);
        if (userID!=-1) {
            openSuccessfulLoginActivity(userID);
        } else {
            // El usuario no está registrado
            Toast.makeText(this, R.string.usuario_no_registrado, Toast.LENGTH_SHORT).show();
        }
    }

    public void registro(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void openSuccessfulLoginActivity(int userID){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(userIntent, userID);
        startActivity(intent);
    }
}