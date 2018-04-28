package exceptions;

import com.google.gson.JsonParseException;

public class MyJsonException extends ConsoleException
{
    public MyJsonException(String message, JsonParseException cause)
    {
        super(message, "Problem with parsing json: " + cause, cause);
    }

    public MyJsonException(JsonParseException cause)
    {
        this("Couldn't parse json.", cause);
    }
}
