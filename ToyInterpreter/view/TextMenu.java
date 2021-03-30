package view;

import exception.MyException;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TextMenu {
    private Map<String, Command> commands;
    public TextMenu(){ commands=new HashMap<>(); }
    public void addCommand(Command c){ commands.put(c.getKey(),c);}
    private void printMenu(){
        for(Command com : commands.values())
        {
            String line=String.format("%4s : %s", com.getKey(), com.getDescription());
            System.out.println(line);
        }
    }
    public void show() throws MyException
    {
        Scanner scanner=new Scanner(System.in);
        while(true)
        {
            printMenu();
            System.out.println("Input the option: ");
            String key=scanner.nextLine();
            Command com=commands.get(key);
            if (com==null)
            {
                 System.out.println("Invalid Option");
                 continue;
            }
        com.execute();
        }
    }
}
