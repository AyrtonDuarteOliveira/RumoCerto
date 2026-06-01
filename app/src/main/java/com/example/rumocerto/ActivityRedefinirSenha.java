package com.example.rumocerto;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class ActivityRedefinirSenha extends ActivityMetodos {
    MaterialButton button_salvar;
    TextInputEditText editSenha, editSenhaNovamente;
    ImageView ImgValidacaoUm, ImgValidacaoDois, ImgValidacaoTres, ImgOlhoSenha, ImgOlhoSenhaNovamente;
    TextView TextValidacaoUm, TextValidacaoDois, TextValidacaoTres;
    Typeface fonte;
    boolean senhaEscondido = false, senhaNovamenteEscondido = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Intent intent = getIntent();
        DBHelper dbHelper = new DBHelper(ActivityRedefinirSenha.this);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_redefinirsenha);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.redefinirsenha), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImgValidacaoUm = findViewById(R.id.ImageViewValidacaoUm);
        ImgValidacaoDois = findViewById(R.id.ImageViewValidacaoDois);
        ImgValidacaoTres = findViewById(R.id.ImageViewValidacaoTres);
        TextValidacaoUm = findViewById(R.id.TextViewValidacaoUm);
        TextValidacaoDois = findViewById(R.id.TextViewValidacaoDois);
        TextValidacaoTres = findViewById(R.id.TextViewValidacaoTres);
        editSenha = findViewById(R.id.TextInputSenha);
        editSenhaNovamente = findViewById(R.id.TextInputSenhaNovamente);
        button_salvar = findViewById(R.id.ButtonSalvar);
        fonte = editSenha.getTypeface();
        ImgOlhoSenha = findViewById(R.id.ImageViewOlhoSenha);
        ImgOlhoSenhaNovamente = findViewById(R.id.ImageViewOlhoSenhaNovamente);

        editSenha.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                ValidaRequisitos(editSenha.getText().toString(), ImgValidacaoUm, TextValidacaoUm, ImgValidacaoDois, TextValidacaoDois, ImgValidacaoTres, TextValidacaoTres);
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
        });

        button_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ValidaRequisitos(editSenha.getText().toString(), ImgValidacaoUm, TextValidacaoUm, ImgValidacaoDois, TextValidacaoDois, ImgValidacaoTres, TextValidacaoTres))
                {
                    if (editSenha.getText().toString().equals(editSenhaNovamente.getText().toString()))
                    {
                        dbHelper.UpdateUser(intent.getIntExtra("usuario_id", -1), intent.getStringExtra("usuario_email"), editSenha.getText().toString());
                        Toast.makeText(ActivityRedefinirSenha.this, "Senha Alterada com Sucesso!", Toast.LENGTH_SHORT).show();
                        RegistraLog("Senha Alterada","Senha: {" + editSenha.getText().toString() + "}");
                        IrParaLogin(view);
                    }
                    else
                    {
                        Toast.makeText(ActivityRedefinirSenha.this, "Repita a Senha Corretamente", Toast.LENGTH_SHORT).show();
                        RegistraLog("SenhaNovamente Incorreta","Senha: {" + editSenha.getText().toString() + "}\nSenhaNovamente: {" + editSenhaNovamente.getText().toString() + "}");
                    }
                }
                else
                {
                    Toast.makeText(ActivityRedefinirSenha.this, "Insira uma Senha Válida", Toast.LENGTH_SHORT).show();
                    RegistraLog("Senha Inválida","Senha: {" + editSenha.getText().toString()+ "}");
                }
            }
        });

        ImgOlhoSenha.setOnClickListener(new View.OnClickListener() {
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

        ImgOlhoSenhaNovamente.setOnClickListener(new View.OnClickListener() {
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
