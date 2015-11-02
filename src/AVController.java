import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AVController implements ActionListener {
    
    private AVModel model;
    private AVView view;


    public AVController(AVModel model) {
        this.model = model;
    }


    public AVView getView () {
        return view;
    }


    public void setView(AVView view) {
        this.view = view;
    }


    public void actionPerformed(ActionEvent e) {
//        model.change();
    }

    public void showCountingView(){
        JFrame frmCountingView = new JFrame();
        frmCountingView.setSize(new Dimension(400, 400));
        Container container =  frmCountingView.getContentPane();
        container.add(new AVCountingPanel(model, this));
        frmCountingView.setVisible(true);
    }

    public void showAddVoteDialog(){
        AVAddVotesPanel frmAddVotesView = new AVAddVotesPanel(model, this);
        frmAddVotesView.setSize(new Dimension(250, 300));
        frmAddVotesView.setVisible(true);
    }

    public void showLoadVotesDialog(){
        AVLoadVotesPanel frmLoadVotesView = new AVLoadVotesPanel(model, this);
        frmLoadVotesView.setSize(new Dimension(300, 250));
        frmLoadVotesView.setVisible(true);
    }


    public void startCoundingVotes(){

    }

    public void redistributeVotes(){
        model.redistribute();
    }

    public void exitApplication(){
        System.exit(0);
    }

    public void closeFrame(Frame lvp){
        lvp.dispose();
    }

    public void selectFile(AVLoadVotesPanel lvp){
        lvp.fc.showOpenDialog(lvp);
        if(lvp.fc.getSelectedFile() != null){
            lvp.btnUpload.setEnabled(true);
        }
    }

    public void uploadCsvFile(String pathToFile, AVLoadVotesPanel lvp){
        model.readCSV(pathToFile);
        lvp.dispose();
    }

    public void addBallot(String[] choices){
        model.addBallot(choices);
    }

}

