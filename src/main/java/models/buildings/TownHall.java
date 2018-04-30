package models.buildings;

import exceptions.BuilderNotFoundException;
import exceptions.NoAvailableBuilderException;
import menus.*;
import models.Builder;
import models.BuilderStatus;
import models.World;
import utils.Point;

import java.util.ArrayList;

public class TownHall extends VillageBuilding
{
    ArrayList<Builder> builders = new ArrayList<>();

    public TownHall(Point location, int buildingNum)
    {
        super(location, buildingNum);
        Builder builder = new Builder(1);
        builders.add(builder);
    }

    public void passTurn()
    {
        addBuilder();
    }

    public static final int BUILDING_TYPE = 5;

    @Override
    public int getType()
    {
        return BUILDING_TYPE;
    }

    @Override
    public void upgrade()
    {
        super.upgrade();
        addBuilder();
    }

    public Builder getAvailableBuilder() throws NoAvailableBuilderException
    {
        return builders.stream()
                .filter(builder -> builder.getBuilderStatus() == BuilderStatus.FREE)
                .findFirst()
                .orElseThrow(NoAvailableBuilderException::new);
    }

    public Builder getBuilderByNum(int num)
    {
        return builders.stream()
                .filter(builder -> builder.getBuilderNum() == num)
                .findFirst()
                .orElseThrow(() -> new BuilderNotFoundException(num));
    }

    private void addBuilder()
    {
        if (builders.size() < this.level / 5 + 1)
        {
            Builder builder = new Builder(builders.size() + 1);
            builders.add(builder);
        }
    }

    @Override
    public BuildingSubmenu getMenu(ParentMenu parent)
    {
        BuildingSubmenu menu = super.getMenu(parent);
        menu.insertItem(new AvailableBuildingsSubmenu(menu, World.sCurrentGame.getVillage()))
                .insertItem(new Menu(Menu.Id.TH_STATUS, "Status"));
        return menu;
    }
}
