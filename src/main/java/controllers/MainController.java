package controllers;

import exceptions.InvalidCommandException;
import models.World;
import utils.ConsoleUtilities;
import utils.ICommandManager;
import views.ConsoleView;
import views.VillageView;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;

public class MainController implements ICommandManager
{
    private ConsoleView theView;

    public MainController(ConsoleView theView)
    {
        this.theView = theView;
    }

    public void start()
    {
        while (true)
        {
            try
            {
                manageCommand(theView.getCommand());
            }
            catch (InvalidCommandException ex)
            {
                theView.showError(ex);
            }
        }
    }

    private ICommandManager childCommandManager = null;

    public void manageCommand(String command) throws InvalidCommandException
    {
        try
        {
            if (childCommandManager != null)
                childCommandManager.manageCommand(command);
            else
                throw new InvalidCommandException(command);
        }
        catch (InvalidCommandException ex)
        {
            Matcher m;
            if (command.equals("newGame"))
            {
                World.newGame();
                enterGame();
            }
            else if ((m = ConsoleUtilities.getMatchedCommand("load\\s+(\\w+)", command)) != null)
            {
                String path = m.group(1);
                //TODO: parsing json
            }
            else if ((m = ConsoleUtilities.getMatchedCommand("save\\s+(\\w+)\\s+(\\w+)", command)) != null)
            {
                saveGame(Paths.get(m.group(1), m.group(2)));
            }
            else
                throw new InvalidCommandException(command);
        }
    }

    private void enterGame()
    {
        childCommandManager = new VillageController(new VillageView(theView.getScanner()));
    }

    private void saveGame(Path path)
    {

    }
}
