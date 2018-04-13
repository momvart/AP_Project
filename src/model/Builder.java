package model;

public class Builder
{
    int builderNum;
    BuilderStatus builderStatus;

    public int getBuilderNum()
    {
        return builderNum;
    }

    public BuilderStatus getBuilderStatus()
    {
        return builderStatus;
    }

    public void setBuilderStatus(BuilderStatus builderStatus)
    {
        this.builderStatus = builderStatus;
    }
}
