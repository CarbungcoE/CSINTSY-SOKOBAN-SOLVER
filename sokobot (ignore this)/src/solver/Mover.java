package solver;
import java.util.ArrayList;

public class Mover {
    Node parent;
    public Mover() {

    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public ArrayList<Node> findValidMoves() { //returns a string containing the directions a certain 'node' can go to in the form of a string (e.g. "udlr" for up down left right, or "ud" if you can only move up and down) also don't pay attention to the order of directions
        ArrayList<Node> nodes = new ArrayList<Node>();

        int[] playerCoords = parent.getPlayerCoords();
        int nRow = playerCoords[0];
        int nCol = playerCoords[1];

        char[][] itemsData = parent.getItemsData();
        char[][] mapData = parent.getMapData();
        int width = parent.getWidth();
        int height = parent.getHeight();
        String path = parent.getPath();
        

        if(verifyLeft()) {      
            char[][] leftData = new char[height][width];
            for(int i = 0; i < height; i++) {
                for(int j = 0; j < width; j++) {
                    leftData[i][j] = itemsData[i][j];
                }
            }
            if(leftData[nRow][nCol-1] == '$') {
                leftData[nRow][nCol-2] = '$';
            }
            leftData[nRow][nCol-1] = '@';
            leftData[nRow][nCol] = ' ';
            Node leftNode = new Node(width, height, mapData, leftData, path + "l");
            // if(!(leftNode.isADeadState())){
            //     nodes.add(leftNode);
            // }
            nodes.add(leftNode);
        }
        if(verifyRight()) {
            char[][] rightData = new char[height][width];
            for(int i = 0; i < height; i++) {
                for(int j = 0; j < width; j++) {
                    rightData[i][j] = itemsData[i][j];
                }
            }
            if(rightData[nRow][nCol+1] == '$') {
                rightData[nRow][nCol+2] = '$';
            }
            rightData[nRow][nCol+1] = '@';
            rightData[nRow][nCol] = ' ';
            Node rightNode = new Node(width, height, mapData, rightData, path + "r");
            // if(!(rightNode.isADeadState())){
            //     nodes.add(rightNode);
            // }
            nodes.add(rightNode);
        }
        if(verifyDown()) {
            char[][] downData = new char[height][width];
            for(int i = 0; i < height; i++) {
                for(int j = 0; j < width; j++) {
                    downData[i][j] = itemsData[i][j];
                }
            }
            if(downData[nRow+1][nCol] == '$') {
                downData[nRow+2][nCol] = '$';
            }
            downData[nRow+1][nCol] = '@';
            downData[nRow][nCol] = ' ';
            Node downNode = new Node(width, height, mapData, downData, path + "d");
            // if(!(downNode.isADeadState())){
            //     nodes.add(downNode);
            // }
            nodes.add(downNode);
        }
        if(verifyUp()) {
            char[][] upData = new char[height][width];
            for(int i = 0; i < height; i++) {
                for(int j = 0; j < width; j++) {
                    upData[i][j] = itemsData[i][j];
                }
            }
            if(upData[nRow-1][nCol] == '$') {
                upData[nRow-2][nCol] = '$';
            }
            upData[nRow-1][nCol] = '@';
            upData[nRow][nCol] = ' ';
            Node upNode = new Node(width, height, mapData, upData, path + "u");
            // if(!(upNode.isADeadState())){
            //     nodes.add(upNode);
            // }
            nodes.add(upNode);
        }

        return nodes;
    }


    //this is for debugging
    // String verifyStuff() {
    //     String thing = "List of applicable moves: ";
    //     if(verifyLeft()) {
    //         thing = thing + "l";
    //     }
    //     if(verifyRight()) {
    //         thing = thing + "r";
    //     }
    //     if(verifyDown()) {
    //         thing = thing + "d";
    //     }
    //     if(verifyUp()) {
    //         thing = thing + "u";
    //     }
    //     return thing;
    // }

    //verifyers will return true if chosen direction to move in is valid, else return false
    private boolean verifyLeft() {
        int[] playerCoords = parent.getPlayerCoords();
        int nRow = playerCoords[0];
        int nCol = playerCoords[1];

        if(nCol == 0) { //out of bounds
            return false;
        }

        if(parent.getMapData()[nRow][nCol-1] == '#') { //in case of wall
            return false;
        } else if (parent.getItemsData()[nRow][nCol-1] == ' ') { //empty space
            return true;
        } else if (parent.getItemsData()[nRow][nCol-1] == '$' && nCol > 1) { // check if crate is movable
            if(parent.getItemsData()[nRow][nCol-2] == '$' || parent.getMapData()[nRow][nCol-2] == '#') {
                return false;
            } else {
                return true;
            }
        } else { //case for out of bounds pushing crate
            return false;
        }
    }
    
    private boolean verifyRight() {
        int[] playerCoords = parent.getPlayerCoords();
        int nRow = playerCoords[0];
        int nCol = playerCoords[1];

        if(nCol == parent.getWidth() - 1) { //out of bounds
            return false;
        }

        if(parent.getMapData()[nRow][nCol+1] == '#') { //in case of wall
            return false;
        } else if (parent.getItemsData()[nRow][nCol+1] == ' ') { //empty space
            return true;
        } else if (parent.getItemsData()[nRow][nCol+1] == '$' && nCol < parent.getWidth() - 2) { // check if crate is movable
            if(parent.getItemsData()[nRow][nCol+2] == '$' || parent.getMapData()[nRow][nCol+2] == '#') {
                return false;
            } else {
                return true;
            }
        } else { //case for out of bounds pushing crate
            return false;
        }
    }

    private boolean verifyUp() {
        int[] playerCoords = parent.getPlayerCoords();
        int nRow = playerCoords[0];
        int nCol = playerCoords[1];

        if(nRow == 0) { //out of bounds
            return false;
        }

        if(parent.getMapData()[nRow-1][nCol] == '#') { //in case of wall
            return false;
        } else if (parent.getItemsData()[nRow-1][nCol] == ' ') { //empty space
            return true;
        } else if (parent.getItemsData()[nRow-1][nCol] == '$' && nRow > 1) { // check if crate is movable
            if(parent.getItemsData()[nRow-2][nCol] == '$' || parent.getMapData()[nRow-2][nCol] == '#') {
                return false;
            } else {
                return true;
            }
        } else { //case for out of bounds pushing crate
            return false;
        }
    }

    private boolean verifyDown() {
        int[] playerCoords = parent.getPlayerCoords();
        int nRow = playerCoords[0];
        int nCol = playerCoords[1];

        if(nRow == parent.getHeight() - 1) { //out of bounds
            return false;
        }

        if(parent.getMapData()[nRow+1][nCol] == '#') { //in case of wall
            return false;
        } else if (parent.getItemsData()[nRow+1][nCol] == ' ') { //empty space
            return true;
        } else if (parent.getItemsData()[nRow+1][nCol] == '$' && nRow < parent.getHeight() - 2) { // check if crate is movable
            if(parent.getItemsData()[nRow+2][nCol] == '$' || parent.getMapData()[nRow+2][nCol] == '#') {
                return false;
            } else {
                return true;
            }
        } else { //case for out of bounds pushing crate
            return false;
        }
    }
}
