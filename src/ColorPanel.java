import javax.swing.*;
import java.awt.*;

public class ColorPanel extends JPanel {

    private AVModel model;

    /** Creates a new instance of ColorPanel */
    public ColorPanel(AVModel model) {
        this.model = model;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

    }

    public Dimension getPreferredSize()
    {
        return new Dimension(40,70);
    }

    public Dimension getMinimumSize()
    {
        return getPreferredSize();
    }

    public Dimension getMaximumSize()
    {
        return getPreferredSize();
    }
}
