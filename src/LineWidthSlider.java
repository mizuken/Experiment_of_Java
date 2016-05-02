
//import java.awt.*;
//import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

//import java.util.Enumeration;

//線の太さを変更するためのボタン
public class LineWidthSlider extends JSlider{
	
	private static final long serialVersionUID = 1L;
	StateManager statemanager;
	Mediator mediator;
	MyDrawing d;
	int width;
	
	public LineWidthSlider(Mediator mediator){
		
		addChangeListener(new LineWidthListener());
		this.mediator = mediator;
	}
	
	class LineWidthListener implements ChangeListener{
	@Override
		public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();
	    width = (int)source.getValue();
	    mediator.setLinewidth(width);
	    mediator.repaint();
	}
 }

}
