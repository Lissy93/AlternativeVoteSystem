
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AVModelTest {

    String[] exampleNames = {"Ollie", "George", "Alicia", "Fiona"};

    @org.junit.Before
    public void setUp() throws Exception {

    }

    @org.junit.After
    public void tearDown() throws Exception {

    }

    @org.junit.Test
    public void testCountVotes() throws Exception {
        AVModel model = new AVModel();
        model.addBallot(exampleNames);
        model.convertBallotResultsToCandidateResults();
        model.countVotes();
        assertEquals(model.candidateTotals.size(), 4);
    }

    @org.junit.Test
    public void testRedistribute() throws Exception {
        AVModel model = new AVModel();
        model.addBallot(exampleNames);
        model.convertBallotResultsToCandidateResults();
        model.countVotes();

        model.redistribute();
        assertEquals(model.candidateTotals.size(), 3);

        model.redistribute();
        assertEquals(model.candidateTotals.size(), 2);

        model.redistribute();
        assertEquals(model.getCandidateTotals().size(), 1);
    }

    @org.junit.Test
    public void testGetCandidateTotals() throws Exception {
        AVModel model = new AVModel();
        model.addBallot(exampleNames);
        assertNotNull(model.getCandidateTotals());
    }

    @org.junit.Test
    public void testGetBallotResults() throws Exception {
        AVModel model = new AVModel();
        model.addBallot(exampleNames);
        assertNotNull(model.getBallotResults());
    }

    @org.junit.Test
    public void testAddBallot() throws Exception {
        AVModel model = new AVModel();
        model.addBallot(exampleNames);
        assertNotNull(model.getBallotResults());
    }

    @org.junit.Test
    public void testReadCSV() throws Exception {
        AVModel model = new AVModel();
        model.readCSV("src/data/voteData.csv");
        assertTrue(model.getBallotResults().size() > 0);
    }

    @org.junit.Test
    public void testConvertBallotResultsToCandidateResults() throws Exception {
        AVModel model = new AVModel();
        model.addBallot(exampleNames);

        // Should be zero before we calculate candidate results
        assertTrue(model.getCandidateTotals().size()==0);

        // Then calculate candidate results
        model.convertBallotResultsToCandidateResults();

        // Should now have vaid candidate results
        assertTrue(model.getCandidateTotals().size()==4);
    }

}