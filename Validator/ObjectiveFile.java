import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;


public class ObjectiveFile {

    private HashMap<String,String> contents;

    private final String format = "ObjectiveFile Function Format";
    private final String description = "Description";
    private final String minGroup = "min group size";
    private final String maxGroup = "max group size";
    private final String belowMin = "below min penalty";
    private final String aboveMax = "above max penalty";
    private final String possiblePenalty = "possible choice penalty";
    private final String diffProfPenalty = "diff professor penalty";
    private final String genderSoloPenalty = "gender solo penalty";


    public ObjectiveFile(File objectveFile){
        contents = new HashMap<>();
        parseObjective(objectveFile);
    }

    public String printAll(){

        StringBuilder builder = new StringBuilder();
        builder.append(getFormat());
        builder.append('\n');
        builder.append(getDescription());
        builder.append('\n');
        builder.append(getMinGroupSize());
        builder.append('\n');
        builder.append(getMaxGroupSize());
        builder.append('\n');
        builder.append(getBelowMinPenalty());
        builder.append('\n');
        builder.append(getAboveMaxPenalty());
        builder.append('\n');
        builder.append(getPossibleChoicePenalty());
        builder.append('\n');

        return builder.toString();
    }

    public int getFormat( ){
        return Integer.parseInt(contents.get(format));
    }

    public String getDescription(){
        return contents.get(description);
    }

    public int getMinGroupSize(){
        return Integer.parseInt(contents.get(minGroup));
    }

    public int getMaxGroupSize(){
        return Integer.parseInt(contents.get(maxGroup));
    }

    public int getBelowMinPenalty(){
        return Integer.parseInt(contents.get(belowMin));
    }

    public int getAboveMaxPenalty(){
        return Integer.parseInt(contents.get(aboveMax));
    }

    public int getPossibleChoicePenalty(){
        return Integer.parseInt(contents.get(possiblePenalty));
    }

    public int getDiffProfessorPenalty(){
        return Integer.parseInt(contents.get(diffProfPenalty));
    }

    public int getGenderSoloPenalty(){
        return Integer.parseInt(contents.get(genderSoloPenalty));
    }

    private void parseObjective( File objectiveFile ){
        try {
            Scanner objScanner = new Scanner(objectiveFile);
            while( objScanner.hasNextLine() ){
                String nextLine =  objScanner.nextLine();
                if( nextLine.startsWith("#"))
                    continue;
                Scanner lineScanner = new Scanner(nextLine).useDelimiter(":");
                contents.put(lineScanner.next().trim(),lineScanner.next().trim());
            }
        } catch ( FileNotFoundException e ){
            System.err.println("File " + objectiveFile.getName() + "not found.");
        }
    }

    public static void main( String[] args ){
        System.out.println( new ObjectiveFile(new File( args[0] )).printAll());
    }
}
