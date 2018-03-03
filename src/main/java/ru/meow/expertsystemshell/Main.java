package ru.meow.expertsystemshell;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.meow.expertsystemshell.view.MainView;


public class Main extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        MainView mainView = new MainView();
        mainView.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
