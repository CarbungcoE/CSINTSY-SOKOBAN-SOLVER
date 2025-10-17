package solver;
import java.util.ArrayList;

public class StatePath {
  BoardState boardState;
  String path;
  public StatePath(BoardState boardState, String path) {
    this.boardState = boardState;
    this.path = path;
  }

  public BoardState getBoardState() {
    return boardState;
  }

  public String getPath() {
    return path;
  }

  public int getPathLength() {
    return path.length();
  }

  public int getAStarScore() {
    return getPathLength() + boardState.getHeuristic();
  }

}