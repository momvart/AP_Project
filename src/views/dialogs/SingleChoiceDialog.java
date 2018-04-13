package views.dialogs;

import exceptions.ConsoleException;
import exceptions.InvalidCommandException;

import java.util.Scanner;

public class SingleChoiceDialog extends ConsoleDialog
{
    public SingleChoiceDialog(Scanner scanner, String message)
    {
        super(scanner, message);
    }

    @Override
    public DialogResult showDialog()
    {
        System.out.println(message + " [Y/N]");
        while (true)
        {
            try
            {
                String command = getCommand();
                switch (command.toLowerCase())
                {
                    case "y":
                        return new DialogResult(DialogResultCode.YES);
                    case "n":
                        return new DialogResult(DialogResultCode.NO);
                    default:
                        throw new InvalidCommandException(command);
                }
            }
            catch (ConsoleException ex)
            {
                showError(ex);
            }
        }
    }


}
