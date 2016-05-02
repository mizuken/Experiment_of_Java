//import java.awt.*;
//import java.awt.BorderLayout;
import java.awt.Color;
//import java.awt.Component;
import java.awt.event.*;

import javax.swing.*;

//import java.util.Enumeration;
//import java.awt.BorderLayout;

public class ColorBox extends JComboBox{

	private static final long serialVersionUID = 1L;
	StateManager stateManager;//StateManager型の変数stateManager
	MyDrawing mydrawing;
	Mediator mediator;
	static Object[] combodata = {"Red", "Blue", "Green", "Black", "White","OtherColor"};
	
	public ColorBox(StateManager stateManager){
		super(combodata);
		this.stateManager = stateManager;
		this.mediator = stateManager.canvas.getMediator();
		addActionListener(new ColorListener());
		
	}
	
	class ColorListener implements ActionListener {
		String color;
		Color chosencolor;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO 自動生成されたメソッド・スタブ
			color = (String)getSelectedItem();
			if(color == "Red"){
				mediator.setColor(Color.red);
			}else if(color == "Blue"){
				mediator.setColor(Color.blue);
			}else if(color == "Green"){
				mediator.setColor(Color.green);
			}else if(color == "Black"){
				mediator.setColor(Color.black);
			}else if(color == "White"){
				mediator.setColor(Color.white);
			}else if(color == "OtherColor"){
				JColorChooser colorchooser = new JColorChooser();
				Color chosencolor = JColorChooser.showDialog(colorchooser, "色の選択", Color.white);
				mediator.setColor(chosencolor);
			}else{}
		mediator.repaint();	
		}
	}

}

