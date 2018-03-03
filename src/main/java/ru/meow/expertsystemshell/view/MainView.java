package ru.meow.expertsystemshell.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ru.meow.expertsystemshell.model.ApplicationData;
import ru.meow.expertsystemshell.model.Variable;
import ru.meow.expertsystemshell.model.Rule;


public class MainView extends BaseView
{
    @FXML
    private Button addVariableButton;
    
    @FXML
    private Button editVariableButton;
    
    @FXML
    private Button removeVariableButton;
    
    @FXML
    private ListView<Variable> variableListView;
    
    @FXML
    private ListView<String> variableValueListView;
    
    @FXML
    private AnchorPane variableManagementAnchorPane;
    
    @FXML
    private ListView<Rule> ruleListView;
    
    @FXML
    private Button addRuleButton;
    
    @FXML
    private Button editRuleButton;
    
    @FXML
    private Button removeRuleButton;
    
    @FXML
    private AnchorPane ruleManagementAnchorPane;
    
    private Stage stage;
    
    public MainView ()
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main.fxml"));
            fxmlLoader.setController(this);
            this.root = fxmlLoader.load();
            this.stage = new Stage();
            stage.setTitle("Expert System Shell");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            inizializeUI();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void inizializeUI ()
    {
        variableListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        variableValueListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        addVariableButton.setOnAction(this::addVariableButtonOnAction);
        editVariableButton.setOnAction(this::editVariableButtonOnAction);
        removeVariableButton.setOnAction(this::removeVariableButtonOnAction);
        variableListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
            updateVariableValueListView(newValue)
        );
        variableManagementAnchorPane.setOnMouseClicked(event -> variableListView.getSelectionModel().clearSelection());
        
        addRuleButton.setOnAction(this::addRuleButtonOnAction);
        editRuleButton.setOnAction(this::editRuleButtonOnAction);
        removeRuleButton.setOnAction(this::removeRuleButtonOnAction);
    }
    
    public void show ()
    {
        stage.show();
    }
    
    private void addVariableButtonOnAction (ActionEvent event)
    {
        VariableView variableView = new VariableView(this);
        variableView.show();
    }
    
    private void editVariableButtonOnAction (ActionEvent event)
    {
        Variable selectedVariable = variableListView.getSelectionModel().getSelectedItem();
        if (selectedVariable == null)
        {
            throwError("Выберите переменную для редактирования!");
            return;
        }
        VariableView variableView = new VariableView(this, selectedVariable);
        variableView.show();
    }
    
    private void removeVariableButtonOnAction (ActionEvent event)
    {
        Variable selectedVariable = variableListView.getSelectionModel().getSelectedItem();
        if (selectedVariable == null)
        {
            throwError("Выберите переменную для удаления!");
            return;
        }
    
        ApplicationData.variables.remove(selectedVariable);
        updateVariableListView();
    }
    
    public void updateVariableListView ()
    {
        variableListView.getItems().clear();
        ApplicationData.variables.forEach(variable -> variableListView.getItems().add(variable));
        variableListView.getSelectionModel().clearSelection();
    }
    
    private void updateVariableValueListView (Variable variable)
    {
        variableValueListView.getItems().clear();
        if (variable == null)
        {
            return;
        }
        variable.getValues().forEach(variableValue -> variableValueListView.getItems().add(variableValue));
        variableValueListView.getSelectionModel().clearSelection();
    }
    
    private void throwError (String text)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.setResizable(false);
        alert.showAndWait();
    }
    
    private void addRuleButtonOnAction (ActionEvent event)
    {
        RuleView ruleView = new RuleView(this);
        ruleView.show();
    }
    
    public void updateRuleListView ()
    {
        ruleListView.getItems().clear();
        ApplicationData.rules.forEach(rule -> ruleListView.getItems().add(rule));
        ruleListView.getSelectionModel().clearSelection();
    }
    
    private void editRuleButtonOnAction (ActionEvent event)
    {
        Rule selectedRule = ruleListView.getSelectionModel().getSelectedItem();
        if (selectedRule == null)
        {
            throwError("Выберите правило для редактирования!");
            return;
        }
        RuleView ruleView = new RuleView(this, selectedRule);
        ruleView.show();
    }
    
    private void removeRuleButtonOnAction (ActionEvent event)
    {
        Rule selectedRule = ruleListView.getSelectionModel().getSelectedItem();
        if (selectedRule == null)
        {
            throwError("Выберите правило для удаления!");
            return;
        }
    
        ApplicationData.rules.remove(selectedRule);
        updateRuleListView();
    }
}
