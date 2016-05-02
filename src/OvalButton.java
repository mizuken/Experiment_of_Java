

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class OvalButton extends JButton {

	private static final long serialVersionUID = 1L;
	StateManager stateManager; //StateManager型の変数stateManager
	
	public OvalButton(StateManager stateManager){
		super("Oval");
	
		addActionListener(new OvalListener());

		this.stateManager = stateManager;
	}

	//Ovalボタンが押された時の処理
	class OvalListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			stateManager.setState(new OvalState(stateManager));
		}
	}
}

class OvalState implements State{

	StateManager ovalstate;
	MyCanvas canvas;
	MyDrawing d,d_s;
	Mediator m;
	int xpt,ypt,wpt,hpt;
	boolean isrectshadow = false;
	int linewidth;
	Color ovalColor,ovalLineColor;
	
	public OvalState(StateManager stateManager){
		this.ovalstate = stateManager;
		this.m = ovalstate.getMediator();
		ovalColor = m.getMediatorColor();
		ovalLineColor = m.getMediatorLineColor();
	}

	public void mouseDown(int x,int y){
		//初期値の保存
		this.xpt = x; 
		this.ypt = y;
		ovalColor = m.getMediatorColor();
		ovalLineColor = m.getMediatorLineColor();
		linewidth = m.getLinewidth();
		ovalstate.addDrawing(d = new MyOval(x,y,0,0,0,ovalLineColor,ovalColor,linewidth));
		//選択用の範囲を設定する
		d.setRegion();
		d.setShadow(ovalstate.getShadow());
		
		//破線状態の判断
		if(ovalstate.getDashed()){
			d.setDashed(true);
		}else{
			d.setDashed(false);
		}

	}
	
	public void mouseUp(int x, int y){
		//一定以下の大きさならば削除
		int xd = x - this.xpt;
		int yd = y - this.ypt;
		int z = (int)Math.sqrt(xd*xd + yd*yd);
		if(z < 10){
			m.removeDrawing(d);
		}
		d.setPoint(d);	
		d.setRegion();
		this.wpt = 0;
		this.hpt = 0;
		m.repaint();
	}
	public void mouseDrag(int x, int y){
		//幅の計算
		this.wpt = x - this.xpt;
		this.hpt = y - this.ypt;
		d.setSize(wpt, hpt);
	}
}
