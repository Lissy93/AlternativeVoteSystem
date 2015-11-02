import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Alicia on 26/10/2015.
 */
public class AVAddVotesPanel extends Frame implements ActionListener {

    private AVModel model;
    private AVController controller;

    // Components
    JLabel lblAddVoteHeading = new JLabel("Add Vote");
    JButton btnSave = new JButton("Save");
    JButton btnCancel = new JButton("Cancel");
    JLabel[] lblVoteNames = {
                new JLabel("Choice 1"),
                new JLabel("Choice 2"),
                new JLabel("Choice 3"),
                new JLabel("Choice 4")
    };
    JTextField[] txtVoteChoices = {
            new JTextField(),
            new JTextField(),
            new JTextField(),
            new JTextField()
    };

    // Sections
    JPanel pnlHeading = new JPanel();
    JPanel pnlVoteList = new JPanel();
    JPanel pnlControlls = new JPanel();


    public AVAddVotesPanel(AVModel model, AVController controller) {
        super();
        this.model = model;
        this.controller = controller;

        // Set up layout managers
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        pnlVoteList.setLayout(new GridLayout(4,2,10,10));
        pnlControlls.setLayout(new FlowLayout());

        // Set properties on each UI component
        lblAddVoteHeading.setFont(lblAddVoteHeading.getFont().deriveFont(35.0f));

        // Add Components to relevant sections
        pnlHeading.add(lblAddVoteHeading);
        for (int i = 0; i < 4; i ++){
            pnlVoteList.add(lblVoteNames[i]);
            pnlVoteList.add(txtVoteChoices[i]);
        }
        pnlControlls.add(btnSave);
        pnlControlls.add(btnCancel);

        // Add action listeners to buttons
        btnSave.addActionListener(this);
        btnCancel.addActionListener(this);

        // Add sections to window
        add(pnlHeading);
        add(pnlVoteList);
        add(pnlControlls);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == btnSave){
            String[] names = new String[4];
            for(int i = 0; i < txtVoteChoices.length; i++){
                names[i] = txtVoteChoices[i].getText();
            }
            controller.addBallot(names);}
        else if(event.getSource() == btnCancel)
            controller.closeFrame(this);
    }
}
