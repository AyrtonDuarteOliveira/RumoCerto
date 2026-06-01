package com.example.rumocerto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;

public class ActivityDashboard extends ActivityMetodos {
    LinearLayout button_metas, button_financas;
    TextView txtResumoSaldo, txtResumoEntradas, txtResumoSaidas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtResumoSaldo = findViewById(R.id.TextViewSaldoDash);
        txtResumoEntradas = findViewById(R.id.TextViewSomaEntradas);
        txtResumoSaidas = findViewById(R.id.TextViewSomaSaidas);

        atualizarCardMetas(getIntent().getIntExtra("usuario_id", -1));
        atualizarResumoMensal(getIntent().getIntExtra("usuario_id", -1));

        Intent intent = getIntent();
        button_metas = findViewById(R.id.BotaoMetas);
        button_metas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IrParaMetas(view, intent.getIntExtra("usuario_id", -1));
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
    public void atualizarCardMetas(int usuarioId) {
        DBHelper db = new DBHelper(this);
        List<Meta> metas = db.ReadMetas(usuarioId);

        LinearLayout container = findViewById(R.id.containerMetasDash);
        container.removeAllViews();

        if (metas != null && !metas.isEmpty()) {
            for (int i = 0; i < metas.size(); i++) {
                Meta meta = metas.get(i);

                View itemView = getLayoutInflater().inflate(R.layout.item_meta_dashboard, null);
                TextView txtNome = itemView.findViewById(R.id.TextNomeMetaDash);
                ProgressBar barra = itemView.findViewById(R.id.ProgressBarMetaDash);

                txtNome.setText(meta.getNome());

                long inicio = meta.getDataInicio();
                long fim = meta.getDataFim();
                long hoje = System.currentTimeMillis();

                int progresso = 0;
                if (hoje >= fim) {
                    progresso = 100;
                } else if (hoje > inicio) {
                    long tempoTotal = fim - inicio;
                    long tempoPassado = hoje - inicio;
                    if (tempoTotal > 0) {
                        progresso = (int) ((tempoPassado * 100) / tempoTotal);
                    }
                }


                txtNome.setText(meta.getNome() + " - " + progresso + "%");
                barra.setProgress(progresso);
                container.addView(itemView);
            }
        } else {
            TextView tvVazio = new TextView(this);
            tvVazio.setText("Nenhuma Meta Cadastrada no Momento");
            container.addView(tvVazio);
        }
    }
    private void atualizarResumoMensal(int usuarioId) {
        DBHelper db = new DBHelper(this);
        long saldoCentavos = db.getSaldoTotal(usuarioId);
        long totalEntradasCentavos = db.getTotalEntradasMesAtual(usuarioId);
        long totalSaidasCentavos = db.getTotalSaidasMesAtual(usuarioId);

        double saldoReal = saldoCentavos / 100.0;
        double entradasReal = totalEntradasCentavos / 100.0;
        double saidasReal = totalSaidasCentavos / 100.0;

        txtResumoSaldo.setText(String.format(new Locale("pt", "BR"), "R$ %.2f", (double) saldoReal));
        txtResumoEntradas.setText(String.format(new Locale("pt", "BR"), "+ R$ %.2f", (double) entradasReal));
        txtResumoSaidas.setText(String.format(new Locale("pt", "BR"), "- R$ %.2f", (double) saidasReal));
    }
}