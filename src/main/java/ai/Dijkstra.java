package ai;

import core.Node;
import core.Position;
import sim.Map;

import java.util.ArrayList;

public class Dijkstra {
    ArrayList<Node> OPEN = new ArrayList<>();
    ArrayList<Node> CLOSED = new ArrayList<>();
    ArrayList<Node> TEMP = new ArrayList<>();
    ArrayList<ArrayList<Node>> nodeList;
    Node targetNode;
    ArrayList<Position> path = new ArrayList<>();
    final private int colNum;
    final private int rowNum;
    final private int nodeSize;
    public Dijkstra(Map map){
        this.colNum = map.getColNum();
        this.rowNum = map.getRowNum();
        this.nodeList = map.getNodeList();
        nodeSize = map.getTileSize();
    }

    public ArrayList<Position> findPath(Position start, Position target){

        int x = start.intX();
        int y = start.intY();
        for( ArrayList<Node> nodeRow : nodeList) {
            for (Node node : nodeRow) {
                if (x >= node.getPosition().intX() &&
                        x < (node.getPosition().intX() + nodeSize) &&
                        y >= node.getPosition().intY() &&
                        y < (node.getPosition().getY() + nodeSize)
                ) {
                    OPEN.add(node);
                }
            }
        }
        x = target.intX();
        y = target.intY();
        for( ArrayList<Node> nodeRow : nodeList) {
            for (Node node : nodeRow) {
                if (x >= node.getPosition().intX() &&
                        x < (node.getPosition().intX() + nodeSize) &&
                        y >= node.getPosition().intY() &&
                        y < (node.getPosition().getY() + nodeSize)
                ) {
                    node.setTarget(true);
                    targetNode = node;
                }
            }
        }

        boolean targetFound = false;
        while(!targetFound) {
            for (Node open : OPEN) {
                ArrayList<Node> nodeNeighbours = neighbours(open);
                for (Node neighbour : nodeNeighbours) {
                    if (!OPEN.contains(neighbour) && !CLOSED.contains(neighbour) && !TEMP.contains(neighbour)) {
                        TEMP.add(neighbour);
                        neighbour.setParent(open);
                    }
                }
                CLOSED.add(open);
            }
            OPEN = new ArrayList<>();
            OPEN.addAll(TEMP);
            TEMP = new ArrayList<>();

            for (Node open : OPEN) {
                if (open.isTarget()) {
                    targetFound = true;
                    break;
                }
            }
        }
        analyzePath();
        return path;
    }
    public void analyzePath(){
        Node node = targetNode;
        do {

            path.add(node.getPosition());
            node = node.getParent();

        }while(node.getParent() != null);
    }
    public boolean isInBands(int num, int bands){
        return num >= 0 && num <= (bands - 1);
    }
    public ArrayList<Node> neighbours(Node node){
        ArrayList<Node> neighbours = new ArrayList<>();

        for(int dy = -1; dy<=1; dy++){
            int row = node.getRow()+dy;

            if (isInBands(row,rowNum)){
                for (int dx = -1; dx<=1; dx++){
                    int col = node.getCol()+dx;
                    if (isInBands(col,colNum)){
                        if (nodeList.get(row).get(col).isTraversable()){
                            neighbours.add(nodeList.get(row).get(col));
                        }
                    }
                }
            }
        }
        return neighbours;
    }

    public void setTargetNode(Map map) {
        this.targetNode.setTarget(false);
        map.setRandomTarget();
    }
}

