package ru.meow.expertsystemshell.model;

public class Statement
{
    private Variable leftPart;
    private String rightPart;
    
    public Variable getLeftPart ()
    {
        return leftPart;
    }
    
    public void setLeftPart (Variable leftPart)
    {
        this.leftPart = leftPart;
    }
    
    public String getRightPart ()
    {
        return rightPart;
    }
    
    public void setRightPart (String rightPart)
    {
        this.rightPart = rightPart;
    }
    
    public Statement (Variable leftPart, String rightPart)
    {
        this.leftPart = leftPart;
        this.rightPart = rightPart;
    }
    
    @Override
    public String toString ()
    {
        return leftPart.toString() + " = " + rightPart;
    }
}
