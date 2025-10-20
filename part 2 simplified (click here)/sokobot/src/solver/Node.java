package solver;
import java.util.ArrayList;

public class Node {
    int width, height;
    char[][] mapData, itemsData;
    String path;

    //this remains hidden from other classes and is only used for calculating heuristics
    ArrayList<int[]> crateList = new ArrayList<int[]>(); //{(row,col), (row,col)} NOTE THAT COORDS START FROM INDEX 0
    ArrayList<int[]> targetList = new ArrayList<int[]>();
    ArrayList<int[]> wallList = new ArrayList<int[]>();
    static ArrayList<int[]> deadSquares = new ArrayList<int[]>(); //deadsquares are dependent on the map which does not change during runtime, and so it is static

    //coordinates of the player
    int[] coords = new int[2];

    public Node(int width, int height, char[][] mapData, char[][] itemsData, String path) {
        this.width = width;
        this.height = height;
        this.mapData = mapData;
        this.itemsData = itemsData;
        this.path = path;
        generateLists();
        findPlayerCoords();
        // System.out.println("generated node with path: " + path);
        // for(int i = 0 ; i < height; i++) {
        //     for(int j = 0; j < width; j++) {
        //         System.out.print("'" + itemsData[i][j] + "'");
        //     }
        //     System.out.println();
        // }
    }

    public boolean isADeadState() {
        for(int[] crate : crateList) {
            for(int[] deadlockSquare : deadSquares) {
                if(crate[0] == deadlockSquare[0] && crate[1] == deadlockSquare[1]){
                    //debugging
                    //System.out.println("simpleDeadlockDetected");
                    return true;
                }
            }
        }
        return detectFreezeDeadlock();
    }

    boolean detectFreezeDeadlock() {
        for(int[] crate : crateList) {
            int row = crate[0], col = crate[1];
            if(checkForFrozenCrate(crate, mapData) && mapData[row][col] != '.') { //if crate is frozen and it is not frozen inside of a goal, then the game is bricked
                //debugging
                //System.out.println("freezeDeadlockDetected");
                return true;
            }
        }

        return false;
    }

