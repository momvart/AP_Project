package Menus;

public class Menu
{
    private int id;
    private String text;
    private String command;

    public Menu(int id, String text, String command)
    {
        this.id = id;
        this.text = text;
        this.command = command;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public String getCommand()
    {
        return command;
    }

    public void setCommand(String command)
    {
        this.command = command;
    }
}
