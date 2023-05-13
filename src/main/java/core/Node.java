package core;

import java.awt.*;

public class Node {
    private final Size size;
    private Position position = new Position(0,0);
    private boolean traversable = true;
    private boolean target = false;
    private Node parent;
    private double vCost;
    private double hCost;
    private double totalCost;
    private int col;
    private int row;

    public Node(int width, int height) {
        size = new Size(width, height);
    }
    public void draw(Graphics2D g2){
            g2.setColor(Color.darkGray);
            g2.drawRect(position.intX(), position.intY(), size.getWidth(), size.getHeight());

            if(target){
                g2.setColor(Color.orange);
                g2.fillRect(position.intX(), position.intY(), size.getWidth(), size.getHeight());
            }
//            if(!traversable){
//                g2.setColor(Color.red);
//                g2.fillRect(position.intX(), position.intY(), size.getWidth(), size.getHeight());
//            }
    }

    public void setPosition(int col, int row) {
        this.col = col;
        this.row = row;
        this.position = new Position(col * size.getWidth(), row * size.getHeight());
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

    public double getVCost() {
        return vCost;
    }

    public void setVCost(double vCost) {
        this.vCost = vCost;
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

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public void setWall() {
        setTraversable(false);
    }

    public Position getPosition() {
        return position;
    }

    public boolean isTraversable() {
        return traversable;
    }
}
