package menus;

public class Menu
{
    public static final String sItemNamePattern = "%d. %s";

    private int id;
    private String text;
    protected boolean clickable = true;

    public Menu(int id, String text)
    {
        this.id = id;
        this.text = text;
    }

    public Menu(int id, String text, boolean clickable)
    {
        this.id = id;
        this.text = text;
        this.clickable = clickable;
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

    public boolean isClickable()
    {
        return clickable;
    }

    public static class Id
    {
        public static final int VILLAGE_MAIN_MENU = 0x0000;
        public static final int VILLAGE_SHOW_BUILDINGS = 0x0100;
        public static final int VILLAGE_RESOURCES = 0x0001;

        public static final int BUILDING_MENU = 0x1000;

        public static final int BUILDING_INFO = 0x1010;
        public static final int OVERALL_INFO = 0x1011;
        public static final int UPGRADE_INFO = 0x1012;
        public static final int UPGRADE_COMMAND = 0x1013;

        public static final int TH_AVAILABLE_BUILDINGS = 0x1110;
        public static final int TH_AVAILABLE_BUILDING_ITEM = 0x1111;
        public static final int TH_STATUS = 0x1120;

        public static final int BARRACKS_TRAIN_SOLDIER = 0x1210;
        public static final int BARRACKS_TRAIN_ITEM = 0x1211;
        public static final int BARRACKS_STATUS = 0x1220;

        public static final int CAMP_CAPACITY_INFO = 0x1311;
        public static final int CAMP_SOLDIERS = 0x1320;

        public static final int STORAGE_SRC_INFO = 0x1411;

        public static final int DEFENSIVE_TARGET_INFO = 0x1511;

        public static final int MINE_MINE = 0x1620;

        public static final int ATTACK_MAIN_MENU = 0x5000;
        public static final int ATTACK_LOAD_MAP = 0x5001;
        public static final int ATTACK_LOAD_MAP_ITEM = 0x5002;
        public static final int ATTACK_MAIN_BACK = 0x5003;

        public static final int ATTACK_MAP_INFO = 0x5011;
        public static final int ATTACK_MAP_ATTACK = 0x5012;

    }
}
