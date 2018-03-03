package ru.meow.expertsystemshell.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import ru.meow.expertsystemshell.model.ApplicationData;
import ru.meow.expertsystemshell.model.Variable;
import java.util.ArrayList;
import java.util.HashSet;


public class VariableView extends BaseView
{
    @FXML
    private VBox variableValuesVBox;
    
    @FXML
    private Button addVariableValueButton;
    
    @FXML
    private Button removeVariableValueButton;
    
    @FXML
    private Button saveChangesButton;
    
    @FXML
    private TextField variableNameTextField;
    
    @FXML
    private RadioButton derivableVariableTypeRadioButton;
    
    @FXML
    private RadioButton requestedVariableTypeRadioButton;
    
    @FXML
    private RadioButton derivableRequestedVariableTypeRadioButton;
    
    @FXML
    private TextArea variableQuestionTextArea;
    
    
    private Stage stage;
    private Variable variable = null;
    private boolean isViewEdited = false;
    private BaseView ownerView;
    
    enum ViewType
    {
        ADD, EDIT
    }
    
    public VariableView (BaseView ownerView)
    {
        this.ownerView = ownerView;
        inizializeView(ownerView.root.getScene().getWindow(), ViewType.ADD);
    }
    
    public VariableView (BaseView ownerView, Variable variable)
    {
        this.ownerView = ownerView;
        this.variable = variable;
        inizializeView(ownerView.root.getScene().getWindow(), ViewType.EDIT);
    }
    
