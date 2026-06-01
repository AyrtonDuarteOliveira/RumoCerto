package com.example.rumocerto;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ActivityEditaMeta extends ActivityMetodos {

    Button button_atualizar, button_voltar, button_pessoais, button_profissionais, button_excluir;
    EditText editNome, editDescricao, editDataInicio, editDataFim;
    int metaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edita_meta);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        DBHelper dbHelper = new DBHelper(ActivityEditaMeta.this);
        Intent intent = getIntent();

        metaId = intent.getIntExtra("meta_id", -1);
        int usuarioId = intent.getIntExtra("usuario_id", -1);

        editNome = findViewById(R.id.EditTextNome);
        editDescricao = findViewById(R.id.EditTextDescricao);
        editDataInicio = findViewById(R.id.EditTextDataInicio);
        editDataFim = findViewById(R.id.EditTextDataFim);
        button_atualizar = findViewById(R.id.ButtonAtualizar);
        button_voltar = findViewById(R.id.ButtonVoltar);
        button_pessoais = findViewById(R.id.ButtonPessoais);
        button_profissionais = findViewById(R.id.ButtonProfissionais);

        Meta meta = dbHelper.ReadMeta(metaId);
        if (meta != null) {
            editNome.setText(meta.getNome());
            editDescricao.setText(meta.getDescricao());
            editDataInicio.setText(sdf.format(new Date(meta.getDataInicio())));
            editDataFim.setText(sdf.format(new Date(meta.getDataFim())));

            if (meta.getTipo().equals("Pessoais")) {
                button_pessoais.setSelected(true);
                button_profissionais.setSelected(false);
            } else {
                button_pessoais.setSelected(false);
                button_profissionais.setSelected(true);
            }
        }

        // --- CONFIGURAÇÃO DOS SELETORES DE DATA ---
        editDataInicio.setOnClickListener(v -> mostrarCalendario(editDataInicio));
        editDataFim.setOnClickListener(v -> mostrarCalendario(editDataFim));

        // --- SELEÇÃO DE TIPO ---
        button_pessoais.setOnClickListener(v -> {
            button_pessoais.setSelected(true);
            button_profissionais.setSelected(false);
        });

        button_profissionais.setOnClickListener(v -> {
            button_pessoais.setSelected(false);
            button_profissionais.setSelected(true);
        });

        button_atualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = editNome.getText().toString();
                String desc = editDescricao.getText().toString();
                String tipo = button_pessoais.isSelected() ? "Pessoais" : "Profissionais";

                if (nome.isEmpty() || desc.isEmpty() || editDataFim.getText().toString().isEmpty()) {
                    Toast.makeText(ActivityEditaMeta.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    long dataInicioLong = sdf.parse(editDataInicio.getText().toString()).getTime();
                    long dataFimLong = sdf.parse(editDataFim.getText().toString()).getTime();

                    if (dataFimLong <= dataInicioLong) {
                        Toast.makeText(ActivityEditaMeta.this, "A data final deve ser maior que a data inicial", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // Chama o metodo de UPDATE no banco
                    boolean sucesso = dbHelper.UpdateMeta(metaId, nome, desc, dataInicioLong, dataFimLong, tipo);

                    if (sucesso) {
                        Toast.makeText(ActivityEditaMeta.this, "Meta atualizada!", Toast.LENGTH_SHORT).show();
                        IrParaMetas(view, usuarioId);
                    } else {
                        Toast.makeText(ActivityEditaMeta.this, "Erro ao atualizar", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(ActivityEditaMeta.this, "Data inválida", Toast.LENGTH_SHORT).show();
                }
            }
        });

        button_voltar.setOnClickListener(v -> IrParaMetas(v, usuarioId));

        button_excluir = findViewById(R.id.ButtonExcluir); // Caso adicione ao XML
        button_excluir.setOnClickListener(v -> {
            new AlertDialog.Builder(ActivityEditaMeta.this)
                    .setTitle("Excluir Meta")
                    .setMessage("Tem certeza que deseja apagar esta meta?")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        if (dbHelper.DeleteMeta(metaId)) {
                            Toast.makeText(this, "Meta excluída", Toast.LENGTH_SHORT).show();
                            IrParaMetas(v, usuarioId);
                        }
                    })
                    .setNegativeButton("Não", null)
                    .show();
        });
    }
    private void mostrarCalendario(EditText campoData) {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    String dataFormatada = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, (monthOfYear + 1), year);
                    campoData.setText(dataFormatada);
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}