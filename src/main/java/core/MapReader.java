package core;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MapReader {

    private final int[][] tiles;
    private final int rowNum;
    private final int colNum;

    public MapReader(int colNum, int rowNum){
        this.rowNum = rowNum;
        this.colNum = colNum;
        tiles = new int[rowNum][colNum];
    }

    public void readFromTxt(String filename){

        try{
            InputStream is = getClass().getResourceAsStream(filename);
            if (is == null) throw new AssertionError();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            for(int y = 0; y < rowNum; y++){

                String row = br.readLine();
                String[] numbers = row.split(" ");

                for (int x = 0; x < colNum; x++){
                    tiles[y][x] = Integer.parseInt(numbers[x]);
                }
            }
            br.close();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public int[][] getTiles() {
        return tiles;
    }
}
