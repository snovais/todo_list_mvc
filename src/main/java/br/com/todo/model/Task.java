package br.com.todo.model;

import java.time.LocalDate;

public class Task {

    private String    titulo;
    private String    materia;
    private LocalDate prazo;
    private boolean   concluida;
    private Urgencia  urgencia;

    public Task(String titulo, String materia, LocalDate prazo) {
        this.titulo    = titulo;
        this.materia   = materia;
        this.prazo     = prazo;
        this.concluida = false;
        this.urgencia  = Urgencia.AUTO;
    }

    public String    getTitulo()             { return titulo; }
    public String    getMateria()            { return materia; }
    public LocalDate getPrazo()              { return prazo; }
    public boolean   isConcluida()           { return concluida; }
    public Urgencia  getUrgencia()           { return urgencia; }
    public void      setConcluida(boolean c) { this.concluida = c; }
    public void      setUrgencia(Urgencia u) { this.urgencia = u; }
    public void      setMateria(String m)    { this.materia = m; }
}