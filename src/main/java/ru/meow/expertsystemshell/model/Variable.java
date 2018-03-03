package ru.meow.expertsystemshell.model;

import java.util.ArrayList;

public class Variable
{
    public enum VariableType
    {
        DERIVABLE, REQUESTED, DERIVABLE_REQUESTED;
    
        @Override
        public String toString ()
        {
            switch (this)
            {
                case DERIVABLE:
                    return "Выводимая";
                case REQUESTED:
                    return "Запрашиваемая";
                case DERIVABLE_REQUESTED:
                    return "Выводимо-запрашиваемая";
            }
            return super.toString();
        }
    }
    
    private String name;
    private ArrayList<String> values = new ArrayList<>();
    private VariableType type;
    private String question;
    private String value = null;
    
    public String getValue ()
    {
        return value;
    }
    
    public void setValue (String value)
    {
        this.value = value;
    }
    
    public String getName ()
    {
        return name;
    }
    
    public String getQuestion ()
    {
        return question;
    }
    
    public ArrayList<String> getValues ()
    {
        return values;
    }
    
    public VariableType getType ()
    {
        return type;
    }
    
    public Variable (String name, ArrayList<String> values, String question, VariableType type)
    {
        this.name = name;
        this.values = values;
        this.question = question;
        this.type = type;
    }
    
    @Override
    public String toString ()
    {
        return name;
    }
}
