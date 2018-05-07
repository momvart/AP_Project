package models.buildings;

public enum DefenseType
{
    GROUND("Ground"),
    AIR("Air"),
    BOTH("Ground and Air");

    private String printName;

    public String getPrintName()
    {
        return printName;
    }

    DefenseType(String printName)
    {
        this.printName = printName;
    }
}
