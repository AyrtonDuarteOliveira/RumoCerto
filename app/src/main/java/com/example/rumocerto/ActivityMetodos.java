package com.example.rumocerto;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class ActivityMetodos extends AppCompatActivity {

    public boolean ValidaEmail(String email)
    {
        //Requisitos:
        //Não vazio
        //Tipo de provedor válido (@gmail.com, @hotmail.com, @protonmail.com, @fatec.sp.gov.br)
        //Conteúdo antes do @
        return !(email.isEmpty()) && (email.contains("@gmail.com")||email.contains("@hotmail.com")||email.contains("@protonmail.com")||email.contains("@fatec.sp.gov.br")) && email.charAt(0) != '@';
    }
    public boolean ValidaSenha(String senha)
    {
        //Requisitos:
        //Pelo menos um 1 caractere especial (!@#$%)
        //Pelo menos 1 letra maiúscula (A-Z)
        //No mínimo 8 caracteres
        return senha.matches(".*[A-Z].*") && senha.matches(".*[^a-zA-Z0-9].*") && senha.length() >= 8;
    }
    public boolean ValidaSenhaNovamente(String senha, String senhaNovamente)
    {
        //Compara se as duas senhas são iguais
        return Objects.equals(senha, senhaNovamente);
    }
    public boolean ValidaRequisitos(String senha, ImageView ImgValidacaoUm, TextView TextValidacaoUm, ImageView ImgValidacaoDois, TextView TextValidacaoDois, ImageView ImgValidacaoTres, TextView TextValidacaoTres)
    {
        boolean ValidacaoUm = false, ValidacaoDois = false, ValidacaoTres = false;
        //Pelo menos um 1 caractere especial (!@#$%)
        if (senha.matches(".*[^a-zA-Z0-9].*"))
        {
            ImgValidacaoUm.setImageResource(R.drawable.imgvalidacaopositivo);
            TextValidacaoUm.setTextColor(Color.parseColor("#28830D"));
            ValidacaoUm = true;
        }
        else
        {
            ImgValidacaoUm.setImageResource(R.drawable.imgvalidacaonegativo);
            TextValidacaoUm.setTextColor(Color.parseColor("#000000"));
        }
        //Pelo menos 1 letra maiúscula (A-Z)
        if (senha.matches(".*[A-Z].*"))
        {
            ImgValidacaoDois.setImageResource(R.drawable.imgvalidacaopositivo);
            TextValidacaoDois.setTextColor(Color.parseColor("#28830D"));
            ValidacaoDois = true;
        }
        else
        {
            ImgValidacaoDois.setImageResource(R.drawable.imgvalidacaonegativo);
            TextValidacaoDois.setTextColor(Color.parseColor("#000000"));
        }
        //No mínimo 8 caracteres
        if (senha.length() >= 8)
        {
            ImgValidacaoTres.setImageResource(R.drawable.imgvalidacaopositivo);
            TextValidacaoTres.setTextColor(Color.parseColor("#28830D"));
            ValidacaoTres = true;
        }
        else
        {
            ImgValidacaoTres.setImageResource(R.drawable.imgvalidacaonegativo);
            TextValidacaoTres.setTextColor(Color.parseColor("#000000"));
        }

        return ValidacaoUm && ValidacaoDois && ValidacaoTres;
    }
    public void IrParaCadastro(View view)
    {
        Intent intent = new Intent(this, ActivityCadastro.class);
        startActivity(intent);
    }
    public void IrParaLogin(View view)
    {
        Intent intent = new Intent(this, ActivityLogin.class);
        startActivity(intent);
    }
    public void IrParaDashboard(View view, int id)
    {
        Intent intent = new Intent(this, ActivityDashboard.class);
        intent.putExtra("usuario_id", id);
        startActivity(intent);
        RegistraLog("Teste usuario_id","ID: {" + id + "}");
    }
    public void IrParaMetas(View view, int id)
    {
        Intent intent = new Intent(this, ActivityMetas.class);
        intent.putExtra("usuario_id", id);
        startActivity(intent);
        RegistraLog("Teste usuario_id","ID: {" + id + "}");
    }
    public void IrParaCriarMeta(View view, int id)
    {
        Intent intent = new Intent(this, ActivityCriaMeta.class);
        intent.putExtra("usuario_id", id);
        startActivity(intent);
        RegistraLog("Teste usuario_id","ID: {" + id + "}");
    }
    public void IrParaFinancas(View view, int id)
    {
        Intent intent = new Intent(this, ActivityFinancas.class);
        intent.putExtra("usuario_id", id);
        startActivity(intent);
    }
    public void IrParaRedefinirSenha(View view, String email, int id)
    {
        Intent intent = new Intent(this, ActivityRedefinirSenha.class);
        intent.putExtra("usuario_id",id); //Armazena ID do Usuário
        intent.putExtra("usuario_email",email); //Armazena E-mail do Usuário
        startActivity(intent);
    }
    public void RegistraLog(String razao, String mensagem)
    {
        Log.d("APP_LOG", "\n==========Dados==========\nRazão: {" + razao + "}\n" + mensagem);
    }
}
