package be.kuleuven.candycrush.model;

import be.kuleuven.candycrush.model.candies.*;

import java.util.Random;

public class MultithreadingClient extends Thread {
    private Board<Candy> board= new Board<>(new BoardSize(5,5), this::getRandomCandy);
    private Thread thread1;
    private Thread thread2;

    public void run(){
        Random random = new Random();
        while (true){
            int x = random.nextInt(board.getBoardSize().width());
            int y = random.nextInt(board.getBoardSize().height());
            var candy = getRandomCandy(null);
            board.replaceCellAt(new Position(x, y, board.getBoardSize()) , candy);
            System.out.println("The following thread: " + this.getName()+ " changed position: " + x + "," + y + ", To Candy: " + candy);
            try {
                Thread.sleep(random.nextLong(1000,5000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private  Candy getRandomCandy(Position position){
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

    public static void main(String[] args){
        MultithreadingClient thread1 = new MultithreadingClient();
        MultithreadingClient thread2 = new MultithreadingClient();
        thread1.start();
        thread2.start();
    }
}
