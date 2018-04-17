package controllers;

import exceptions.InvalidCommandException;
import models.World;
import utils.ConsoleUtilities;
import views.ConsoleView;
import views.VillageView;

import java.util.regex.Matcher;

public class MainController
{
    ConsoleView theView;

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

    private void manageCommand(String command) throws InvalidCommandException
    {
        Matcher m;
        if (command.equals("newGame"))
        {
            World.newGame();
            enterGame();
        }
        else if ((m = ConsoleUtilities.getMatchedCommand("load (%s)", command)) != null)
        {
            String path = m.group(0);
            //TODO: parsing json
        }
        else
            throw new InvalidCommandException(command);
    }

    private void enterGame()
    {
        new VillageController(this, new VillageView(theView.getScanner())).start();
    }
}
