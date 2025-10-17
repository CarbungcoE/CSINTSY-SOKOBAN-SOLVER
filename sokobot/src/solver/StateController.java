package solver;
import java.util.ArrayList;

public class StateController {
  StatePath state;
  public StateController(StatePath state) {
    this.state = state;
  }

  public ArrayList<StatePath> getSuccessorStates() {
    int playerLocation[]; //[0] = x/column, [1] = y/height
    int boardHeight = state.getBoardState().getHeight();
    int boardWidth = state.getBoardState().getWidth();

    for(int i = 0; i < boardHeight; i++) {
      for(int j = 0; j < boardWidth; j++) {
        if(state.getBoardState().getItemsData()[i][j] = '@') {
          playerLocation[0] = j;
          playerLocation[1] = i;
          i = boardHeight;
          j = boardWidth;
        }
      }
    }

    ArrayList<StatePath> temp = new ArrayList<StatePath>();
    StatePath up, right, down, left;
    

    return temp;
  }

  char[][] checkUp() { //parameter is the location of player
    char[][] tempMap = state.getBoardState().getMapData();
    char[][] tempItems = state.getBoardState().getItemsData();
    

  }

}