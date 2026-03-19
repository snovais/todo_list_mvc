package br.com.todo.view;

import br.com.todo.controller.TaskController;
import br.com.todo.model.Urgencia;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class FormPanel extends JPanel {

    private final JTextField        campoTexto;
    private final JTextField        campoMateria;
    private final JTextField        campoPrazo;
    private final JButton           btnUrgencia;
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private       Urgencia          urgenciaSelecionada = Urgencia.AUTO;

    public FormPanel(TaskController controller) {
        setLayout(new GridBagLayout());
        setBackground(new Color(25, 25, 40));
        setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));

        campoTexto   = criarCampo();
        campoMateria = criarCampo();
        campoPrazo   = criarCampo();

        // ── botão urgência com popup ─────────────────────
        btnUrgencia = new JButton("⚙ Auto ▾");
        btnUrgencia.setFont(new Font("Arial", Font.PLAIN, 16));
        btnUrgencia.setBackground(new Color(50, 50, 70));
        btnUrgencia.setForeground(Color.WHITE);
        btnUrgencia.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 120)));
        btnUrgencia.setFocusPainted(false);
        btnUrgencia.setOpaque(true);
        btnUrgencia.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPopupMenu popup = new JPopupMenu();
        popup.setBackground(new Color(50, 50, 70));
        popup.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 120)));

        String[][] opcoes = {
            {"🟢 Tranquila", "VERDE"},
            {"🟡 Atenção",   "AMARELO"},
            {"🔴 Urgente",   "VERMELHO"},
            {"⚙ Auto",      "AUTO"}
        };

        for (String[] opcao : opcoes) {
            JMenuItem item = new JMenuItem(opcao[0]);
            item.setFont(new Font("Arial", Font.PLAIN, 16));
            item.setBackground(new Color(50, 50, 70));
            item.setForeground(Color.WHITE);
            item.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
            item.setOpaque(true);
            item.addActionListener(e -> {
                urgenciaSelecionada = Urgencia.valueOf(opcao[1]);
                btnUrgencia.setText(opcao[0] + " ▾");
            });
            popup.add(item);
        }

        btnUrgencia.addActionListener(e ->
            popup.show(btnUrgencia, 0, btnUrgencia.getHeight()));

        // ── botão adicionar ──────────────────────────────
        JButton botao = new JButton("+ Adicionar");
        botao.setFont(new Font("Arial", Font.BOLD, 16));
        botao.setBackground(new Color(120, 80, 255));
        botao.setForeground(Color.WHITE);
        botao.setBorder(BorderFactory.createEmptyBorder(10, 24, 10, 24));
        botao.setFocusPainted(false);
        botao.setOpaque(true);
        botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botao.addActionListener(e -> adicionar(controller));
        campoTexto.addActionListener(e -> adicionar(controller));

        // ── layout ───────────────────────────────────────
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 8, 6, 8);
        g.anchor = GridBagConstraints.WEST;

        // linha 1: Tarefa
        g.gridy = 0;
        g.gridx = 0; g.weightx = 0; g.fill = GridBagConstraints.NONE;
        add(makeLabel("Tarefa:"), g);
        g.gridx = 1; g.weightx = 1; g.fill = GridBagConstraints.HORIZONTAL; g.gridwidth = 4;
        add(campoTexto, g);

        // linha 2: Matéria
        g.gridy = 1; g.gridwidth = 1;
        g.gridx = 0; g.weightx = 0; g.fill = GridBagConstraints.NONE;
        add(makeLabel("Assunto:"), g);
        g.gridx = 1; g.weightx = 1; g.fill = GridBagConstraints.HORIZONTAL; g.gridwidth = 4;
        add(campoMateria, g);

        // linha 3: Prazo | Urgência | Botão
        g.gridy = 2; g.gridwidth = 1;
        g.gridx = 0; g.weightx = 0; g.fill = GridBagConstraints.NONE;
        add(makeLabel("Prazo:"), g);
        g.gridx = 1; g.weightx = 0.3; g.fill = GridBagConstraints.HORIZONTAL;
        add(campoPrazo, g);
        g.gridx = 2; g.weightx = 0; g.fill = GridBagConstraints.NONE;
        add(makeLabel("Urgência:"), g);
        g.gridx = 3; g.weightx = 0.3; g.fill = GridBagConstraints.HORIZONTAL;
        add(btnUrgencia, g);
        g.gridx = 4; g.weightx = 0; g.fill = GridBagConstraints.NONE;
        add(botao, g);
    }

    private void adicionar(TaskController controller) {
        String titulo   = campoTexto.getText().trim();
        String materia  = campoMateria.getText().trim();
        String prazoStr = campoPrazo.getText().trim();

        if (titulo.isEmpty()) {
            mostrarErro("Digite o título da tarefa!");
            return;
        }

        LocalDate prazo;
        try {
            prazo = LocalDate.parse(prazoStr, fmt);
        } catch (DateTimeParseException e) {
            mostrarErro("Data inválida! Use o formato dd/MM/yyyy");
            return;
        }

        controller.adicionar(titulo, materia, prazo, urgenciaSelecionada);
        campoTexto.setText("");
        campoMateria.setText("");
        campoPrazo.setText("");
        urgenciaSelecionada = Urgencia.AUTO;
        btnUrgencia.setText("⚙ Auto ▾");
        campoTexto.requestFocus();
    }

    private JLabel makeLabel(String texto) {
        JLabel l = new JLabel(texto);
        l.setForeground(new Color(180, 180, 200));
        l.setFont(new Font("Arial", Font.PLAIN, 16));
        return l;
    }

    private void mostrarErro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Atenção", JOptionPane.WARNING_MESSAGE);
    }

    private JTextField criarCampo() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("Arial", Font.PLAIN, 16));
        campo.setBackground(new Color(50, 50, 70));
        campo.setForeground(Color.WHITE);
        campo.setCaretColor(Color.WHITE);
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 120)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        return campo;
    }
}
