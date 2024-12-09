package es.umh.dadm.movieTracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PeliculasActivity extends AppCompatActivity {

    ListView listViewPeliculas;
    List<Pelicula> listaPeliculas;
    PeliculaAdapter adapter;
    private int ACCESO_DESDE_MENU = 0;
    private int ACCESO_DESDE_PLATAFORMAS = 1;

    private String userIntent = "USER_ID";
    private String modeIntent = "MODE";
    private String nombrePlataformaIntent = "nombrePlataforma";
    private String idPlataformaIntent = "ID_PLATAFORMA";
    private String idPeliculaIntent = "idPelicula";
    private int userID = -1;
    private int idPlataforma = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peliculas);

        DatabaseHelper db = new DatabaseHelper(this);

        userID = getIntent().getIntExtra(userIntent, -1);
        int mode = getIntent().getIntExtra(modeIntent, -1);

        if(userID != -1){
            if(mode == ACCESO_DESDE_MENU) {
                listaPeliculas = db.obtenerPeliculasUsuario(userID);
                List<String> peliculasInfo = new ArrayList<>();

                for (Pelicula pelicula : listaPeliculas) {
                    String infoPelicula = pelicula.getTitulo() + " - " + calificacion(pelicula.getCalificacion());
                    peliculasInfo.add(infoPelicula);
                }

                listViewPeliculas = findViewById(R.id.listViewPeliculas);

                adapter = new PeliculaAdapter(this, listaPeliculas);
                listViewPeliculas.setAdapter(adapter);

                listViewPeliculas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        abrirPelicula(position);
                    }
                });

                // Añadir listener para pulsación larga
                listViewPeliculas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        new AlertDialog.Builder(PeliculasActivity.this)
                                .setTitle(R.string.eliminar_pelicula)
                                .setMessage(R.string.confirmar_eliminar_pelicula)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Lógica para eliminar la película
                                        Pelicula peliculaSeleccionada = listaPeliculas.get(position);
                                        db.eliminarPelicula(peliculaSeleccionada.getId());
                                        // Actualizar la lista
                                        listaPeliculas.remove(position);
                                        peliculasInfo.remove(position);
                                        adapter.notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                        return true; // Indica que hemos consumido el evento de click largo
                    }
                });
            }
            else if(mode == ACCESO_DESDE_PLATAFORMAS){
                idPlataforma = getIntent().getIntExtra(idPlataformaIntent, -1);
                if(idPlataforma != -1){
                    listaPeliculas = db.obtenerPeliculasPorPlataforma(idPlataforma);
                    List<String> peliculasInfo = new ArrayList<>();

                    String nombrePlataforma = getIntent().getStringExtra(nombrePlataformaIntent);

                    for (Pelicula pelicula : listaPeliculas) {
                        String infoPelicula = pelicula.getTitulo() + " - " + calificacion(pelicula.getCalificacion());
                        peliculasInfo.add(infoPelicula);
                    }

                    listViewPeliculas = findViewById(R.id.listViewPeliculas);

                    adapter = new PeliculaAdapter(this, listaPeliculas);
                    listViewPeliculas.setAdapter(adapter);

                    listViewPeliculas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            abrirPelicula(position);
                        }
                    });

                    // Añadir listener para pulsación larga
                    listViewPeliculas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            new AlertDialog.Builder(PeliculasActivity.this)
                                    .setTitle(R.string.eliminar_pelicula)
                                    .setMessage(R.string.confirmar_eliminar_pelicula)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Lógica para eliminar la película
                                            Pelicula peliculaSeleccionada = listaPeliculas.get(position);
                                            db.eliminarPelicula(peliculaSeleccionada.getId());
                                            // Actualizar la lista
                                            listaPeliculas.remove(position);
                                            peliculasInfo.remove(position);
                                            adapter.notifyDataSetChanged();
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                            return true; // Indica que hemos consumido el evento de click largo
                        }
                    });

                    if (nombrePlataforma != null) {
                        TextView textViewPeliculas = findViewById(R.id.textViewPeliculas);
                        textViewPeliculas.setText(getString(R.string.peliculas_de) + nombrePlataforma);
                    }
                }
                else{
                    Toast.makeText(this, R.string.error_plataforma_id, Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(this, R.string.error_modo_acceso, Toast.LENGTH_LONG).show();

            }
        }
        else{
            Toast.makeText(this, R.string.error_user_id, Toast.LENGTH_LONG).show();
        }
    }

    private String calificacion(float calificacion) {
        return String.format("%.1f estrellas", calificacion);
    }

    public void abrirPelicula(int pos) {
        Pelicula peliculaSeleccionada = listaPeliculas.get(pos);
        Intent intent = new Intent(this, DetallePeliculaActivity.class);
        intent.putExtra(userIntent, userID);
        intent.putExtra(idPeliculaIntent, peliculaSeleccionada.getId());

        startActivity(intent);
    }

    public void addPelicula(View view){
        if(userID != -1) {
            Intent intent = new Intent(this, CreatePeliculaActivity.class);
            intent.putExtra(userIntent, userID);
            startActivity(intent);
        } else {
            Toast.makeText(this, R.string.error_user_id, Toast.LENGTH_LONG).show();
        }
    }

    public void volverPeliculas(View view){
        if(userID != -1) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(userIntent, userID);
            startActivity(intent);
        } else {
            Toast.makeText(this, R.string.error_user_id, Toast.LENGTH_LONG).show();
        }
    }

    public void exportPeliculas(View view){
        int userId = getIntent().getIntExtra(userIntent, -1);
        if(userId != -1) {
            Exportar exportador = new Exportar(this);
            exportador.exportarPeliculas(userId);
        } else {
            Toast.makeText(this, R.string.error_user_id, Toast.LENGTH_LONG).show();
        }
    }
}