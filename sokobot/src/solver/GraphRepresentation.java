package solver;
import java.util.ArrayList;

public class GraphRepresentation {
  ArrayList<StatePath> openList;
  ArrayList<StatePath> closedList;

  public GraphRepresentation(StatePath startNode) {
    openList.add(startNode);
  }

  public boolean isOpenListEmpty() { //return true if it is empty, return false if not
    return openList.size() == 0;
  }

  public StatePath getFrontOfOpenList() {
    return openList.get(openList.size() - 1);
  }

  public void removeFrontOfOpenList() {
    openList.remove(openList.size() - 1);
  }

  public StatePath getCurrentLowestAStarScoreInOpenList() {
    int lowestIdx = 0;
    int lowestVal = openList.get(0).getAstarScore();
    for(StatePath state : openList) {
        if(lowestVal > state.getAStarScore()) {
            lowestIdx = openList.indexOf(state);
            lowestVal = state.getAStarScore();
        }
    }

    return lowestIdx;
  }

  public StatePath closeOpenStatePath(StatePath state) {
    closedList.add(state);
    openList.remove(state);

    return state;
  }
}