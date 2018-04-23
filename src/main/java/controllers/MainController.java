package controllers;

import com.google.gson.*;
import exceptions.*;
import models.*;
import models.buildings.*;
import utils.*;
import views.ConsoleView;
import views.VillageView;

import java.io.*;
import java.nio.file.*;
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
            catch (ConsoleException ex)
            {
                theView.showError(ex);
            }
        }
    }

    private ICommandManager childCommandManager = null;

    public void manageCommand(String command) throws ConsoleException
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
            else if ((m = ConsoleUtilities.getMatchedCommand("load\\s+(\\S+)", command)) != null)
            {
                openGame(Paths.get(m.group(1)));
                enterGame();
            }
            else if ((m = ConsoleUtilities.getMatchedCommand("save\\s+(\\S+)\\s+(\\S+)", command)) != null)
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

    private void openGame(Path path) throws MyJsonException, MyIOException
    {
        try (BufferedReader reader = Files.newBufferedReader(path))
        {
            World.openGame(reader);
        }
        catch (JsonSyntaxException | JsonIOException ex)
        {
            throw new MyJsonException(ex);
        }
        catch (NoSuchFileException ex)
        {
            throw new MyIOException("File not found.", ex);
        }
        catch (IOException ex)
        {
            throw new MyIOException(ex);
        }
    }

    private void saveGame(Path path) throws MyJsonException, MyIOException
    {
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))
        {
            World.saveGame(writer);
        }
        catch (JsonIOException ex)
        {
            throw new MyJsonException(ex);
        }
        catch (NoSuchFileException ex)
        {
            throw new MyIOException("File not found.", ex);
        }
        catch (IOException ex)
        {
            throw new MyIOException(ex);
        }
    }
}
