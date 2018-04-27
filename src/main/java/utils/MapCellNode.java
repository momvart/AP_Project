package utils;

import java.util.Objects;

public class MapCellNode
{
    private Point point;
    private double distEnd;
    private double distStart = Double.MAX_VALUE;
    private MapCellNode parent;
    private boolean visited = false;

    public MapCellNode(Point point, MapCellNode parent, double distEnd)
    {
        this.point = point;
        this.parent = parent;
        this.distEnd = distEnd;
    }

    public Point getPoint()
    {
        return point;
    }

    public int getX() { return point.getX(); }

    public int getY() { return point.getY(); }

    public double getDistEnd()
    {
        return distEnd;
    }

    public void setDistEnd(double distEnd)
    {
        this.distEnd = distEnd;
    }

    public double getDistStart()
    {
        return distStart;
    }

    public void setDistStart(double distStart)
    {
        this.distStart = distStart;
    }

    public double getF()
    {
        return distStart + distEnd;
    }

    public MapCellNode getParent()
    {
        return parent;
    }

    public void setParent(MapCellNode parent)
    {
        this.parent = parent;
    }

    public boolean isVisited()
    {
        return visited;
    }

    public void setVisited(boolean visited)
    {
        this.visited = visited;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapCellNode node = (MapCellNode)o;
        return Objects.equals(point, node.point);
    }

    @Override
    public String toString()
    {
        if (parent != null)
            return point.toString() + "->" + parent.point.toString();
        else
            return point.toString();
    }
}
