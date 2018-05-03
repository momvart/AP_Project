package views;

import exceptions.ConsoleException;
import exceptions.ConsoleRuntimeException;

import java.util.Scanner;

public class ConsoleView
{
    protected Scanner scanner;

    public ConsoleView(Scanner scanner)
    {
        this.scanner = scanner;
    }

    public Scanner getScanner()
    {
        return scanner;
    }

    public String getCommand()
    {
        if (!scanner.hasNextLine())
            System.exit(10);
        String command = "";
        while (command.isEmpty())
            command = scanner.nextLine().trim();
        return command;
    }

    public void showText(String text)
    {
        System.out.println(text);
    }

    public void showError(ConsoleException ex)
    {
        System.out.println(ex.getMessage());
        System.err.println(ex.getDatailedMessage());
    }

    public void showError(ConsoleRuntimeException ex)
    {
        System.out.println(ex.getMessage());
        System.err.println(ex.getDatailedMessage());
    }
}
