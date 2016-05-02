import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class RectButton extends JButton{
	private static final long serialVersionUID = 1L; //バージョンID
	StateManager stateManager;//StateManager型の変数stateManager
	
	public RectButton(StateManager stateManager){
		super("Rectangle");
		addActionListener(new RectListener());
		this.stateManager = stateManager;
	}
	
	//Rectangleボタンが押された時の処理
	class RectListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			stateManager.setState(new RectState(stateManager));
		}
	}
	
}

class RectState implements State{

	StateManager rectstate;
	MyCanvas canvas;
	MyDrawing d;
	Mediator m;
	int xpt,ypt,wpt,hpt;
	boolean isrectshadow = false;
	int linewidth;
	Color rectColor,rectLineColor;

		public RectState(StateManager stateManager){
			this.rectstate = stateManager;
			this.m = rectstate.getMediator();
			rectColor = m.getMediatorColor();
			rectLineColor = m.getMediatorLineColor();
		}

		public void mouseDown(int x,int y){
			//初期値の保存
			this.xpt = x; 
			this.ypt = y;
			rectColor = m.getMediatorColor(); //内部色情報の取得
			rectLineColor = m.getMediatorLineColor(); //線分色情報の取得
			linewidth = m.getLinewidth(); //線分の太さの情報
			m.addDrawing(d = new MyRectangle(x,y,0,0,0,rectLineColor,rectColor,linewidth));
			//選択用の範囲を設定する
			d.setRegion();
			//影状態ならばつける
			d.setShadow(rectstate.getShadow());
			//破線状態なら切り替える	
			if(rectstate.getDashed()){
				d.setDashed(true);
			}else{
				d.setDashed(false);
			}
		}
		
		public void mouseUp(int x, int y){
			//一定大きさ以下なら削除
			int xd = x - this.xpt;
			int yd = y - this.ypt;
			int z = (int)Math.sqrt(xd*xd + yd*yd);
			if( z < 5){
				m.removeDrawing(d);
			}
			//どの状態で描いたとしても左上から描いたのと同じように座標を配置する
			d.setPoint(d);
			this.wpt = 0;
			this.hpt = 0;
			d.setRegion();
			m.repaint();
		}
		
		public void mouseDrag(int x, int y){
			//幅の計算
			this.wpt = x - this.xpt;
			this.hpt = y - this.ypt;
			d.setSize(wpt, hpt);
		}
		
}