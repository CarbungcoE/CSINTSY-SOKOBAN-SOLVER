package solver;
import java.util.ArrayList;

public class BoardState {
  int width;
  int height; 
  char[][] mapData;
  char[][] itemsData;
  ArrayList<int[]> crateList = new ArrayList<int[]>(); //{(row,col), (row,col)} NOTE THAT COORDS START FROM INDEX 0
  ArrayList<int[]> targetList = new ArrayList<int[]>();

  public BoardState(int width, int height, char[][] mapData, char[][] itemsData) { //note that this will only be used by the original boardstate
    this.width = width;
    this.height = height;
    this.mapData = mapData;
    this.itemsData = itemsData;
    generateLists();
  }

  public void clearLists() {
    crateList = crateList.clear();
    targetList = targetList.clear();
  }

  void generateLists() {
    for(int i = 0; i < height; i++) {
        for(int j = 0; j < width; j++) {
          if(itemsData[i][j] == '$'){
            crateList.add(new int[]{i,j});
          }
          if(mapData[i][j] == '.'){
            targetList.add(new int[]{i,j});
          }
          
        }
      }
  }

  public ArrayList<int[]> getCrateList() {
    return crateList;
  }

  public ArrayList<int[]> getTargetList() {
    return targetList;
  }

  public int getHeight() {
    return height;
  }

  public int getWidth() {
    return width;
  }

  public char[][] getMapData() {
    return mapData;
  }

  public char[][] getItemsData() {
    return itemsData;
  }

  public void set(char[][] mapData, char[][] itemsData) {
    this.mapData = mapData;
    this.itemsData = itemsData;
    clearLists();
    generateLists();
  }

  public boolean isEqualTo(BoardState state) {
    return (state.mapData.equals(this.mapData) && state.itemsData.equals(this.itemsData));
  }

  //calculates heuristic at a given 'state'
  public int getHeuristic() {
    int nHeuristic = 0;
    int currentLowestMhtVal = 0, mhtVal;
    ArrayList<Integer> manhattanDistances = new ArrayList<Integer>();

    for(int[] crate : this.crateList) {

      for(int[] target : this.targetList) { //this does not factor in the fact that each crate has to go to a different target

        //find the lowest heuristic of current crate
        if(this.targetList.indexOf(target) == 0) {
          mhtVal = Math.abs(crate[0] - target[0]) + Math.abs(crate[1] - target[1]);
          currentLowestMhtVal = mhtVal;
        } else {
          mhtVal = Math.abs(crate[0] - target[0]) + Math.abs(crate[1] - target[1]);
          if(currentLowestMhtVal > mhtVal) {
            currentLowestMhtVal = mhtVal;
          }
        }

        //for debugging
        //System.out.println("Crate - " + crateList.indexOf(crate) + ", Target - " + targetList.indexOf(target) + ":"+ currentLowestMhtVal);

      }

      manhattanDistances.add(currentLowestMhtVal);
    }

    for(Integer mhtDistance : manhattanDistances) {
      nHeuristic += mhtDistance.intValue();
    }
    return nHeuristic; //heuristic = sum of manhattan distances
  }
  
}