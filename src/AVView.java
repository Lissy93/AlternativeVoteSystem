import java.util.Observer;
import javax.swing.*;
import java.awt.*;


public class AVView implements Observer {
    
    private AVModel model;
    private AVController controller;
    private JFrame frame;
    private AVBallotPanel panel;
    private ColorPanel colorPanel;
    private AVCountingPanel countingView;

    public AVView(AVModel model, AVController controller)  {
        this.model = model; 
        model.addObserver(this);
        this.controller = controller;

        countingView = new AVCountingPanel(model, controller);

        createControls();
        controller.setView(this);
        update(model, null);
    } 
    
    public void createControls() {
        frame = new JFrame("MVC Alternative Vote System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
        panel = new AVBallotPanel(model, controller);
        contentPane.add(panel);

        colorPanel = new ColorPanel(model);
        contentPane.add(colorPanel);

        frame.pack();
        frame.setVisible(true);
    }


     public void update(java.util.Observable o, Object arg) {

         if(frame != null) {
             frame.revalidate();
             frame.repaint();
             countingView.updateView();
         }

    }
}
   