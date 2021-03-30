package guiView;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Controller.Controller;
import javafx.scene.control.Control;
import model.expression.*;
import model.statements.AssgnStmt;
import model.statements.CloseFile;
import model.statements.CompStmt;
import model.statements.forkStmt;
import model.statements.newHeap;
import model.statements.WriteHeap;
import model.statements.Statement;
import model.statements.IfStmt;
import model.statements.OpenRFile;
import model.statements.PrintStmt;
import model.statements.ReadFile;
import model.statements.WhileStatement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.expression.ValueExp;
import model.expression.VarExpression;
import model.statements.*;
import model.types.BoolType;
import model.types.IntType;
import model.types.RefType;
import model.types.StringType;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.StringValue;
import repo.PrgState;
import repo.Repo;
import view.RunExample;

public class PrgListController implements Initializable {

    @FXML
    ListView<Controller> myPrgList;
    @FXML
    Button runButton;
    private Controller c1,c2,c3,c4,c5,c6,c7,c8,c9,c10;
    public void setUp() {
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

        Statement ExSt = new CompStmt(new DeclStmt("a",new RefType(new IntType())),
                                    new CompStmt(new newHeap("a",new ValueExp(new IntValue(20))),
                                    new CompStmt(new ForStmt("v",new ValueExp(new IntValue(0)),new ValueExp(new IntValue(3)),
                                            new ArithmeticExp(1,new VarExpression("v"),new ValueExp(new IntValue(1))),
                                            new forkStmt(new CompStmt(new PrintStmt(new VarExpression("v")),
                                                    new AssgnStmt("v",new ArithmeticExp(3,new VarExpression("v"),new ReadHeap(new VarExpression("a")))))))
                                            ,new PrintStmt(new ReadHeap(new VarExpression("a"))))));

        try {
            Controller.typeCheck(fStmt);
            PrgState p1 = new PrgState(fStmt);
            Repo r1=new Repo(p1,"log1.txt");
            c1 = new Controller(r1);
        }
        catch(Exception e) {
            System.out.println(fStmt.toString()+"\n"+e.getMessage());
        }

        try {
            Controller.typeCheck(ndStmt);
            PrgState p2 = new PrgState(ndStmt);
            Repo r2 = new Repo(p2, "log2.txt");
            c2 = new Controller(r2);
        }
        catch(Exception e)
        {
            System.out.println(ndStmt.toString()+"\n"+e.getMessage());
        }
        try {
            Controller.typeCheck(rdStmt);
            PrgState p3 = new PrgState(rdStmt);
            Repo r3 = new Repo(p3, "log3.txt");
            c3 = new Controller(r3);
        }
        catch(Exception e)
        {
            System.out.println(rdStmt.toString()+"\n"+e.getMessage());
        }

        try {
            Controller.typeCheck(fourthStmt);
            PrgState p4 = new PrgState(fourthStmt);
            Repo r4 = new Repo(p4, "log4.txt");
            c4 = new Controller(r4);
        }
        catch (Exception e){
            System.out.println(fourthStmt.toString()+"\n"+e.getMessage());
        }

        try {
            Controller.typeCheck(fifthStatement);
            PrgState p5 = new PrgState(fifthStatement);
            Repo r5 = new Repo(p5, "log5.txt");
            c5 = new Controller(r5);
        }
        catch (Exception e){
            System.out.println(fifthStatement.toString()+"\n"+e.getMessage());
        }
        try {
            Controller.typeCheck(sixthStatement);
            PrgState p6 = new PrgState(sixthStatement);
            Repo r6 = new Repo(p6, "log6.txt");
            c6 = new Controller(r6);
        }
        catch (Exception e){
            System.out.println(sixthStatement.toString()+"\n"+e.getMessage());
        }
        try {
            Controller.typeCheck(seventhStatement);
            PrgState p7 = new PrgState(seventhStatement);
            Repo r7 = new Repo(p7, "log7.txt");
            c7 = new Controller(r7);
        }
        catch (Exception e){
            System.out.println(seventhStatement.toString()+"\n"+e.getMessage());
        }
        try {
            Controller.typeCheck(eightStatement);
            PrgState p8 = new PrgState(eightStatement);
            Repo r8 = new Repo(p8, "log8.txt");
            c8 = new Controller(r8);
        }
        catch (Exception e){
            System.out.println(eightStatement.toString()+"\n"+e.getMessage());
        }
        try {
            Controller.typeCheck(ninthStatement);
            PrgState p9 = new PrgState(ninthStatement);
            Repo r9 = new Repo(p9, "log9.txt");
            c9 = new Controller(r9);
        }
        catch (Exception e)
        {
            System.out.println(ninthStatement.toString()+"\n"+e.getMessage());
        }

        try {
            Controller.typeCheck(ExSt);
            PrgState p10 = new PrgState(ExSt);
            Repo r10 = new Repo(p10, "log10.txt");
            c10 = new Controller(r10);
        }
        catch (Exception e)
        {
            System.out.println(ExSt.toString()+"\n"+e.getMessage());
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUp();
        ObservableList<Controller> myData = FXCollections.observableArrayList();
        myData.add(c1);
        myData.add(c2);
        myData.add(c3);
        myData.add(c4);
        myData.add(c5);
        myData.add(c6);
        myData.add(c7);
        myData.add(c8);
        myData.add(c9);
        myData.add(c10);

        myPrgList.setItems(myData);
        myPrgList.getSelectionModel().selectFirst();
        runButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle (ActionEvent e) {
                Stage programStage = new Stage();
                Parent programRoot;
                Callback<Class<?>, Object> controllerFactory = type -> {
                    if (type == PrgRunController.class) {
                        return new PrgRunController(myPrgList.getSelectionModel().getSelectedItem());
                    } else {
                        try {
                            return type.newInstance() ;
                        } catch (Exception exc) {
                            System.err.println("Could not create controller for "+type.getName());
                            throw new RuntimeException(exc);
                        }
                    }
                };
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ProgramLayout.fxml"));
                    fxmlLoader.setControllerFactory(controllerFactory);
                    programRoot = fxmlLoader.load();
                    Scene programScene = new Scene(programRoot);
                    programStage.setTitle("Toy Language - Program running");
                    programStage.setScene(programScene);
                    programStage.show();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

}
