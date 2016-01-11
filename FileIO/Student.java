import java.util.ArrayList;

public class Student {

    private int _year;
    private String _name, _email, _professor, _sex;
    private String _comments = "";
    private ArrayList<String> _goodTimes, _possibleTimes;

    public Student( String name, String email, String professor, String sex, int year, ArrayList<String> goodTimes, ArrayList<String> possibleTimes ){
        _name = name;
        _email = email;
        _professor = professor;
        _sex =sex;
        _year = year;
        _goodTimes = goodTimes;
        _possibleTimes = possibleTimes;
    }

    public void setComment( String comment ){
        _comments = comment;
    }

    public String print(){

        StringBuilder builder = new StringBuilder();

        builder.append("Name: ").append( _name ).append('\n')
                .append("Email: ").append( _email ). append('\n')
                .append("Professor: ").append(_professor).append('\n')
                .append("Year: ").append(_year).append('\n')
                .append("Sex: "). append(_sex).append('\n')
                .append("Number of good times: "). append(_goodTimes.size()).append('\n');

        for( String s: _goodTimes){
            builder.append(s).append('\n');
        }
        builder.append("Number of possible times: "). append(_possibleTimes.size()).append('\n');

        for( String s: _possibleTimes ){
            builder.append(s).append('\n');
        }
        return builder.toString();

    }

    public String getName(){
        return _name;
    }
    public int getYear(){
        return _year;
    }
    public String getEmail(){
        return _email;
    }
    public String getLectureTime(){
        return _professor;
    }
    public String getSex(){
        return _sex;
    }
    public ArrayList<String> getGoodTimes(){
        return _goodTimes;
    }
    public ArrayList<String> getPossibleTimes(){
        return _possibleTimes;
    }
    public String getComments(){
        return _comments;
    }
}
