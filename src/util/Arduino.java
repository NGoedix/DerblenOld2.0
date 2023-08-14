package util;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import main.Pantalla;

import javax.swing.*;

public class Arduino {

    private static String data = "";
    private static boolean comienza = false;

    private static boolean isConnected = false;

    private static final SerialPort[] ports = SerialPort.getCommPorts();

    public static void connectArduino() {
        Thread t = new Thread(() -> {
            if (Arduino.connect()) {
                JOptionPane.showMessageDialog(null, "Arduino conectado correctamente.", "Conexión iniciada", JOptionPane.INFORMATION_MESSAGE);
                isConnected = true;
            }
        });
        t.start();
    }

    private static boolean setPort(int indexPort) {
        SerialPort activePort = ports[indexPort];

        if (activePort.openPort())
            System.out.println(activePort.getPortDescription() + " puerto abierto.");
        else
            return false;

        activePort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            @Override
            public void serialEvent(SerialPortEvent serialPortEvent) {
                int size = serialPortEvent.getSerialPort().bytesAvailable();
                byte[] buffer = new byte[size];
                serialPortEvent.getSerialPort().readBytes(buffer, size);
                for (byte b : buffer) {
                    control((char) b);
                }
            }
        });

        return true;
    }

    private static void control(char c) {
        if ((c == 'X' || c == 'Y') && !comienza) {
            data += c;
            comienza = true;
        } else if (c == 'X' || c == 'Y') {
            double cant = 0;
            try {
                cant = Double.parseDouble(data.substring(data.indexOf(':') + 1, data.length() - 2));
            } catch (NumberFormatException ignored) {}

            if (cant != 0) {
                double rY = Pantalla.getCamera().getrY();
                double rX = Pantalla.getCamera().getrX();

                double rXX = Math.abs(Pantalla.getCamera().getrX());
                Pantalla.getCamera().setIsToRight(rXX % 360 > 90 && rXX % 360 < 270);

                if (data.charAt(0) == 'Y') {
                    Pantalla.getCamera().setrX(rX - (float) (cant * 10));
                } else {
                    Pantalla.getCamera().setrY(rY + (Pantalla.getCamera().getIsToRight() ? -(float) (cant * 10) : (float) (cant * 10)));
                }
                Pantalla.getInstance().render();
            }

            data = "";
            data += c;
        }

        if (comienza && !(c == 'X' || c == 'Y')) {
            data += c;
        }
    }

    private static String showPorts() {
        StringBuilder s = new StringBuilder("¿Qué puerto quieres usar?\n");
        for (int i = 0; i < ports.length; i++) {
            s.append(i + 1).append(". ").append(ports[i].getDescriptivePortName()).append(" ").append(ports[i].getPortDescription()).append("\n");
        }

        return s.toString();
    }

    public static boolean connect() {
        int port;
        try {
            String strPorts = showPorts();
            if (strPorts.equals("¿Qué puerto quieres usar?\n")) {
                JOptionPane.showMessageDialog(null, "No hay ningún puerto conectado. Prueba a reiniciar la aplicación", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            port = Integer.parseInt(JOptionPane.showInputDialog(strPorts));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El puerto debe tener un valor numérico.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (port < 1 || port > ports.length) {
            JOptionPane.showMessageDialog(null, "El puerto debe estar dentro del rango.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return setPort(port - 1);
    }

    public static boolean isConnected() {
        return isConnected;
    }
}
