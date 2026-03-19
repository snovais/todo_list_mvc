package br.com.todo.repository;

import br.com.todo.model.Task;
import java.util.List;

public interface TaskRepository {
    void        salvar(List<Task> tasks);
    List<Task>  carregar();
}