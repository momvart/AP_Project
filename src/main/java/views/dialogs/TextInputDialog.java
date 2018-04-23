package views.dialogs;

import exceptions.InvalidCommandException;
import utils.ConsoleUtilities;

import java.util.Scanner;
import java.util.regex.Matcher;

public class TextInputDialog extends ConsoleDialog
{
    public static final String KEY_TEXT = "text";
    public static final String KEY_MATCHER = "matcher";

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

    /**
     * @return returns a dialog result with a matcher as data if pattern is defined, otherwise the command will be included in the result.
     */
    @Override
    public DialogResult showDialog()
    {
        showMessage();
        while (true)
        {
            String command = getCommand();
            if (command.equalsIgnoreCase("back"))
                return new DialogResult(DialogResultCode.CANCEL);
            Matcher matched = null;
            if (!pattern.isEmpty())
                matched = ConsoleUtilities.getMatchedCommand(pattern, command);
            try
            {
                if (matched != null)
                    return new DialogResult(DialogResultCode.YES).addData(KEY_TEXT, matched.group(0)).addData(KEY_MATCHER, matched);
                else if (pattern.isEmpty())
                    return new DialogResult(DialogResultCode.YES).addData(KEY_TEXT, command);
                else
                    throw new InvalidCommandException(command, "Input doesn't match pattern.");
            }
            catch (InvalidCommandException ex)
            {
                showError(ex);
            }
        }
    }

}
