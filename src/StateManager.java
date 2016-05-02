/*
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
*/
public class StateManager {

	private State state;
	MyCanvas canvas;
	Mediator mediator;
	private boolean isDashed = false; //破線にするかどうか
	private boolean isShadow = false; //影をつけるかどうか
	private boolean isSelected = false; //選択モードかそうでないか
	
	public StateManager(MyCanvas canvas){
		this.canvas = canvas;
		this.mediator = canvas.getMediator();
		this.state = new StartState(this);
	}
	
	//状態を切り替える
	public void setState(State state){this.state = state;}
	public Mediator getMediator(){return mediator;}
	
	//影状態について
	public void setShadow(boolean b){isShadow = b;}
	public boolean getShadow(){return isShadow;}
	
	//破線状態について
	public void setDashed(boolean b){isDashed = b;}
	public boolean getDashed(){return isDashed;}
	
	//選択モードについて
	public boolean getSelected(){return isSelected;}
	public void setSelected(boolean isSelected){this.isSelected = isSelected;}
	
	//図形の生成、削除
	public void addDrawing(MyDrawing mydrawing){mediator.addDrawing(mydrawing);}
	public void removeDrawing(MyDrawing mydrawing){mediator.removeDrawing(mydrawing);}
	
	//マウスイベント処理
	public void mouseDown(int x, int y){state.mouseDown(x,y);}
	public void mouseUp(int x, int y){state.mouseUp(x,y);}
	public void mouseDrag(int x,int y){state.mouseDrag(x,y);	}
}
