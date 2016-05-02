
//import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

//影をつけるかどうかのチェックボックスの管理
public class ShadowCheck extends JCheckBox{

	private static final long serialVersionUID = 1L;
	StateManager stateManager;
	
	public ShadowCheck(StateManager stateManager) {
		super("Shadow");
		
		addActionListener(new ShadowListener());
		
		this.stateManager = stateManager;
	}
	
	//影状態の切り替えを行う
	class ShadowListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(stateManager.getShadow()){
				stateManager.setShadow(false);
			}else{
				stateManager.setShadow(true);
			}
		}
	}
}