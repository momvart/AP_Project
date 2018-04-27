package exceptions;

import utils.Point;

public class FilledCellException extends ConsoleRuntimeException
{
    public FilledCellException(Point location)
    {
        super("Cell doesn't any space.", "Cell doesn't any space: " + location.toString());
    }
}
