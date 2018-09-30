
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Graphics {

    private JTextArea eventTimeStampLogger;
    private JScrollPane scrollPane;
    private JLabel imageLabel;
    private JButton exitButton;

    private JFrame f = new JFrame();
    private ClientApp cApp;
    
    public Graphics(ClientApp cApp) throws IOException, InterruptedException {

        this.cApp = cApp;
        
        f.setLocation(800, 1);
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        f.setContentPane(panel);

        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(500, 700));

        initiateComponents();
        createPanel(panel);

        initiateListeners();

        f.pack();
        f.setVisible(true);

    }//Constructor

    //Updates the panel with the timestamp and the new image
    final public void updateUIInfo(byte[] image, String timeStamp) throws IOException {

        ImageIcon icon = new ImageIcon(image);
        imageLabel.setIcon(icon);
            
        f.add(imageLabel);

        eventTimeStampLogger.append("\n" + timeStamp);
        eventTimeStampLogger.setCaretPosition(eventTimeStampLogger.getText().length());

    }

    //Initiates the listeners
    private void initiateListeners() {
        exitButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    System.out.println("Logging off..");                    
                    //Unsubscribe from server before logging off
                    cApp.unsubscribe();

                } catch (RemoteException ex) {}
                System.exit(0);
            }
        }
        );

    }

    //Initialize the components of the UI
    private void initiateComponents() {

        imageLabel = new JLabel("");
        exitButton = new JButton("Exit");
        eventTimeStampLogger = new JTextArea();
        scrollPane = new JScrollPane(eventTimeStampLogger);                         
        scrollPane.setPreferredSize(new Dimension(500, 230));
        eventTimeStampLogger.setLineWrap(true);                                 
           
    }

    //Main panel
    private void createPanel(JPanel panel) {
        panel.add(scrollPane, BorderLayout.NORTH);
        panel.add(imageLabel, BorderLayout.CENTER);
        panel.add(exitButton, BorderLayout.SOUTH);
    }

}
