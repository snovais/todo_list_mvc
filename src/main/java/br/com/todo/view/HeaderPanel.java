package br.com.todo.view;

import br.com.todo.controller.TaskController;

import javax.swing.*;
import java.awt.*;

public class HeaderPanel extends JPanel {

    private final JLabel     labelContador;
    private final JTextField campoBusca;

    public HeaderPanel(TaskController controller) {
        setLayout(new BorderLayout(0, 8));
        setBackground(new Color(30, 30, 46));
        setBorder(BorderFactory.createEmptyBorder(20, 24, 12, 24));

        // linha de cima: título + contador
        JPanel linhaTopo = new JPanel(new BorderLayout());
        linhaTopo.setBackground(new Color(30, 30, 46));

        JLabel titulo = new JLabel("📋 Minhas Tarefas");
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(new Color(180, 140, 255));

        labelContador = new JLabel("0/0 concluídas");
        labelContador.setFont(new Font("Arial", Font.PLAIN, 14));
        labelContador.setForeground(new Color(130, 130, 160));

        linhaTopo.add(titulo,        BorderLayout.WEST);
        linhaTopo.add(labelContador, BorderLayout.EAST);

        // campo de busca
        campoBusca = new JTextField();
        campoBusca.setFont(new Font("Arial", Font.PLAIN, 14));
        campoBusca.setBackground(new Color(50, 50, 70));
        campoBusca.setForeground(Color.WHITE);
        campoBusca.setCaretColor(Color.WHITE);
        campoBusca.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 110)),
            BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));

        // placeholder
        campoBusca.setText("🔍 Buscar tarefa...");
        campoBusca.setForeground(new Color(100, 100, 130));
        campoBusca.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (campoBusca.getText().equals("🔍 Buscar tarefa...")) {
                    campoBusca.setText("");
                    campoBusca.setForeground(Color.WHITE);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (campoBusca.getText().isBlank()) {
                    campoBusca.setText("🔍 Buscar tarefa...");
                    campoBusca.setForeground(new Color(100, 100, 130));
                }
            }
        });

        add(linhaTopo,  BorderLayout.NORTH);
        add(campoBusca, BorderLayout.SOUTH);
    }

    public void atualizar(long concluidas, int total) {
        labelContador.setText(concluidas + "/" + total + " concluídas");
    }

    public JTextField getCampoBusca() {
        return campoBusca;
    }
}
