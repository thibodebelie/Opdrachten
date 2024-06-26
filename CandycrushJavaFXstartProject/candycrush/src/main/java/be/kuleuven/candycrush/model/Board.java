package be.kuleuven.candycrush.model;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimaps;
import javafx.geometry.Pos;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import com.google.common.collect.Multimap;

public class Board <T>{

    private volatile Map<Position , T> list = new ConcurrentHashMap<Position, T>() {
    };

    private volatile Multimap<T, Position> reverseList = ArrayListMultimap.create();

    public BoardSize boardSize;

    public Board(BoardSize boardSize, Function<Position , T> CellCreator) {
        this.boardSize = boardSize;
        fill(CellCreator);
    }

    public BoardSize getBoardSize() {
        return boardSize;
    }

    public Multimap<T, Position> getReverseList() {
        return reverseList;
    }

    public Map<Position, T> getList() {
        return list;
    }

    public synchronized T getCellAt(Position position) {
        if (position.toIndex() <= list.size()) {
            return list.get(position);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }
    public synchronized void replaceCellAt(Position position, T newCell){
        if (position.toIndex() <= list.size()) {
            T temp = list.get(position);
            reverseList.remove(temp , position);
            reverseList.put(newCell , position);
            list.replace(position,newCell);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }
    public synchronized void fill(Function<Position , T> cellCreator){
        for(int i = 0 ; i< boardSize.height();i++){
            for(int j = 0; j <boardSize.width();j++){
                Position position = new Position(i,j , getBoardSize());
                list.put(position , cellCreator.apply(position));
                reverseList.put(cellCreator.apply(position), position);
            }
        }
    }

    public synchronized void copyTo(Board<T> otherBoard){
        if(boardSize.equals(otherBoard.boardSize)){
            for(int i = 0; i< boardSize.height(); i++){
                for(int j = 0 ; j< boardSize.width() ; j++){
                    Position position = new Position(i,j, boardSize);
                    otherBoard.replaceCellAt(position, getCellAt(position));
                }
            }
        }
        else{
            throw new IllegalArgumentException("Different boardsize");
        }
    }
    public synchronized List<Position> getPositionsOfElement(T cell){
        return Collections.unmodifiableList((List<Position>) reverseList.get(cell));
    }

}
