import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class AVModel extends Observable implements IAVModel {

    ArrayList<AVBallot> ballotResults = new ArrayList<>();

    ArrayList<AVCandidate> candidates = new ArrayList<>();

    HashMap<String, Integer> candidateTotals = new HashMap<>();


    @Override
    public void countVotes(){
        assert ballotResults.size()>0 : "Ballots must not be empty";
        removeBlankVotes();     // We don't want to store or sort empty votes
        makeCandidateList();    // Assigns class var candidates list of unique candidates
        collectBallots();       // Finds initially the first vote of each ballot and assigns it
        makeCandidateTotals();  // Calculates the totals for each candidate in list
        this.setChanged();
        this.notifyObservers();
    }


    @Override
    public void redistribute(){
        AVCandidate lowestCandidate = findLowestRankingCandidate(); // candidate with least votes
        removeLowestCandidateFromBallots(lowestCandidate);
        candidates = new ArrayList<>(); // reinitialise
        candidateTotals = new HashMap<>();
        countVotes(); // Repeat the counting process, this time without the lowest candidate
        this.setChanged();
        this.notifyObservers();
    }


    @Override
    public HashMap<String, Integer> getCandidateTotals() {
        assert candidateTotals !=null : "Candidate totals is not initialised";
        return candidateTotals;
    }


    @Override
    public ArrayList<AVBallot> getBallotResults(){
        return this.ballotResults;
    }


    @Override
    public void addBallot(String[] choices){
        assert ballotResults != null : "Ballot results must be intialised to new arraylist";
        ArrayList<String> newVote = new ArrayList<String>( Arrays.asList(choices ) );
        AVBallot newBallot = new AVBallot();
        newBallot.setAllCandidateChoices(newVote);
        ballotResults.add(newBallot);
        this.setChanged();
        this.notifyObservers();
    }


    @Override
    public void readCSV(String pathToFile) {
        assert pathToFile != "" : "Path to file can not be empty";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {
            br = new BufferedReader(new FileReader(pathToFile));
            while ((line = br.readLine()) != null) {
                AVBallot ballotsFromFile = new AVBallot();
                String[] candidates = line.split(cvsSplitBy);
                ballotsFromFile.setAllCandidateChoices(new ArrayList<>(Arrays.asList(candidates)));
                this.ballotResults.add(ballotsFromFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void convertBallotResultsToCandidateResults() {
        assert ballotResults.size() > 0 : "Ballot results can not be empty";
        for (AVBallot votesList : ballotResults) {
            for (String aVotesList : votesList.getAllCandidateChoices()) {
                Integer candidateVoteTotal = candidateTotals.get(aVotesList);
                if (candidateVoteTotal != null) {
                    candidateTotals.put(aVotesList, candidateVoteTotal + 1);
                } else {
                    candidateTotals.put(aVotesList, 1);
                }
            }
        }
        sortCandidateTotalsByVotes();
    }


    /**
     * Removes empty candidate entries in the ballots class
     * @pre ballots must not be empty
     * @post there will be no more empty candidates in ballot candidate list
     */
    private void removeBlankVotes(){
        assert ballotResults.size() > 0 : "Ballot results can not be empty";
        for(AVBallot ballot : ballotResults){
            for(int i = 0; i< ballot.getAllCandidateChoices().size(); i++){
                if(ballot.getAllCandidateChoices().get(i).equals("")){
                    ballot.removeCandidateAtIndex(i);
                }
            }
        }
    }


    /**
     * Add all candidates from all ballots to the candidates list
     * if they aren't yet in it
     * @pre there must be ballots
     * @post the candidates list will be populated
     */
    private void makeCandidateList(){
        assert ballotResults.size() > 0 : "Ballot results can not be empty";
        candidates = new ArrayList<>();
        for(AVBallot ballot: ballotResults){
            candidates.addAll(
                    ballot.getAllCandidateChoices().stream()
                            .filter(candidateName -> !doesCandidateExist(candidateName))
                            .map(AVCandidate::new).collect(Collectors.toList())
            );
        }
    }


    /**
     * Distributes ballots between remaining candidates
     * @pre there must be ballots and some remaining candidates
     * @post the candidates will have ballot objects assigned to them
     */
    private void collectBallots() {
        assert ballotResults.size() > 0 : "Ballot results can not be empty";
        assert candidates.size() > 0 : "There must be some remaining candidates";
        ballotResults.stream()
                .filter(ballot -> ballot.getAllCandidateChoices().size() > 0)
                .forEach(ballot -> {
                    String firstChoice = ballot.getCandidateAtIndex(0);
                    int candidateIndex = findCandidateIndexFromName(firstChoice);
                    AVCandidate candidate = candidates.get(candidateIndex);
                    candidate.appendBallot(ballot);
                    candidates.add(candidateIndex, candidate);
                });
    }


    /**
     * Populates the candidate totals HashMap
     * with values from the candidates list
     * @pre the candidates list should not be empty
     * @post the candidateTotals HashMap will be populated
     */
    private void makeCandidateTotals(){
        assert candidates.size() > 0 : "Candidates list can not be empty";

        for(AVCandidate candidate: candidates){
            candidateTotals.put(candidate.getName(), candidate.numOfBallots());
        }
        sortCandidateTotalsByVotes();
    }


    /**
     * Sorts the candidateResults class variable by number of votes
     * @pre candidateVotes should have valid data
     * @post candidateVotes will be sorted
     */
    private void sortCandidateTotalsByVotes() {
        assert candidateTotals.size()>0 : "There need to be some candidate totalls";
        candidateTotals =
                (HashMap<String, Integer>)new MapUtil().sortByValue(candidateTotals);
    }


    /**
     * Checks if a candidate object with given candidateName is present
     * in the candidates class variable
     * @pre candidates list should not be empty
     * @post will return boolean value, no data modified
     * @param candidateName the string identifier of candidate
     * @return true if candidate found, if not found return false
     */
    private boolean doesCandidateExist(String candidateName){
        for(AVCandidate candidate : candidates){
            if(candidate.getName().equals(candidateName)){
                return true;
            }
        }
        return false;
    }


    /**
     * Returns the index for a given candidate specified by their name
     * @pre candidate must exist
     * @post index in list will be returned
     * @param candidateName the text identifier of a given candidate
     * @return integer value indicating position in candidate class list
     */
    private int findCandidateIndexFromName(String candidateName){
        assert !Objects.equals(candidateName, "") : "Candidate must have a name and exist";
        for(int i = 0; i < candidates.size(); i++){
            if(candidates.get(i).getName().equals(candidateName)){
                return i;
            }
        }
        return 0;
    }


    /**
     * Finds the lest popular candidate
     * @pre there must be candidates availible
     * @post candidate with lowest rank will be returned
     * @return AVCandidate with lowest rank
     */
    private AVCandidate findLowestRankingCandidate(){
        assert candidates.size() > 0 : "There must be some rementing candidates";
        Map.Entry<String, Integer> lowestCandidate = null;
        for (Map.Entry<String, Integer> entry : candidateTotals.entrySet()) {
            if (lowestCandidate == null ||
                    entry.getValue().compareTo(lowestCandidate.getValue()) < 0){
                lowestCandidate = entry;
            }
        }
        assert lowestCandidate != null;
        return candidates.get(findCandidateIndexFromName(lowestCandidate.getKey()));
    }


    /**
     * Remove low ranking candidate from all ballots
     * @pre ballots list must not be empty
     * @post ballots list will have the lowest candidate removed
     */
    private void removeLowestCandidateFromBallots(AVCandidate lowestCandidate){
        assert ballotResults.size()>0 : "Ballots must not be empty";
        String lowestCandidateName = lowestCandidate.getName();
        for (AVBallot ballotResult : ballotResults) {
            ArrayList<String> choices = ballotResult.getAllCandidateChoices();
            for (int i = 0; i < choices.size(); i++) {
                if (choices.get(i).equals(lowestCandidateName)) {
                    ballotResult.removeCandidateAtIndex(i);
                }
            }
        }

    }


    public void initialise() {
        setChanged();
        notifyObservers();
    }


    public AVModel() {
        initialise();
    }


    /**
     * Sorts a HashMap of candidate names and votes by votes
     * @pre candidateVotes passed in should not be empty of null
     * @post will return the sorted candidateVotes data
     */
    private class MapUtil {
        public  <K, V extends Comparable<? super V>> Map<K, V>
        sortByValue( Map<K, V> map ) {
            List<Map.Entry<K, V>> list = new LinkedList<>( map.entrySet() );
            Collections.sort( list, (o2, o1) -> (o1.getValue()).compareTo( o2.getValue() ));
            Map<K, V> result = new LinkedHashMap<>();
            for (Map.Entry<K, V> entry : list) {
                result.put( entry.getKey(), entry.getValue() );
            }
            return result;
        }
    }
}