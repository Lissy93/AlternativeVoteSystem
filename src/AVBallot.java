import java.util.ArrayList;

/**
 * Class for ballots containing the ballot as a list of names and
 * related functions for manipulating ballot data
 */
public class AVBallot{
    ArrayList<String> candidateChoices = new ArrayList<>(); // List of choices


    /**
     * Appends a candidate onto the class variable candidate choices list
     * @param candidate the String name of candidate
     */
    public void appendCandidate(String candidate){
        candidateChoices.add(candidate);
    }


    /**
     * Returns a specific candidate object at a given index
     * @param index int index value, must be smaller than size
     * @return Candidate object
     */
    public String getCandidateAtIndex(int index){
        return candidateChoices.get(index);
    }


    /**
     * Removes a candidate object denoted by index from the class variable list
     * @param index integer position in list
     */
    public void removeCandidateAtIndex(int index){
        candidateChoices.remove(index);
    }


    /**
     * Inserts a new candidate to the class variable candidate list
     * @param index the integer position to insert new candidate
     * @param candidate the Candidate object to be inserted
     */
    public void setCandidateAtIndex(int index, String candidate){
        candidateChoices.set(index, candidate);
    }


    /**
     * Getter for candidates class variable
     * @return the Candidats ArrayList class variable
     */
    public ArrayList<String> getAllCandidateChoices() {
        return candidateChoices;
    }


    /**
     * Setter for candidates class variable
     * @param candidateChoices and ArrayList of Candidate objects
     */
    public void setAllCandidateChoices(ArrayList<String> candidateChoices) {
        this.candidateChoices = candidateChoices;
    }
}