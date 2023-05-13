package sim;

import ai.Dijskra;
import core.MapReader;
import core.Node;
import core.Position;
import entity.Wall;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Map {
    private final int tileSize = 16;
    private final int colNum = 40;
    private final int rowNum = 40;
    private final int mapWidth = tileSize * colNum;
    private final int mapHeight = tileSize * rowNum;
    private final ArrayList<ArrayList<Node>> nodeList;
    private ArrayList<Wall> walls;
    private final MapReader mapReader = new MapReader(colNum,rowNum);
    private Dijskra pathfinder;
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
        mapReader.readFromTxt("/map/map.txt");
        createWalls(sim);
        setRandomTarget();
        pathfinder = new Dijskra(this);
    }

    public void createWalls(Sim sim){
        int[][] mapData = mapReader.getTiles();
        ArrayList<Position> wallsPosition = new ArrayList<>();
        walls = new ArrayList<>();

        for(int row = 0; row < rowNum; row++){
            for(int col = 0; col < colNum; col++){
                if (mapData[row][col] == 1){
                    wallsPosition.add(new Position(col * tileSize, row * tileSize));
                }
            }
        }
        for(ArrayList<Node> nodeRow : nodeList){
            for (Node node : nodeRow) {
                for (Position position : wallsPosition) {
                    int x = position.intX();
                    int y = position.intY();

                    if (node.getPosition().intX() == x && node.getPosition().intY() == y) {
                        node.setWall();
                        node.setTraversable(false);
                        Wall wall = new Wall();
                        wall.setPosition(position);
                        walls.add(wall);
                    }
                }
            }
        }
    }

    public List<Position> findPath(Position start, Position target) {
        return pathfinder.findPath(start, target);
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
                    setTargetPosition(node.getPosition());
                    found = true;
                }
            }
        }
    }

    public ArrayList<Wall> getWalls() {
        return walls;
    }

    public ArrayList<ArrayList<Node>> getNodeList() {
        return nodeList;
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
}
