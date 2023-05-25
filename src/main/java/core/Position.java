package core;

public class Position {
    public static int PROXIMITY_RANGE = 5;
    private double x;
    private double y;

    public Position(double x, double y){
        this.x = x;
        this.y = y;
    }
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Position copyOf(Position position) {
        return new Position(position.intX(),position.intY());
    }

    public void apply(Motion motion) {
        Vector2D vector = motion.getVector();
        if (vector != null) {
            x += vector.getX();
            y += vector.getY();
        }
    }
    public double distanceTo(Position other) {
        double deltaX = this.getX() - other.getX();
        double deltaY = this.getY() - other.getY();

        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
    public boolean isInRangeOf(Position position) {
        return this.distanceTo(position) < PROXIMITY_RANGE;
    }

    public int intX() {
        return (int) Math.round(x);
    }
    public int intY() {
        return (int) Math.round(y);
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public int getRow() {
        return (int) y / 10;
    }
    public int getCol() {
        return (int) x / 10;
    }

    public void applyX(Motion motion) {
        x += motion.getVector().getX();
    }
    public void applyY(Motion motion){
        y += motion.getVector().getY();
    }

    public void setX(int x) {
        this.x = x;
    }
}
