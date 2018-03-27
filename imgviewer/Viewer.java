import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;


public class Viewer {

    public static void main(String[] args) {
        new Viewer();
    }

    public Viewer() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }

                JFrame frame = new JFrame("Testing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new ViewerPane());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public class ViewerPane extends JPanel {

        private JLabel map;

        public ViewerPane() {
            setLayout(new BorderLayout());
            try {
                map = new JLabel(new ImageIcon(ImageIO.read(new File("/home/martin/Desktop/test_MapImage_wrappers_4.png"))));
                map.setAutoscrolls(true);
                add(new JScrollPane(map));

                MouseAdapter ma = new MouseAdapter() {

                    private Point origin;

                    @Override
                    public void mousePressed(MouseEvent e) {
                        origin = new Point(e.getPoint());
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                    }

                    @Override
                    public void mouseDragged(MouseEvent e) {
                        if (origin != null) {
                            JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, map);
                            if (viewPort != null) {
                                int deltaX = origin.x - e.getX();
                                int deltaY = origin.y - e.getY();

                                Rectangle view = viewPort.getViewRect();
                                view.x += deltaX;
                                view.y += deltaY;

                                map.scrollRectToVisible(view);
                            }
                        }
                    }

                };

                map.addMouseListener(ma);
                map.addMouseMotionListener(ma);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(200, 200);
        }

    }

}
