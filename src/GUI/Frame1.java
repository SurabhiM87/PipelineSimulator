package GUI;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Frame1 extends JFrame implements ActionListener {
	
	JLabel answer = new JLabel("");
	JPanel pane = new JPanel();
	JButton runPipelineBtn  = new JButton("Run");
	  Frame1() // the frame constructor method
	  {
		  
	    super("Pipeline Simulator"); 
	    setBounds(500,300,300,100);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    Container con = this.getContentPane(); // inherit main frame
	    con.add(pane); // add the panel to frame
	    runPipelineBtn.setMnemonic('P'); // associate hotkey to button
	    
	    runPipelineBtn.addActionListener(this);
	    runPipelineBtn.setLocation(0,0);
	    pane.add(runPipelineBtn); 
	   
//	    runPipelineBtn.requestFocus();
	    // pane.add(someWidget);
	    setVisible(true); // display this frame
	  }
	  public void actionPerformed(ActionEvent event)
	  {
	    Object source = event.getSource();
	    if (source == runPipelineBtn)
	    {
	      answer.setText("Button pressed!");
	      JOptionPane.showMessageDialog(null,"I hear you!","Message Dialog", JOptionPane.PLAIN_MESSAGE); 
	      setVisible(true);  // show something
	    }
	  }
	  public static void main(String args[]) {
		  new Frame1();
//		  Frame1.setDefaultLookAndFeelDecorated(true);
	  }

}
