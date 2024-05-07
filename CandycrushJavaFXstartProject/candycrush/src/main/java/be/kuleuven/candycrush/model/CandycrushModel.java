package be.kuleuven.candycrush.model;


import be.kuleuven.candycrush.model.candies.*;
import com.google.common.collect.Streams;
import javafx.geometry.Pos;
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
        if (candy instanceof EmptyCandy){
            return false;
        }
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

    public void clearMatch(List<Position> match){
        if (!match.isEmpty() && match != null ){
            board.replaceCellAt(match.getFirst(),new EmptyCandy());
            clearMatch(match.subList(1,match.size()));
        }
    }
    public void fallDownTo(Position pos) {
        if (pos.y() > 0) { //Niet bovenste rij
            int abovePositionY = pos.y() - 1;
            if (abovePositionY >= 0 && abovePositionY < board.getBoardSize().height()) { //Check for valid range
                Position abovePosition = new Position(pos.x(), abovePositionY, board.getBoardSize());
                if (board.getCellAt(abovePosition) != null) {
                    board.replaceCellAt(pos, board.getCellAt(abovePosition));
                    board.replaceCellAt(abovePosition, new EmptyCandy());
                    fallDownTo(abovePosition);
                }
            }
        }
    }

    public boolean updateBoard(){
        var match = findAllMatches();

        if(!match.isEmpty()){
            match.forEach(this::clearMatch);
            match.forEach(list -> list.forEach(this::fallDownTo));
            var newMatch = findAllMatches();
            return updateBoard(newMatch);
        }
        return false;
    }

    public boolean updateBoard(Set<List<Position>> match){
        if (match.isEmpty()){
            return true;
        }
        else{
            match.forEach(this::clearMatch);
            match.forEach(list -> list.forEach(this::fallDownTo));
            var newMatch = findAllMatches();
            if (newMatch.isEmpty()){
                return true;
            }
            else{
                return updateBoard(newMatch);
            }
        }

    }





}
