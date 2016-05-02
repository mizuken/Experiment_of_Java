
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class StarButton extends JButton{

	private static final long serialVersionUID = 1L;
	StateManager stateManager;//StateManager型の変数stateManager
	
	public StarButton(StateManager stateManager){
		super("Star");
		addActionListener(new StarListener());
		this.stateManager = stateManager;
	}
	//Rectangleボタンが押された時の処理
	class StarListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			stateManager.setState(new StarState(stateManager));
		}
	}
}

class StarState implements State{

	StateManager starstate;
	MyCanvas canvas;
	MyDrawing d;
	Mediator m;
	int xpt,ypt,wpt,hpt,rpt;
	boolean istriangleshadow = false;
	int linewidth;
	Color starColor,starLineColor;
	
	public StarState(StateManager stateManager){
		this.starstate = stateManager;
		this.m = starstate.getMediator();
		starColor = m.getMediatorColor();
		starLineColor = m.getMediatorLineColor();
	}

	public void mouseDown(int x,int y){
		//初期値の保存
		this.xpt = x; 
		this.ypt = y;
		starColor = m.getMediatorColor();
		starLineColor = m.getMediatorLineColor();
		linewidth = m.getLinewidth();
		starstate.addDrawing(d = new MyStar(x,y,0,0,0,starLineColor,starColor,linewidth));
		d.setRegion();
		d.setShadow(starstate.getShadow());
		
		//破線状態ならば切り替える
		if(starstate.getDashed()){
			d.setDashed(true);
		}else{
			d.setDashed(false);
		}
		
	}

	public void mouseUp(int x, int y){
		//一定大きさ以下なら削除する
		int xd = x - this.xpt;
		int yd = y - this.ypt;
		int z = (int)Math.sqrt(xd*xd + yd*yd);
				this.wpt = 0;
		if(z < 5 ){
			starstate.removeDrawing(d);
		}
		//高さ判定のために値としてｈを設定しておく
		hpt = (int) (rpt + rpt* Math.cos(Math.toRadians(36)));
		d.setH(hpt); //高さはR半径の２倍
		//幅判定のために値としてwを設定しておく
		wpt = (int)(2*rpt*Math.sin(Math.toRadians(72)));
		d.setW(wpt); 
		d.setRegion();
		this.wpt = 0;
		this.hpt = 0;
		this.rpt = 0;
		m.repaint();
	}
	
	public void mouseDrag(int x, int y){
		//幅の計算
		this.wpt = x - this.xpt;
		this.hpt = y - this.ypt;
		this.rpt = (int)Math.sqrt(wpt*wpt + hpt*hpt);
		//d.setLocation(x, y);
		d.setStar(wpt,hpt,rpt);
	}

}