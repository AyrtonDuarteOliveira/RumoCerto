package com.example.rumocerto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

public class ActivityMetas extends ActivityMetodos {

    LinearLayout button_home, button_financas;
    Button button_novameta;
    RecyclerView recyclerView;
    TextView textViewVazio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_metas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        recyclerView = findViewById(R.id.RecyclerViewMetas);
        textViewVazio = findViewById(R.id.TextViewVazio);
        DBHelper db = new DBHelper(this);
        int usuarioId = getIntent().getIntExtra("usuario_id", -1);
        List<Meta> listaDeMetas = db.ReadMetas(usuarioId);

        if (listaDeMetas.isEmpty()) {
            textViewVazio.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textViewVazio.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            MetaAdapter adapter = new MetaAdapter(listaDeMetas);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(meta -> {
               Intent intentEdicao = new Intent(ActivityMetas.this, ActivityEditaMeta.class);
               intentEdicao.putExtra("meta_id", meta.getId());
               intentEdicao.putExtra("usuario_id", usuarioId);
               startActivity(intentEdicao);
            });
        }

        button_novameta = findViewById(R.id.ButtonNovaMeta);
        button_novameta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IrParaCriarMeta(view, intent.getIntExtra("usuario_id", -1));
            }
        });

        button_home = findViewById(R.id.BotaoHome);
        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IrParaDashboard(view, intent.getIntExtra("usuario_id", -1));
            }
        });

        button_financas = findViewById(R.id.BotaoFinancas);
        button_financas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IrParaFinancas(view, intent.getIntExtra("usuario_id", -1));
            }
        });
    }
}