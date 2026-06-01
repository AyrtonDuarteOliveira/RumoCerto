package com.example.rumocerto;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

public class ActivityLogin extends ActivityMetodos {

    boolean senhaEscondido = false;
    Button button_entrar;
    TextInputEditText editEmail,editSenha;
    TextView textRedefinirSenha;
    ImageView imageOlhoSenha;
    Typeface fonte;
    MaterialCardView botaoGoogle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        /*TODO Log-in Persistente
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);

        int userId = prefs.getInt("user_id", -1);

        if (userId != -1)
        {
            Intent intent = new Intent(ActivityLogin.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        */
        button_entrar = findViewById(R.id.ButtonSalvar);
        editEmail = findViewById(R.id.TextInputEmail);
        editSenha = findViewById(R.id.TextInputSenha);
        botaoGoogle = findViewById(R.id.BlocoGoogle);
        textRedefinirSenha = findViewById(R.id.TextViewEsqueceuASenha);
        imageOlhoSenha = findViewById(R.id.ImageViewOlhoSenha);
        fonte = editSenha.getTypeface();

        DBHelper dbHelper = new DBHelper(ActivityLogin.this);

        botaoGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ActivityLogin.this, "WIP", Toast.LENGTH_SHORT).show();
            }
        });

        button_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(ValidaEmail(editEmail.getText().toString())))
                {
                    Toast.makeText(ActivityLogin.this, "Insira um E-mail Válido", Toast.LENGTH_SHORT).show();
                    RegistraLog("E-mail Inválido","E-mail: {" + editEmail.getText().toString() + "}");
                } else if (!(ValidaSenha(editSenha.getText().toString()))) {
                    Toast.makeText(ActivityLogin.this, "Insira uma Senha Válida", Toast.LENGTH_SHORT).show();
                    RegistraLog("Senha Inválida","Senha: {" + editSenha.getText().toString()+ "}");
                } else if (dbHelper.ProcuraEmail(editEmail.getText().toString()) == -1) {
                    Toast.makeText(ActivityLogin.this, "Usuário não Encontrado", Toast.LENGTH_SHORT).show();
                    RegistraLog("E-mail não Registrado","E-mail: {" + editEmail.getText().toString() + "}");
                } else {
                    int id = dbHelper.ReadUser(editEmail.getText().toString(), editSenha.getText().toString());
                    if (id>-1)
                    {
                        /*TODO Log-in Persistente
                        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();

                        editor.putInt("user_id", id);
                        editor.apply();

                        Intent intent = new Intent(ActivityLogin.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        */
                        Toast.makeText(ActivityLogin.this, "Log-in Realizado com Sucesso", Toast.LENGTH_SHORT).show();
                        RegistraLog("Log-in Realizado","E-mail: {" + editEmail.getText().toString() + "}\nSenha: {" + editSenha.getText().toString() + "}");
                        IrParaDashboard(view, id);
                    }
                    else if (id==-2)
                    {
                        Toast.makeText(ActivityLogin.this, "Usuário Desativado", Toast.LENGTH_SHORT).show();
                    }
                    else if (id==-3)
                    {
                        Toast.makeText(ActivityLogin.this, "Usuário Bloqueado Por Cinco Minutos", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(ActivityLogin.this, "Senha Incorreta", Toast.LENGTH_SHORT).show();
                        RegistraLog("Senha Incorreta","Senha: {" + editSenha.getText().toString() + "}");
                    }
                }
            }
        });

        editSenha.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (editSenha.getText().toString().isEmpty())
                {
                    textRedefinirSenha.setVisibility(View.VISIBLE);
                    imageOlhoSenha.setVisibility(View.INVISIBLE);

                }
                else
                {
                    textRedefinirSenha.setVisibility(View.INVISIBLE);
                    imageOlhoSenha.setVisibility(View.VISIBLE);
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

        textRedefinirSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ValidaEmail(editEmail.getText().toString()))
                {

                    int id = dbHelper.ProcuraEmail(editEmail.getText().toString());

                    if (id != -1)
                    {
                        IrParaRedefinirSenha(view, editEmail.getText().toString(), id);
                    }
                }
                else
                {
                    Toast.makeText(ActivityLogin.this, "Insira um E-mail de uma Conta Existente", Toast.LENGTH_SHORT).show();
                    RegistraLog("E-mail não Registrado","E-mail: {" + editEmail.getText().toString() + "}");
                }
            }
        });
    }
}