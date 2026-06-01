package com.example.rumocerto;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ActivityCriaMeta extends ActivityMetodos {

    Button button_salvar, button_voltar, button_pessoais, button_profissionais;
    EditText editNome, editDescricao, editDataInicio, editDataFim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cria_meta);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Intent intent = getIntent();

        editNome = findViewById(R.id.EditTextNome);
        editDescricao = findViewById(R.id.EditTextDescricao);
        editDataInicio = findViewById(R.id.EditTextDataInicio);
        editDataFim = findViewById(R.id.EditTextDataFim);
        button_salvar = findViewById(R.id.ButtonSalvar);
        button_voltar = findViewById(R.id.ButtonVoltar);
        button_pessoais = findViewById(R.id.ButtonPessoais);
        button_profissionais = findViewById(R.id.ButtonProfissionais);
        button_pessoais.setSelected(true);
        button_profissionais.setSelected(false);

        editDataInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarCalendario(editDataInicio);
            }
        });

        editDataFim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarCalendario(editDataFim);
            }
        });

        button_pessoais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button_pessoais.setSelected(true);
                button_profissionais.setSelected(false);
            }
        });

        button_profissionais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button_pessoais.setSelected(false);
                button_profissionais.setSelected(true);
            }
        });

        DBHelper dbHelper = new DBHelper(ActivityCriaMeta.this);

        button_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long dataInicio = 0, dataFim = 0;
                String tipo = "";

                if (editNome.getText().toString().isEmpty()) {
                    Toast.makeText(ActivityCriaMeta.this, "Insira um nome para a meta", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (editDescricao.getText().toString().isEmpty()){
                    Toast.makeText(ActivityCriaMeta.this, "Insira uma descrição para a meta", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    Date date = sdf.parse(editDataInicio.getText().toString());
                    RegistraLog("Teste Date","Data: {" + date + "}");
                    dataInicio = date.getTime();
                    RegistraLog("Teste DataInicio","Data: {" + dataInicio + "}");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Date date = sdf.parse(editDataFim.getText().toString());
                    RegistraLog("Teste Date","Data: {" + date + "}");
                    dataFim = date.getTime();
                    RegistraLog("Teste DataFim","Data: {" + dataFim + "}");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (dataFim <= dataInicio)
                {
                    Toast.makeText(ActivityCriaMeta.this, "A data final deve ser maior que a data inicial", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (button_pessoais.isSelected())
                {
                    tipo = "Pessoais";
                }
                else if (button_profissionais.isSelected())
                {
                    tipo = "Profissionais";
                }
                else
                {
                    Toast.makeText(ActivityCriaMeta.this, "Selecione um tipo de meta", Toast.LENGTH_SHORT).show();
                    return;
                }

                dbHelper.CreateMeta(intent.getIntExtra("usuario_id", -1), editNome.getText().toString(), editDescricao.getText().toString(), dataInicio, dataFim, tipo);
                Toast.makeText(ActivityCriaMeta.this, "Meta criada com sucesso!", Toast.LENGTH_SHORT).show();
                IrParaMetas(view, intent.getIntExtra("usuario_id", -1));
            }
        });
        button_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IrParaMetas(view, intent.getIntExtra("usuario_id", -1));
            }
        });
    }
    private void mostrarCalendario(EditText campoData) {
        // Pega a data atual para mostrar como padrão no calendário
        final Calendar c = Calendar.getInstance();
        int ano = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);

        // Cria o seletor de data
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    // Formata a data e coloca no EditText
                    // monthOfYear + 1 pois Janeiro é 0 no Java
                    String dataFormatada = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, (monthOfYear + 1), year);
                    campoData.setText(dataFormatada);
                }, ano, mes, dia);

        datePickerDialog.show();
    }
}