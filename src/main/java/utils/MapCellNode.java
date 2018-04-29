package utils;

import java.util.Objects;

public class MapCellNode
{
    private Point point;
    private int distEnd;
    private int distStart = Integer.MAX_VALUE;
    private MapCellNode parent;
    private boolean visited = false;

    public MapCellNode(Point point, MapCellNode parent, int distEnd)
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

    public int getDistEnd()
    {
        return distEnd;
    }

    public void setDistEnd(int distEnd)
    {
        this.distEnd = distEnd;
    }

    public int getDistStart()
    {
        return distStart;
    }

    public void setDistStart(int distStart)
    {
        this.distStart = distStart;
    }

    public int getF()
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
        MapCellNode that = (MapCellNode)o;
        return Objects.equals(point, that.point);
    }

    @Override
    public int hashCode()
    {

        return Objects.hash(point);
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
