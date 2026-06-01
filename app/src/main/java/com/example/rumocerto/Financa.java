package com.example.rumocerto;

public class Financa {
    private int id;
    private String tipo;
    private long valor;
    private String descricao;
    private long data;

    public Financa(int id, String tipo, long valor, String descricao, long data) {
        this.id = id;
        this.tipo = tipo;
        this.valor = valor;
        this.descricao = descricao;
        this.data = data;
    }

    public int getId() { return id; }
    public String getTipo() { return tipo; }
    public long getValor() { return valor; }
    public String getDescricao() {return descricao; }
    public long getData() { return data; }
}
