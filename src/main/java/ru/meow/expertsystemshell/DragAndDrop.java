package ru.meow.expertsystemshell;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;


public class DragAndDrop extends Application
{
    @Override
    public void start (Stage primaryStage)
    {
        try
        {
            final VBox root = new VBox(5);
            final ScrollPane scroller = new ScrollPane();
            scroller.setContent(root);
            final Scene scene = new Scene(scroller, 400, 200);
            
            for (int i = 1; i <= 20; i++)
            {
                final Label label = new Label("Item " + i);
                addWithDragging(root, label);
            }
            
            // in case user drops node in blank space in root:
            root.setOnMouseDragReleased(new EventHandler<MouseDragEvent>()
            {
                @Override
                public void handle (MouseDragEvent event)
                {
                    int indexOfDraggingNode = root.getChildren().indexOf(event.getGestureSource());
                    rotateNodes(root, indexOfDraggingNode, root.getChildren().size() - 1);
                }
            });
            
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void addWithDragging (final VBox root, final Label label)
    {
        label.setOnDragDetected(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle (MouseEvent event)
            {
                label.startFullDrag();
            }
        });
        
        // next two handlers just an idea how to show the drop target visually:
        label.setOnMouseDragEntered(new EventHandler<MouseDragEvent>()
        {
            @Override
            public void handle (MouseDragEvent event)
            {
                label.setStyle("-fx-background-color: #b6deff;");
            }
        });
        label.setOnMouseDragExited(new EventHandler<MouseDragEvent>()
        {
            @Override
            public void handle (MouseDragEvent event)
            {
                label.setStyle("");
            }
        });
        
        label.setOnMouseDragReleased(new EventHandler<MouseDragEvent>()
        {
            @Override
            public void handle (MouseDragEvent event)
            {
                label.setStyle("");
                int indexOfDraggingNode = root.getChildren().indexOf(event.getGestureSource());
                int indexOfDropTarget = root.getChildren().indexOf(label);
                rotateNodes(root, indexOfDraggingNode, indexOfDropTarget);
                event.consume();
            }
        });
        root.getChildren().add(label);
    }
    
    private void rotateNodes (final VBox root, final int indexOfDraggingNode, final int indexOfDropTarget)
    {
        if (indexOfDraggingNode >= 0 && indexOfDropTarget >= 0)
        {
            final Node node = root.getChildren().remove(indexOfDraggingNode);
            root.getChildren().add(indexOfDropTarget, node);
        }
    }
    
    
    public static void main (String[] args)
    {
        launch(args);
    }
}
