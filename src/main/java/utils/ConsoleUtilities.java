package utils;

import java.util.*;
import java.util.regex.*;

public class ConsoleUtilities
{
    public static Matcher getMatchedCommand(String pattern, String text)
    {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        if (m.find())
            return m;
        else
            return null;
    }
}
