package es.umh.dadm.movieTracker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

public class PlataformaAdapter extends ArrayAdapter<Plataforma> {

    public PlataformaAdapter(Context context, List<Plataforma> plataformas) {
        super(context, 0, plataformas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.elemento_lista_plataforma, parent, false);
            holder = new ViewHolder();
            holder.tvNombrePlataforma = convertView.findViewById(R.id.tvNombrePlataforma);
            holder.tvURLPlataforma = convertView.findViewById(R.id.tvURLPlataforma);
            holder.imageViewPlataforma = convertView.findViewById(R.id.imageViewPlataforma);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Plataforma currentItem = getItem(position);
        if (currentItem != null) {
            holder.tvNombrePlataforma.setText(currentItem.getNombrePlataforma());
            holder.tvURLPlataforma.setText(currentItem.getUrlAcceso());

            if (currentItem.getImagen() != null && !currentItem.getImagen().isEmpty()) {
                File imgFile = new File(currentItem.getImagen());
                if(imgFile.exists()){
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    holder.imageViewPlataforma.setImageBitmap(myBitmap);
                } else {
                    holder.imageViewPlataforma.setImageResource(R.drawable.image_icon); // Considera tener una imagen predeterminada
                }
            } else {
                holder.imageViewPlataforma.setImageResource(R.drawable.image_icon); // Imagen predeterminada si no hay ruta válida
            }
        }

        return convertView;
    }

    // ViewHolder pattern para mejorar el rendimiento en ListView
    static class ViewHolder {
        TextView tvNombrePlataforma;
        TextView tvURLPlataforma;
        ImageView imageViewPlataforma;
    }
}
