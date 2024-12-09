package es.umh.dadm.movieTracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private String nombre;
    TextView textViewNombre;
    private BottomNavigationView bottomNavigationView;
    private String userIntent = "USER_ID";
    private String modeIntent = "MODE";
    private int userID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userID = getIntent().getIntExtra(userIntent, -1);
        if(userID != -1){
            DatabaseHelper db = new DatabaseHelper(this);
            nombre = db.getNombreApellidoUsuario(userID);
            textViewNombre = findViewById(R.id.textViewNombreyApellidoUser);
            textViewNombre.setText(nombre);
            textViewNombre.setVisibility(View.VISIBLE);

            bottomNavigationView = findViewById(R.id.bottom_navigation);
            bottomNavigationView.setSelectedItemId(R.id.invisible);

            bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
                item.setCheckable(true);
                if (item.getItemId() == R.id.home) {
                    Toast.makeText(MainActivity.this, R.string.pagina_principal, Toast.LENGTH_SHORT).show();
                    return true;
                } else if (item.getItemId() == R.id.Back) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return true;
                } else {
                    return false;
                }
            });
        } else {
            Toast.makeText(this, R.string.error_user_id, Toast.LENGTH_LONG).show();
        }
    }
    public void irPlataformas(View view) {

        if(userID != -1) {
            Intent intent = new Intent(MainActivity.this, PlataformaActivity.class);
            intent.putExtra(userIntent, userID);
            startActivity(intent);
        } else {
            Toast.makeText(this, R.string.error_user_id, Toast.LENGTH_LONG).show();
        }
    }

    public void irPeliculas(View view){
        if(userID != -1) {
            Intent intent = new Intent(MainActivity.this, PeliculasActivity.class);
            intent.putExtra(userIntent, userID);
            intent.putExtra(modeIntent, 0);
            startActivity(intent);
        } else {
            Toast.makeText(this, R.string.error_user_id, Toast.LENGTH_LONG).show();
        }
    }
}