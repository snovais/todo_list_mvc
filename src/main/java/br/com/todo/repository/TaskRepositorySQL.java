package br.com.todo.repository;

import br.com.todo.database.DatabaseManager;
import br.com.todo.model.Task;
import br.com.todo.model.Urgencia;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskRepositorySQL implements TaskRepository {

    private final DatabaseManager db = new DatabaseManager();

    @Override
    public void salvar(List<Task> tasks) {
        String deletar = "DELETE FROM tarefas";
        String inserir = "INSERT INTO tarefas (titulo, materia, prazo, concluida, urgencia) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = db.conectar();
             Statement stmtDel = conn.createStatement();
             PreparedStatement stmtIns = conn.prepareStatement(inserir)) {

            stmtDel.execute(deletar);

            for (Task task : tasks) {
                stmtIns.setString(1, task.getTitulo());
                stmtIns.setString(2, task.getMateria());
                stmtIns.setString(3, task.getPrazo().toString());
                stmtIns.setInt(4, task.isConcluida() ? 1 : 0);
                stmtIns.setString(5, task.getUrgencia().name());
                stmtIns.executeUpdate();
            }

        } catch (SQLException e) {
            System.err.println("Erro ao salvar: " + e.getMessage());
        }
    }

    @Override
    public List<Task> carregar() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT titulo, materia, prazo, concluida, urgencia FROM tarefas";

        try (Connection conn = db.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String    titulo    = rs.getString("titulo");
                String    materia   = rs.getString("materia");
                LocalDate prazo     = LocalDate.parse(rs.getString("prazo"));
                boolean   concluida = rs.getInt("concluida") == 1;
                Urgencia  urgencia  = Urgencia.valueOf(
                    rs.getString("urgencia") != null
                        ? rs.getString("urgencia")
                        : "AUTO"
                );

                Task task = new Task(titulo, materia != null ? materia : "", prazo);
                task.setConcluida(concluida);
                task.setUrgencia(urgencia);
                tasks.add(task);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao carregar: " + e.getMessage());
        }

        return tasks;
    }
}
