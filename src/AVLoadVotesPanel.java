import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Alicia on 26/10/2015.
 */
public class AVLoadVotesPanel extends Frame implements ActionListener{

    private AVModel model;
    private AVController controller;

    final JFileChooser fc = new JFileChooser();


    // Components
    JLabel lblLoadVoteHeading = new JLabel("Load Votes");
    JButton btnChoose = new JButton("Choose File");
    JButton btnUpload = new JButton("Upload");
    JButton btnCancel = new JButton("Cancel");
    JLabel lblSelectFile = new JLabel("Select File");

    // Sections
    JPanel pnlHeading = new JPanel();
    JPanel pnlFileUpload = new JPanel();
    JPanel pnlControlls = new JPanel();

    public AVLoadVotesPanel(AVModel model, AVController controller) {
        super();
        this.model = model;
        this.controller = controller;


        // Set up layout managers
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        pnlFileUpload.setLayout(new FlowLayout());
        pnlControlls.setLayout(new FlowLayout());


        // Set properties on each UI component
        lblLoadVoteHeading.setFont(lblLoadVoteHeading.getFont().deriveFont(35.0f));
        btnUpload.setEnabled(false);
        fc.setAcceptAllFileFilterUsed(false);
        fc.setMultiSelectionEnabled(false);
        fc.setFileFilter(new FileNameExtensionFilter("CSV Ballot", "csv"));


        // Add Components to relevant sections
        pnlHeading.add(lblLoadVoteHeading);
        pnlFileUpload.add(lblSelectFile);
        pnlFileUpload.add(btnChoose);
        pnlControlls.add(btnUpload);
        pnlControlls.add(btnCancel);

        // Add action listeners to buttons
        btnChoose.addActionListener(this);
        btnUpload.addActionListener(this);
        btnCancel.addActionListener(this);

        // Add sections to window
        add(pnlHeading);
        add(pnlFileUpload);
        add(pnlControlls);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == btnChoose)
            controller.selectFile(this);
        else if (event.getSource() == btnUpload)
            controller.uploadCsvFile(fc.getSelectedFile().toString(), this);
        else if (event.getSource() == btnCancel)
            controller.closeFrame(this);

    }
}
