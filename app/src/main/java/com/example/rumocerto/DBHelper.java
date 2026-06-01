package com.example.rumocerto;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String USUARIOS_TABELA = "USUARIOS_TABELA";
    public static final String ID = "ID";
    public static final String USUARIO_EMAIL = "USUARIO_EMAIL";
    public static final String USUARIO_SENHA = "USUARIO_SENHA";
    public static final String USUARIO_CREATEDAT = "USUARIO_CREATEDAT";
    public static final String USUARIO_ATIVO = "USUARIO_ATIVO";
    public static final String USUARIO_ULTIMOLOGIN = "USUARIO_ULTIMOLOGIN";
    public static final String USUARIO_TENTATIVASLOGIN = "USUARIO_TENTATIVASLOGIN";
    public static final String USUARIO_BLOQUEADOATE = "USUARIO_BLOQUEADOATE";
    public static final String METAS_TABELA = "METAS_TABELA";
    public static final String METAS_NOME = "METAS_NOME";
    public static final String METAS_DESCRICAO = "METAS_DESCRICAO";
    public static final String METAS_DATA_INICIO = "METAS_DATA_INICIO";
    public static final String METAS_DATA_FINAL = "METAS_DATA_FINAL";
    public static final String METAS_TIPO = "METAS_TIPO";
    public static final String METAS_USUARIO_ID = "METAS_USUARIO_ID";
    public static final String FINANCAS_TABELA = "FINANCAS_TABELA";
    public static final String FINANCAS_USUARIO_ID = "FINANCAS_USUARIO_ID";
    public static final String FINANCAS_TIPO = "FINANCAS_TIPO";
    public static final String FINANCAS_VALOR = "FINANCAS_VALOR";
    public static final String FINANCAS_DATA = "FINANCAS_DATA";
    public static final String FINANCAS_DESCRICAO = "FINANCAS_DESCRICAO";


    public DBHelper(@Nullable Context context) { super(context, "usuarios.db", null, 4);}
    @Override
    public void onCreate(SQLiteDatabase db) {
        String criaTabelaUsuarios = "CREATE TABLE " + USUARIOS_TABELA + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USUARIO_EMAIL + " TEXT UNIQUE NOT NULL, " + USUARIO_SENHA + " TEXT NOT NULL, " + USUARIO_CREATEDAT + " INTEGER NOT NULL, " + USUARIO_ATIVO + " INTEGER DEFAULT 1, " + USUARIO_ULTIMOLOGIN + " INTEGER NOT NULL, " + USUARIO_TENTATIVASLOGIN + " INTEGER DEFAULT 0, " + USUARIO_BLOQUEADOATE + " INTEGER" + ")";
        db.execSQL(criaTabelaUsuarios);
        String criaTabelaMetas = "CREATE TABLE " + METAS_TABELA + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + METAS_USUARIO_ID + " INTEGER NOT NULL, " + METAS_NOME + " TEXT NOT NULL, " + METAS_DESCRICAO + " TEXT, " + METAS_DATA_INICIO + " INTEGER, " + METAS_DATA_FINAL + " INTEGER, " + METAS_TIPO + " TEXT, FOREIGN KEY (" + METAS_USUARIO_ID + ") REFERENCES " + USUARIOS_TABELA + "(" + ID + ")" + " ON DELETE CASCADE " + ")";
        db.execSQL(criaTabelaMetas);
        String criaTabelaFinancas = "CREATE TABLE " + FINANCAS_TABELA + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FINANCAS_USUARIO_ID + " INTEGER NOT NULL, " + FINANCAS_TIPO + " TEXT NOT NULL, " + FINANCAS_VALOR + " INTEGER NOT NULL, " + FINANCAS_DESCRICAO + " TEXT NOT NULL, " + FINANCAS_DATA + " INTEGER NOT NULL, FOREIGN KEY (" + FINANCAS_USUARIO_ID + ") REFERENCES " + USUARIOS_TABELA + "(" + ID + ")" + " ON DELETE CASCADE " + ")";
        db.execSQL(criaTabelaFinancas);
    }
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }
    public boolean CreateUser(String email, String senha)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String senhaHash = BCrypt.hashpw(senha, BCrypt.gensalt());
        ContentValues insertValues = new ContentValues();
        insertValues.put(USUARIO_EMAIL, email.replace("'","''"));
        insertValues.put(USUARIO_SENHA, senhaHash.replace("'","''"));
        insertValues.put(USUARIO_CREATEDAT, System.currentTimeMillis());
        insertValues.put(USUARIO_ATIVO, 1);
        insertValues.put(USUARIO_ULTIMOLOGIN, System.currentTimeMillis());
        insertValues.put(USUARIO_BLOQUEADOATE, 0);
        long insert = db.insert(USUARIOS_TABELA,null, insertValues);
        return insert != -1;
    }
    public boolean CreateMeta(int usuario_id, String nome, String descricao, long data_inicio, long data_final, String tipo)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues insertValues = new ContentValues();
        insertValues.put(METAS_USUARIO_ID, usuario_id);
        insertValues.put(METAS_NOME, nome.replace("'","''"));
        insertValues.put(METAS_DESCRICAO, descricao.replace("'","''"));
        insertValues.put(METAS_DATA_INICIO, data_inicio);
        insertValues.put(METAS_DATA_FINAL, data_final);
        insertValues.put(METAS_TIPO, tipo.replace("'","''"));
        long insert = db.insert(METAS_TABELA,null, insertValues);
        return insert != -1;
    }
    public boolean CreateFinanca(int usuario_id, String tipo, long valor, String descricao)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues insertValues = new ContentValues();
        insertValues.put(FINANCAS_USUARIO_ID, usuario_id);
        insertValues.put(FINANCAS_TIPO, tipo.replace("'","''"));
        insertValues.put(FINANCAS_VALOR, valor);
        insertValues.put(FINANCAS_DESCRICAO, descricao.replace("'","''"));
        insertValues.put(FINANCAS_DATA, System.currentTimeMillis());
        long insert = db.insert(FINANCAS_TABELA,null, insertValues);
        return insert != -1;
    }
    public int ReadUser(String email, String senha)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d("APP_LOG", "Teste 1");
        Cursor cursor = db.rawQuery("SELECT ID, " + USUARIO_SENHA + ", " + USUARIO_TENTATIVASLOGIN + ", " + USUARIO_BLOQUEADOATE + ", " + USUARIO_ATIVO + " FROM " + USUARIOS_TABELA + " WHERE " + USUARIO_EMAIL + " = ?", new String[]{email});
        Log.d("APP_LOG", "Teste 2");
        if (cursor != null)
        {
            if (cursor.moveToFirst())
            {
                int indexid = cursor.getColumnIndex("ID");
                int id = cursor.getInt(indexid);
                int indexativo = cursor.getColumnIndex(USUARIO_ATIVO);
                int ativo = cursor.getInt(indexativo);
                if (ativo == 0)
                {
                    return -2;
                }

                long agora = System.currentTimeMillis();
                int indexbloqueadoate = cursor.getColumnIndex(USUARIO_BLOQUEADOATE);
                long bloqueadoate = cursor.getLong(indexbloqueadoate);
                Log.d("APP_LOG", "agora: " + agora);
                Log.d("APP_LOG", "bloqueadoate: " + bloqueadoate);
                if (bloqueadoate != 0 && agora < bloqueadoate)
                {
                    return -3;
                }
                else
                {
                    SQLiteDatabase dbWrite = this.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(USUARIO_BLOQUEADOATE, 0);
                    dbWrite.update(USUARIOS_TABELA, values, "ID = ?", new String[]{String.valueOf(id)});
                }


                int indexsenha = cursor.getColumnIndex(USUARIO_SENHA);


                String senhaHash = cursor.getString(indexsenha);

                Log.d("APP_LOG", "Teste 3");
                if (BCrypt.checkpw(senha, senhaHash))
                {
                    cursor.close();
                    SQLiteDatabase dbWrite = this.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(USUARIO_ULTIMOLOGIN, System.currentTimeMillis());
                    values.put(USUARIO_TENTATIVASLOGIN, 0);
                    values.put(USUARIO_BLOQUEADOATE, 0);

                    dbWrite.update(USUARIOS_TABELA, values, "ID = ?", new String[]{String.valueOf(id)});

                    return id;
                }
                else
                {
                    int indextentativaslogin = cursor.getColumnIndex(USUARIO_TENTATIVASLOGIN);
                    int tentativaslogin = cursor.getInt(indextentativaslogin);

                    tentativaslogin++;
                    Log.d("APP_LOG", "Teste 4");

                    ContentValues values = new ContentValues();
                    values.put(USUARIO_TENTATIVASLOGIN, tentativaslogin);

                    if (tentativaslogin >= 5)
                    {
                        long bloqueio = System.currentTimeMillis() + (5 * 60 * 1000);
                        values.put(USUARIO_TENTATIVASLOGIN, 0);
                        values.put(USUARIO_BLOQUEADOATE, bloqueio);
                    }
                    Log.d("APP_LOG", "Teste 5");
                    db.update(USUARIOS_TABELA, values, "ID = ?", new String[]{String.valueOf(id)});
                }
            }
            cursor.close();
        }
        Log.d("APP_LOG", "Teste DB");
        return -1;
    }
    public List<Meta> ReadMetas(int usuario_id)
    {
        List<Meta> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + METAS_TABELA +
                " WHERE " + METAS_USUARIO_ID + " = ?", new String[]{String.valueOf(usuario_id)});

        if (cursor.moveToFirst()) {
            do {
                Meta meta = new Meta(
                        cursor.getInt(0), // ID
                        cursor.getString(2), // Nome
                        cursor.getString(3), // Descrição
                        cursor.getLong(4), // Data Início
                        cursor.getLong(5),  // Data Final
                        cursor.getString(6)); //Tipo;
                lista.add(meta);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }
    public Meta ReadMeta(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + METAS_TABELA + " WHERE id = ?", new String[]{String.valueOf(id)});

        if (cursor != null && cursor.moveToFirst()) {
            Meta meta = new Meta(
                    cursor.getInt(0), // ID
                    cursor.getString(2), // Nome
                    cursor.getString(3), // Descrição
                    cursor.getLong(4), // Data Início
                    cursor.getLong(5),  // Data Final
                    cursor.getString(6)); //Tipo;
            cursor.close();
            return meta;
        }
        return null;
    }
    public List<Financa> ReadFinancas(int usuario_id) {
        List<Financa> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + FINANCAS_TABELA +
                        " WHERE " + FINANCAS_USUARIO_ID + " = ? ORDER BY " + FINANCAS_DATA + " DESC",
                new String[]{String.valueOf(usuario_id)});

        if (cursor.moveToFirst()) {
            do {
                Financa financa = new Financa(
                        cursor.getInt(0), // ID
                        cursor.getString(2), // Tipo
                        cursor.getLong(3), // Valor
                        cursor.getString(4), // Descrição
                        cursor.getLong(5) // Data
                );
                lista.add(financa);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }
    public long getSaldoTotal(int usuario_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        long totalEntradas = 0;
        long totalSaidas = 0;

        Cursor cursorE = db.rawQuery("SELECT SUM(" + FINANCAS_VALOR + ") FROM " + FINANCAS_TABELA +
                " WHERE " + FINANCAS_USUARIO_ID + " = ? AND " + FINANCAS_TIPO + " = 'Entrada'",
                new String[]{String.valueOf(usuario_id)});
        if (cursorE.moveToFirst()) {
            totalEntradas = cursorE.getLong(0);
        }
        cursorE.close();

        Cursor cursorS = db.rawQuery("SELECT SUM(" + FINANCAS_VALOR + ") FROM " + FINANCAS_TABELA +
                " WHERE " + FINANCAS_USUARIO_ID + " = ? AND " + FINANCAS_TIPO + " = 'Saída'",
                new String[]{String.valueOf(usuario_id)});
        if (cursorS.moveToFirst()) {
            totalSaidas = cursorS.getLong(0);
        }
        cursorS.close();

        return totalEntradas - totalSaidas;
    }
    public long getTotalEntradasMesAtual(int usuario_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        long total = 0;
        Cursor cursor = db.rawQuery("SELECT SUM(" + FINANCAS_VALOR + ") FROM " + FINANCAS_TABELA +
                " WHERE " + FINANCAS_USUARIO_ID + " = ? AND " + FINANCAS_TIPO + " = 'Entrada' " +
                " AND strftime('%m-%Y', " + FINANCAS_DATA + " / 1000, 'unixepoch') = strftime('%m-%Y', 'now')",
                new String[]{String.valueOf(usuario_id)});

        if (cursor.moveToFirst()) total = cursor.getLong(0);
        cursor.close();
        return total;
    }
    public long getTotalSaidasMesAtual(int usuario_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        long total = 0;
        Cursor cursor = db.rawQuery("SELECT SUM(" + FINANCAS_VALOR + ") FROM " + FINANCAS_TABELA +
                        " WHERE " + FINANCAS_USUARIO_ID + " = ? AND " + FINANCAS_TIPO + " = 'Saída' " +
                        "AND strftime('%m-%Y', " + FINANCAS_DATA + " / 1000, 'unixepoch') = strftime('%m-%Y', 'now')",
                        new String[]{String.valueOf(usuario_id)});

        if (cursor.moveToFirst()) total = cursor.getLong(0);
        cursor.close();
        return total;
    }
    public boolean UpdateUser(int id, String email, String senha)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String senhaHash = BCrypt.hashpw(senha, BCrypt.gensalt());
        ContentValues updateValues = new ContentValues();
        updateValues.put(USUARIO_EMAIL, email.replace("'","''"));
        updateValues.put(USUARIO_SENHA, senhaHash.replace("'","''"));
        long update = db.update(USUARIOS_TABELA, updateValues, "ID=" + id, null);
        return update >= 0;
    }
    public boolean UpdateMeta(int id, String nome, String descricao, long dataInicio, long dataFim, String tipo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(METAS_NOME, nome);
        values.put(METAS_DESCRICAO, descricao);
        values.put(METAS_DATA_INICIO, dataInicio);
        values.put(METAS_DATA_FINAL, dataFim);
        values.put(METAS_TIPO, tipo);

        return db.update(METAS_TABELA, values, "ID = ?", new String[]{String.valueOf(id)}) > 0;
    }
    public boolean UpdateFinanca(int id, String tipo, long valor, String descricao) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FINANCAS_TIPO, tipo);
        values.put(FINANCAS_VALOR, valor);
        values.put(FINANCAS_DESCRICAO, descricao);

        return db.update(FINANCAS_TABELA, values, "ID = ?", new String[]{String.valueOf(id)}) > 0;
    }
    public boolean DeleteUser(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(USUARIOS_TABELA, ID + "=" + id, null) > 0;
    }
    public boolean DeleteMeta(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(METAS_TABELA, ID + "=" + id, null) > 0;
    }
    public void DeleteAllMetas(int usuarioId) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Apaga apenas as metas do usuário logado
        db.delete(METAS_TABELA, METAS_USUARIO_ID + " = ?", new String[]{String.valueOf(usuarioId)});
    }
    public boolean DeleteFinanca(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(FINANCAS_TABELA, ID + "=" + id, null) > 0;
    }
    public int ProcuraEmail(String email)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USUARIOS_TABELA + " WHERE " + USUARIO_EMAIL + " = ?", new String[]{email});
        if (cursor != null)
        {
            if (cursor.moveToFirst())
            {
                int indexid = cursor.getColumnIndex("ID");
                int id = cursor.getInt(indexid);
                cursor.close();
                return id;
            }
            cursor.close();
        }
        return -1;
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + USUARIOS_TABELA);
        db.execSQL("DROP TABLE IF EXISTS " + METAS_TABELA);
        db.execSQL("DROP TABLE IF EXISTS " + FINANCAS_TABELA);

        onCreate(db);
    }
}
