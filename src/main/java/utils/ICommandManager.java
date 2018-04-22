package utils;

import exceptions.InvalidCommandException;

public interface ICommandManager
{

    void manageCommand(String command) throws InvalidCommandException;
}
