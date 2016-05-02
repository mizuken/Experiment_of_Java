
import java.awt.Color;
import java.awt.event.*;

import javax.swing.*;

import java.util.Enumeration;
//import java.awt.Cursor;
public class SelectButton extends JButton{

	private static final long serialVersionUID = 1L;
	StateManager stateManager;//StateManager型の変数stateManager
	MyDrawing mydrawing;
	
	public SelectButton(StateManager stateManager){
		super("Select");
		addActionListener(new SelectListener());
		this.stateManager = stateManager;
	}

	class SelectListener implements ActionListener{	
		public void actionPerformed(ActionEvent e){
			stateManager.setState(new SelectState(stateManager));
		}
	}
}

class SelectState implements State{	
	
	StateManager selectstate;
	MyCanvas canvas;
	MyDrawing d,d_s,d_m,d_r,d_range,d_multi,d_single;
	
	Mediator m;
	int xpt,ypt,wpt,hpt;
	int xs,ys; //クリック座標の保存
	int xc,yc; //座標変換後の保存
	boolean isrectshadow = false;
	boolean isSelected = false; //一つでも選択したものがあったかどうかを保存する
	int linewidth;
	
	//課題４
	boolean multipleSelect = false; //複数選択モードの切り替え
	boolean multidrag = false; //複数の図形の移動などの処理切り替え
	Color select = new Color(255,255,255,0);//選択範囲を示すために使う色
	
	public SelectState(StateManager stateManager){
		this.selectstate = stateManager;
		this.m = selectstate.getMediator();
	}
	
	/////////////////////////////////マウスクリックの際に選択するメソッド/////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////
	
	public void mouseDown(int x,int y){	
		///////クリックされれば必ず実行される/////////////////////////////////////////////////////
		//最初のクリックした位置を一度保存する
		this.xpt = x;
		this.ypt = y;
		this.xs = x;//選択に使う
		this.ys = y;//選択に使う
		isSelected = false;//ここでは何も図形を選択していない状態にする

		///////////////////////////////////////////////////////////////////////
		////////////////////選択要素数の数を調べる//////////////////////////////////
		int i = 0;//選択図形の数
		for(int a = 0; a < m.drawings.size(); a++){
			if(m.drawings.elementAt(a).getSelected() == true){
				i++;
			}
		}
		/////////////////////////////////////////////////////////////////////////////////
		////////選択要素数が単数以下の場合、単数図形選択判定にする//////////////////////////////////
		if(i <= 1){
		for(int t = 0; t < m.drawings.size(); t++){
			d = m.drawings.elementAt(t); //使う図形を代入
			//含んでいれば選択状態
			if(d.contains(x, y)){
				d.setSelected(true); //選択状態にする
				d_s = d; //選択図形に一度保存
			//それまでの図形を非選択にする
				for(int u = 0; u < t ;u++){
					m.drawings.elementAt(u).setSelected(false);
				}
				isSelected = true; //選択した図形があったことを示す
			}
			////////////含んでいなければ非選択状態にする/////////////////////////
			else{
				d.setSelected(false);
			}
	    }
		} //ここまでが、単数選択モードでこれ以降は選択図形が複数あった場合の処理
		else{//つまりは i >= 2の状況
			//選択図形は複数あるものの、クリックした位置は選択されている図形なのかどうかで場合分け
			for(int t = 0; t < m.selectedDrawings.size(); t++){
				d_multi = m.selectedDrawings.elementAt(t); //使う図形を代入
				//一個でも選択状態の図形をクリックしていた場合
				if(d_multi.contains(x, y)){
					multidrag = true; //選択した図形があったことを示す
				}
			}
			///////////////////////////////////////////////////////////////
			//もし選択図形をクリックしているのではなかったら今度は図形をクリックしているかどうか場合分け
			if(multidrag == false){
			  for(int q = 0; q < m.drawings.size(); q++){
         			d_single = m.drawings.elementAt(q);
         			if(d_single.contains(x, y)){
        				d_single.setSelected(true); //選択状態にする
        				d_s = d_single; //選択図形に一度保存
        			//それまでの図形を非選択にする
        				for(int u = 0; u < q-1 ;u++){
        					m.drawings.elementAt(u).setSelected(false);
        				}
        				isSelected = true; //選択した図形があったことを示す
        			}
        			////////////含んでいなければ非選択状態にする/////////////////////////
        			else{
        				d_single.setSelected(false);
        			}
			   }
			} else {
				//何もなかったので選択したものがひとつもなかったという処理をつないでいく
			}			
			}
		m.repaint();
		
		if(multidrag == false){
	   //////選択したものがひとつもなかった場合/////////////////////////////////////
				if(isSelected == false){
					m.clearSelected();
					//これより選択モードを複数選択モードに切り替える
					multipleSelect = true;
					m.addDrawing(d_range= new MyRectangle(x,y,0,0,0,Color.black,select,1));
					d_range.setDashed(true);
					m.repaint();
				}
				else{//これから操作をする対象として選択した図形を保存する	
					//m.clearSelected();
					m.clearSelectedDrawings();
					m.setSelectedDrawing(d_s);//選択図形配列に挿入
					m.setLinewidth(m.getLinewidth());
					/*
					if(m.getSelectedDrawing() != null){
						d_r = m.getSelectedDrawing().lastElement(); //選択配列の最後の要素を取り出す
						d_m = d_r;
						
						m.removeDrawing(d_r);
						m.addDrawing(d_m);	
						}
					*/
					m.repaint();
					}
				
		}else{///複数が選択されていた場合
			//選択している図形をクリックしているかどうか判断する
			for(int t = 0; t < m.selectedDrawings.size(); t++){
				d = m.selectedDrawings.elementAt(t); //使う図形を代入
				//一個でも選択状態の図形をクリックしていた場合
				if(d.contains(x, y)){
					isSelected = true; //選択した図形があったことを示す
				}
			}
			//複数あるが、図形のある位置をクリックしてない場合
			if(isSelected == false){
				multidrag = false;
				multipleSelect = true;
				m.addDrawing(d_range= new MyRectangle(x,y,0,0,0,Color.black,select,1));
				d_range.setDashed(true);
				m.repaint();
			}else{
				multidrag = true;
			}
		}
		}
	///////////////////////////////////ここまでmouseDown処理////////////////////////////////////////////
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	public void mouseUp(int x, int y){
		//複数選択モードであるならば、範囲内にある図形を選択配列に追加する
		if(multipleSelect == true){
			m.removeDrawing(d_range);
		 for(int i=0; i < m.drawings.size();i++){
		 	if(m.drawings.elementAt(i).getSelected() == true){
		 		m.setSelectedDrawing(m.drawings.elementAt(i));
		 	}
		  }
			//複数選択モードの終了
	   	  multipleSelect = false;
		  multidrag = false;
		}else{
			multidrag = false;
		}
	}
	/////////////////////////////////ここまでmouseUp処理///////////////////////////////////////////////