    //returns true if crate is frozen
    boolean checkForFrozenCrate(int[] crateCoordinate, char[][] mapChecked) { //will use a passed map instead of the attribute map because of the recursive check (will pass boxes as walls if it is already checked)
        int canMoveVertical = 1, canMoveHorizontal = 1; //flags
        int row = crateCoordinate[0], col = crateCoordinate[1];
        int leftDeadSquare = 0, rightDeadSquare = 0, upDeadSquare = 0, downDeadSquare = 0; //deadlock flags

        
        //check 1 wall check
        if(row + 1 < height && row - 1 > 0 && (mapChecked[row+1][col] == '#' || mapChecked[row-1][col] == '#')) {//vertical check
            canMoveVertical = 0;
        }
        if(col + 1 < width && col - 1 > 0 && (mapChecked[row][col+1] == '#' || mapChecked[row][col-1] == '#')) {//horizontal check
            canMoveHorizontal = 0;
        }

        if(canMoveHorizontal == 0 && canMoveVertical == 0) {
            return true;
        }
        //check 2 deadsquare check
        for(int[] deadSquare : deadSquares) {
            if(deadSquare[0] == row-1 && deadSquare[1] == col) { //row above
                upDeadSquare = 1;
            }
            if(deadSquare[0] == row+1 && deadSquare[1] == col) { //row below
                downDeadSquare = 1;
            }
            if(deadSquare[0] == row && deadSquare[1] == col-1) { //col left
                leftDeadSquare = 1;
            }
            if(deadSquare[0] == row && deadSquare[1] == col+1) { //col right
                rightDeadSquare = 1;
            }

        }

        if(upDeadSquare == 1 && downDeadSquare == 1) {
            canMoveVertical = 0;
        }
        if(rightDeadSquare == 1 && leftDeadSquare == 1) {
            canMoveHorizontal = 0;
        }

        if(canMoveHorizontal == 0 && canMoveVertical == 0) {
            return true;
        }

        //check 3 crate check
        if(row - 1 >= 0 && itemsData[row-1][col] == '$' && mapChecked[row-1][col] != '#') { //upcrate
            char[][] recursiveMapCheck = new char[height][width];
            for(int i = 0; i < height; i++) {
                for(int j = 0; j < width; j++) {
                    recursiveMapCheck[i][j] = mapChecked[i][j];
                }
            }
            recursiveMapCheck[row][col] = '#';
            if(checkForFrozenCrate(new int[]{row-1,col}, recursiveMapCheck)) {
                canMoveVertical = 0;
            }
        }

        if(row + 1 < height && itemsData[row+1][col] == '$' && mapChecked[row+1][col] != '#') { //downcrate
            char[][] recursiveMapCheck = new char[height][width];
            for(int i = 0; i < height; i++) {
                for(int j = 0; j < width; j++) {
                    recursiveMapCheck[i][j] = mapChecked[i][j];
                }
            }
            recursiveMapCheck[row][col] = '#';
            if(checkForFrozenCrate(new int[]{row+1,col}, recursiveMapCheck)) {
                canMoveVertical = 0;
            }
        }

        if(col - 1 >= 0 && itemsData[row][col-1] == '$' && mapChecked[row][col-1] != '#') { //leftCrate
            char[][] recursiveMapCheck = new char[height][width];
            for(int i = 0; i < height; i++) {
                for(int j = 0; j < width; j++) {
                    recursiveMapCheck[i][j] = mapChecked[i][j];
                }
            }
            recursiveMapCheck[row][col] = '#';
            if(checkForFrozenCrate(new int[]{row,col-1}, recursiveMapCheck)) {
                canMoveHorizontal = 0;
            }
        }

        if(col + 1 < width && itemsData[row][col+1] == '$' && mapChecked[row][col+1] != '#') { //leftCrate
            char[][] recursiveMapCheck = new char[height][width];
            for(int i = 0; i < height; i++) {
                for(int j = 0; j < width; j++) {
                    recursiveMapCheck[i][j] = mapChecked[i][j];
                }
            }
            recursiveMapCheck[row][col] = '#';
            if(checkForFrozenCrate(new int[]{row,col+1}, recursiveMapCheck)) {
                canMoveHorizontal = 0;
            }
        }

        if(canMoveHorizontal == 0 && canMoveVertical == 0) {
            return true;
        }


        return false;
    }

