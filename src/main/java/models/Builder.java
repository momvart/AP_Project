package models;

public class Builder
{
    int builderNum;
    BuilderStatus builderStatus;

    public Builder(int builderNum)
    {
        this.builderNum = builderNum;
        this.builderStatus = BuilderStatus.FREE;
    }

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
