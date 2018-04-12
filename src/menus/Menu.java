package menus;

public class Menu
{
    public static final String sItemNamePattern = "%d. %s";

    private int id;
    private String text;
    private String command;

    public Menu(int id, String text)
    {
        this.id = id;
        this.text = text;
    }

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


    public static class Id
    {
        public static final int BUILDING_MENU = 1000;

        public static final int BUILDING_INFO = 1010;
        public static final int OVERALL_INFO = 1011;
        public static final int UPGRADE_INFO = 1012;

        public static final int TH_AVAILABLE_BUILDINGS = 1110;
        public static final int TH_AVAILABLE_BUILDING_ITEM = 1111;
        public static final int TH_STATUS = 1120;

        public static final int BARRACKS_TRAIN_SOLDIER = 1210;
        public static final int BARRACKS_TRAIN_ITEM = 1211;
    }
}
