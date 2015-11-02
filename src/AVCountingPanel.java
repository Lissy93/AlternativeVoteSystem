import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class AVCountingPanel extends JPanel implements ActionListener{

    private AVModel model;
    private AVController controller;


    // Data and constants for layout
    String[] columnNames = {"Candidate", "Votes" };

    // Components
    JLabel lblCountingHeading = new JLabel("Candidate Votes");
    JTable tblBallotCount;


    // Sections
    JPanel pnlHeading = new JPanel();
    JPanel pnlData = new JPanel();
    JPanel pnlControlls = new JPanel();
    JButton btnStart = new JButton("Start");
    JButton btnRedistribute = new JButton("Redistribute");

    public AVCountingPanel(AVModel model, AVController controller) {
        super();
        this.model = model;
        this.controller = controller;

        model.countVotes();
        Object[][] ballotResults = (formatCandiatesForTable());

        // Set up components from model
        tblBallotCount = new JTable(ballotResults, columnNames);

        // Set up layout managers
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(BorderFactory.createEmptyBorder(0, 20, 30, 30));
        pnlData.setLayout(new BorderLayout());
        pnlControlls.setLayout(new FlowLayout());

        // Set properties on each UI component
        lblCountingHeading.setFont (lblCountingHeading.getFont ().deriveFont (35.0f));
        tblBallotCount.setEnabled(false);

        // Add Components to relevant sections
        pnlHeading.add(lblCountingHeading);

        pnlData.add(tblBallotCount);
        pnlData.add(new JScrollPane(tblBallotCount));

        pnlControlls.add(btnStart);
        pnlControlls.add(btnRedistribute);

        // Add action listeners to buttons
        btnStart.addActionListener(this);
        btnRedistribute.addActionListener(this);

        // Add sections to window
        add(pnlHeading);
        add(pnlData);
        add( Box.createRigidArea(new Dimension(0,30)));
        add(pnlControlls);
        add( Box.createRigidArea(new Dimension(0,30)));
    }


    public void updateView(){
        tblBallotCount = new JTable(formatCandiatesForTable(), columnNames);
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == btnStart)
            controller.startCoundingVotes();
        else if (event.getSource() == btnRedistribute)
            controller.redistributeVotes();
    }


    /**
     * Returns an Object[][] suitable for presenting data in a JTable
     * Uses the private candidateTotals HashMap class variable
     * @return Object[][]
     * @pre the candidateTotals class variable is populated
     * @post Object[][] will be returned for the view
     */
    public Object[][] formatCandiatesForTable() {
        Object[][] results = new Object[model.getCandidateTotals().size()][2]; // To be returned
        Iterator<Map.Entry<String, Integer>> entriesIterator = model.getCandidateTotals().entrySet().iterator();
        int i = 0;
        while (entriesIterator.hasNext()) {
            Map.Entry<String, Integer> mapping = entriesIterator.next();
            results[i][0] = mapping.getKey();
            results[i][1] = mapping.getValue();
            i++;
        }
        return results;
    }

}
