package com.example.rumocerto;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MetaAdapter extends RecyclerView.Adapter<MetaAdapter.MetaViewHolder> {

    private List<Meta> listaMetas;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public MetaAdapter(List<Meta> listaMetas) {
        this.listaMetas = listaMetas;
    }

    @NonNull
    @Override
    public MetaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meta, parent, false);
        return new MetaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MetaViewHolder holder, int position) {
        Meta meta = listaMetas.get(position);
        holder.txtNome.setText(meta.getNome());
        holder.txtTipo.setText(meta.getTipo());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(meta);
                }
            }
        });

        // Converte o Long do banco para String legível
        holder.txtData.setText(sdf.format(new java.util.Date(meta.getDataFim())));
    }

    @Override
    public int getItemCount() {
        return listaMetas.size();
    }

    public static class MetaViewHolder extends RecyclerView.ViewHolder {
        TextView txtNome, txtData, txtTipo;

        public MetaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNome = itemView.findViewById(R.id.txtNomeMetaCard);
            txtData = itemView.findViewById(R.id.txtDataMetaCard);
            txtTipo = itemView.findViewById(R.id.txtTipoMetaCard);
        }
    }

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Meta meta);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}