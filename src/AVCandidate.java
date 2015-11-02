import java.util.ArrayList;

/**
 * Class containing candidate name and ballots that were allocated to them
 */
public class AVCandidate{
    String name; // Name of candidate
    ArrayList<AVBallot> ballots = new ArrayList<>(); // List of allocated ballots


    /**
     * Initializer, sets the candidate name
     * @param name String identifier for candidate
     */
    public AVCandidate(String name){
        this.name = name;
    }


    /**
     * Getter for candidate name
     * @return String candidate name
     */
    public String getName() {
        return name;
    }


    /**
     * Integer number of ballots allocated to that candidate
     * @return int number of ballots
     */
    public int numOfBallots(){
        return ballots.size();
    }


    /**
     * Adds a ballot to candidate ballot list
     * @param ballot object
     */
    public void appendBallot(AVBallot ballot){
        ballots.add(ballot);
    }
}