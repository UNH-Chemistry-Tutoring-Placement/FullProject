package GUI;

import javax.swing.*;
import java.awt.*;

/**
 * Created by BradleySnay on 4/4/16.
 */
public class Frame extends JFrame {
    public Frame(String title){
        super(title);
        this.setPreferredSize(new Dimension(400,600));
        this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        Panel p = new Panel(this);
        //p.setPreferredSize(new Dimension(1000,1000));
        JScrollPane scrollPane = new JScrollPane(p);
       // scrollPane.setPreferredSize(new Dimension( 500,500));
        //p.setAutoscrolls(true);
        scrollPane.setViewportView(p);
        this.add(scrollPane);
        this.setVisible( true );
        this.pack();
    }
}
