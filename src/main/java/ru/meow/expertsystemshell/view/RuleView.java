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
import ru.meow.expertsystemshell.model.Condition;
import ru.meow.expertsystemshell.model.Rule;
import ru.meow.expertsystemshell.model.Variable;

import java.util.ArrayList;

public class RuleView extends BaseView
{
    private Stage stage;
    private BaseView ownerView;
    private Rule rule = null;
    private boolean isViewEdited = false;

    @FXML
    private VBox ruleConditionsVBox;
    
    @FXML
    private TextField ruleNameTextField;

    @FXML
    private Button addRuleConditionButton;
    
    @FXML
    private Button removeRuleConditionButton;
    
    @FXML
    private Button saveChangesButton;
    
    enum ViewType
    {
        ADD, EDIT
    }

    public RuleView (BaseView ownerView)
    {
        this.ownerView = ownerView;
        inizializeView(ownerView.root.getScene().getWindow(), ViewType.ADD);
    }

    public RuleView (BaseView ownerView, Rule rule)
    {
        this.ownerView = ownerView;
        this.rule = rule;
        inizializeView(ownerView.root.getScene().getWindow(), ViewType.EDIT);
    }

    private void inizializeView (Window ownerWindow, ViewType viewType)
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("rule.fxml"));
            fxmlLoader.setController(this);
            this.root = fxmlLoader.load();
            this.stage = new Stage();
            String title = (viewType == ViewType.ADD ? "Добавление правила" : "Редактирование правила");
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
                ruleNameTextField.setText(rule.getName());

//                rule.getConditions().forEach(condition ->
//                {
//                    HBox variableValueHbox = createNewRuleCondition();
//                    TextField variableValueTextField = (TextField) variableValueHbox.getChildren().get(0);
//                    variableValueTextField.setVisible(true);
//                    Button removeVariableValueButton = (Button) variableValueHbox.getChildren().get(2);
//                    removeVariableValueButton.setVisible(false);
//                    removeVariableValueButton.setManaged(false);
//                    variableValuesVBox.getChildren().add(variableValueHbox);
//                });
            }

            inizializeUI();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void show ()
    {
        stage.show();
    }
    
    private void inizializeUI ()
    {
        addRuleConditionButton.setOnAction(this::addRuleConditionButtonOnAction);
        removeRuleConditionButton.setOnAction(this::removeRuleConditionButtonOnAction);
        saveChangesButton.setOnAction(this::saveChangesButtonOnAction);
        ruleNameTextField.setOnKeyTyped(event -> isViewEdited = true);
    }
    
    
    private boolean saveChanges ()
    {
//        if ()
//        {
//            return false;
//        }
        
        ArrayList<Condition> conditions = new ArrayList<>();
        
        for (Node node : ruleConditionsVBox.getChildren())
        {
            HBox hbox = (HBox)node;
            Variable selectedVariable = ((ComboBox<Variable>)hbox.getChildren().get(0)).getSelectionModel().getSelectedItem();
            String value = ((ComboBox<String>)hbox.getChildren().get(1)).getSelectionModel().getSelectedItem();
            conditions.add(new Condition(selectedVariable, value));
        }
    
        String ruleName = ruleNameTextField.getText();
        Rule rule = new Rule(ruleName, conditions, null, null);
    
        if (this.rule == null)
        {
            ApplicationData.rules.add(rule);
        }
        else
        {
            ApplicationData.rules.set(ApplicationData.rules.indexOf(this.rule), rule);
        }
    
        ((MainView)ownerView).updateRuleListView();
        
        return true;
    }

    private void addRuleConditionButtonOnAction (ActionEvent event)
    {
        HBox hbox = createNewRuleCondition();
        ruleConditionsVBox.getChildren().add(hbox);
    }
    
    public void removeRuleConditionButtonOnAction (ActionEvent event)
    {
        for (Node node: ruleConditionsVBox.getChildren())
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

    private HBox createNewRuleCondition ()
    {
        for (Node node: ruleConditionsVBox.getChildren())
        {
            HBox hbox = (HBox)node;
            Button button = (Button)(hbox.getChildren().get(hbox.getChildren().size() - 1));
            button.setVisible(false);
            button.setManaged(false);
        }

        HBox hbox = new HBox();
        
        ComboBox<Variable> variableComboBox = new ComboBox<>();
        ComboBox<String> variableValuesCombobox = new ComboBox<>();
    
        variableComboBox.setPrefWidth(1000.0);
        variableValuesCombobox.setPrefWidth(1000.0);

        //textField.setOnKeyTyped(event -> isViewEdited = true);

        Button button = new Button("-");
        button.setPrefWidth(25.0);
        button.setPrefHeight(25.0);
        button.setMinWidth(25.0);
        button.setMinHeight(25.0);
        button.setVisible(false);
        button.setManaged(false);
        //button.setOnAction(event -> variableValuesVBox.getChildren().remove(hbox));
        hbox.getChildren().addAll(variableComboBox, variableValuesCombobox, button);
        HBox.setMargin(variableComboBox, new Insets(10.0, 10.0, 0.0, 10.0));
        HBox.setMargin(variableValuesCombobox, new Insets(10.0, 10.0, 0.0, 10.0));
        HBox.setMargin(button, new Insets(10.0, 10.0, 0.0, 10.0));
        return hbox;
    }
    
    
}
