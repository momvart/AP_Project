package utils;

import exceptions.ConsoleException;
import exceptions.ConsoleRuntimeException;

public interface ICommandManager
{

    void manageCommand(String command) throws ConsoleException, ConsoleRuntimeException;
}
