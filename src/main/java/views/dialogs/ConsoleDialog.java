package views.dialogs;

import views.ConsoleView;

import java.util.Scanner;

public abstract class ConsoleDialog extends ConsoleView
{
    protected String message;

    public ConsoleDialog(Scanner scanner, String message)
    {
        super(scanner);
        this.message = message;
    }

    public void showMessage()
    {
        System.out.println(message);
    }

    public abstract DialogResult showDialog();
}
