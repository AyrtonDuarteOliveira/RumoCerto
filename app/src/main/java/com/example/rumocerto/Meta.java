package com.example.rumocerto;

public class Meta {
    private int id;
    private String nome;
    private String descricao;
    private long dataInicio;
    private long dataFim;
    private String tipo;

    public Meta(int id, String nome, String descricao, long dataInicio, long dataFim, String tipo) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.tipo = tipo;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public long getDataInicio() { return dataInicio; }
    public long getDataFim() { return dataFim; }
    public String getTipo() { return tipo; }
}
