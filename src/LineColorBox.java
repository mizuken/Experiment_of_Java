import java.awt.Color;
import java.awt.event.*;

import javax.swing.*;


public class LineColorBox extends JComboBox {


	private static final long serialVersionUID = 1L;
	StateManager stateManager;//StateManager型の変数stateManager
	MyDrawing mydrawing;
	Mediator mediator;
	static Object[] combodata = {"Red", "Blue", "Green", "Black", "White","OtherColor"};
	
	public LineColorBox(StateManager stateManager){
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
				mediator.setLineColor(Color.red);
			}else if(color == "Blue"){
				mediator.setLineColor(Color.blue);
			}else if(color == "Green"){
				mediator.setLineColor(Color.green);
			}else if(color == "Black"){
				mediator.setLineColor(Color.black);
			}else if(color == "White"){
				mediator.setLineColor(Color.white);
			}else if(color == "OtherColor"){
				JColorChooser colorchooser = new JColorChooser();
				Color chosencolor = JColorChooser.showDialog(colorchooser, "色の選択", Color.white);
				mediator.setLineColor(chosencolor);
			}else{}
		mediator.repaint();	
		}
	}
}
