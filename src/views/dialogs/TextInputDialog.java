package views.dialogs;

import java.util.Scanner;

public class TextInputDialog extends ConsoleDialog
{
    public static final String KEY_TEXT = "text";

    public TextInputDialog(Scanner scanner, String message)
    {
        super(scanner, message);
    }

    @Override
    public DialogResult showDialog()
    {
        showMessage();
        return new DialogResult(DialogResultCode.YES).addData(KEY_TEXT, getCommand());
    }


}
