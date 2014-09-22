package com.github.strattonbrazil.checklist.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ChecklistFrame extends JFrame implements ActionListener
{
    private static ChecklistFrame ui = null;

    public static void showUi() {
        if (ui == null) {
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        ui = new ChecklistFrame();
                        ui.setVisible(true);
                    }
                });
        }
    }

    public ChecklistFrame() {
        setupMenu();

        setTitle("Checklist");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(400, 800));

        JLabel label = new JLabel("Hello World");
        getContentPane().add(label);

        pack();
    }

    private void setupMenu() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // file menu
        //
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenuItem quitItem = _addMenuItem("Quit");
        quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
        fileMenu.add(quitItem);
    }

    public void actionPerformed(ActionEvent event) {
        System.out.println(event.getActionCommand());
        switch (event.getActionCommand()) {
        case "Quit":
            System.exit(0);
            break;
        }
    }

    private JMenuItem _addMenuItem(String s) {
        JMenuItem i = new JMenuItem(s);
        i.addActionListener(this);
        return i;
    }
}
