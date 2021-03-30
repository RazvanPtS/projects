import Controller.Controller;
import exception.MyException;
import repo.PrgState;
import model.expression.*;
import model.statements.*;
import model.types.BoolType;
import model.types.IntType;
import model.types.RefType;
import model.types.StringType;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.StringValue;
import repo.Repo;
import view.*;

import java.security.spec.ECField;
import java.util.concurrent.Delayed;
import java.util.concurrent.locks.Lock;

public class Main {
    public static void main(String[] args) throws MyException {
        Statement fStmt= new CompStmt(new DeclStmt("v",new IntType()),new CompStmt(new AssgnStmt("v"
                ,new ValueExp(new IntValue(2))),new PrintStmt(new VarExpression("v"))));
        Statement ndStmt = new CompStmt( new DeclStmt("a",new IntType()),  new CompStmt(new DeclStmt("b",new IntType()),
                new CompStmt(new AssgnStmt("a", new ArithmeticExp(1,new ValueExp(new IntValue(2)),
                        new ArithmeticExp(3,new ValueExp(new IntValue(3)), new ValueExp(new IntValue(5))))),
                        new CompStmt(new AssgnStmt("b",new ArithmeticExp(1,new VarExpression("a"), new ValueExp(new IntValue(1)))),
                                new PrintStmt(new VarExpression("b"))))));
        Statement rdStmt =new CompStmt(new DeclStmt("a",new BoolType()),new CompStmt(new DeclStmt("v", new IntType()),
                new CompStmt(new AssgnStmt("a", new ValueExp(new BoolValue(true))),
                        new CompStmt(new IfStmt(new VarExpression("a"),new AssgnStmt("v",new ValueExp(new IntValue(2))),
                                new AssgnStmt("v", new ValueExp(new IntValue(3)))), new PrintStmt(new VarExpression("v"))))));
        Statement fourthStmt=new CompStmt(new DeclStmt("varf",new StringType()),
                new CompStmt(new AssgnStmt("varf",new ValueExp(new StringValue("test.in"))),
                        new CompStmt(new OpenRFile(new VarExpression("varf")),new CompStmt(new DeclStmt("varc",new IntType()),
                                new CompStmt(new ReadFile(new VarExpression("varf"),"varc"),
                                        new CompStmt(new PrintStmt(new VarExpression("varc")),
                                                new CompStmt(new ReadFile(new VarExpression("varf"),"varc"),new CompStmt(
                                                        new PrintStmt(new VarExpression("varc")),new CloseFile(new VarExpression("varf"))))))))));


        Statement fifthStatement = new CompStmt(new DeclStmt("v",new RefType(new IntType())),
                                    new CompStmt(new newHeap("v",new ValueExp(new IntValue(20))),
                                            new CompStmt(new PrintStmt(new ReadHeap(new VarExpression("v"))),
                                                    new CompStmt(new WriteHeap("v",new ValueExp(new IntValue(30))),
                                                            new PrintStmt(new ArithmeticExp(1,new ReadHeap(new VarExpression("v")),
                                                                    new ValueExp(new IntValue(5))))))));
        Statement sixthStatement = new CompStmt(new DeclStmt("v",new RefType(new IntType())),
                                    new CompStmt(new newHeap("v",new ValueExp(new IntValue(20))),
                                            new CompStmt(new DeclStmt("a",new RefType(new RefType(new IntType()))),
                                                    new CompStmt(new newHeap("a",new VarExpression("v")),
                                                            new CompStmt(new PrintStmt(new VarExpression("v")),
                                                                    new PrintStmt(new VarExpression("a")))))));

        Statement seventhStatement = new CompStmt(new DeclStmt("a",new IntType()),
                                       new WhileStatement(new RelationalExpression(new VarExpression("a"),new ValueExp(new IntValue(5)),"<"),
                                               new CompStmt(new PrintStmt(new VarExpression("a")),
                                                new AssgnStmt("a",new ArithmeticExp(1,new VarExpression("a"),new ValueExp(new IntValue(1)))))));

        Statement eightStatement = new CompStmt(new DeclStmt("v",new RefType(new IntType())),
                new CompStmt(new newHeap("v",new ValueExp(new IntValue(20))),
                        new CompStmt(new DeclStmt("a",new RefType(new RefType(new IntType()))),
                                new CompStmt(new newHeap("a",new VarExpression("v")),
                                        new CompStmt(new newHeap("v",new ValueExp(new IntValue(50))),
                                                new CompStmt(new PrintStmt(new ReadHeap(new VarExpression("v"))),
                                                new PrintStmt(new ReadHeap(new ReadHeap(new VarExpression("a"))))))))));
        Statement forkStatement = new CompStmt(new WriteHeap("a",new ValueExp(new IntValue(30))),
                                    new CompStmt(new AssgnStmt("v",new ValueExp(new IntValue(32))),
                                        new CompStmt(new PrintStmt(new VarExpression("v")),
                                                new PrintStmt(new ReadHeap(new VarExpression("a"))))));
        Statement ninthStatement = new CompStmt(new DeclStmt("v",new IntType()),
                                    new CompStmt(new DeclStmt("a",new RefType(new IntType())),
                                            new CompStmt(new AssgnStmt("v",new ValueExp(new IntValue(10))),
                                                    new CompStmt(new newHeap("a",new ValueExp(new IntValue(22))),
                                                            new CompStmt(new forkStmt(forkStatement),
                                                                    new CompStmt(new PrintStmt(new VarExpression("v")),
                                                                            new PrintStmt(new ReadHeap(new VarExpression("a")))))))));
       /* Statement st = new CompStmt(new DeclStmt("v1",new RefType(new IntType())),
                            new CompStmt(new DeclStmt("v2",new RefType(new IntType())),
                                    new CompStmt(new DeclStmt("x",new IntType()),
                                    new CompStmt("q",new IntType(),
                                    new CompStmt(new newHeap("v1",new ValueExp(new IntValue(20))),
                                    new CompStmt(new newHeap("v2",new ValueExp(new IntValue(30))),
                                    new CompStmt(new NewLock("x"),
                                            new CompStmt(new forkStmt(new CompStmt(new forkStmt(new CompStmt(new LockSt("x"),new CompStmt(new WriteHeap("v1",new ArithmeticExp(2,new ReadHeap(new VarExpression("v1")),new ValueExp(new IntValue(1)))),new UnlockSt("x")))),
                                                    new CompStmt(new LockSt("x"),new CompStmt(new WriteHeap("v1",new ArithmeticExp(2,new ReadHeap(new VarExpression("v1")),new ValueExp(new IntValue(1)))),new UnlockSt("x"))))),
                                            new CompStmt(new NewLock("q"),
                                                    new CompStmt(new forkStmt(new CompStmt(new forkStmt(new CompStmt(new LockSt("q"),new CompStmt(new WriteHeap("v2",new ArithmeticExp(1,new ReadHeap(new VarExpression("v2")),new ValueExp(new IntValue(5)))),new UnlockSt("q"))))),new CompStmt(new LockSt("q"),new CompStmt(new WriteHeap("v2",new ArithmeticExp(3,new ReadHeap(new VarExpression("v2")),new ValueExp(new IntValue(10)))),new UnlockSt("q")))),new CompStmt(new NopStmt(),
                                                    new CompStmt(new NopStmt(),new CompStmt(new NopStmt(),new CompStmt(new NopStmt(),
                                                            new CompStmt(new LockSt("x"),new CompStmt(new PrintStmt(new ReadHeap(new VarExpression("v1"))),
                                                                    new CompStmt(new UnlockSt("x"),new CompStmt(new LockSt("q"),new CompStmt(new PrintStmt(new ReadHeap(new VarExpression("v2"))),new UnlockSt("q")))))))))))))

                                    )))))));

        new forkStmt(new CompStmt(new LockSt("x"),new CompStmt(new WriteHeap("v1",new ArithmeticExp(2,new ReadHeap(new VarExpression("v1")),new ValueExp(new IntValue(1)))),new UnlockSt("x"))));
*/
        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0", "exit"));

        try {
            Controller.typeCheck(fStmt);
            PrgState p1 = new PrgState(fStmt);
            Repo r1=new Repo(p1,"log1.txt");
            Controller c1 = new Controller(r1);
            menu.addCommand(new RunExample("1",fStmt.toString(),c1));
        }
        catch(Exception e) {
            System.out.println(fStmt.toString()+"\n"+e.getMessage());
        }

        try {
            Controller.typeCheck(ndStmt);
            PrgState p2 = new PrgState(ndStmt);
            Repo r2 = new Repo(p2, "log2.txt");
            Controller c2 = new Controller(r2);
            menu.addCommand(new RunExample("2",ndStmt.toString(),c2));
        }
        catch(Exception e)
        {
            System.out.println(ndStmt.toString()+"\n"+e.getMessage());
        }
        try {
            Controller.typeCheck(rdStmt);
            PrgState p3 = new PrgState(rdStmt);
            Repo r3 = new Repo(p3, "log3.txt");
            Controller c3 = new Controller(r3);
            menu.addCommand(new RunExample("3",rdStmt.toString(),c3));
        }
        catch(Exception e)
        {
            System.out.println(rdStmt.toString()+"\n"+e.getMessage());
        }

        try {
            Controller.typeCheck(fourthStmt);
            PrgState p4 = new PrgState(fourthStmt);
            Repo r4 = new Repo(p4, "log4.txt");
            Controller c4 = new Controller(r4);
            menu.addCommand(new RunExample("4",fourthStmt.toString(),c4));
        }
        catch (Exception e){
            System.out.println(fourthStmt.toString()+"\n"+e.getMessage());
        }

        try {
            Controller.typeCheck(fifthStatement);
            PrgState p5 = new PrgState(fifthStatement);
            Repo r5 = new Repo(p5, "log5.txt");
            Controller c5 = new Controller(r5);
            menu.addCommand(new RunExample("5",fifthStatement.toString(),c5));
        }
        catch (Exception e){
            System.out.println(fifthStatement.toString()+"\n"+e.getMessage());
        }
        try {
            Controller.typeCheck(sixthStatement);
            PrgState p6 = new PrgState(sixthStatement);
            Repo r6 = new Repo(p6, "log6.txt");
            Controller c6 = new Controller(r6);
            menu.addCommand(new RunExample("6", sixthStatement.toString(),c6));
        }
        catch (Exception e){
            System.out.println(sixthStatement.toString()+"\n"+e.getMessage());
        }
        try {
            Controller.typeCheck(seventhStatement);
            PrgState p7 = new PrgState(seventhStatement);
            Repo r7 = new Repo(p7, "log7.txt");
            Controller c7 = new Controller(r7);
            menu.addCommand(new RunExample("7", seventhStatement.toString(),c7));
        }
        catch (Exception e){
            System.out.println(seventhStatement.toString()+"\n"+e.getMessage());
        }
        try {
            Controller.typeCheck(eightStatement);
            PrgState p8 = new PrgState(eightStatement);
            Repo r8 = new Repo(p8, "log8.txt");
            Controller c8 = new Controller(r8);
            menu.addCommand(new RunExample("8", eightStatement.toString(),c8));
        }
        catch (Exception e){
            System.out.println(eightStatement.toString()+"\n"+e.getMessage());
        }
        try {
            Controller.typeCheck(ninthStatement);
            PrgState p9 = new PrgState(ninthStatement);
            Repo r9 = new Repo(p9, "log9.txt");
            Controller c9 = new Controller(r9);
            menu.addCommand(new RunExample("9", ninthStatement.toString(),c9));
        }
        catch (Exception e)
        {
            System.out.println(ninthStatement.toString()+"\n"+e.getMessage());
        }

        menu.show();
    }
}
