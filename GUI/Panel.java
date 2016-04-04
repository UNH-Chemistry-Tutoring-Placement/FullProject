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

    private IntField minGroupField;
    private IntField maxGroupField;
    private IntField belowMinField;
    private IntField aboveMaxField;
    private IntField possibleField;
    private IntField diffProfField;
    private IntField genderSoloField;
    private IntField runTimeField;

    public Panel(Container parent){
        super();

        this.parent = parent;

        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx =0;
        c.gridy =0;
        c.fill = GridBagConstraints.HORIZONTAL;
        JPanel step1Panel = new JPanel();
        step1Panel.setLayout(new GridBagLayout() );
        JLabel step1 = new JLabel("Step 1: Select an input file");
        step1Panel.add(step1,c);

        JPanel step1InputPanel = new JPanel();
        step1InputPanel.setLayout(new BoxLayout(step1InputPanel,BoxLayout.LINE_AXIS));
        c.gridx = 0;
        c.gridy = 0;

        c.fill = GridBagConstraints.HORIZONTAL;

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
        step1InputPanel.add(inputBrowse,c);
        c.gridx = 1;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        step1InputPanel.add(inputFileField,c);



        JLabel space = new JLabel(" ");
        JLabel space2 = new JLabel(" ");
        JLabel step2 = new JLabel("Step 2: Set penalties", JLabel.LEFT);
        JLabel minGroup = new JLabel("Minimum Group Size:");
        JLabel maxGroup = new JLabel("Maximum Group Size:");
        JLabel belowMinPenalty = new JLabel("Below Mininum Penalty:");
        JLabel aboveMaxPenalty = new JLabel("Above Maximum Penalty:");
        JLabel possiblePenalty = new JLabel("Possible Choice Penalty:");
        JLabel diffProfPenalty = new JLabel("Different Professor Penalty:");
        JLabel genderSoloPenalty = new JLabel("Gender Solo Penalty:");
        JLabel runTime = new JLabel("Runtime (seconds):");

        JPanel step2Panel = new JPanel();
        step2Panel.setLayout(new BoxLayout(step2Panel, BoxLayout.LINE_AXIS));
        step2Panel.add(step2);




        minGroupField = new IntField(5);
        maxGroupField = new IntField(10);
        belowMinField = new IntField(100);
        aboveMaxField = new IntField(100);
        possibleField = new IntField(10);
        diffProfField = new IntField(10);
        genderSoloField = new IntField(5);
        runTimeField = new IntField(10);

        minGroupField.setMaximumSize(new Dimension(500,20));
        maxGroupField.setMaximumSize(new Dimension(500,20));
        belowMinField.setMaximumSize(new Dimension(500,20));
        aboveMaxField.setMaximumSize(new Dimension(500,20));
        possibleField.setMaximumSize(new Dimension(500,20));
        diffProfField.setMaximumSize(new Dimension(500,20));
        genderSoloField.setMaximumSize(new Dimension(500,20));
        runTimeField.setMaximumSize(new Dimension(500,20));

        InputPanel minGroupPanel = new InputPanel(minGroup,minGroupField);
        InputPanel maxGroupPanel = new InputPanel(maxGroup, maxGroupField);
        InputPanel belowMinPanel = new InputPanel(belowMinPenalty, belowMinField);
        InputPanel aboveMaxPanel = new InputPanel(aboveMaxPenalty,aboveMaxField);
        InputPanel possiblePanel = new InputPanel(possiblePenalty,possibleField);
        InputPanel diffProfPanel = new InputPanel(diffProfPenalty, diffProfField);
        InputPanel genderSoloPanel = new InputPanel(genderSoloPenalty, genderSoloField);
        InputPanel runTimePanel = new InputPanel(runTime, runTimeField);

        JLabel step3 = new JLabel("Step 3: Select/create an output file", JLabel.LEFT);
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

        JPanel step3Panel = new JPanel();
        step3Panel.setLayout(new BoxLayout(step3Panel, BoxLayout.LINE_AXIS));
        step3Panel.add(step3);
        JPanel step3InputPanel = new JPanel();
        step3InputPanel.setLayout(new BoxLayout(step3InputPanel, BoxLayout.LINE_AXIS));
        step3InputPanel.add(outputBrowse);
        step3InputPanel.add(outputFileField);

        JButton run = new JButton("Run");
        run.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solve();
            }
        });


        add(step1Panel);
        add(step1InputPanel);
        add(space);

        add(step2Panel);
        add(minGroupPanel);
        add(maxGroupPanel);
        add(belowMinPanel);
        add(aboveMaxPanel);
        add(possiblePanel);
        add(diffProfPanel);
        add(genderSoloPanel);
        add(runTimePanel);
        add(space2);

        add(step3Panel);
        add(step3InputPanel);

        add(run);


    }

    private boolean checkInput(){
        return inputFileName != null && outputFile != null;
    }

    private void solve(){

        if( !checkInput() ){
            JOptionPane.showMessageDialog(null,"Please specify both an input and output file.");
            return;
        }

        String info = makeObjFile() + classInfo + io.getStudentFile_string();
        int runTime = Integer.parseInt(runTimeField.getText());

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

    private class InputPanel extends JPanel{
        public JLabel label;
        public JTextField field;

        public InputPanel(JLabel label, JTextField field){
            super();
            this.label = label;
            this.field = field;

            GridBagConstraints c = new GridBagConstraints();

            c.gridwidth = 1;

            setLayout( new GridBagLayout() );
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 0.5;
            c.gridx = 0;
            c.gridy = 0;
            add(this.label,c);

            c.weightx = 0.5;
            c.gridx = 0;
            c.gridy = 1;
            add(this.field,c);

        }
    }

    private class InputPanelCollection extends JPanel{
        private ArrayList<InputPanel> panels;
        private JLabel title;

        public InputPanelCollection(String name){
            panels = new ArrayList<>();
            title = new JLabel(name);
        }

        public void addPanel( InputPanel p ){
            panels.add(p);
        }
    }

}
