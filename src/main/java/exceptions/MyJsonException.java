package exceptions;

import com.google.gson.JsonParseException;

public class MyJsonException extends ConsoleException
{
    public MyJsonException(JsonParseException cause)
    {
        super("Couldn't parse json.", "Problem with parsing json: " + cause, cause);
    }
}
