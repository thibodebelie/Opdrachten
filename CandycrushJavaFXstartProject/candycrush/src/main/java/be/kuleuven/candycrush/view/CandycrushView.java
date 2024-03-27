package be.kuleuven.candycrush.view;

import be.kuleuven.candycrush.model.Candy;
import be.kuleuven.candycrush.model.CandycrushModel;
import be.kuleuven.candycrush.model.Position;
import be.kuleuven.candycrush.model.candies.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.*;
import java.util.Collection;
import java.util.Iterator;

import static javafx.scene.paint.Color.color;

public class CandycrushView extends Region {
    private CandycrushModel model;
    private int widthCandy;
    private int heigthCandy;

    public CandycrushView(CandycrushModel model) {
        this.model = model;
        widthCandy = 30;
        heigthCandy = 30;
        update();
    }

    public void update(){
        getChildren().clear();
        for (Position position : model.getBoardSize().positions()) {
            Node candy = makeCandyShape(position, model.getCandyFromPosition(position));
            getChildren().addAll(candy);
        }
    }

    public Position getPositionOfClicked(MouseEvent me){
        Position position = null;
        int row = (int) me.getX()/heigthCandy;
        int column = (int) me.getY()/widthCandy;
        System.out.println(me.getX()+" - "+me.getY()+" - "+row+" - "+column);
        if (row < model.getBoardSize().width() && column < model.getBoardSize().height()){
            position = new Position(row, column , model.getBoardSize());
        }
        return position;
    }

    public Node makeCandyShape(Position position , Candy candy){
        if( candy instanceof normalCandy){
            switch(((normalCandy) candy).color()){
                case 0:
                    return new Circle(position.x()* widthCandy + widthCandy/2, position.y() * heigthCandy + heigthCandy/2, widthCandy*.45 , Color.BLUE);
                case 1:
                    return new Circle(position.x() *widthCandy + widthCandy/2 , position.y() * heigthCandy + heigthCandy/2, widthCandy*.45 , Color.RED);
                case 2:
                    return new Circle(position.x() * widthCandy + widthCandy/2, position.y() * heigthCandy + heigthCandy/2, widthCandy*.45 , Color.GREEN);
                case 3:
                    return new Circle(position.x() * widthCandy + widthCandy/2, position.y() * heigthCandy + heigthCandy/2, widthCandy*.45 , Color.YELLOW);

                default:
                    throw new IllegalStateException("Unexpected value: " + ((normalCandy) candy).color());
            }
        }
        else{
            switch (candy) {
                case MultiCandy multiCandy -> {
                    Rectangle rechthoek = new Rectangle(position.x() * widthCandy , position.y() *heigthCandy, widthCandy *0.9 , heigthCandy*0.9);
                    rechthoek.setFill(Color.RED);
                    return rechthoek;
                }
                case RareCandy rareCandy -> {
                    Rectangle rechthoek = new Rectangle(position.x() * widthCandy , position.y() *heigthCandy, widthCandy *0.9, heigthCandy*0.9);
                    rechthoek.setFill(Color.BLUE);
                    return rechthoek;
                }
                case RowSnapper rowSnapper -> {
                    Rectangle rechthoek = new Rectangle(position.x() * widthCandy , position.y() *heigthCandy , widthCandy*0.9, heigthCandy*0.9);
                    rechthoek.setFill(Color.GREEN);
                    return rechthoek;
                }
                case TurnMaster turnMaster -> {
                    Rectangle rechthoek = new Rectangle(position.x() * widthCandy , position.y() *heigthCandy, widthCandy*0.9 , heigthCandy*0.9);
                    rechthoek.setFill(Color.YELLOW);
                    return rechthoek;
                }
                default -> throw new IllegalStateException("Unexpected value: " + candy);
            }
        }
    }
}
