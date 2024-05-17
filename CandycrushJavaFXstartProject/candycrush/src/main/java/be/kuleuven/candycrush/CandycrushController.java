package be.kuleuven.candycrush;

import java.net.URL;
import java.util.ResourceBundle;

import be.kuleuven.candycrush.model.CandycrushModel;
import be.kuleuven.candycrush.model.Position;
import be.kuleuven.candycrush.model.PositionPairs;
import be.kuleuven.candycrush.model.PositionScorePair;
import be.kuleuven.candycrush.view.CandycrushView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import static be.kuleuven.candycrush.model.CandycrushModel.createBoardFromString;

public class CandycrushController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label Label;

    @FXML
    private Button btn;

    @FXML
    private AnchorPane paneel;

    @FXML
    private AnchorPane speelbord;

    @FXML
    private TextField textInput;

    @FXML
    private Label Score;

    private CandycrushModel model;
    private CandycrushView view;

    CandycrushModel model1 = createBoardFromString("""
        @@o#
        o*#o
        @@**
        *#@@""");

    CandycrushModel model2 = createBoardFromString("""
        #oo##
        #@o@@
        *##o@
        @@*@o
        **#*o""");

    CandycrushModel model3 = createBoardFromString("""
           #@#oo@
           @**@**
           o##@#o
           @#oo#@
           @*@**@
           *#@##*""");


    @FXML
    void initialize() {
        assert Label != null : "fx:id=\"Label\" was not injected: check your FXML file 'candycrush-view.fxml'.";
        assert btn != null : "fx:id=\"btn\" was not injected: check your FXML file 'candycrush-view.fxml'.";
        assert paneel != null : "fx:id=\"paneel\" was not injected: check your FXML file 'candycrush-view.fxml'.";
        assert speelbord != null : "fx:id=\"speelbord\" was not injected: check your FXML file 'candycrush-view.fxml'.";
        assert textInput != null : "fx:id=\"textInput\" was not injected: check your FXML file 'candycrush-view.fxml'.";
        model = new CandycrushModel("Test");

        view = new CandycrushView(model);
        //view = new CandycrushView(model1);
        //view = new CandycrushView(model2);
        //view = new CandycrushView(model3);

        speelbord.getChildren().add(view);
        //view.setOnMouseClicked(this::onCandyClicked);

        model1.updateBoard();
        PositionScorePair scorePair = model1.solve();
        for(PositionPairs pair : scorePair.pairs()) {
            System.out.print(
                    "(r" + (pair.position1().x()+1) + ",c" + (pair.position1().y()+1) + ")" +
                            "⇄" +
                            "(r" + (pair.position2().x()+1) + ",c" + (pair.position2().y()+1) + ")"
            );
            System.out.print(" ; ");
        }
        System.out.println("Hoogste score " + scorePair.score());
    }

    @FXML
    void onStartButtonClicked(ActionEvent event) {
        // Initialize model and view
        model = new CandycrushModel("Test");
        view = new CandycrushView(model);
        speelbord.getChildren().add(view);
        view.setOnMouseClicked(this::onCandyClicked);

        // Set initial score label
        Score.setText("Score: 0");

    }

    public void update(){
        view.update();
    }

    public void onCandyClicked(MouseEvent me){
        Position candyposition = view.getPositionOfClicked(me);
        //model.changeNeighbours(candyposition);
        System.out.println("update: " + model.updateBoard());
        update();
    }

}
