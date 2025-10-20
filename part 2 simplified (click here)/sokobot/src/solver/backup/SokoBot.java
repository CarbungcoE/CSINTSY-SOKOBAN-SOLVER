package solver;
import java.util.ArrayList;

public class SokoBot {

  public String solveSokobanPuzzle(int width, int height, char[][] mapData, char[][] itemsData) {
    Node root = new Node(width, height, mapData, itemsData, "");
    root.generateDeadSquareDeadlocks(); //generates the static list of deadsquares
    for(int[] square : root.getDeadSquareList()) {
      System.out.println(square[0] + ", " + square[1]);
    }
    Mover mover = new Mover();
    ArrayList<Node> openList = new ArrayList<Node>();
    ArrayList<Node> closedList = new ArrayList<Node>();
    ArrayList<Node> successors = new ArrayList<Node>();
    ArrayList<Node> successorsToSkip = new ArrayList<Node>();

    int lowestStarVal, iterations = 0;
    Node lowestStarNode;
    try {
      openList.add(root);
      //step 0, check if the root node is already the correct answer
      if(root.isSovled()) {
        return root.getPath();
      }

      while(openList.size() != 0) {
        System.out.println("Current number of searched nodes: " + iterations);
        iterations++;

        //step 1, find node with lowest a star score from the openlist
        lowestStarNode = openList.get(0);
        lowestStarVal = openList.get(0).getAStarScore();

        for(Node node : openList) {
          if(node.getAStarScore() < lowestStarVal) {
            lowestStarVal = node.getAStarScore();
            lowestStarNode = node;
          }
        }

        //step 2 check said node's successor/s and remove it from the openlist
        mover.setParent(lowestStarNode);
        successors.clear();
        successors.addAll(mover.findValidMoves());
        openList.remove(lowestStarNode);

        //step 3 check each successor
        for(Node successorNode : successors) {
          //step 3.1 check if successor is solved, return path if yes
          if(successorNode.isSovled()) {
            System.out.println(successorNode.getPath());
            return successorNode.getPath();
          }
          //step 3.2.A check if there is already a node in the openlist or closedlist with the same state and lower a* score
          successorsToSkip.clear();
          for(Node openListNode : openList) {
            if(openListNode.isSameStateAs(successorNode) && !(successorsToSkip.contains(successorNode))) {
              successorsToSkip.add(successorNode);
            }
          }
          for(Node closedListNode : closedList) {
            if(closedListNode.isSameStateAs(successorNode) && !(successorsToSkip.contains(successorNode))) {
              successorsToSkip.add(successorNode);
            }
          }

          
          
        }
        // System.out.println("size of successors to skip " + successorsToSkip.size());
        //step 4 remove all successors with a better equivalent
        if(!(successorsToSkip.isEmpty())) {
            successors.removeAll(successorsToSkip);
        }
        successorsToSkip.clear();

        //step 5 add all valid successors into the openlist
        openList.addAll(successors);

        //step 6 add the parent (loweststarnode) to the closedlist
        closedList.add(lowestStarNode);

      }
      //System.out.println("size of closedList " + closedList.size());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return "lrlrlrlrlrlrlrlrlr";
  }

}
