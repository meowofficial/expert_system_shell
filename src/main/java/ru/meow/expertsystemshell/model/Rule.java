package ru.meow.expertsystemshell.model;

import java.util.ArrayList;

public class Rule
{
    private String name;
    private ArrayList<Condition> conditions = new ArrayList<>();
    private Statement statement;
    private String reason;
    
    public Rule (String name, ArrayList<Condition> conditions, Statement statement, String reason)
    {
        this.name = name;
        this.conditions = conditions;
        this.statement = statement;
        this.reason = reason;
    }
    
    public String getName ()
    {
        return name;
    }
    
    public ArrayList<Condition> getConditions ()
    {
        return conditions;
    }
    
    public Statement getStatement ()
    {
        return statement;
    }
    
    public String getReason ()
    {
        return reason;
    }
    
    @Override
    public String toString ()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(name);
        builder.append(": if ");

        for (int i = 0; i < conditions.size(); i++)
        {
            builder.append(conditions.get(i).toString());
            builder.append(' ');

            if (i != conditions.size() - 1)
            {
                builder.append("& ");
            }
        }

        builder.append("then ");
        //builder.append(statement.toString());

        return builder.toString();
    }
}
