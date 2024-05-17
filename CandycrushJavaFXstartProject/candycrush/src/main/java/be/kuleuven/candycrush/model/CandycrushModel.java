package be.kuleuven.candycrush.model;


import be.kuleuven.candycrush.model.candies.*;
import com.google.common.collect.Streams;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CandycrushModel {
    private String speler;

    private int score =0;

    private Board<Candy> board;
    private BoardSize boardSize;



    public CandycrushModel(String speler) {
        this.speler = speler;
        boardSize = new BoardSize(10,10);
        this.board = new Board<>(boardSize, this::getRandomCandy);
    }
    public CandycrushModel(String speler, BoardSize boardsize) {
        this.speler = speler;
        this.boardSize = boardsize;
        this.board = new Board<>(boardSize, this::getRandomCandy);

    }

    public String getSpeler() {
        return speler;
    }


    public BoardSize getBoardSize(){
        return board.getBoardSize();
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
            default -> new NormalCandy(random.nextInt(4));
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

    // Backtracking code:
    public static CandycrushModel createBoardFromString(String configuration) {
        var lines = configuration.toLowerCase().lines().toList();
        BoardSize size = new BoardSize(lines.size(), lines.getFirst().length());
        var model = new CandycrushModel("Recursie", size); // deze moet je zelf voorzien
        for (int col = 0; col < lines.size(); col++) {
            var line = lines.get(col);
            for (int row = 0; row < line.length(); row++) {
                model.board.replaceCellAt(new Position(row, col, size), characterToCandy(line.charAt(row)));
            }
        }
        return model;
    }
    private static Candy characterToCandy(char c) {
        return switch(c) {
            case '.' -> null;
            case 'o' -> new NormalCandy(0);
            case '*' -> new NormalCandy(1);
            case '#' -> new NormalCandy(2);
            case '@' -> new NormalCandy(3);
            default -> throw new IllegalArgumentException("Unexpected value: " + c);
        };
    }

    public  PositionScorePair solve() {
        PositionScorePair currentSwaps = new PositionScorePair(new ArrayList<>(), 0);
        PositionScorePair highestSwaps = new PositionScorePair(new ArrayList<>(), 0);
        return findMaximzeScore(currentSwaps, highestSwaps);
    }

    private  PositionScorePair findMaximzeScore(PositionScorePair current, PositionScorePair heighest) {
        if(this.findAllSwaps().isEmpty()) {
            return this.getBestResult(current, heighest);
        }
        for(PositionPairs pair : this.findAllSwaps()) {
            Board<Candy> boardCopy = new Board<>(this.board.getBoardSize(), this::getRandomCandy);
            this.board.copyTo(boardCopy);

            PositionScorePair copyCurrent = new PositionScorePair(new ArrayList<>(), 0);
            copyCurrent.changeScore(current.score());
            copyCurrent.pairs().addAll(current.pairs());

            this.board = swapPositions(pair.position1(), pair.position2(), this.board);
            current.pairs().add(pair);
            updateBoard();

            current = current.changeScore(this.score(this.board));
            heighest = findMaximzeScore(current, heighest);

            board = boardCopy;
            current = copyCurrent;
        }
        return heighest;
    }

    public PositionScorePair getBestResult(PositionScorePair current, PositionScorePair heighest) {
        if(current.score() > heighest.score()) {
            return current;
        }
        else if(current.score() == heighest.score()) {
            if(current.pairs().size() < heighest.pairs().size()) {
                return current;
            }
            else {
                return heighest;
            }
        }
        else {
            return heighest;
        }
    }

    public int score(Board<Candy> board){
        int score = 0;
        score = this.board.getList().values().stream()
                .filter(a -> a instanceof EmptyCandy).toList().size();
        return score;
    }

    public Set<PositionPairs> findAllSwaps() {
        Set<PositionPairs> positionPairs= new HashSet<>();
        var swaps = new int[][] {
                {0, -1}, {1,0}
        };
        for(Map.Entry<Position, Candy> poscan : board.getList().entrySet()) {
            for(var swap : swaps) {
                if(poscan.getKey().x() + swap[0] > board.boardSize.width() - 1 || poscan.getKey().x() + swap[0] < 0) {continue;}
                if(poscan.getKey().y() + swap[1] > board.boardSize.height() - 1|| poscan.getKey().y() + swap[1] < 0) {continue;}
                Position newPosition = new Position(poscan.getKey().x() + swap[0], poscan.getKey().y() + swap[1], this.getBoardSize());
                if (isValidSwap(poscan.getKey(), newPosition)) {
                    positionPairs.add(new PositionPairs(poscan.getKey(), newPosition));
                }
            }
        }
        return positionPairs;
    }

    private boolean isValidSwap(Position beginPosition, Position endPosition) {
        if(beginPosition.equals(endPosition)) {return false;}
        if(board.getCellAt(beginPosition) instanceof EmptyCandy || board.getCellAt(endPosition) instanceof EmptyCandy) {return false;}
        Board<Candy> boardCopy = new Board<>(this.board.getBoardSize(), this::getRandomCandy);
        this.board.copyTo(boardCopy);
        this.board = swapPositions(beginPosition, endPosition, this.board);
        if(!findAllMatches().isEmpty()) {
            this.board = boardCopy;
            return true;
        }
        this.board = boardCopy;
        return false;
    }


    private Board<Candy> swapPositions(Position beginPosition, Position endPosition, Board<Candy> board) {
        Candy tempCandy = board.getCellAt(endPosition);
        board.replaceCellAt(endPosition, board.getCellAt(beginPosition));
        board.replaceCellAt(beginPosition, tempCandy);
        return board;
    }



}
