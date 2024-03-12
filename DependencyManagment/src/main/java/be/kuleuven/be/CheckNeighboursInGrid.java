package be.kuleuven.be;
import java.util.ArrayList;
import java.util.Collections;


public class CheckNeighboursInGrid {

    /**
     * This method takes a 1D Iterable and an element in the array and gives back an iterable containing the indexes of all neighbours with the same value as the specified element
     *@return - Returns a 1D Iterable of ints, the Integers represent the indexes of all neighbours with the same value as the specified element on index 'indexToCheck'.
     *@param grid - This is a 1D Iterable containing all elements of the grid. The elements are integers.
     *@param width - Specifies the width of the grid.
     *@param height - Specifies the height of the grid (extra for checking if 1D grid is complete given the specified width)
     *@param indexToCheck - Specifies the index of the element which neighbours that need to be checked
     */
    public static Iterable<Integer> getSameNeighboursIds(Iterable<Integer> grid,int width, int height, int indexToCheck){

        /* Stel element op positie x,y ==> alle elementen rondom controleren

        | (x-1,y-1)  (x , y-1)  (x+1 , y-1) |
        | (x-1, y )  ( x , y )  ( x+1 , y ) |
        | (x-1,y+1)  ( x ,y+1)  (x+1 , y+1) |
         */

        //write you code below
        ArrayList<Integer> result = new ArrayList<>();

        for(Integer integer : grid){
            result.add(integer);
        }

        int valueToCheck = result.get(indexToCheck);
        int posVertical = indexToCheck/width;
        int posHorizontal = indexToCheck -(posVertical *height);


        return getNeighbours(width,posVertical,posHorizontal, valueToCheck , result);

    }
    private static ArrayList<Integer> getNeighbours(int width, int VerticalPosition, int horizontalPosition, int number, ArrayList<Integer> list) {
        ArrayList<Integer> buren = new ArrayList<>();
        for (int i = -1 ; i<=1 ;i++){
            for(int j = -1; j<=1; j++){
                if(i ==0 && j==0){
                }
                else{
                    int index = (VerticalPosition +j)*width +(horizontalPosition + i);
                    try {
                        if (list.get(index) == number) {
                            buren.add(index);
                        }
                    }
                    catch (IndexOutOfBoundsException ignored) {
                    }
                }

            }
        }
        Collections.sort(buren);
        return buren;
    }

}