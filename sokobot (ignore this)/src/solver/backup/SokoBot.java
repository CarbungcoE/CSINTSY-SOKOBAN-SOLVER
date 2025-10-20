package solver;
import java.util.ArrayList; //this is allowed right?

public class SokoBot {
  /*
   * NOTE FOR MY IMPLEMENTATION: I used a very simple calculation of the heuristic (manhattan distance from x crate to y closest target), there's a better way to do this if you want to be more efficient.
   */
  public String solveSokobanPuzzle(int width, int height, char[][] mapData, char[][] itemsData) {

    String solution = "";
    //mapCopy is here because I don't want to tamper with the original scanned crate/targerlist
    ArrayList<ArrayList<int[]>> mapCopy = new  ArrayList<ArrayList<int[]>>();
    BoardState state = new BoardState(width, height, mapData, itemsData);
    
    try {

      mapCopy.add((ArrayList<int[]>)state.getCrateList().clone()); //index 0 is the copied crateList
      mapCopy.add((ArrayList<int[]>)state.getTargetList().clone()); //index 1 is the copied target list
      

      System.out.println("Number of Crates: " + state.getCrateList().size());
      System.out.println("Location of Each: ");
      for(int[] crateCoords : state.getCrateList()) {
        System.out.println("{" + crateCoords[0] + ", " + crateCoords[1] + "}");
      }

      System.out.println("Targets: " + state.getTargetList().size());
      System.out.println("Location of Each: ");
      for(int[] targetCoords : state.getTargetList()) {
        System.out.println("{" + targetCoords[0] + ", " + targetCoords[1] + "}");
      }

      //this next part is just for a test case wherein a problem isn't solvable (not enough targets or there are no crates)
      if(state.getCrateList().size() != state.getTargetList().size() || state.getCrateList().size() == 0) {
        return ""; //not sure if early return statements are ok because there are no rubrics, remember to ask sir abt this thx.
      }

      //wip
      System.out.println("Heuristic at very start: " + state.getHeuristic());




    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return solution;
  }
}
