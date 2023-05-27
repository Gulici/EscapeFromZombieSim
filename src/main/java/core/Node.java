package core;

import java.awt.*;
import java.util.ArrayList;

public class Node {
    private final Size size;
    private int col;
    private int row;
    private Position position = new Position(0,0);
    private boolean traversable = true;
    private boolean target = false;
    private Node parent;
    private double gCost;
    private double hCost;
    private double fCost;
    private ArrayList<Position> pathToExitLeft;
    private ArrayList<Position> pathToExitRight;


    public Node(int width, int height) {
        size = new Size(width, height);
    }
    public Node(int sideLength){
        size = new Size(sideLength,sideLength);
    }

    public Position getCenterPosition(){
        double x = position.getX() + (double) size.getWidth()/2;
        double y = position.getY() + (double) size.getHeight()/2;

        return new Position(x,y);
    }
    public void draw(Graphics2D g2){

//            if(target){
//                g2.setColor(Color.orange);
//                g2.fillRect(position.intX(), position.intY(), size.getWidth(), size.getHeight());
//            }
    }

    public void setPosition(int col, int row) {
        this.col = col;
        this.row = row;
        this.position = new Position(col * size.getWidth(), row * size.getHeight());
    }

    public void resetNode() {
        parent = null;
    }
    public void setTraversable(boolean traversable){
        this.traversable = traversable;
    }

    public boolean isTarget() {
        return target;
    }

    public void setTarget(boolean target) {
        this.target = target;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getParent() {
        return parent;
    }

    public double getGCost() {
        return gCost;
    }

    public void setGCost(double vCost) {
        this.gCost = vCost;
    }

    public double getHCost() {
        return hCost;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public void setHCost(double hCost) {
        this.hCost = hCost;
    }

    public double getFCost() {
        return fCost;
    }

    public void setFCost() {
        this.fCost = gCost + hCost;
    }

    public Position getPosition() {
        return position;
    }

    public boolean isTraversable() {
        return traversable;
    }

    public ArrayList<Position> copyOfLeftPath() {
        return new ArrayList<>(pathToExitLeft);
    }
    public ArrayList<Position> copyOfRightPath() {
        return new ArrayList<>(pathToExitRight);
    }

    public ArrayList<Position> getPathToExitLeft() {
        return pathToExitLeft;
    }

    public void setPathToExitLeft(ArrayList<Position> pathToExitLeft) {
        this.pathToExitLeft = pathToExitLeft;
    }

    public ArrayList<Position> getPathToExitRight() {
        return pathToExitRight;
    }

    public void setPathToExitRight(ArrayList<Position> pathToExitRight) {
        this.pathToExitRight = pathToExitRight;
    }
}
