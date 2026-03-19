package br.com.todo.view;

import br.com.todo.controller.TaskController;

import javax.swing.*;
import java.awt.*;

public class AppFrame extends JFrame {

    public AppFrame() {
        setTitle("Minhas Tarefas");
        java.net.URL iconUrl = getClass().getClassLoader().getResource("icon.png");
        System.out.println("Ícone encontrado: " + iconUrl);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // // ícone da janela
        // try {
        //     java.net.URL iconUrl = getClass().getClassLoader().getResource("icon.png");
        //     if (iconUrl != null) {
        //         setIconImage(new ImageIcon(iconUrl).getImage());
        //     }
        // } catch (Exception e) {
        //     System.err.println("Ícone não encontrado: " + e.getMessage());
        // }

        Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int)(tela.width * 0.75), (int)(tela.height * 0.80));
        setLocationRelativeTo(null);

        TaskController controller = new TaskController();

        HeaderPanel headerPanel = new HeaderPanel(controller);
        ListPanel   listPanel   = new ListPanel(controller, headerPanel);
        FooterPanel footerPanel = new FooterPanel(controller);
        FormPanel   formPanel   = new FormPanel(controller);

        JPanel topo = new JPanel(new BorderLayout());
        topo.setBackground(new Color(30, 30, 46));
        topo.add(headerPanel, BorderLayout.NORTH);
        topo.add(formPanel,   BorderLayout.SOUTH);

        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(30, 30, 46));
        painel.add(topo,        BorderLayout.NORTH);
        painel.add(listPanel,   BorderLayout.CENTER);
        painel.add(footerPanel, BorderLayout.SOUTH);

        add(painel);

        listPanel.atualizar();

        setVisible(true);
    }
}
