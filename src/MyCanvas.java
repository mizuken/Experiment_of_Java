
import java.util.*;
import java.awt.*;

import javax.swing.*;

public class MyCanvas extends JPanel
{	
	private static final long serialVersionUID = 1L;
	Mediator mediator;
	
	public MyCanvas(){
		this.mediator = new Mediator(this);
		setBackground(Color.white);
		setFocusable(true);
	}
	
	public Mediator getMediator(){
		return mediator;
	}
	
	public void paint( Graphics g) {
		super.paint(g);
		Enumeration<MyDrawing> e = mediator.drawingsElements();
		while(e.hasMoreElements()){
			MyDrawing d = e.nextElement();
			d.draw(g);
		}
		
	}
}
