package com.example.rumocerto;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class ActivityCadastro extends ActivityMetodos {

    Button button_cadastrar;
    TextInputEditText editEmail,editSenha, editSenhaNovamente;
    Typeface fonte;
    ImageView imageOlhoSenha, imageOlhoSenhaNovamente;
    boolean senhaEscondido = false, senhaNovamenteEscondido = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cadastro), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        button_cadastrar = findViewById(R.id.ButtonCadastrar);
        editEmail = findViewById(R.id.TextInputEmail);
        editSenha = findViewById(R.id.TextInputSenha);
        editSenhaNovamente = findViewById(R.id.TextInputSenhaNovamente);
        imageOlhoSenha = findViewById(R.id.ImageViewOlhoSenha);
        imageOlhoSenhaNovamente = findViewById(R.id.ImageViewOlhoSenhaNovamente);
        fonte = editSenha.getTypeface();

        DBHelper dbHelper = new DBHelper(ActivityCadastro.this);

        button_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(ValidaEmail(editEmail.getText().toString()))) {
                    Toast.makeText(ActivityCadastro.this, "Insira um E-mail Válido", Toast.LENGTH_SHORT).show();
                    RegistraLog("E-mail Inválido","E-mail: {" + editEmail.getText().toString() + "}");
                } else if (dbHelper.ProcuraEmail(editEmail.getText().toString()) != -1){
                    Toast.makeText(ActivityCadastro.this, "E-mail já Registrado", Toast.LENGTH_SHORT).show();
                    RegistraLog("E-mail já Registrado","E-mail: {" + editEmail.getText().toString() + "}");
                } else if (!(ValidaSenha(editSenha.getText().toString()))) {
                    Toast.makeText(ActivityCadastro.this, "Senha Inválida", Toast.LENGTH_SHORT).show();
                    RegistraLog("Senha Inválida","Senha: {" + editSenha.getText().toString()+ "}");
                } else if (!(ValidaSenhaNovamente(editSenha.getText().toString(), editSenhaNovamente.getText().toString()))) {
                    Toast.makeText(ActivityCadastro.this, "Repita a Senha Corretamente", Toast.LENGTH_SHORT).show();
                    RegistraLog("SenhaNovamente Incorreta","Senha: {" + editSenha.getText().toString() + "}\nSenhaNovamente: {" + editSenhaNovamente.getText().toString() + "}");
                } else if (!(dbHelper.CreateUser(editEmail.getText().toString(), editSenha.getText().toString()))){
                    Toast.makeText(ActivityCadastro.this, "Erro no Banco de Dados", Toast.LENGTH_SHORT).show();
                    RegistraLog("Erro no Banco de Dados","E-mail: {" + editEmail.getText().toString() + "}\nSenha: {" + editSenha.getText().toString() + "}");
                } else {
                    Toast.makeText(ActivityCadastro.this, "Conta Criada com Sucesso!", Toast.LENGTH_SHORT).show();
                    RegistraLog("Conta Criada","E-mail: {" + editEmail.getText().toString() + "}\nSenha: {" + editSenha.getText().toString() + "}");
                }
            }
        });

        imageOlhoSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (senhaEscondido) {
                    editSenha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    editSenha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                editSenha.setTypeface(fonte);
                editSenha.setSelection(editSenha.length());
                senhaEscondido = !senhaEscondido;
            }
        });

        imageOlhoSenhaNovamente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (senhaNovamenteEscondido) {
                    editSenhaNovamente.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    editSenhaNovamente.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                editSenhaNovamente.setTypeface(fonte);
                editSenhaNovamente.setSelection(editSenhaNovamente.length());
                senhaNovamenteEscondido = !senhaNovamenteEscondido;
            }
        });
    }
}