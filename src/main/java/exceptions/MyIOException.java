package exceptions;

import java.io.*;

public class MyIOException extends ConsoleException
{
    public MyIOException(String message, Throwable cause)
    {
        super(message, "Problem in IO operation: " + cause, cause);
    }

    public MyIOException(IOException cause)
    {
        super("Couldn't do the IO job.", "Problem in IO operation: " + cause, cause);
    }
}
