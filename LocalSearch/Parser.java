package LocalSearch;

public class Parser {

    private String[] input;
    private int index;

    public Parser(String _input) {
        input = _input.split("\n");
        index = 0;
    }

    public String getLine() {
        String line;
        while(true) {
            try {
                line = input[index++];
            } catch(ArrayIndexOutOfBoundsException e) {
                return "";
            }

            if(line.indexOf("#") != 0) {
                return line;
            }
        }
    }

    public String getSecondPart() {
        String line = this.getLine();
        int _index = line.indexOf(": ");

        return line.substring(_index + 2);
    }

    public int getSecondPartAsNumber() {
        return Integer.parseInt(getSecondPart());
    }
}
