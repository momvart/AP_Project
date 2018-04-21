package models.buildings;

import exceptions.NoAvailableBuilderException;
import menus.AvailableBuildingsSubmenu;
import menus.Menu;
import menus.ParentMenu;
import menus.Submenu;
import models.Builder;
import models.BuilderStatus;
import models.World;
import utils.Point;

import java.util.ArrayList;

public class TownHall extends VillageBuilding
{
    ArrayList<Builder> builders = new ArrayList<>();

    public TownHall(Point location)
    {
        super(location);
        Builder builder = new Builder(1);
        builders.add(builder);
    }


    @Override
    public int getType()
    {
        return 5;
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
        for (Builder builder : builders)
        {
            if (builder.getBuilderNum() == num)
                return builder;
        }
        // TODO: 4/13/18 : throw BuilderNotFound Exception
        return null;
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
    public Submenu getMenu(ParentMenu parent)
    {
        Submenu menu = super.getMenu(parent);
        menu.insertItem(new AvailableBuildingsSubmenu(menu, World.sCurrentGame.getVillage()))
                .insertItem(new Menu(Menu.Id.TH_STATUS, "Status"));
        return menu;
    }
}
