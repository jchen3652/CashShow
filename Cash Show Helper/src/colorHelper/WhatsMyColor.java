package colorHelper;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridBagLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class WhatsMyColor {

    public static void main(String[] args) throws IOException {
        new WhatsMyColor();
    }

    public WhatsMyColor() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException ex) {
                } catch (InstantiationException ex) {
                } catch (IllegalAccessException ex) {
                } catch (UnsupportedLookAndFeelException ex) {
                }

                try {
                    JFrame frame = new JFrame();
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setLayout(new BorderLayout());
                    frame.add(new MouseColorPane());
                    frame.setSize(400, 200);
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                } catch (Exception exp) {
                    exp.printStackTrace();
                }

            }
        });
    }

    public class MouseColorPane extends JPanel implements MouseMonitorListener {

        private Robot robot;

        private JLabel label;

        public MouseColorPane() throws AWTException {

            label = new JLabel();

            setLayout(new GridBagLayout());
            add(label);

            robot = new Robot();
            PointerInfo pi = MouseInfo.getPointerInfo();
            updateColor(pi.getLocation());
            MouseMonitor monitor = new MouseMonitor();
            monitor.setMouseMonitorListener(this);
            monitor.start();

        }

        protected void updateColor(Point p) {

            Color pixelColor = robot.getPixelColor(p.x, p.y);
            setBackground(pixelColor);

            label.setText(p.x + "x" + p.y + " = " + pixelColor);

        }

        @Override
        public void mousePositionChanged(final Point p) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    updateColor(p);
                }

            });
        }
    }

    public interface MouseMonitorListener {

        public void mousePositionChanged(Point p);
    }

    public static class MouseMonitor extends Thread {

        private Point lastPoint;
        private MouseMonitorListener listener;

        public MouseMonitor() {
            setDaemon(true);
            setPriority(MIN_PRIORITY);
        }

        public void setMouseMonitorListener(MouseMonitorListener listener) {
            this.listener = listener;
        }

        public MouseMonitorListener getMouseMonitorListener() {
            return listener;
        }

        protected Point getMouseCursorPoint() {
            PointerInfo pi = MouseInfo.getPointerInfo();
            return pi.getLocation();
        }

        @Override
        public void run() {
            lastPoint = getMouseCursorPoint();
            while (true) {
                try {
                    sleep(250);
                } catch (InterruptedException ex) {
                }

                Point currentPoint = getMouseCursorPoint();
                if (!currentPoint.equals(lastPoint)) {
                    lastPoint = currentPoint;
                    MouseMonitorListener listener = getMouseMonitorListener();
                    if (listener != null) {
                        listener.mousePositionChanged((Point) lastPoint.clone());
                    }
                }

            }
        }
    }
}