package es.umh.dadm.movieTracker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

public class PeliculaAdapter extends ArrayAdapter<Pelicula> {
    public PeliculaAdapter(Context context, List<Pelicula> peliculas) {
        super(context, 0, peliculas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtén el objeto Pelicula en esta posición
        Pelicula pelicula = getItem(position);

        // Verifica si la vista existente está siendo reutilizada, de lo contrario, infle la vista
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.elemento_lista_pelicula, parent, false);
        }

        // Busca las vistas para poblar los datos
        TextView tvTitulo = convertView.findViewById(R.id.tvTitulo);
        TextView tvDirector = convertView.findViewById(R.id.tvDirector);
        TextView tvDuracion = convertView.findViewById(R.id.tvDuracion);
        RatingBar ratingBar = convertView.findViewById(R.id.ratingBarPel);
        ImageView imageView = convertView.findViewById(R.id.imageViewPlataforma);

        // Población de datos en las vistas
        tvTitulo.setText(pelicula.getTitulo());
        tvDirector.setText(pelicula.getGenero());
        tvDuracion.setText(pelicula.getDuracion() + " min");
        ratingBar.setRating(pelicula.getCalificacion());
        Bitmap bitmap = BitmapFactory.decodeFile(pelicula.getCaratula());
        imageView.setImageBitmap(bitmap);


        return convertView;
    }
}
