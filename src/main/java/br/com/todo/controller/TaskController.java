package br.com.todo.controller;

import br.com.todo.model.Task;
import br.com.todo.model.Urgencia;
import br.com.todo.repository.TaskRepository;
import br.com.todo.repository.TaskRepositorySQL;
import br.com.todo.database.DatabaseManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TaskController {

    private final List<Task>       tasks      = new ArrayList<>();
    private final TaskRepository   repository;
    private       Runnable         onUpdate;

    public TaskController() {
        this.repository = new TaskRepositorySQL();
        new DatabaseManager().inicializar();
        tasks.addAll(repository.carregar());
        ordenar();
    }

    // construtor para testes
    public TaskController(TaskRepository repository) {
        this.repository = repository;
    }

    // ── observer: a view registra um callback para ser notificada ──
    public void setOnUpdate(Runnable onUpdate) {
        this.onUpdate = onUpdate;
    }

    private void notificar() {
        if (onUpdate != null) onUpdate.run();
    }

    // ── operações ──────────────────────────────────────────────────
    public void adicionar(String titulo, String materia, LocalDate prazo, Urgencia urgencia) {
        Task task = new Task(titulo, materia, prazo);
        task.setUrgencia(urgencia);
        tasks.add(task);
        ordenar();
        salvar();
        notificar();
    }

    public void remover(Task task) {
        tasks.remove(task);
        salvar();
        notificar();
    }

    public void concluir(Task task, boolean concluida) {
        task.setConcluida(concluida);
        ordenar();
        salvar();
        notificar();
    }

    public void limparConcluidas() {
        tasks.removeIf(Task::isConcluida);
        salvar();
        notificar();
    }

    // ── consultas ──────────────────────────────────────────────────
    public List<Task> getTasks() {
        return tasks;
    }

    public List<Task> buscar(String filtro) {
        if (filtro == null || filtro.isBlank()) return tasks;
        String f = filtro.toLowerCase();
        return tasks.stream()
            .filter(t -> t.getTitulo().toLowerCase().contains(f)
                      || t.getMateria().toLowerCase().contains(f))
            .collect(Collectors.toList());
    }

    public long totalConcluidas() {
        return tasks.stream().filter(Task::isConcluida).count();
    }

    public int total() {
        return tasks.size();
    }

    // ── internos ───────────────────────────────────────────────────
    private void salvar() {
        repository.salvar(tasks);
    }

    private void ordenar() {
        tasks.sort(
            Comparator.comparing(Task::isConcluida)
                      .thenComparing(Task::getPrazo)
        );
    }
}
