package exceptions;

import java.io.*;

public class MyIOException extends ConsoleException
{
    public MyIOException(String message, IOException cause)
    {
        super(message, "Problem in IO operation: " + cause, cause);
    }

    public MyIOException(IOException cause)
    {
        this("Couldn't do the IO job.", cause);
    }
}
