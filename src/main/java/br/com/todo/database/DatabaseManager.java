package br.com.todo.database;

import java.sql.*;

public class DatabaseManager {

    private static final String URL = "jdbc:sqlite:tarefas.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver SQLite não encontrado: " + e.getMessage());
        }
    }

    public Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public void inicializar() {
        String criarTabela = """
            CREATE TABLE IF NOT EXISTS tarefas (
                id        INTEGER PRIMARY KEY AUTOINCREMENT,
                titulo    TEXT    NOT NULL,
                materia   TEXT    NOT NULL DEFAULT '',
                prazo     TEXT    NOT NULL,
                concluida INTEGER NOT NULL DEFAULT 0,
                urgencia  TEXT    NOT NULL DEFAULT 'AUTO'
            )
            """;

        try (Connection conn = conectar();
             Statement stmt = conn.createStatement()) {
            stmt.execute(criarTabela);
            try { stmt.execute("ALTER TABLE tarefas ADD COLUMN urgencia TEXT NOT NULL DEFAULT 'AUTO'"); }
            catch (SQLException e) { /* já existe */ }
            try { stmt.execute("ALTER TABLE tarefas ADD COLUMN materia TEXT NOT NULL DEFAULT ''"); }
            catch (SQLException e) { /* já existe */ }
            System.out.println("Banco inicializado.");
        } catch (SQLException e) {
            System.err.println("Erro ao inicializar banco: " + e.getMessage());
        }
    }
}
