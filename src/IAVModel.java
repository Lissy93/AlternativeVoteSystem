import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Alicia on 27/10/2015.
 */
public interface IAVModel {

    // Stores list of ballots
    ArrayList<AVBallot> ballotResults = new ArrayList<>();

    // Stores list of all remaining candidates
    ArrayList<AVCandidate> candidates = new ArrayList<>();

    // Stores total votes for each candidate name
    HashMap<String, Integer> candidateTotals = new HashMap<>();


    /**
     * Initial method called by the counting view
     * @pre ballots must not be empty
     * @post candidates and candidate totals will be populated
     */
    void countVotes();


    /**
     * Removes votes from the lowest ranking candidate and distributes them
     * to the next choice candidates
     */
    void redistribute();


    /**
     * Getter for the candidateTotals private class variable HashMap
     * @return HashMap
     * @pre candidateTotals should be initialised
     * @post a copy of candidateTotals will be returned
     */
    HashMap<String, Integer> getCandidateTotals();


    /**
     * Getter for ballot results private class variable ArrayList
     * @return ArrayList of Ballots
     * @pre ballotResults should be initialised
     * @post a copy of ballotResults is returned
     */
    ArrayList<AVBallot> getBallotResults();


    /**
     * Adds a new Ballot into the ballot object from the users String list of choices
     * @pre ballot list is initialised as new arraylist
     * @post ballot list has new entry
     */
    void addBallot(String[] choices);


    /**
     * Reads specified CSV file and sets class variable to the output
     * @pre valid CSV file must be specified
     * @post class variable candidateTotals will be updated to contents of CSV file
     * @throws IOException if not able to read file
     */
    public void readCSV(String pathToFile) ;


    /**
     * Takes the value for the ballots class variable anc calculates the totals
     * and assigns the result to the candidate totals class variable
     *
     * @pre Must be a value in ballotResults
     * @post candidateTotals will be set
     */
    void convertBallotResultsToCandidateResults();


}