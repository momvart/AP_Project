package utils;

import exceptions.ConsoleException;

public interface ICommandManager
{

    void manageCommand(String command) throws ConsoleException;
}
