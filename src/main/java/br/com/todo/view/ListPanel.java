package br.com.todo.view;

import br.com.todo.controller.TaskController;
import br.com.todo.model.Task;
import br.com.todo.model.Urgencia;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ListPanel extends JPanel {

    private final TaskController    controller;
    private final HeaderPanel       headerPanel;
    private final JPanel            conteudo;
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ListPanel(TaskController controller, HeaderPanel headerPanel) {
        this.controller  = controller;
        this.headerPanel = headerPanel;

        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 46));

        conteudo = new JPanel();
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));
        conteudo.setBackground(new Color(30, 30, 46));

        JScrollPane scroll = new JScrollPane(conteudo);
        scroll.getViewport().setBackground(new Color(30, 30, 46));
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        estilizarScrollBar(scroll);
        add(scroll, BorderLayout.CENTER);

        // escuta o campo de busca em tempo real
        headerPanel.getCampoBusca().getDocument().addDocumentListener(
            new javax.swing.event.DocumentListener() {
                public void insertUpdate(javax.swing.event.DocumentEvent e)  { atualizar(); }
                public void removeUpdate(javax.swing.event.DocumentEvent e)  { atualizar(); }
                public void changedUpdate(javax.swing.event.DocumentEvent e) { atualizar(); }
            }
        );

        // registra callback no controller — atualiza quando dados mudam
        controller.setOnUpdate(this::atualizar);
    }

    public void atualizar() {
        conteudo.removeAll();

        String filtro = headerPanel.getCampoBusca().getText().trim();
        boolean buscando = !filtro.isBlank() && !filtro.equals("🔍 Buscar tarefa...");

        var tarefas = buscando
            ? controller.buscar(filtro)
            : controller.getTasks();

        if (tarefas.isEmpty()) {
            JLabel vazio = new JLabel(buscando
                ? "Nenhuma tarefa encontrada."
                : "Nenhuma tarefa ainda. Adicione uma acima! 🎉");
            vazio.setForeground(new Color(100, 100, 130));
            vazio.setFont(new Font("Arial", Font.PLAIN, 15));
            vazio.setAlignmentX(CENTER_ALIGNMENT);
            vazio.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
            conteudo.add(vazio);
        } else {
            for (Task task : tarefas) {
                conteudo.add(criarCard(task));
            }
        }

        headerPanel.atualizar(controller.totalConcluidas(), controller.total());

        conteudo.revalidate();
        conteudo.repaint();
    }

    private JPanel criarCard(Task task) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(50, 50, 70));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 5, 0, 0, corSemaforo(task)),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        card.setMinimumSize(new Dimension(0, 70));
        card.setPreferredSize(new Dimension(0, 70));

        JCheckBox check = new JCheckBox();
        check.setSelected(task.isConcluida());
        check.setBackground(new Color(50, 50, 70));
        check.setPreferredSize(new Dimension(24, 24));
        check.addActionListener(e ->
            controller.concluir(task, check.isSelected()));

        String textoTitulo = task.isConcluida()
            ? "<html><strike>" + task.getTitulo() + "</strike></html>"
            : task.getTitulo();

        JLabel labelTitulo = new JLabel(textoTitulo);
        labelTitulo.setForeground(task.isConcluida() ? new Color(100, 100, 130) : Color.WHITE);
        labelTitulo.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel labelMateria = new JLabel(
            task.getMateria().isEmpty() ? "" : " · " + task.getMateria());
        labelMateria.setForeground(new Color(120, 120, 180));
        labelMateria.setFont(new Font("Arial", Font.ITALIC, 13));

        JPanel painelTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        painelTitulo.setBackground(new Color(50, 50, 70));
        painelTitulo.add(labelTitulo);
        painelTitulo.add(labelMateria);

        JLabel semaforo = new JLabel("●");
        semaforo.setFont(new Font("Arial", Font.BOLD, 22));
        semaforo.setForeground(corSemaforo(task));

        JLabel labelPrazo = new JLabel(task.getPrazo().format(fmt) + "  " + statusTexto(task));
        labelPrazo.setForeground(corDoCard(task));
        labelPrazo.setFont(new Font("Arial", Font.BOLD, 13));

        JButton btnRemover = new JButton("✕");
        btnRemover.setFont(new Font("Arial", Font.BOLD, 13));
        btnRemover.setForeground(new Color(255, 70, 90));
        btnRemover.setBackground(new Color(50, 50, 70));
        btnRemover.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        btnRemover.setFocusPainted(false);
        btnRemover.setContentAreaFilled(false);
        btnRemover.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRemover.addActionListener(e -> controller.remover(task));

        JPanel esquerda = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        esquerda.setBackground(new Color(50, 50, 70));
        esquerda.setOpaque(true);
        esquerda.add(semaforo);
        esquerda.add(check);
        esquerda.add(painelTitulo);

        JPanel direita = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        direita.setBackground(new Color(50, 50, 70));
        direita.add(labelPrazo);
        direita.add(btnRemover);

        card.add(esquerda, BorderLayout.WEST);
        card.add(direita,  BorderLayout.EAST);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1
                        && e.getSource() != btnRemover) {
                    check.doClick();
                }
            }
        });

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(new Color(30, 30, 46));
        wrapper.setBorder(BorderFactory.createEmptyBorder(4, 16, 4, 16));
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 78));
        wrapper.add(card);

        return wrapper;
    }

    private void estilizarScrollBar(JScrollPane scroll) {
        JScrollBar bar = scroll.getVerticalScrollBar();
        bar.setBackground(new Color(30, 30, 46));
        bar.setPreferredSize(new Dimension(8, 0));
        bar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                thumbColor = new Color(80, 80, 120);
                trackColor = new Color(30, 30, 46);
            }
            @Override
            protected JButton createDecreaseButton(int o) { return invisivel(); }
            @Override
            protected JButton createIncreaseButton(int o) { return invisivel(); }
            private JButton invisivel() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                return b;
            }
        });
    }

    private Color corSemaforo(Task task) {
        if (task.isConcluida()) return new Color(100, 100, 130);
        return switch (task.getUrgencia()) {
            case VERDE    -> new Color(60, 210, 130);
            case AMARELO  -> new Color(255, 190, 50);
            case VERMELHO -> new Color(255, 70, 90);
            case AUTO     -> corDoCard(task);
        };
    }

    private Color corDoCard(Task task) {
        if (task.isConcluida()) return new Color(100, 100, 130);
        long dias = ChronoUnit.DAYS.between(LocalDate.now(), task.getPrazo());
        if (dias < 0)  return new Color(255, 70, 90);
        if (dias <= 2) return new Color(255, 190, 50);
        return new Color(60, 210, 130);
    }

    private String statusTexto(Task task) {
        if (task.isConcluida()) return "✓ Concluída";
        long dias = ChronoUnit.DAYS.between(LocalDate.now(), task.getPrazo());
        return switch ((int) Math.max(dias, -1)) {
            case -1 -> "⚠ Atrasada";
            case  0 -> "🔥 Hoje!";
            case  1 -> "⏰ Amanhã";
            default -> "📅 " + dias + " dias";
        };
    }
}
