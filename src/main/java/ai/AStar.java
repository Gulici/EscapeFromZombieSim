/*
1) Inicjalizacja listy Open
    Dodanie startowego Node do Open
2) Inicjalizacja listy Closed
3) while(!open.isEmpty())
    a) znaleźć node o najmniejszym f w Open,
    b) znaleźć sąsiednie node'y, przez które można się przemieszczać,
        ustawić dla nich wcześniejszego node jako parent
    c) dla każdego dziecka:
        - jeśli jest targetem, zakończyć algorytm,
        - w innym wypadku, obliczyć koszt g i h
            child.g = parent.g + koszt ruchu
            child.h = dystans między child a target,
            child.f = child.g + child.h.
        - pominąć ,jeśli istnieje node o takiej samej pozycji w Open
          jak obecnie analizowane dziecko i jego koszt f jest mniejszy,
        - pominąć, jeśli istnieje node o takiej samej pozycji w Closed
          i jego koszt jest mniejszy,
        - w innym wypadku ustawić analizowany node jako parent i dodać dziecko do Open
    d) przenieść analizowanego node'a do listy Closed

 */
package ai;

import core.Node;
import core.Position;
import sim.Map;

import java.util.ArrayList;
import java.util.List;

public class AStar {
    private List<Node> open;
    private List<Node> closed;
    private final ArrayList<ArrayList<Node>> nodeList;
    private ArrayList<Position> path;
    private final int nodeSize;
    private final int rowNum;
    private final int colNum;

    public AStar(Map map){
        this.nodeList = map.getNodeList();
        this.nodeSize = map.getTileSize();
        this.rowNum = map.getRowNum();
        this.colNum = map.getColNum();
    }
    public boolean findPath(Position start, Position target){
        path = new ArrayList<>();
        open = new ArrayList<>();
        closed = new ArrayList<>();
        Node startNode = findNodeByPosition(start);
        startNode.setGCost(0);
        startNode.setHCost(0);
        startNode.setFCost();
        open.add(startNode);

        Node targetNode = findNodeByPosition(target);
        Position targetPosition = targetNode.getCenterPosition();

        while (!open.isEmpty()) {
            Node current = null;
            for (Node node : open){
                if(current == null){
                    current = node;
                }
                else if (node.getFCost() < current.getFCost()) {
                    current = node;
                }
            }


            Position currentPosition = current.getCenterPosition();
            ArrayList<Node> neighbours = neighbourNodes(current);
            

            for (Node child : neighbours) {
                Position childPosition = child.getCenterPosition();
                double gCost = childPosition.distanceTo(currentPosition) + current.getGCost();
                double hCost = childPosition.distanceTo(targetPosition);
                double fCost = gCost + hCost;

                
                if (child == targetNode) {
                    
                    child.setParent(current);
                    analyzePath(targetNode);
                    clearNodes();
                    return true;
                }

                boolean isInAnyList = false;

                for (Node openNode : open){
                    
                    if (child.equals(openNode)){
                        isInAnyList = true;
                        if (openNode.getFCost() > fCost) {
                            open.remove(openNode);
                            addToOpen(child, gCost, hCost);
                            child.setParent(current);
                            isInAnyList = true;
                            break;
                        }
                    }
                }

                for (Node closedNode : closed) {
                    
                    if (child.equals(closedNode)){
                        isInAnyList = true;
                        if (closedNode.getFCost() > fCost) {
                            closed.remove(closedNode);
                            addToOpen(child,gCost,hCost);
                            child.setParent(current);
                            break;
                        }
                    }
                }

                if (!isInAnyList){
                    addToOpen(child,gCost,hCost);
                    child.setParent(current);

                }
            }

            open.remove(current);
            closed.add(current);
        }

        clearNodes();
        
        return false;
    }

    /*
    startujemy od wyjścia
    szukamy sąsiadów, ustawiamy child.parent na current;
    analizujemy ścieżkę:
        bierzemy parent.path i przypisujemy do niego pozycje child
        przypisujemy powiększony path do child
     */
    public void findPathFromRightExit(Map map, Position target) {
        Node start = map.getNode(target);
        start.setGCost(0);

        ArrayList<Position> rightPath = new ArrayList<>();
        rightPath.add(start.getCenterPosition());
        start.setPathToExitRight(rightPath);

        open = new ArrayList<>();
        closed = new ArrayList<>();
        open.add(start);

        while(!open.isEmpty()) {
            Node current = null;
            for (Node node : open){
                if(current == null){
                    current = node;
                }
                else if (node.getGCost() < current.getGCost()) {
                    current = node;
                }
            }

            ArrayList<Node> neighbours = neighbourNodes(current);
            Position currentPosition = current.getCenterPosition();

            for (Node child : neighbours) {
                Position childPosition = child.getCenterPosition();
                double gCost = current.getGCost() + childPosition.distanceTo(currentPosition);

                if(!open.contains(child) && !closed.contains(child)) {
                    open.add(child);

                    child.setParent(current);
                    child.setGCost(gCost);

                    ArrayList<Position> childPath = current.copyOfRightPath();
                    childPath.add(childPosition);
                    child.setPathToExitRight(childPath);
                }
                if (open.contains(child) && child.getGCost() > gCost) {
                    child.setParent(current);
                    child.setGCost(gCost);

                    ArrayList<Position> childPath = current.copyOfRightPath();
                    childPath.add(childPosition);
                    child.setPathToExitRight(childPath);
                }
                if (closed.contains(child) && child.getGCost() > gCost) {
                    closed.remove(child);
                    open.add(child);
                    ArrayList<Position> childPath = current.copyOfRightPath();
                    childPath.add(childPosition);
                    child.setPathToExitRight(childPath);
                }
            }
            closed.add(current);
            open.remove(current);
        }
    }

