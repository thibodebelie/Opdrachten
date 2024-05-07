package be.kuleuven.candycrush.model;


import be.kuleuven.candycrush.model.candies.*;
import com.google.common.collect.Streams;
import javafx.scene.control.skin.SliderSkin;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CandycrushModel {
    private String speler;

    private int score;

    private Board<Candy> board;



    public CandycrushModel(String speler) {
        this.speler = speler;
        this.board = new Board<>(new BoardSize(10 ,10), this::getRandomCandy);
    }

    public String getSpeler() {
        return speler;
    }


    public BoardSize getBoardSize(){
        return board.boardSize;
    }

    public void candyWithIndexSelected(Position position){
        //TODO: update method so it also changes direct neighbours of same type and updates score
        if (position.toIndex() != -1){
            board.replaceCellAt(position , getRandomCandy(null));
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

    private Candy getRandomCandy(Position position){
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
        return board.getCellAt(position);
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

    public Set<List<Position>> findAllMatches(){
        var streamHor = horizontalStartingPosition()
                .map(this::longestMatchToRight)
                .filter(list->list.size() >=3);
        var streamVer = verticalStartingPosition()
                .map(this::longestMatchDown)
                .filter(list->list.size() >=3);
        return Streams.concat(streamHor,streamVer).collect(Collectors.toSet());
    }

    public boolean firstTwoHaveSameCandy(Candy candy, Stream<Position> positions){
        return positions.limit(2).allMatch(position -> candy.equals(getCandyFromPosition(position)));
    }


    public Stream<Position> horizontalStartingPosition() {
        return IntStream.range(0, getBoardSize().width() * getBoardSize().height())
                .mapToObj(i -> new Position(i % getBoardSize().width(), i / getBoardSize().width(), this.getBoardSize()))
                .filter(pos -> pos.x() >= 0 && firstTwoHaveSameCandy(getCandyFromPosition(pos), pos.walkRight()));
    }

    public Stream<Position> verticalStartingPosition() {
        return IntStream.range(0, getBoardSize().width() * getBoardSize().height())
                .mapToObj(i -> new Position(i % getBoardSize().width(), i / getBoardSize().width(), this.getBoardSize()))
                .filter(pos -> pos.y() >= 0 && firstTwoHaveSameCandy(getCandyFromPosition(pos), pos.walkDown()));
    }


    public List<Position> longestMatchToRight(Position pos){
        return pos.walkRight().takeWhile(position -> getCandyFromPosition(position).equals(getCandyFromPosition(pos)))
                .collect(Collectors.toList());
    }
    public List<Position> longestMatchDown(Position pos){
        return pos.walkDown().takeWhile(position -> getCandyFromPosition(position).equals(getCandyFromPosition(pos)))
                .collect(Collectors.toList());
    }

}
