public class AlternativeVoteMain {
    
    public static void main(String [] args) {
        AVModel model = new AVModel();
        AVController controller = new AVController(model);
        AVView view = new AVView(model, controller);
    }
}
