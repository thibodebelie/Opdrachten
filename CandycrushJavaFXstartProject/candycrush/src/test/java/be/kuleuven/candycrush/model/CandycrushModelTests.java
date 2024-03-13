package be.kuleuven.candycrush.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CandycrushModelTests {

    @Test
    public void gegeven_wanneer_dan(){
        CandycrushModel model = new CandycrushModel("Thibo");
        String result = model.getSpeler();
        assert (result.equals("Thibo"));
    }

    @Test
    public void initiele_scor(){
        CandycrushModel model = new CandycrushModel("Thibo");
        int score = model.getScore();
        assert (score == 0);
    }
    @Test
    public void test_width(){
        CandycrushModel model = new CandycrushModel("Thibo",5,5);
        int width = model.getWidth();
        assert (width == 5);
    }
    @Test
    public void test_Height(){
        CandycrushModel model = new CandycrushModel("Thibo",5,5);
        int height = model.getHeight();
        assert (height ==5);
    }

    @Test
    public void test_lengte(){
        CandycrushModel model = new CandycrushModel("Test" , 4,4);
        int lengte = model.getSpeelbord().size();
        assert (lengte == 16);
    }

    @Test
    public void test_getIndex(){
        CandycrushModel model = new CandycrushModel("Test", 5,5);
        int index = model.getIndexFromRowColumn(2,3);
        assert (index == 13);
    }

    @Test
    public void test_verhoog_score(){
        CandycrushModel model = new CandycrushModel("Test");
        model.verhoogScore();
        assert(model.getScore() == 1);
    }
    @Test
    public void test_verhoog_score_tien_keer(){
        CandycrushModel model = new CandycrushModel("Test");
        for (int i =0 ; i<10;i++){
            model.verhoogScore();
        }
        assert(model.getScore() == 10);
    }

    @Test
    public void test(){
        CandycrushModel model = new CandycrushModel("Test",5,5);
        ArrayList<Integer> orignalSpeelbord = new ArrayList<>(model.getSpeelbord());
        Random random = new Random();
        int index = random.nextInt(25);
        model.candyWithIndexSelected(index);
        assertNotEquals(orignalSpeelbord , model.getSpeelbord());
    }

    @Test
    public void setAllValuesToZero(){
        CandycrushModel model = new CandycrushModel("Test" , 4,4);
        for (int i = 0; i < model.getSpeelbord().size(); i++) {
            model.getSpeelbord().set(i, 0);
        }
        ArrayList<Integer> zeroList = new ArrayList<>();
        for (int i = 0; i < model.getSpeelbord().size(); i++) {
            zeroList.add(0);
        }
        assertEquals(model.getSpeelbord(), zeroList);

    }
}
