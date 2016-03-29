package LocalSearch;

import java.util.ArrayList;
import java.util.HashMap;

public class LocalSearch {

    //main method to run everything
    public static void main(String[] args) {
        FileData fileData = new FileData();
        if(args.length != 2) {
            System.err.println("Usage: time outputFile");
            return;
        }

        int grade = 0;
        HashMap<String, ArrayList<String>> assignments;

        fileData.load();

        //fileData.out(grade, assignments, args[1]);
    }
}