	/////////////////////////////////////////////////////////////////////////////////////////////////
	public void mouseDrag(int x, int y){
		////複数図形移動モードかどうか判断する//////////////////////////////////////////
		if(multidrag == true){
			///移動距離の保存
			 int dx = x - this.xpt;
			 int dy = y - this.ypt;
			 int move_x;
			 int move_y;
			 
			 //実際に図形を動かしていく
			 for(int c = 0; c < m.selectedDrawings.size();c++){
				 move_x = dx;
				 move_y = dy;
				 m.selectedDrawings.elementAt(c).move(move_x, move_y);
				 ///移動距離の初期化
				 move_x = 0;
				 move_y = 0;
			 }
			///移動後の位置を保存し直す
			 this.xpt = x;
			 this.ypt = y;
		}else{
		////複数選択モードかどうかを判断する////////////////////////////////////////////
		  if(multipleSelect == true){
			 m.clearSelectedDrawings();
			this.wpt = x - this.xpt;
			this.hpt = y - this.ypt;
			d_range.setSize(wpt, hpt);
			//選択範囲に対して重なっているかどうか判断する
			Enumeration<MyDrawing> e = m.drawingsElements();
			while(e.hasMoreElements()){
				d = e.nextElement();
				///含んでいれば選択状態にする////////////////////////////
				//選択範囲を正しく実装するために操作を加えている
				if ( wpt < 0 ) {
					wpt *= -1;
				}
				if ( hpt < 0 ) {
					hpt *= -1;
				}
				if(x > xs){
					xc = xs;
				}else {
					xc = x;
				}
				if (y > ys){
					yc = ys;
				}else{
					yc = y;
				}
				if(d.intersect(xc, yc, wpt, hpt)){
					d.setSelected(true);
					}else{
						d.setSelected(false);
					}
			}
		}else{
		//移動をするために選択されている図形の取得
		if(m.getSelectedDrawing() != null){
			//移動距離の保存
			 int dx = x - this.xpt;
			 int dy = y - this.ypt;
			 for(int i=0; i < m.selectedDrawings.size(); i++){
				 d_m = m.selectedDrawings.elementAt(i);
				 d_m.move(dx,dy);
			 }
			 //移動後の位置を保存し直す
			 this.xpt = x;
			 this.ypt = y;
		 }
		}
	}
	}
}
///////////////////////////////////ここまでmouseDrag処理/////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////////////////////////