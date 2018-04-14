package views.dialogs;

import exceptions.InvalidCommandException;

import java.util.Scanner;

public class TextInputDialog extends ConsoleDialog
{
    public static final String KEY_TEXT = "text";

    private String pattern;

    public TextInputDialog(Scanner scanner, String message)
    {
        this(scanner, message, "");
    }

    public TextInputDialog(Scanner scanner, String message, String pattern)
    {
        super(scanner, message);
        this.pattern = pattern;
    }

    @Override
    public DialogResult showDialog()
    {
        showMessage();
        if (pattern.isEmpty())
            return new DialogResult(DialogResultCode.YES).addData(KEY_TEXT, getCommand());
        else
            while (true)
            {
                String command = getCommand();
                try
                {
                    if (command.matches(pattern))
                        return new DialogResult(DialogResultCode.YES).addData(KEY_TEXT, command);
                    else
                        throw new InvalidCommandException(command);
                }
                catch (InvalidCommandException ex)
                {
                    showError(ex);
                }
            }
    }

}
