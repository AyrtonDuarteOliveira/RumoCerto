package com.example.rumocerto;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ActivityFinancas extends ActivityMetodos {

    Button btnSalvar, btnTipoEntrada, btnTipoSaida;
    LinearLayout button_home, button_metas, containerTransacoes;
    EditText editValor, editDescricao;
    TextView txtSaldoValor, labelNovaTransacao, txtResumoEntradas, txtResumoSaidas, txtViewMes;
    String tipoSelecionado = "Saída";
    DBHelper dbHelper;
    int usuarioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_financas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editValor = findViewById(R.id.editValorTransacao);
        editDescricao = findViewById(R.id.editDescricaoTransacao);
        btnSalvar = findViewById(R.id.btnSalvar);
        btnTipoEntrada = findViewById(R.id.btnTipoEntrada);
        btnTipoSaida = findViewById(R.id.btnTipoSaida);
        labelNovaTransacao = findViewById(R.id.labelNovaTransacao);
        txtResumoEntradas = findViewById(R.id.txtResumoEntradas);
        txtResumoSaidas = findViewById(R.id.txtResumoSaidas);
        txtViewMes = findViewById(R.id.textViewMes);
        containerTransacoes = findViewById(R.id.containerTransacoes);

        btnTipoEntrada.setOnClickListener(v -> {
            tipoSelecionado = "Entrada";
            atualizarVisualBotoes("Entrada");
        });

        btnTipoSaida.setOnClickListener(v -> {
            tipoSelecionado = "Saída";
            atualizarVisualBotoes("Saída");
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarTransacao();
            }
        });
        dbHelper = new DBHelper(this);
        usuarioId = getIntent().getIntExtra("usuario_id", -1);

        txtSaldoValor = findViewById(R.id.textViewSaldoValor);
        configurarMesAtual();
        atualizarSaldo();
        atualizarResumoMensal();
        atualizarVisualBotoes("Saída");
        atualizarListaRecentes();

        button_metas = findViewById(R.id.BotaoMetas);
        button_metas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IrParaMetas(view, usuarioId);
            }
        });
        button_home = findViewById(R.id.BotaoHome);
        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IrParaDashboard(view, usuarioId);
            }
        });
    }
    private void atualizarSaldo() {

        long saldoCentavos = dbHelper.getSaldoTotal(usuarioId);

        double saldoReal = saldoCentavos / 100.0;

        String saldoFormatado = String.format(Locale.getDefault(), "R$ %.2f", (double) saldoReal);

        txtSaldoValor.setText(saldoFormatado);

    }
    private void salvarTransacao() {
        String valorString = editValor.getText().toString().trim();
        String descricao = editDescricao.getText().toString().trim();

        if (valorString.isEmpty() || descricao.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double valorDouble = Double.parseDouble(valorString.replace(",", "."));
            long valorCentavos = (long) (valorDouble * 100);

            boolean sucesso = dbHelper.CreateFinanca(usuarioId, tipoSelecionado, valorCentavos, descricao);

            if (sucesso) {
                Toast.makeText(this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
                editValor.setText("");
                editDescricao.setText("");
                atualizarSaldo();
                atualizarResumoMensal();
                atualizarListaRecentes();
            } else {
                Toast.makeText(this, "Erro ao salvar a transação.", Toast.LENGTH_SHORT).show();
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Valor inválido.", Toast.LENGTH_SHORT).show();
        }
    }
    private void atualizarVisualBotoes(String tipo) {
        if (tipo.equals("Entrada")) {
            btnTipoEntrada.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
            btnTipoEntrada.setTextColor(Color.WHITE);

            btnTipoSaida.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            btnTipoSaida.setTextColor(Color.parseColor("#757575"));

            labelNovaTransacao.setText("Nova Entrada");
            labelNovaTransacao.setTextColor(Color.parseColor("#4CAF50"));
        } else {
            btnTipoSaida.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D32F2F")));
            btnTipoSaida.setTextColor(Color.WHITE);

            btnTipoEntrada.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            btnTipoEntrada.setTextColor(Color.parseColor("#757575"));

            labelNovaTransacao.setText("Nova Saída");
            labelNovaTransacao.setTextColor(Color.parseColor("#D32F2F"));
        }
    }
    private void atualizarResumoMensal() {
        long totalEntradasCentavos = dbHelper.getTotalEntradasMesAtual(usuarioId);
        long totalSaidasCentavos = dbHelper.getTotalSaidasMesAtual(usuarioId);

        double entradasReal = totalEntradasCentavos / 100.0;
        double saidasReal = totalSaidasCentavos / 100.0;

        txtResumoEntradas.setText(String.format(Locale.getDefault(), "+R$ %.2f", entradasReal));
        txtResumoSaidas.setText(String.format(Locale.getDefault(), "-R$ %.2f", saidasReal));
    }
    private void configurarMesAtual() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        String nomeMes = calendar.getDisplayName(java.util.Calendar.MONTH, java.util.Calendar.LONG, new Locale("pt", "BR"));

        if (nomeMes != null && !nomeMes.isEmpty()) {
            nomeMes = nomeMes.substring(0, 1).toUpperCase() + nomeMes.substring(1);
        }

        txtViewMes.setText("Mês de " + nomeMes);
    }
    private void atualizarListaRecentes() {
        containerTransacoes.removeAllViews();

        List<Financa> lista = dbHelper.ReadFinancas(usuarioId);

        for (Financa f : lista) {
            View itemView = getLayoutInflater().inflate(R.layout.item_transacao, null);
            TextView txtNome = itemView.findViewById(R.id.txtNomeTransacao);
            TextView txtValor = itemView.findViewById(R.id.txtValorTransacao);
            TextView txtData = itemView.findViewById(R.id.txtDataTransacao);

            txtNome.setText(f.getDescricao());

            double valorReal = f.getValor() / 100.0;
            String prefixo = f.getTipo().equals("Entrada") ? "+ R$ " : "- R$ ";
            txtValor.setText(String.format(Locale.getDefault(), "%s%.2f", prefixo, valorReal));

            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR"));
            String dataFormada = sdf.format(new java.util.Date(f.getData()));
            txtData.setText(dataFormada);

            containerTransacoes.addView(itemView);
        }
    }
}