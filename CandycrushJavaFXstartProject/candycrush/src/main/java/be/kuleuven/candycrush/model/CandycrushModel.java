package be.kuleuven.candycrush.model;


import be.kuleuven.candycrush.model.candies.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class CandycrushModel {
    private String speler;
    private ArrayList<Candy> speelbord;

    private BoardSize boardSize;
    private int score;



    public CandycrushModel(String speler) {
        this.speler = speler;
        speelbord = new ArrayList<>();
        this.boardSize = new BoardSize(10,10);
        for (int i = 0; i < boardSize.width()* boardSize.height(); i++){
            speelbord.add(getRandomCandy());
        }
    }

    public String getSpeler() {
        return speler;
    }

    public ArrayList<Candy> getSpeelbord() {
        return speelbord;
    }

    public BoardSize getBoardSize(){
        return boardSize;
    }

    public void candyWithIndexSelected(Position position){
        //TODO: update method so it also changes direct neighbours of same type and updates score
        if (position.toIndex() != -1){
            speelbord.set(position.toIndex(),getRandomCandy());
        }else{
            System.out.println("model:candyWithIndexSelected:indexWasMinusOne");
        }
    }

    public void verhoogScore(){
        score++;
    }

    public int getScore(){
        return this.score;
    }

    private Candy getRandomCandy(){
        Random random = new Random();
        int rand_int = random.nextInt(9) + 1;
        Candy randomCandy = switch (rand_int) {
            case 1 -> new MultiCandy();
            case 2 -> new RareCandy();
            case 3 -> new RowSnapper();
            case 4 -> new TurnMaster();
            default -> new normalCandy(random.nextInt(4));
        };

        return randomCandy;
    }

    public Candy getCandyFromPosition(Position position){
        return speelbord.get(position.toIndex());
    }


    public void changeNeighbours(Position position){
        ArrayList<Position> Neighbours = (ArrayList<Position>) getSameNeighbourPositions(position);
        if(Neighbours.size() >= 4) {
            candyWithIndexSelected(position);
            score++;
            for (Position pos : Neighbours) {
                candyWithIndexSelected(pos);
                score++;
            }
        }
        }
    public Iterable<Position> getSameNeighbourPositions(Position position){
        ArrayList <Position> neighbours = new ArrayList<Position>();
        for(Position p : position.neighbourPosition()){
            if (getCandyFromPosition(p).equals(getCandyFromPosition(position))){
                neighbours.add(p);
                score++;
            }
        }
        return neighbours;
    }

}
