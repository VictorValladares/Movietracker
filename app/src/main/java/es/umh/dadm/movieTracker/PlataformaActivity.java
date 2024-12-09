package es.umh.dadm.movieTracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class PlataformaActivity extends AppCompatActivity {

    ListView listViewPlataformas;
    List<Plataforma> listaPlataformas;
    ArrayAdapter<Plataforma> adapter;
    private String userIntent = "USER_ID";
    private String idPlataformaIntent = "ID_PLATAFORMA";
    private int userID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plataforma);

        DatabaseHelper db = new DatabaseHelper(this);

        userID = getIntent().getIntExtra(userIntent, -1);

        if(userID != -1){
            listaPlataformas = db.obtenerPlataformasUsuario(userID);
            listViewPlataformas = findViewById(R.id.listViewPlataformas);

            if(listaPlataformas.isEmpty()){
                Toast.makeText(this, R.string.aviso_no_hay_plataformas, Toast.LENGTH_LONG).show();
            }
            else{
                adapter = new PlataformaAdapter(this, listaPlataformas);
                listViewPlataformas.setAdapter(adapter);

                listViewPlataformas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        abrirPlataforma(position, userID);
                    }
                });

                listViewPlataformas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                        // Obtener la plataforma seleccionada
                        Plataforma plataformaSeleccionada = (Plataforma) parent.getItemAtPosition(position);

                        // Dialogo para confirmar la eliminación
                        new AlertDialog.Builder(PlataformaActivity.this)
                                .setTitle(R.string.dialogo_eliminar_plataforma)
                                .setMessage(R.string.dialogo_pregunta_confirmacion)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        eliminarPlataforma(plataformaSeleccionada.getId());
                                        // Actualizar el ListView después de eliminar
                                        cargarPlataformas(userID);
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                        return true; // Devuelve true para indicar que el evento de clic largo ha sido consumido.
                    }
                });
            }
        }
        else{
            Toast.makeText(this, R.string.error_verificar_user, Toast.LENGTH_LONG).show();

            // Redirigir al usuario a la pantalla de inicio de sesión.
            Intent loginIntent = new Intent(this, LoginActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Limpia la pila de actividades
            startActivity(loginIntent);
            finish();
        }
    }

    public void addPlataforma(View view){
        userID = getIntent().getIntExtra(userIntent, -1);
        if(userID != -1) {
            Intent intent = new Intent(this, CreatePlataformaActivity.class);
            intent.putExtra(userIntent, userID);
            startActivity(intent);
        } else {
            Toast.makeText(this, R.string.error_user_id, Toast.LENGTH_LONG).show();
        }
    }

    private void abrirPlataforma(int position, int userId) {
        Plataforma plataformaSeleccionada = listaPlataformas.get(position);

        Intent intent = new Intent(this, DetallePlataformaActivity.class);
        intent.putExtra(userIntent, userId);
        intent.putExtra(idPlataformaIntent, plataformaSeleccionada.getId());

        startActivity(intent);
    }

    private void eliminarPlataforma(int id) {
        DatabaseHelper db = new DatabaseHelper(this);
        db.eliminarPlataforma(id);
    }

    private void cargarPlataformas(int idUsuario) {
        DatabaseHelper db = new DatabaseHelper(this);
        List<Plataforma> listaDePlataformasActualizada = db.obtenerPlataformasUsuario(idUsuario);
        if(adapter == null) {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaDePlataformasActualizada);
            listViewPlataformas.setAdapter(adapter);
        } else {
            adapter.clear();
            adapter.addAll(listaDePlataformasActualizada);
            adapter.notifyDataSetChanged();
        }
    }

    public void volverPlataforma(View view){
        userID = getIntent().getIntExtra(userIntent, -1);
        if(userID != -1) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(userIntent, userID);
            startActivity(intent);
        } else {
            Toast.makeText(this, R.string.error_user_id, Toast.LENGTH_LONG).show();
        }
    }

    public void exportPlat(View view){
        userID = getIntent().getIntExtra(userIntent, -1);
        if(userID != -1) {
            Exportar exportador = new Exportar(this);
            exportador.exportarPlataformas(userID);
        } else {
            Toast.makeText(this, R.string.error_user_id, Toast.LENGTH_LONG).show();
        }
    }
}
