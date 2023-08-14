package main;

import listeners.WindowComponent;
import objects.ComplexObject;
import util.Arduino;
import util.ObjectReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import listeners.Window;

public class Ventana extends JFrame {

    private static Ventana v;

    public static void main(String[] args) {
        v = new Ventana();
    }

    public Ventana() {
        // Instances
        Pantalla pantalla;
        JMenuBar menuBar;
        JMenu menuArchives, menuArduino;
        JMenuItem menuItemOpenFile, menuItemArduinoConnect, menuItemArduinoPorts;

        // Configuration of the window
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ignored) {}
        //setSize(new Dimension(WIDTH, HEIGHT));
        setTitle("Derblen 0.0.1a - By Goedix");
        setResizable(true);
        setUndecorated(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()), (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()-50));
        setLocationRelativeTo(null);
        setPreferredSize(new Dimension((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()), (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()-50)));
        setExtendedState(MAXIMIZED_BOTH);
        addWindowListener(new Window());

        // Configuration of the menubar and the menuitems
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        menuArchives = new JMenu("Archivo");
        menuBar.add(menuArchives);

        menuItemOpenFile = new JMenuItem("Importar modelo (.obj)");
        menuItemOpenFile.addActionListener(e -> ObjectReader.importFile());
        menuArchives.add(menuItemOpenFile);

        menuArduino = new JMenu("Arduino");
        menuBar.add(menuArduino);

        menuItemArduinoConnect = new JMenuItem("Conectar arduino");
        menuArduino.add(menuItemArduinoConnect);
        // TEMP
        menuItemArduinoConnect.addActionListener(e -> { if (!Arduino.isConnected()) Arduino.connectArduino(); });

        // TODO De este arduino tiene que salir otra ventana con los puertos.

        // Add and start the screen requesting the focus automatically
        pantalla = new Pantalla(getWidth(), getHeight());

        setVisible(true);

        requestFocus();
        add(pantalla, BorderLayout.CENTER);
        pantalla.start();
    }



    public static Ventana getInstance() {
        return v;
    }
}
