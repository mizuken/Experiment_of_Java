
//import java.awt.*;

//初期状態を管理するクラス
public class StartState implements State{

	StateManager startstate;
	
	public StartState(StateManager stateManager){
		this.startstate = stateManager;
	}
	public void mouseDown(int x,int y){}
	public void mouseUp(int x, int y){}
	public void mouseDrag(int x, int y){}
	
}
