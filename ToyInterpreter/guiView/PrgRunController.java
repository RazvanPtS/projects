package guiView;

import java.io.BufferedReader;
import java.net.URL;
import java.nio.Buffer;
import java.util.*;

import Controller.Controller;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.statements.Statement;
import model.value.Value;
import repo.PrgState;

public class PrgRunController implements Initializable {

    private Controller myController;
    private PrgRunController thisRun;
    @FXML
    Label nrPrgStates;
    @FXML
    Button runButton;
    @FXML
    TableView<Map.Entry<Integer, Value>> heapTable;
    @FXML
    TableColumn<HashMap.Entry<Integer,Value>, String> heapTableAddress;
    @FXML
    TableColumn<HashMap.Entry<Integer,Value>, String> heapTableValue;
    @FXML
    ListView<String> outList;
    @FXML
    TableView<Map.Entry<String,BufferedReader>> fileTable;
    @FXML
    TableColumn<HashMap.Entry<String, BufferedReader>, String> fileTableIdentifier;
    @FXML
    TableColumn<HashMap.Entry<String, BufferedReader>, String> fileTableFileName;
    @FXML
    ListView<String> prgStateList;
    @FXML
    TableView<Map.Entry<String, Value>> symTable;
    @FXML
    TableColumn<HashMap.Entry<String, Value>, String> symTableVariable;
    @FXML
    TableColumn<HashMap.Entry<String, Value>, String> symTableValue;
    @FXML
    ListView<String> exeStackList;

    public PrgRunController(Controller myController) {
        this.myController = myController;
        thisRun = this;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initialRun();
        myController.prepGUIRun(thisRun);
        prgStateList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setSymTableAndExeStack();
            }
        });
        runButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle (ActionEvent e) {
                try {
                    myController.nextStepGUI(thisRun);

                } catch (Exception e1) {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Toy Language - Current program finished");
                    alert.setHeaderText(null);
                    alert.setContentText("Program successfully finished!");
                    Button confirm = (Button) alert.getDialogPane().lookupButton( ButtonType.OK );
                    confirm.setDefaultButton(false);
                    confirm.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
                    alert.showAndWait();
                    Stage stage = (Stage) heapTable.getScene().getWindow();
                    stage.close();
                    myController.exitGUIRun();
                }
            }
        });
    }

    public void initialRun() {
        setNumberLabel();
        setHeapTable();
        setOutList();
        setFileTable();
        setPrgStateList();
        prgStateList.getSelectionModel().selectFirst();
        setSymTableAndExeStack();
    }

    public void updateUIComponents() {
        setNumberLabel();
        setHeapTable();
        setOutList();
        setFileTable();
        setPrgStateList();
        if(prgStateList.getSelectionModel().getSelectedItem() == null) {
            prgStateList.getSelectionModel().selectFirst();
        }
        setSymTableAndExeStack();
    }

    public void setNumberLabel() {
        nrPrgStates.setText("The number of PrgStates: " + myController.getRepo().getPrgList().size());
    }

    public void setHeapTable() {
        ObservableList<HashMap.Entry<Integer, Value>> heapTableList = FXCollections.observableArrayList();
        heapTableAddress.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(Integer.toString(cellData.getValue().getKey())));
        heapTableValue.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getValue().toString()));
        heapTableList.addAll(myController.getRepo().getPrgList().get(0).getHeap().getADT().entrySet());
        heapTable.setItems(heapTableList);
        heapTable.refresh();
    }

    public void setOutList() {
        ObservableList<String> outObservableList = FXCollections.observableArrayList();
        outObservableList.addAll(myController.getRepo().getPrgList().get(0).getOutput().getADT());
        outList.setItems(outObservableList);
        outList.refresh();
    }

    public void setFileTable() {
        ObservableList<HashMap.Entry<String,BufferedReader>> fileTableList = FXCollections.observableArrayList();
        fileTableIdentifier.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getKey()));
        fileTableFileName.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getValue().toString()));
        fileTableList.addAll(myController.getRepo().getPrgList().get(0).getFileTable().getADT().entrySet());
        fileTable.setItems(fileTableList);
        fileTable.refresh();
    }

    public void setPrgStateList() {
        ObservableList<String> prgStateIdList = FXCollections.observableArrayList();
        for(PrgState e : myController.getRepo().getPrgList()) {
            prgStateIdList.add(Integer.toString(e.getId()));
        }
        prgStateList.setItems(prgStateIdList);
        prgStateList.refresh();
    }

    public void setSymTableAndExeStack() {
        ObservableList<Map.Entry<String, Value>> symTableList = FXCollections.observableArrayList();
        ObservableList<String> exeStackItemsList = FXCollections.observableArrayList();
        symTableVariable.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getKey()));
        symTableValue.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getValue().toString()));
        List<PrgState> allPrgs = myController.getRepo().getPrgList();
        PrgState prgResult = null;
        for(PrgState e : allPrgs) {
            if(e.getId() == Integer.parseInt(prgStateList.getSelectionModel().getSelectedItem())) {
                prgResult = e;
                break;
            }
        }
        if(prgResult != null) {
            symTableList.addAll(prgResult.getSymTab().getADT().entrySet());
            for(Statement e : prgResult.getStack().getADT()) {
                exeStackItemsList.add(e.toString());
            }
            symTable.setItems(symTableList);
            symTable.refresh();
            exeStackList.setItems(exeStackItemsList);
        }
    }

}
