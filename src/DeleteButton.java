
//import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;

import javax.swing.*;

public class DeleteButton extends JButton {
	
	private static final long serialVersionUID = 1L;
	StateManager statemanager;
	Mediator mediator;
	MyDrawing d;
	
	public DeleteButton(Mediator mediator){
		super("Delete");
		addActionListener(new DeleteListener());
		this.mediator = mediator;
	}

	class DeleteListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			//ここで選択された図形を選ぶ
			Enumeration<MyDrawing> se = mediator.selectedDrawingsElements();
			while(se.hasMoreElements()){
				mediator.removeDrawing(se.nextElement());
				}
			//再描写
			mediator.repaint();
		}
	}
}