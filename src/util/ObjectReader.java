package util;

import main.Pantalla;
import objects.ComplexObject;
import objects.Triangle;
import objects.Vertice;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ObjectReader {

    private static final String COMMAND_VERTEX = "v";
    private static final String COMMAND_FACE = "f";
    private static final String COMMAND_OBJECT = "o";
    private static final String COMMAND_OBJECT_ALTERNATIVE = "g";
    private static final String COMMAND_COMMENT = "#";

    public static void importFile() {
        Thread t = new Thread(() -> Pantalla.setObjects(openObjFile()));
        t.start();
    }

    public static void importFileInFile() {
        Thread t = new Thread(() -> Pantalla.addObjects(openObjFile()));
        t.start();
    }

    private static File chooseFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Importar archivo (.obj)");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Object files: .obj", "obj");
        chooser.setFileFilter(filter);
        String userDir = System.getProperty("user.home");
        chooser.setCurrentDirectory(new java.io.File(userDir + "/Desktop"));
        chooser.setVisible(true);

        return chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION ? chooser.getSelectedFile() : null;
    }

    private static ComplexObject[] openObjFile() {
        File file = chooseFile();
        LocalTime now = LocalTime.now();
        System.out.println("Started at: " + now.toString());

        if (file == null) return null;

        ArrayList<Vertice> vertices = new ArrayList<>();
        ArrayList<Triangle> allTriangles = new ArrayList<>(); // Triangles of all objects
        ComplexObject[] objects = new ComplexObject[0];

        try {
            Scanner sc = new Scanner(file);
            try {
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    String command = line.substring(0, line.indexOf(' '));

                    switch (command) {
                        case COMMAND_OBJECT:
                        case COMMAND_OBJECT_ALTERNATIVE: {
                            objects = Arrays.copyOf(objects, objects.length + 1);
                            objects[objects.length - 1] = new ComplexObject();
                            if (objects.length > 1 && objects[objects.length - 2] != null && objects[objects.length - 2].getTrianglesList().size() != 0) {
                                allTriangles.addAll(objects[objects.length - 2].getTrianglesList());
                            }
                            break;
                        }
                        case COMMAND_VERTEX:
                        {
                            String[] v = line.split(" ");
                            vertices.add(new Vertice((int) (Double.parseDouble(v[1]) * 1000), (int) (Double.parseDouble(v[2]) * 1000), (int) (Double.parseDouble(v[3]) * 1000)));
                            break;
                        }
                        case COMMAND_FACE:
                        {
                            String[] f = line.split(" ");
                            if (f[0].contains("/") || f[1].contains("/") || f[2].contains("/")) {
                                objects[objects.length - 1].getTrianglesList().add(new Triangle(objects[objects.length - 1], vertices.get(Integer.parseInt(f[1].substring(0, f[1].indexOf("/"))) - 1), vertices.get(Integer.parseInt(f[2].substring(0, f[2].indexOf("/"))) - 1), vertices.get(Integer.parseInt(f[3].substring(0, f[3].indexOf("/"))) - 1)));
                            } else {
                                objects[objects.length - 1].getTrianglesList().add(new Triangle(objects[objects.length - 1], vertices.get(Integer.parseInt(f[1]) - 1), vertices.get(Integer.parseInt(f[2]) - 1), vertices.get(Integer.parseInt(f[3]) - 1)));
                            }
                            break;
                        }
                    }
                }

                allTriangles.addAll(objects[objects.length - 1].getTrianglesList());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "No se ha encontrado el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        if (allTriangles.size() == 0) {
            JOptionPane.showMessageDialog(null, "El archivo no es procesable por nuestro motor.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        System.gc(); // Run the Garbage Collector
        now = LocalTime.now();
        System.out.println("Finished at: " + now.toString());
        return objects;
    }
}
