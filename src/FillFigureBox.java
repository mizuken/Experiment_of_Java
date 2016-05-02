
//import java.awt.*;
//import java.awt.event.*;

import javax.swing.*;


//図形の色を変更するクラス
public class FillFigureBox extends JComboBox{
	
	private static final long serialVersionUID = 1L;
	Mediator mediator;
	
	public FillFigureBox(Mediator mediator){
		//addChangeListener(new LineWidthListener());
		this.mediator = mediator;
	}

}
