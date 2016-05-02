
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class TriangleButton extends JButton{
	
	private static final long serialVersionUID = 1L;
	StateManager stateManager;//StateManager型の変数stateManager
	
	public TriangleButton(StateManager stateManager){
		super("Triangle");
	
		addActionListener(new TriangleListener());
	
		this.stateManager = stateManager;
	}
	//Rectangleボタンが押された時の処理
	class TriangleListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			stateManager.setState(new TriangleState(stateManager));
		}
	}
	
}

class TriangleState implements State{

	StateManager trianglestate;
	MyCanvas canvas;
	MyDrawing d;
	Mediator m;
	int xpt,ypt,wpt,hpt;
	boolean istriangleshadow = false;
	int linewidth;
	Color triangleColor,triangleLineColor;

	public TriangleState(StateManager stateManager){
		this.trianglestate = stateManager;
		this.m = trianglestate.getMediator();
		triangleColor = m.getMediatorColor();
		triangleLineColor = m.getMediatorLineColor();
	}

	public void mouseDown(int x,int y){
	
		//初期値の保存
		this.xpt = x; 
		this.ypt = y;
		triangleColor = m.getMediatorColor();
		triangleLineColor = m.getMediatorLineColor();
		linewidth = m.getLinewidth();
		trianglestate.addDrawing(d = new MyTriangle(x,y,0,0,0,triangleLineColor,triangleColor,linewidth));
		//選択用の範囲を設定する
		d.setRegion();
		d.setShadow(trianglestate.getShadow());
		
		//破線状態の切り替え
		if(trianglestate.getDashed()){
			d.setDashed(true);
		}else{
			d.setDashed(false);
		}
	
	}

	public void mouseUp(int x, int y){
		
		int xd = x - this.xpt;
		int yd = y - this.ypt;
		int z = (int)Math.sqrt(xd*xd + yd*yd);
		//一定大きさ以下なら削除
		if(z < 5 ){
			m.removeDrawing(d);
		}	
		d.setRegion();
		d.setPoint(d);
		this.wpt = 0;
		this.hpt = 0;
			
	}
	
	public void mouseDrag(int x, int y){
		//幅の計算
		this.wpt = x - this.xpt;
		this.hpt = y - this.ypt;
		d.setSize(wpt, hpt);
	}
}
