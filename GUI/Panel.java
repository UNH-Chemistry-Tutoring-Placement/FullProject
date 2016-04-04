package GUI;

import FileIO.StudentIO;
import LocalSearch.LocalSearch;
import Validator.Validate;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Panel extends JPanel {
    private Container parent;
    private String inputFileName, classInfo;
    private File outputFile;
    private StudentIO io;
    private ArrayList<TimePanel> timePanels;

    private JTextField minGroupField;
    private JTextField maxGroupField;
    private JTextField belowMinField;
    private JTextField aboveMaxField;
    private JTextField possibleField;
    private JTextField diffProfField;
    private JTextField genderSoloField;
    private JTextField runTimeField;

    public Panel(Container parent){
        super();

        this.parent = parent;

        setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));

        JLabel step1 = new JLabel("Step 1: Select an input file");
        JTextField inputFileField = new JTextField("No input file chosen");
        inputFileField.setMaximumSize(new Dimension(500,20));
        //inputFileField.setMinimumSize(new Dimension(getWidth(), 15));
        JButton inputBrowse = new JButton("Browse");
        inputBrowse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newFileName = StudentIO.getFileName();
                if( newFileName != null ){
                    inputFileName = newFileName;
                    inputFileField.setText( new File(inputFileName).getName());
                    doStudentIO();
                }
            }
        });
        JLabel space = new JLabel(" ");
        JLabel space2 = new JLabel(" ");
        JLabel step2 = new JLabel("Step 2: Set penalties");
        JLabel minGroup = new JLabel("Minimum Group Size:");
        JLabel maxGroup = new JLabel("Maximum Group Size:");
        JLabel belowMinPenalty = new JLabel("Below Mininum Penalty:");
        JLabel aboveMaxPenalty = new JLabel("Above Maximum Penalty:");
        JLabel possiblePenalty = new JLabel("Possible Choice Penalty:");
        JLabel diffProfPenalty = new JLabel("Different Professor Penalty:");
        JLabel genderSoloPenalty = new JLabel("Gender Solo Penalty:");
        JLabel runTime = new JLabel("Runtime (seconds):");

        minGroupField = new JTextField("5");
        maxGroupField = new JTextField("10");
        belowMinField = new JTextField("100");
        aboveMaxField = new JTextField("100");
        possibleField = new JTextField("10");
        diffProfField = new JTextField("10");
        genderSoloField = new JTextField("5");
        runTimeField = new JTextField("10");

        minGroupField.setMaximumSize(new Dimension(500,20));
        maxGroupField.setMaximumSize(new Dimension(500,20));
        belowMinField.setMaximumSize(new Dimension(500,20));
        aboveMaxField.setMaximumSize(new Dimension(500,20));
        possibleField.setMaximumSize(new Dimension(500,20));
        diffProfField.setMaximumSize(new Dimension(500,20));
        genderSoloField.setMaximumSize(new Dimension(500,20));
        runTimeField.setMaximumSize(new Dimension(500,20));

        JLabel step3 = new JLabel("Step 3: Select/create an output file");
        JButton outputBrowse = new JButton("Browse");
        JTextField outputFileField = new JTextField("No output file chosen");
        outputFileField.setMaximumSize(new Dimension(500,20));
        outputBrowse.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser saveDialog = new JFileChooser();
                saveDialog.showSaveDialog(null);
                File saveFile = saveDialog.getSelectedFile();
                if( saveFile != null ){
                    outputFile = saveFile;
                    outputFileField.setText(outputFile.getName());
                }
            }
        });

        JButton run = new JButton("Run");
        run.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solve();
            }
        });


        add(step1);
        add(inputBrowse);
        add(inputFileField);
        add(space);

        add(step2);
        add(minGroup);
        add(minGroupField);
        add(maxGroup);
        add(maxGroupField);
        add(belowMinPenalty);
        add(belowMinField);
        add(aboveMaxPenalty);
        add(aboveMaxField);
        add(possiblePenalty);
        add(possibleField);
        add(diffProfPenalty);
        add(diffProfField);
        add(genderSoloPenalty);
        add(genderSoloField);
        add(runTime);
        add(runTimeField);
        add(space2);

        add(step3);
        add(outputBrowse);
        add(outputFileField);

        add(run);


    }

    private void solve(){
        String info = makeObjFile() + classInfo + io.getStudentFile_string();
        int runTime;
        try{
            runTime = Integer.parseInt(runTimeField.getText());
        } catch( NumberFormatException e ){
            JOptionPane.showMessageDialog(null,"The given runtime is not an integer.");
            return;
        }

        LocalSearch localSearch = new LocalSearch(info, runTime );
        String solution = localSearch.solution();
        String allInfo = info + solution;

        Validate validate = new Validate(allInfo);
        validate.saveToCSV(outputFile);

        JOptionPane.showMessageDialog(null,"Success");
    }

    private String makeObjFile(){
        StringBuilder builder = new StringBuilder();
        builder.append("Objective Function Format: 3\n" +
                "Description: Init. Does not take impossible choice penalty or balance terms\n");
        builder.append("min group size: " + minGroupField.getText() + "\n");
        builder.append("max group size: " + maxGroupField.getText()+ "\n");
        builder.append("below min penalty: " + belowMinField.getText()+ "\n");
        builder.append("above max penalty: " + aboveMaxField.getText()+ "\n");
        builder.append("possible choice penalty: " + possibleField.getText()+ "\n");
        builder.append("diff professor penalty: " + diffProfField.getText()+ "\n");
        builder.append("gender solo penalty: " + genderSoloField.getText()+ "\n");

        return builder.toString();
    }

    private void doStudentIO(){
        String[] arg = {inputFileName};
        io = new StudentIO(arg, false);
        classInfo = io.makeClassTemplate();
        //addTimeFields();
    }
    private void addTimeFields(){
        HashMap<String, Pair<String,String>> taMap = io.timeToTAMap;
        ArrayList<String> times = io.getGroupTimes();
        timePanels = new ArrayList<>();

        for( String time: times){
            TimePanel timePanel = new TimePanel(time);
            timePanels.add(timePanel);
            add(timePanel);
        }
        updateUI();
    }

    private class TimePanel extends JPanel{
        public JLabel time, name, email;
        public JTextField nameField, emailField;

        public TimePanel(String time){
            super();
            GridBagConstraints c = new GridBagConstraints();

            c.gridwidth = 1;
            setLayout( new GridBagLayout() );
            this.time = new JLabel("Time: " + time);
            name = new JLabel("Name:");
            email = new JLabel("Email:");
            nameField = new JTextField();
            emailField = new JTextField();

            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 0.5;
            c.gridx = 0;
            c.gridy = 0;
            add(this.time,c);

            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 0.5;
            c.gridx = 0;
            c.gridy = 1;
            add(name,c);

            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1;
            c.gridx = 1;
            c.gridy = 1;
            add(nameField,c);

            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 0.5;
            c.gridx = 0;
            c.gridy = 2;
            add(email,c);

            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1;
            c.gridx = 1;
            c.gridy = 2;
            add(emailField,c);

        }
    }

}
