package menus;

import graphics.GraphicsValues;

import java.nio.file.*;

public class AttackMapItem extends Submenu
{
    private Path filePath;

    public AttackMapItem(ParentMenu parent, Path filePath)
    {
        super(Id.ATTACK_LOAD_MAP_ITEM, filePath.getFileName().toString().replaceFirst("\\.[^.]+$", ""), parent);
        this.filePath = filePath;
        insertItem(Menu.Id.ATTACK_MAP_INFO, "Map Info", GraphicsValues.IconPaths.Info);
        insertItem(Menu.Id.ATTACK_MAP_ATTACK, "Attack Map", GraphicsValues.IconPaths.Axes);
        clickable = true;
        setIconPath(GraphicsValues.IconPaths.Map);
    }

    public Path getFilePath()
    {
        return filePath;
    }
}
