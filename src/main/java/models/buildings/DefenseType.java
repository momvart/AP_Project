package models.buildings;

import models.soldiers.MoveType;

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

    public MoveType convertToMoveType()
    {
        switch (this)
        {
            case GROUND:
                return MoveType.GROUND;
            case AIR:
                return MoveType.AIR;
            case BOTH:
                return null;
        }
        return null;
    }
}
