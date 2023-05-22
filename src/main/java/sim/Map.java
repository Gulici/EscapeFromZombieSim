package sim;

import ai.AStar;
import ai.Dijkstra;
import core.MapReader;
import core.Node;
import core.Position;
import entity.Entity;
import entity.Exit;
import entity.Wall;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    private Dijkstra pathfinder;
    private AStar aStar;
    private Position targetPosition;

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
        createMap(sim);
        pathfinder = new Dijkstra(this);
        aStar = new AStar(this);
    }

    public void createMap(Sim sim){
        int[][] mapData = mapReader.getTiles();
        ArrayList<Position> wallsPosition = new ArrayList<>();
        ArrayList<Position> exitPosition = new ArrayList<>();
        walls = new ArrayList<>();
        exits = new ArrayList<>();

        for(int row = 0; row < rowNum; row++){
            for(int col = 0; col < colNum; col++){
                if (mapData[row][col] == 1){
                    wallsPosition.add(new Position(col * tileSize, row * tileSize));
                }
                if (mapData[row][col] ==2) {
                    exitPosition.add(new Position(col * tileSize, row * tileSize));
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
                    }
                }
                for (Position position : exitPosition) {
                    int x = position.intX();
                    int y = position.intY();

                    if (node.getPosition().intX() == x && node.getPosition().intY() == y) {
                        node.setTarget(true);
                        targetPosition = position;
                        Exit exit = new Exit();
                        exit.setPosition(position);
                        exits.add(exit);
                    }
                }
            }
        }
    }

    public List<Position> findPath(Position start, Position target) {
        return pathfinder.findPath(start, target);
    }

    public List<Position> findPathAS(Position start, Position target) {
        aStar.findPath(start,target);
        return aStar.getPath();
    }

    public void draw(Graphics2D graphics2D){
        nodeList.forEach(nodes -> nodes.forEach(node -> node.draw(graphics2D)));
    }

    public void setRandomTarget(){
        boolean found = false;

        while (!found){
            int row = (int)(Math.random() * rowNum);
            int col = (int)(Math.random() * colNum);

            if(row < rowNum && col < colNum){
                Node node = nodeList.get(row).get(col);
                if(node.isTraversable()){
                    node.setTarget(true);
                    setTargetPosition(node.getCenterPosition());
                    found = true;
                }
            }
        }
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

    public Position getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(Position targetPosition) {
        this.targetPosition = targetPosition;
    }

    public ArrayList<Exit> getExits() {
        return exits;
    }
}
