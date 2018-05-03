package exceptions;

import utils.Point;

public class FilledCellException extends ConsoleRuntimeException
{
    public FilledCellException(Point location)
    {
        this(location, "");
    }

    public FilledCellException(Point location, String message)
    {
        super("Cell doesn't have enough space", "Cell doesn't have enough space: " + location.toString() + "\t" + message);
    }
}
