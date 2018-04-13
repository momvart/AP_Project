package views;

import exceptions.ConsoleException;

import java.util.Scanner;

public abstract class ConsoleView
{
    protected Scanner scanner;

    public ConsoleView(Scanner scanner)
    {
        this.scanner = scanner;
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

    public void showError(ConsoleException ex)
    {
        System.out.println(ex.getMessage());
        System.err.println(ex.getDatailedMessage());
    }
}
