package view;
import Controller.Controller;
import exception.AdtEmptyException;
import exception.MyException;

public class RunExample extends Command {
    private Controller ctr;
    public RunExample(String key, String desc, Controller ctr)
    {
        super(key, desc);
        this.ctr=ctr;
    }
    @Override
    public void execute()throws MyException {
        try {
            ctr.allStep();
        } catch (Exception error) {
            throw new RuntimeException(error.getMessage());
        }
    }
}
