package utils;

import java.util.Objects;

public class Node
{
    private Point point;
    private int h;
    private int g;
    private int f;
    private Node parent;

    public Node(Point point, Node parent, int g, int h)
    {
        this.point = point;
        this.parent = parent;
        this.h = h;
        this.g = g;
        f = g + h;
    }

    public Point getPoint()
    {
        return point;
    }

    public void setPoint(Point point)
    {
        this.point = point;
    }

    public int getH()
    {
        return h;
    }

    public void setH(int h)
    {
        this.h = h;
    }

    public int getG()
    {
        return g;
    }

    public void setG(int g)
    {
        this.g = g;
        f = g + h;
    }

    public int getF()
    {
        return f;
    }

    public void setF(int f)
    {
        this.f = f;
    }

    public Node getParent()
    {
        return parent;
    }

    public void setParent(Node parent)
    {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node)o;
        return Objects.equals(point, node.point);
    }
}
