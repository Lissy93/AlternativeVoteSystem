import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AVBallotPanel extends JPanel implements ActionListener{
    
    private AVModel model;
    private AVController controller;

    // Components
    JLabel lblBallotsHeading = new JLabel("Ballots View");
    JButton btnAddVote = new JButton("Add Vote");
    JButton btnCountingView = new JButton("Counting");
    JButton btnLoadVotes = new JButton("Load Votes");
    JButton btnExit = new JButton("Exit");
    JList<Object> lstBallots;

    // Sections
    JPanel pnlHeading = new JPanel();
    JPanel pnlData = new JPanel();
    JPanel pnlControlls = new JPanel();

    public AVBallotPanel(AVModel model, AVController controller) {
        super();
        this.model = model;
        this.controller = controller;

        // Set up layout managers
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(BorderFactory.createEmptyBorder(0, 30, 10, 0));
        pnlData.setLayout(new BorderLayout());
        pnlControlls.setLayout(new GridLayout(2,2, 10, 10));

        // Set properties on each UI component
        lblBallotsHeading.setFont (lblBallotsHeading.getFont ().deriveFont (35.0f));

        lstBallots = new JList<Object>(formatBallotsForList());
        lstBallots.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lstBallots.setVisibleRowCount(10);
        JScrollPane listScrollPane = new JScrollPane(lstBallots);


        // Add Components to relevant sections
        pnlHeading.add(lblBallotsHeading);
        pnlData.add(listScrollPane);
        pnlControlls.add(btnLoadVotes);
        pnlControlls.add(btnAddVote);
        pnlControlls.add(btnCountingView);
        pnlControlls.add(btnExit);


        // Add sections to window
        add(pnlHeading);
        add(pnlData);
        add( Box.createRigidArea(new Dimension(0,10)));
        add(pnlControlls);
        add( Box.createRigidArea(new Dimension(0,10)));


        // Add action listeners to buttons
        btnLoadVotes.addActionListener(this);
        btnAddVote.addActionListener(this);
        btnCountingView.addActionListener(this);
        btnExit.addActionListener(this);
    }


     
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == btnCountingView)
	    controller.showCountingView();
        else if (event.getSource() == btnAddVote)
            controller.showAddVoteDialog();
        else if (event.getSource() == btnLoadVotes)
            controller.showLoadVotesDialog();
        else if (event.getSource() == btnExit)
            controller.exitApplication();
    }
    

    public Dimension getPreferredSize() {
        return new Dimension(250,250);
    }
    
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }
    
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    /**
     * Returns an Object[] suitable for presenting data in a JList
     * Uses the private ballot List class variable
     * @return Object[]
     * @pre the ballots class variable is populated
     * @post Object[] will be returned for the view
     */
    public Object[] formatBallotsForList() {
        Object[] results = new Object[model.getBallotResults().size()]; // To be returned
        for(int i = 0; i< model.getBallotResults().size(); i++){
            results[i] = model.getBallotResults().get(i);
        }
        return results;
    }
}
