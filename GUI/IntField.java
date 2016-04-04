package GUI;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;


public class IntField extends JTextField{

    public IntField(int val){
        super();
        PlainDocument doc = (PlainDocument) getDocument();
        doc.setDocumentFilter(new IntFilter());
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                if(getText().compareTo("") == 0)
                    setText(val+"");
            }
        });
        setText(val+"");
    }


}