    public void findPathFromLeftExit(Map map, Position target) {
        Node start = map.getNode(target);
        start.setGCost(0);

        ArrayList<Position> leftPath = new ArrayList<>();
        leftPath.add(start.getCenterPosition());
        start.setPathToExitLeft(leftPath);

        open = new ArrayList<>();
        closed = new ArrayList<>();
        open.add(start);

        while(!open.isEmpty()) {
            Node current = null;
            for (Node node : open){
                if(current == null){
                    current = node;
                }
                else if (node.getGCost() < current.getGCost()) {
                    current = node;
                }
            }

            ArrayList<Node> neighbours = neighbourNodes(current);
            Position currentPosition = current.getCenterPosition();

            for (Node child : neighbours) {
                Position childPosition = child.getCenterPosition();
                double gCost = current.getGCost() + childPosition.distanceTo(currentPosition);

                if(!open.contains(child) && !closed.contains(child)) {
                    open.add(child);

                    child.setParent(current);
                    child.setGCost(gCost);

                    ArrayList<Position> childPath = current.copyOfLeftPath();
                    childPath.add(childPosition);
                    child.setPathToExitLeft(childPath);
                }
                if (open.contains(child) && child.getGCost() > gCost) {
                    child.setParent(current);
                    child.setGCost(gCost);

                    ArrayList<Position> childPath = current.copyOfLeftPath();
                    childPath.add(childPosition);
                    child.setPathToExitLeft(childPath);
                }
                if (closed.contains(child) && child.getGCost() > gCost) {
                    closed.remove(child);
                    open.add(child);
                    ArrayList<Position> childPath = current.copyOfLeftPath();
                    childPath.add(childPosition);
                    child.setPathToExitLeft(childPath);
                }
            }
            closed.add(current);
            open.remove(current);
        }
    }


    private void analyzePath(Node target){
        if(target.getParent() == null){
            path = null;
        }
        else {
            Node node = target;
            do {
                
                path.add(node.getCenterPosition());
                node = node.getParent();
            } while (node.getParent() != null && node != target);
        }
    }
    private void addToOpen(Node child, double gCost, double hCost){
        child.setGCost(gCost);
        child.setHCost(hCost);
        child.setFCost();
        open.add(child);
    }
    public Node findNodeByPosition(Position position){
        int x = position.intX();
        int y = position.intY();
        Node wantedNode = new Node(nodeSize);

        for(ArrayList<Node> nodeRow : nodeList){
            for(Node node : nodeRow){
                if (x >= node.getPosition().intX() &&
                        x < (node.getPosition().intX() + nodeSize) &&
                        y >= node.getPosition().intY() &&
                        y < (node.getPosition().getY() + nodeSize)) {
                    wantedNode = node;
                    break;
                }
            }
        }
        return wantedNode;
    }
    public Node findNodeByRowCol(int row, int col){
        for (ArrayList<Node> nodes : nodeList) {
            for (Node node : nodes) {
                if (node.getRow() == row && node.getCol() == col) {
                   return node;
                }
            }
        }
        return null;
    }
    public ArrayList<Node> neighbourNodes(Node node){
        ArrayList<Node> neighbourNodes = new ArrayList<>();

        for (int dRow = -1 ; dRow <= 1 ; dRow++) {
            for (int dCol = -1 ; dCol <= 1 ; dCol++) {
                int row = node.getRow() + dRow;
                int col = node.getCol() + dCol;
                if(isInBands(row, col, rowNum , colNum) && !(dCol == 0 && dRow == 0)){
                    Node neighbour = findNodeByRowCol(row, col);

                    if (neighbour.isTraversable()) neighbourNodes.add(neighbour);
                }
            }
        }

        return neighbourNodes;
    }
    public boolean isInBands(int row, int col, int rowNum, int colNum){
        return row >= 0 && row <= rowNum && col >= 0 && col < colNum;
    }
    public ArrayList<Position> getPath() {
        
        return path;
    }

    public void clearNodes(){
        for (ArrayList<Node> nodes : nodeList) {
            
            for (Node node : nodes) {
                node.setParent(null);
                node.setGCost(0);
                node.setHCost(0);
                node.setFCost();
            }
        }
    }
}
