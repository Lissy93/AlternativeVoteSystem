public class AVModel {

    ArrayList<AVBallot> ballotResults = new ArrayList<>();

    ArrayList<AVCandidate> candidates = new ArrayList<>();

    HashMap<String, Integer> candidateTotals = new HashMap<>();


    /**
     * Initial method called by the counting view
     * @pre ballots must not be empty
     * @post candidates and candidate totals will be populated
     */
    public void countVotes(){
        Debug.Assert(ballotResults.Size() > 0);
        removeBlankVotes();     // We don't want to store or sort empty votes
        makeCandidateList();    // Assigns class var candidates list of unique candidates
        collectBallots();       // Finds initially the first vote of each ballot and assigns it
        makeCandidateTotals();  // Calculates the totals for each candidate in list
        this.setChanged();
        this.notifyObservers();
    }


    /**
     * Removes votes from the lowest ranking candidate and distributes them
     * to the next choice candidates
     */
    public void redistribute(){
        AVCandidate lowestCandidate = findLowestRankingCandidate(); // candidate with least votes
        removeLowestCandidateFromBallots(lowestCandidate);
        candidates = new ArrayList<>(); // reinitialise
        candidateTotals = new HashMap<>();
        countVotes(); // Repeat the counting process, this time without the lowest candidate
        this.setChanged();
        this.notifyObservers();
    }


    /**
     * Getter for the candidateTotals private class variable HashMap
     * @return HashMap
     * @pre candidateTotals should be initialised
     * @post a copy of candidateTotals will be returned
     */
    public HashMap<String, Integer> getCandidateTotals() {
        Debug.Assert(candidateTotals != null);
        return candidateTotals;
    }


    /**
     * Getter for ballot results private class variable ArrayList
     * @return ArrayList of Ballots
     * @pre ballotResults should be initialised
     * @post a copy of ballotResults is returned
     */
    public ArrayList<AVBallot> getBallotResults(){
        return this.ballotResults;
    }


    /**
     * Adds a new Ballot into the ballot object from the users String list of choices
     * @pre ballot list is initialised as new arraylist
     * @post ballot list has new entry
     */
    public void addBallot(String[] choices){
        Debug.Assert( ballotResults != null );
        ArrayList<String> newVote = new ArrayList<String>(Arrays.asList(choices));
        AVBallot newBallot = new AVBallot();
        newBallot.setAllCandidateChoices(newVote);
        ballotResults.add(newBallot);
        this.setChanged();
        this.notifyObservers();
    }


    /**
     * Reads specified CSV file and sets class variable to the output
     * @pre valid CSV file must be specified
     * @post class variable candidateTotals will be updated to contents of CSV file
     * @throws IOException if not able to read file
     */
    public void readCSV(String pathToFile) {
        Debug.Assert(pathToFile != "");
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try
        {
            br = new BufferedReader(new FileReader(pathToFile));
            while ((line = br.readLine()) != null)
            {
                AVBallot ballotsFromFile = new AVBallot();
                String[] candidates = line.split(cvsSplitBy);
                ballotsFromFile.setAllCandidateChoices(new ArrayList<>(Arrays.asList(candidates)));
                this.ballotResults.add(ballotsFromFile);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (br != null)
            {
                try
                {
                    br.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * Takes the value for the ballots class variable anc calculates the totals
     * and assigns the result to the candidate totals class variable
     *
     * @pre Must be a value in ballotResults
     * @post candidateTotals will be set
     */
    public void convertBallotResultsToCandidateResults() {
        Debug.Assert( ballotResults.size() > 0 );
        foreach (AVBallot votesList in ballotResults)
        {
            foreach (String aVotesList in votesList.getAllCandidateChoices())
            {
                Integer candidateVoteTotal = candidateTotals.get(aVotesList);
                if (candidateVoteTotal != null)
                {
                    candidateTotals.put(aVotesList, candidateVoteTotal + 1);
                }
                else
                {
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
        Debug.Assert(ballotResults.size() > 0);
        foreach (AVBallot ballot in ballotResults)
        {
            for (int i = 0; i < ballot.getAllCandidateChoices().size(); i++)
            {
                if (ballot.getAllCandidateChoices().get(i).equals(""))
                {
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
        Debug.Assert( ballotResults.size() > 0 );
        candidates = new ArrayList<>();
        foreach (AVBallot ballot in ballotResults)
        {
            candidates.addAll(
                    ballot.getAllCandidateChoices().stream()
                            .filter(candidateName-> doesCandidateExist(candidateName))
                            .map(new AVCandidate()).collect(Collectors.toList())
            );
        }
    }


    /**
     * Distributes ballots between remaining candidates
     * @pre there must be ballots and some remaining candidates
     * @post the candidates will have ballot objects assigned to them
     */
    private void collectBallots() {
        Debug.Assert( ballotResults.size() > 0 );
        Debug.Assert( candidates.size() > 0 );

        if (ballot.getAllCandidateChoices().size() > 0) {
            foreach (AVBallot ballot in ballotResults) {
                String firstChoice = ballot.getCandidateAtIndex(0);
                int candidateIndex = findCandidateIndexFromName(firstChoice);
                AVCandidate candidate = candidates.get(candidateIndex);
                candidate.appendBallot(ballot);
                candidates.add(candidateIndex, candidate);
            }
        }
    }


    /**
     * Populates the candidate totals HashMap
     * with values from the candidates list
     * @pre the candidates list should not be empty
     * @post the candidateTotals HashMap will be populated
     */
    private void makeCandidateTotals(){
        Debug.Assert( candidates.size() > 0 );

        foreach (AVCandidate candidate in candidates){
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
        Debug.Assert (candidateTotals.size() > 0 );
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
        foreach (AVCandidate candidate in candidates) {
            if (candidate.getName().equals(candidateName))
            {
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
        Debug.Assert( !Objects.equals(candidateName, "") );
        for (int i = 0; i < candidates.size(); i++)
        {
            if (candidates.get(i).getName().equals(candidateName))
            {
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
        Dbug.Assert(candidates.size() > 0 );
        Map.Entry<String, Integer> lowestCandidate = null;
        foreach (Map.Entry<String, Integer> entry in candidateTotals.entrySet())
        {
            if (lowestCandidate == null ||
                    entry.getValue().compareTo(lowestCandidate.getValue()) < 0)
            {
                lowestCandidate = entry;
            }
        }
        Debug.Assert(lowestCandidate != null);
        return candidates.get(findCandidateIndexFromName(lowestCandidate.getKey()));
    }


    /**
     * Remove low ranking candidate from all ballots
     * @pre ballots list must not be empty
     * @post ballots list will have the lowest candidate removed
     */
    private void removeLowestCandidateFromBallots(AVCandidate lowestCandidate){
        Debug.Assert( ballotResults.size() > 0);
        String lowestCandidateName = lowestCandidate.getName();
        foreach (AVBallot ballotResult in ballotResults)
        {
            ArrayList<String> choices = ballotResult.getAllCandidateChoices();
            for (int i = 0; i < choices.size(); i++)
            {
                if (choices.get(i).equals(lowestCandidateName))
                {
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

}