    private void inizializeView (Window ownerWindow, ViewType viewType)
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("variable.fxml"));
            fxmlLoader.setController(this);
            this.root = fxmlLoader.load();
            this.stage = new Stage();
            String title = (viewType == ViewType.ADD ? "Добавление переменной" : "Редактирование переменной");
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initOwner(ownerWindow);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setOnCloseRequest(event ->
            {
                if (isViewEdited)
                {
                    ButtonType save = new ButtonType("Yes");
                    ButtonType dontSave = new ButtonType("No");
                    Alert needSaveAlert = new Alert(Alert.AlertType.NONE);
                    needSaveAlert.getButtonTypes().addAll(save, dontSave);
                    needSaveAlert.setTitle(null);
                    needSaveAlert.setHeaderText(null);
                    needSaveAlert.setResizable(false);
                    needSaveAlert.setContentText("Сохранить изменения?");
                    needSaveAlert.showAndWait().ifPresent(response ->
                    {
                        if (response == save)
                        {
                            if (!saveChanges())
                            {
                                event.consume();
                            }
                        }
                    });
                }
            });
            
            if (viewType == ViewType.EDIT)
            {
                variableNameTextField.setText(variable.getName());
                variableQuestionTextArea.setText(variable.getQuestion());
                
                if (variable.getType() == Variable.VariableType.REQUESTED)
                {
                    requestedVariableTypeRadioButton.setSelected(true);
                }
                else if (variable.getType() == Variable.VariableType.DERIVABLE)
                {
                    derivableVariableTypeRadioButton.setSelected(true);
                }
                else if (variable.getType() == Variable.VariableType.DERIVABLE_REQUESTED)
                {
                    derivableRequestedVariableTypeRadioButton.setSelected(true);
                }
    
                variable.getValues().forEach(variableValue ->
                {
                    HBox variableValueHbox = createNewVariableValue();
                    TextField variableValueTextField = (TextField) variableValueHbox.getChildren().get(0);
                    variableValueTextField.setVisible(true);
                    Button removeVariableValueButton = (Button) variableValueHbox.getChildren().get(1);
                    removeVariableValueButton.setVisible(false);
                    removeVariableValueButton.setManaged(false);
                    variableValueTextField.setText(variableValue);
                    variableValuesVBox.getChildren().add(variableValueHbox);
                });
            }
            
            inizializeUI();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void inizializeUI ()
    {
        addVariableValueButton.setOnAction(this::addVariableValueOnAction);
        removeVariableValueButton.setOnAction(this::removeVariableValueOnAction);
        variableNameTextField.setOnKeyTyped(event -> isViewEdited = true);
        requestedVariableTypeRadioButton.setOnAction(this::variableTypeRadioButtonOnAction);
        derivableVariableTypeRadioButton.setOnAction(this::variableTypeRadioButtonOnAction);
        derivableRequestedVariableTypeRadioButton.setOnAction(this::variableTypeRadioButtonOnAction);
        saveChangesButton.setOnAction(this::saveChangesButtonOnAction);
    }
    
    public void show ()
    {
        stage.show();
    }
    
    Variable.VariableType getVariableType ()
    {
        if (requestedVariableTypeRadioButton.isSelected())
        {
            return Variable.VariableType.REQUESTED;
        }
        if (derivableVariableTypeRadioButton.isSelected())
        {
            return Variable.VariableType.DERIVABLE;
        }
        if (derivableRequestedVariableTypeRadioButton.isSelected())
        {
            return Variable.VariableType.DERIVABLE_REQUESTED;
        }
        return null;
    }
    
    private boolean saveChanges ()
    {
        ArrayList<String> variableValues = new ArrayList<>();
        boolean isVariableExist = false;
        boolean isVariableValueExist = false;
    
        variableValuesVBox.getChildren().forEach(childNode ->
        {
            HBox hbox = (HBox)childNode;
            variableValues.add(((TextField) hbox.getChildren().get(0)).getText());
        });
    
        HashSet<String> variableValueSet = new HashSet<>();
    
        for (String variableValue : variableValues)
        {
            if (variableValueSet.contains(variableValue))
            {
                isVariableValueExist = true;
            }
            else
            {
                variableValueSet.add(variableValue);
            }
        }
    
        for (Variable variable : ApplicationData.variables)
        {
            if (variable != this.variable && variable.getName().equals(variableNameTextField.getText()))
            {
                isVariableExist = true;
            }
        }
    
        if (isVariableExist)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setResizable(false);
            alert.setContentText("Переменная с таким именем уже существует!");
            return false;
        }
        else if (isVariableValueExist)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setResizable(false);
            alert.setContentText("Значения переменных повторяются!");
            return false;
        }
        else
        {
            String variableName = variableNameTextField.getText();
            String variableQuestion = variableQuestionTextArea.getText();
            Variable.VariableType variableType = getVariableType();
            Variable variable = new Variable(variableName, variableValues, variableQuestion, variableType);
        
            if (this.variable == null)
            {
                ApplicationData.variables.add(variable);
            }
            else
            {
                ApplicationData.variables.set(ApplicationData.variables.indexOf(this.variable), variable);
                this.variable = variable;
            }
        
            ((MainView)ownerView).updateVariableListView();
            
            return true;
        }
    }
    
    private void variableTypeRadioButtonOnAction (ActionEvent event)
    {
        Variable.VariableType variableType = getVariableType();
        if (variableType == Variable.VariableType.REQUESTED || variableType == Variable.VariableType.DERIVABLE_REQUESTED)
        {
            variableQuestionTextArea.setVisible(true);
            variableQuestionTextArea.setManaged(true);
        }
        else
        {
            variableQuestionTextArea.setVisible(false);
            variableQuestionTextArea.setManaged(false);
            variableQuestionTextArea.setText("");
        }
        isViewEdited = true;
    }
    
    private HBox createNewVariableValue ()
    {
        for (Node node: variableValuesVBox.getChildren())
        {
            HBox hbox = (HBox)node;
            Button button = (Button)(hbox.getChildren().get(hbox.getChildren().size() - 1));
            button.setVisible(false);
            button.setManaged(false);
        }
    
        HBox hbox = new HBox();
        TextField textField = new TextField();
        textField.setPrefWidth(1000.0);
        
        textField.setOnKeyTyped(event -> isViewEdited = true);
        
        Button button = new Button("-");
        button.setPrefWidth(25.0);
        button.setPrefHeight(25.0);
        button.setMinWidth(25.0);
        button.setMinHeight(25.0);
        button.setVisible(false);
        button.setManaged(false);
        button.setOnAction(event -> variableValuesVBox.getChildren().remove(hbox));
        hbox.getChildren().addAll(textField, button);
        HBox.setMargin(textField, new Insets(10.0, 10.0, 0.0, 10.0));
        HBox.setMargin(button, new Insets(10.0, 10.0, 0.0, 10.0));
        return hbox;
    }
    
    public void addVariableValueOnAction (ActionEvent event)
    {
        HBox hbox = createNewVariableValue();
        variableValuesVBox.getChildren().add(hbox);
    }
    
    public void removeVariableValueOnAction (ActionEvent event)
    {
        for (Node node: variableValuesVBox.getChildren())
        {
            HBox hbox = (HBox)node;
            Button button = (Button)(hbox.getChildren().get(hbox.getChildren().size() - 1));
            button.setVisible(!button.isVisible());
            button.setManaged(!button.isManaged());
        }
    }
    
    private void saveChangesButtonOnAction (ActionEvent event)
    {
        if (saveChanges())
        {
            isViewEdited = false;
        }
    }
    
}
