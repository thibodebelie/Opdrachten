package be.kuleuven.candycrush.model;

import be.kuleuven.CheckNeighboursInGrid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class CandycrushModel {
    private String speler;
    private ArrayList<Integer> speelbord;
    private int width;
    private int height;
    private int score;



    public CandycrushModel(String speler , int width , int height) {
        this.speler = speler;
        speelbord = new ArrayList<>();
        this.width = width;
        this.height = height;

        for (int i = 0; i < width*height; i++){
            Random random = new Random();
            int randomGetal = random.nextInt(5) + 1;
            speelbord.add(randomGetal);
        }
    }
    public CandycrushModel(String speler) {
        this.speler = speler;
        speelbord = new ArrayList<>();
        width = 10;
        height = 10;

        for (int i = 0; i < width*height; i++){
            Random random = new Random();
            int randomGetal = random.nextInt(5) + 1;
            speelbord.add(randomGetal);
        }
    }



    public static void main(String[] args) {
        CandycrushModel model = new CandycrushModel("Thibo",10,10);
        int i = 1;
        Iterator<Integer> iter = model.getSpeelbord().iterator();
        while(iter.hasNext()){
            int candy = iter.next();
            System.out.print(candy);
            if(i% model.getWidth()==0){
                System.out.print("\n");
                i = 1;
            }
            i++;
        }
        System.out.print("\n");

    }
    public String getSpeler() {
        return speler;
    }

    public ArrayList<Integer> getSpeelbord() {
        return speelbord;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void candyWithIndexSelected(int index){
        //TODO: update method so it also changes direct neighbours of same type and updates score
        if (index != -1){
            Random random = new Random();
            int randomGetal = random.nextInt(5) + 1;
            speelbord.set(index,randomGetal);
        }else{
            System.out.println("model:candyWithIndexSelected:indexWasMinusOne");
        }
    }

    public void changeNeigbours(int indexToCheck){
        ArrayList<Integer> buren = (ArrayList<Integer>) CheckNeighboursInGrid.getSameNeighboursIds(speelbord,width, height,indexToCheck);
        if(buren.size() >= 3){
            candyWithIndexSelected(indexToCheck);
            for(Integer i : buren){
                candyWithIndexSelected(i);
            }
        }
    }

    public int getIndexFromRowColumn(int row, int column) {
        return column+row*width;
    }
    public void verhoogScore(){
        score++;
    }

    public int getScore(){
        return this.score;
    }
}
