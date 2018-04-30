package exceptions;

public class BuilderNotFoundException extends ConsoleRuntimeException
{
    public BuilderNotFoundException(int builderNum)
    {
        super("Builder not found.", "Can't find builder with num: " + builderNum);
    }
}