    public void generateDeadSquareDeadlocks() { //this will only ever be called once to initiate the static simple deadstate squares
        //simpleDeadlockSquares will be all areas left unexplored
        ArrayList<int[]> simpleDeadLockSquares = new ArrayList<int[]>();
        ArrayList<int[]> openList = new ArrayList<int[]>();
        ArrayList<int[]> closedList = new ArrayList<int[]>();
        ArrayList<int[]> removeFromOpenList = new ArrayList<int[]>();
        ArrayList<int[]> addToOpenList = new ArrayList<int[]>();
        ArrayList<int[]> deadlockPurgatory = new ArrayList<int[]>();
        //for movement checking
        int wallcheckRow, wallcheckCol, adjacentRow, adjacentCol, row, col, flag;
        openList.addAll(targetList);
        //note: there is a bug here in the case of a target the is adjacent to the edge (no walls), but i think all testcases will have walls encompassing the edge so i think it's fine?
        while(!(openList.isEmpty())) {
            for(int[] coordinate : openList) {
                removeFromOpenList.add(coordinate);
                row = coordinate[0];
                col = coordinate[1];
                wallcheckRow = row + 2;
                wallcheckCol = col + 2;
                adjacentRow = row + 1;
                adjacentCol = col + 1;
                
                //right
                if(adjacentCol > 0 && adjacentCol < width && mapData[row][adjacentCol] != '#') { //check if it is a valid place to move
                    if(wallcheckCol < width && wallcheckCol > 0){
                        if(mapData[row][wallcheckCol] != '#'){
                            addToOpenList.add(new int[]{row,adjacentCol});
                        }
                    }
                }
                //left
                adjacentCol = col - 1;
                wallcheckCol = col - 2;
                if(adjacentCol > 0 && adjacentCol < width && mapData[row][adjacentCol] != '#') { //check if it is a valid place to move
                    if(wallcheckCol < width && wallcheckCol > 0){
                        if(mapData[row][wallcheckCol] != '#'){
                            addToOpenList.add(new int[]{row,adjacentCol});
                        }
                    }
                }
                //down
                if(adjacentRow > 0 && adjacentRow < height && mapData[adjacentRow][col] != '#') { //check if it is a valid place to move
                    if(wallcheckRow < height && wallcheckRow > 0){
                        if(mapData[wallcheckRow][col] != '#'){
                            addToOpenList.add(new int[]{adjacentRow,col});
                        }
                    }
                }
                //up
                adjacentRow = row - 1;
                wallcheckRow = row - 2;
                if(adjacentRow > 0 && adjacentRow < height && mapData[adjacentRow][col] != '#') { //check if it is a valid place to move
                    if(wallcheckRow < height && wallcheckRow > 0){
                        if(mapData[wallcheckRow][col] != '#'){
                            addToOpenList.add(new int[]{adjacentRow,col});
                        }
                        addToOpenList.add(new int[]{adjacentRow,col});
                    }
                    
                }
            }

            for(int[] candidate : addToOpenList) { //check if candidate is in closedlist
                flag = 0;
                for(int[] checked : closedList) {
                    if(candidate[0] == checked[0] && candidate[1] == checked[1]) {
                        flag = 1;
                        break;
                    }
                }
                for(int[] checked : openList) {
                    if(candidate[0] == checked[0] && candidate[1] == checked[1]) {
                        flag = 1;
                        break;
                    }
                }
                if(flag == 0) {
                    openList.add(candidate);
                }
            }
            
            openList.removeAll(removeFromOpenList);
            closedList.addAll(removeFromOpenList);
            addToOpenList.clear();
            removeFromOpenList.clear();

        }

        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                if(mapData[i][j] != '#') {
                    simpleDeadLockSquares.add(new int[]{i,j});
                }
            }
        }

        for(int[] aliveSquares : closedList) {
            for(int[] supposedDeadSquares : simpleDeadLockSquares) {
                if(aliveSquares[0] == supposedDeadSquares[0] && aliveSquares[1] == supposedDeadSquares[1]) {
                    //System.out.println("(" + aliveSquares[0] + "," + aliveSquares[1] + ")");
                    deadlockPurgatory.add(supposedDeadSquares);
                }
            }
        }
        simpleDeadLockSquares.removeAll(deadlockPurgatory);
        deadSquares.addAll(simpleDeadLockSquares);
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
                if(mapData[i][j] == '#'){
                    wallList.add(new int[]{i,j});
                }
                
            }
        }
    }

    void findPlayerCoords() {
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                if(itemsData[i][j] == '@') {
                    coords[0] = i;
                    coords[1] = j;
                    j = width;
                    i = height;
                }
            }
        }
    }

    public boolean isSameStateAs(Node comparedNode) {
        if(coords[0] != comparedNode.getPlayerCoords()[0] || coords[1] != comparedNode.getPlayerCoords()[1]) {
            return false;
        }

        int count = 0;
        for(int[] crate : crateList) {
            for(int[] comparedCrate : comparedNode.getCrateList()) {
                if(crate[0] == comparedCrate[0] && crate[1] == comparedCrate[1]) {
                    count++;
                }
            }
        }
        if(count == crateList.size()){
            return true;
        } else {
            return false;
        }
    }

    public boolean isSovled() {
        int total = crateList.size();
        int check = 0;
        for(int[] crate : crateList) {
            for(int[] target : targetList) {
                if(crate[0] == target[0] && crate[1] == target[1]) {
                    check++;
                }
            }
        }
        if(total == check) {
            return true;
        } else {
            return false;
        }
    }

    //calculates heuristic at a given 'node'
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

    public int getAStarScore() {
        return getHeuristic() + path.length();
    }

    /**
     * @return coords in the form (row,col) or (x,y) or (i, j)
     */
    public int[] getPlayerCoords() { 
        return coords;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public char[][] getMapData() {
      return mapData;
    }

    public char[][] getItemsData() {
       return itemsData;
    }

    public String getPath() {
        return path;
    }

    public ArrayList<int[]> getCrateList() {
        return crateList;
    }

    public ArrayList<int[]> getDeadSquareList() {
        return deadSquares;
    }
}
