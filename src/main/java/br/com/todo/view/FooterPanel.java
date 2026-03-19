package br.com.todo.view;

import br.com.todo.controller.TaskController;

import javax.swing.*;
import java.awt.*;

public class FooterPanel extends JPanel {

    public FooterPanel(TaskController controller) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 16, 10));
        setBackground(new Color(25, 25, 40));
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(60, 60, 90)));

        JButton btnLimpar = new JButton("🗑 Limpar concluídas");
        btnLimpar.setFont(new Font("Arial", Font.BOLD, 13));
        btnLimpar.setBackground(new Color(80, 40, 80));
        btnLimpar.setForeground(Color.WHITE);
        btnLimpar.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btnLimpar.setFocusPainted(false);
        btnLimpar.setOpaque(true);
        btnLimpar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLimpar.addActionListener(e -> controller.limparConcluidas());

        JLabel dica = new JLabel("💡 Clique no checkbox para marcar como concluída");
        dica.setFont(new Font("Arial", Font.PLAIN, 12));
        dica.setForeground(new Color(100, 100, 130));

        add(dica);
        add(btnLimpar);
    }
}
