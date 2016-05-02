
//import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class DashCheck extends JCheckBox{

	private static final long serialVersionUID = 1L;
	StateManager stateManager;
	
	public DashCheck(StateManager stateManager) {
		super("Dash");
		
		addActionListener(new ShadowListener());
		
		this.stateManager = stateManager;
	}

	//破線状態を切り替えるクラス
	class ShadowListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			 if(stateManager.getDashed()){
				 stateManager.setDashed(false);
			 }else{
				 stateManager.setDashed(true);
			 }
		}
	}
}
