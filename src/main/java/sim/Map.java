package sim;

import ai.AStar;
import core.MapReader;
import core.Node;
import core.Position;
import entity.Exit;
import entity.Wall;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Class responsible for creating and handling map of simulation.
 */
public class Map {
    private final int tileSize = 10;
    private final int colNum = 100;
    private final int rowNum = 100;
    private final int mapWidth = tileSize * colNum;
    private final int mapHeight = tileSize * rowNum;
    private final ArrayList<ArrayList<Node>> nodeList;
    private ArrayList<Wall> walls;
    private ArrayList<Exit> exits;
    private final MapReader mapReader = new MapReader(colNum,rowNum);
    private AStar aStar;
    private Position exitPositionLeft, exitPositionRight;

    /**
     * @param sim
     * Constructor of Map object.
     * Creating Node object for each tile of map.
     * Reading map data from .txt file.
     */
    public Map(Sim sim){
        nodeList = new ArrayList<>();

        for(int row = 0; row < rowNum; row++){
            ArrayList<Node> nodeRow = new ArrayList<>();

            for (int col = 0; col < colNum; col++){
                Node node = new Node(tileSize,tileSize);
                node.setPosition(col, row);
                nodeRow.add(node);
            }
            nodeList.add(nodeRow);
        }

        mapReader.readFromTxt("/map/map100x100.txt");
        aStar = new AStar(this);
        createMap(sim);
    }

    /**
     * @param sim
     * Method that setting walls and exits in correct places.
     * At end invoke pathfinder to calculate the shortest paths from each tile to exit.
     */
    public void createMap(Sim sim){
        int[][] mapData = mapReader.getTiles();
        ArrayList<Position> wallsPosition = new ArrayList<>();
        ArrayList<Position> exitPositionLeft = new ArrayList<>();
        ArrayList<Position> exitPositionRight = new ArrayList<>();

        walls = new ArrayList<>();
        exits = new ArrayList<>();

        for(int row = 0; row < rowNum; row++){
            for(int col = 0; col < colNum; col++){
                if (mapData[row][col] == 1){
                    wallsPosition.add(new Position(col * tileSize, row * tileSize));
                }
                if (mapData[row][col] == 2) {
                    exitPositionLeft.add(new Position(col * tileSize, row * tileSize));
                }
                if (mapData[row][col] == 3) {
                    exitPositionRight.add(new Position(col * tileSize, row * tileSize));
                }
            }
        }

        for(ArrayList<Node> nodeRow : nodeList){
            for (Node node : nodeRow) {
                for (Position position : wallsPosition) {
                    int x = position.intX();
                    int y = position.intY();

                    if (node.getPosition().intX() == x && node.getPosition().intY() == y) {
                        node.setTraversable(false);
                        Wall wall = new Wall();
                        wall.setPosition(position);
                        walls.add(wall);
                        break;
                    }
                }
                for (Position position : exitPositionLeft) {
                    int x = position.intX();
                    int y = position.intY();

                    if (node.getPosition().intX() == x && node.getPosition().intY() == y) {
                        node.setTarget(true);
                        this.exitPositionLeft = position;
                        Exit exit = new Exit();
                        exit.setPosition(position);
                        exits.add(exit);
                    }
                }

                for (Position position : exitPositionRight) {
                    int x = position.intX();
                    int y = position.intY();

                    if (node.getPosition().intX() == x && node.getPosition().intY() == y) {
                        node.setTarget(true);
                        this.exitPositionRight = position;
                        Exit exit = new Exit();
                        exit.setPosition(position);
                        exits.add(exit);
                    }
                }
            }
        }
        aStar.findPathFromLeftExit(this, this.exitPositionLeft);
        aStar.findPathFromRightExit(this, this.exitPositionRight);
    }

    public List<Position> findPathAS(Position start, Position target) {
        aStar.findPath(start,target);
        return aStar.getPath();
    }
    public Position getRandomPosition() {
        while (true) {
            int row = (int)(Math.random() * rowNum);
            int col = (int)(Math.random() * colNum);

            if(row < rowNum && col < colNum){
                Node node = nodeList.get(row).get(col);
                if(node.isTraversable()){
                    return node.getPosition();
                }
            }
        }
    }

    public void resetNodes() {
        for (ArrayList<Node> nodeRow : nodeList) {
            for (Node node : nodeRow) node.resetNode();
        }
        aStar = new AStar(this);
    }

    public Node getNode(Position position) {
        int row = position.getRow();
        int col = position.getCol();
        return nodeList.get(row).get(col);
    }

    public ArrayList<ArrayList<Node>> getNodeList() {
        return nodeList;
    }
    public ArrayList<Wall> getWalls() {
        return walls;
    }

    public int getColNum() {
        return colNum;
    }

    public int getRowNum() {
        return rowNum;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getTileSize() {
        return tileSize;
    }

    public ArrayList<Exit> getExits() {
        return exits;
    }
}
