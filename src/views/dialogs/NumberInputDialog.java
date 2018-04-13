package views.dialogs;

import exceptions.InvalidCommandException;

import java.util.Scanner;

public class NumberInputDialog extends ConsoleDialog
{
    public static final String KEY_NUMBER = "number";

    public NumberInputDialog(Scanner scanner, String message)
    {
        super(scanner, message);
    }

    @Override
    public DialogResult showDialog()
    {
        showMessage();
        while (true)
        {
            String command = getCommand();
            try
            {
                return new DialogResult(DialogResultCode.YES).addData(KEY_NUMBER, Integer.parseInt(command));
            }
            catch (NumberFormatException ex)
            {
                showError(new InvalidCommandException(command));
            }
        }
    }
}
