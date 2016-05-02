import java.awt.Color;
import java.util.Enumeration;
import java.util.Vector;

public class Mediator {

	Vector<MyDrawing> drawings; //描写する図形の配列
	MyCanvas canvas;
	//課題４の実装
	//選択状態オブジェクトをベクトル配列型//////////////////
	Vector<MyDrawing> selectedDrawings; //範囲内にあった図形をここに代入する
	////////////////////////////////////////////////
	Color nowcolor = Color.white; //現在描写する色
	Color nowlinecolor = Color.black; //現在描写する線の色
	int linewidth=1; //線の太さ 初期値=1

	Vector<MyDrawing> buffers; //Cut,Copyバッファ
	boolean isCut = false; //カットしたのかどうかを見る
	int dis_l,dis_t;
	MyDrawing d_letf,d_top; //ペーストのための一時的図形保存
	float coloralpha = 255; //色の透明度：デフォルトは不透明
	
	
	public Mediator(MyCanvas canvas){
		this.canvas = canvas;
		drawings = new Vector<MyDrawing>();
		selectedDrawings = new Vector<MyDrawing>(); //選択配列を扱えるように
		buffers = new Vector<MyDrawing>(); //バッファー配列を扱えるように
		
	}
	
	//////////////図形オブジェクト操作/////////////////////
	//図形の配列を取り出す
	public Enumeration<MyDrawing> drawingsElements(){
		return drawings.elements();
	}
	
	//図形の配列に追加する(新規作成)
	public void addDrawing(MyDrawing d){
		clearSelected(); //選択状態のリセット
		clearSelectedDrawings(); //選択状態配列のリセット
		d.setSelected(true); //図形を選択状態にする
		drawings.add(d); //描画する図形の配列に追加する
		this.selectedDrawings.add(d);//選択図形配列に追加する
	}
	//図形の配列に追加する(単純に追加するだけ)
	public void addSetDrawing(MyDrawing d){
		drawings.add(d);
	}
	//図形の配列から削除する
	public void removeDrawing(MyDrawing d){drawings.remove(d);}
	//選択状態にある図形の取得
	public Vector<MyDrawing> getSelectedDrawing(){return selectedDrawings;}
	//選択した図形を動かす
	public void move(int dx,int dy){}//実装していない
	//再描写
	public void repaint(){canvas.repaint();}	
	//すべての選択状態にあった図形を非選択状態にする
	public void clearSelected(){
		Enumeration<MyDrawing> e = drawingsElements();
		while(e.hasMoreElements()){
			e.nextElement().setSelected(false);//非選択状態にする
		}
	}
	//（x、y）位置にある図形を選択状態にする
	public void setSelected(int x,int y){}
	
	///課題４に関係するメソッド///////////////////////////////////
	//選択図形配列を取り出す
	public Enumeration<MyDrawing> selectedDrawingsElements(){
		return selectedDrawings.elements();
	}
	//選択した図形を選択配列にいれる
	public void setSelectedDrawing(MyDrawing d){
		this.selectedDrawings.add(d);
	}
	//選択した図形を選択配列から削除する
	public void removeSelectedDrawings(MyDrawing d){
		this.selectedDrawings.remove(d);
	}
	//選択状態配列を空にする
	public void clearSelectedDrawings(){
		this.selectedDrawings.clear();
	}
	///////////////////////////////////////////////////////
	
	////図形のカット,コピー,ペースト//////////////////////////////
	public void clearBuffer(){
		//バッファのクリア
		buffers.clear();;
	}
	public void copy(){
		//図形をコピーする
		clearBuffer();
		for(int i=0; i < selectedDrawings.size(); i++){
			buffers.add(selectedDrawings.elementAt(i).clone());
		}
		repaint();
	}
	public void cut(){
		//図形をカットする
		clearBuffer();
		for(int i=0; i < selectedDrawings.size(); i++){
			buffers.add(selectedDrawings.elementAt(i).clone());
		}
		for(int i = 0; i < selectedDrawings.size(); i++){
			for(int j=0; j < drawings.size(); j++){
				if(selectedDrawings.elementAt(i)==drawings.elementAt(j)){
					removeDrawing(drawings.elementAt(j));
				}
			}
		}
	   clearSelectedDrawings(); //選択図形配列の削除
	   repaint();
	}
	public void paste(int x,int y){
		//図形をペーストする
		////////////////全図形で一番左上にある図形を基準位置として保存する処理をする
		MyDrawing d_paste = buffers.elementAt(0).clone(); //座標関係に使うための図形
		MyDrawing d_left =  buffers.elementAt(0).clone(); //一番左保存のために用意
		MyDrawing d_top =  buffers.elementAt(0).clone(); //一番上保存のために用意
		//一番左にある図形の取得
		for(int i=0; i < buffers.size(); i++){
			MyDrawing d_temp = buffers.elementAt(i).clone();
			if(d_temp.getX() < d_left.getX()){
			    d_left = d_temp;
			}
		}
		//一番上にある図形の取得
		for(int i=0; i < buffers.size(); i++){
			MyDrawing d_temp = buffers.elementAt(i).clone();
			if(d_temp.getY() < d_top.getY()){
			    d_top = d_temp;
			}
		}
		//一番左と一番上の図形とでクリック位置が近いものを取得
		dis_l = (x - d_left.getX())*(x - d_left.getX()) + (y - d_left.getY())*(y - d_left.getY());
		dis_t = (x -  d_top.getX())*(x -  d_top.getX()) + (y -  d_top.getY())*(y -  d_top.getY());
	    if(dis_l < dis_t){
	    	d_paste = d_left;
	    }else{
	    	d_paste = d_top;
	    }
	    
	    //ここから図形のペースト
		for(int i=0; i < buffers.size(); i++){
		 MyDrawing clone = (MyDrawing)buffers.elementAt(i).clone();
		 clone.setbufferLocation((d_paste.getX() - x),(d_paste.getY() - y));
		 addSetDrawing(clone);
		 setSelectedDrawing(clone);
		 clone.setRegion();
		}
		//もしカットした場合ならばbuffersを空にする
		if(getCut()==true){
			clearBuffer();
		}
		repaint();
	}
	
	//図形ペーストの状態管理
	public void setCut(boolean ispaste){this.isCut = ispaste;}
	public boolean getCut(){return isCut;}
	//////////////////////////////////////////////////////
	
	/////////////////////////////////////////////////////////////////////
	
	/////////図形のソートに関するメソッド定義///////////////////////////////////
	
	//上端で揃える
	public void sortByTop(){
		if(! selectedDrawings.isEmpty()){
			//一番左にある図形を確定させる
			MyDrawing d_left =  selectedDrawings.elementAt(0); //一番左保存のために用意
			for(int i=1; i < selectedDrawings.size(); i++){
				MyDrawing d_temp = selectedDrawings.elementAt(i);
				if(d_temp.getX() < d_left.getX()){
				    d_left = d_temp;
				}
			}
			//一番左にある図形の乗変似あわせて整列する
		int inter = 0;
		  for(int i=0 ; i < selectedDrawings.size(); i++){
			if(selectedDrawings.elementAt(i).getType() == 'S'){
			   //星形ならば
				selectedDrawings.elementAt(i).setLocation(inter+10+i*20,d_left.getY()+selectedDrawings.elementAt(i).getR());
			}else if(selectedDrawings.elementAt(i).getType() == 'T'){
				//三角形ならば
				if(selectedDrawings.elementAt(i).getH() >= 0){
					selectedDrawings.elementAt(i).setLocation(inter+10+i*20,d_left.getY());
				}else{
					selectedDrawings.elementAt(i).setLocation(inter+10+i*20,d_left.getY()-selectedDrawings.elementAt(i).getH());
				}
			}else{
				//それ以外ならば
		      selectedDrawings.elementAt(i).setLocation(inter+10+i*20, d_left.getY());
		    	}
			selectedDrawings.elementAt(i).setRegion(); //領域の設定
			inter += selectedDrawings.elementAt(i).getW(); //次のために幅をとっておく
			}	
		}
		clearSelected(); //すべての選択状態を解除する
		clearSelectedDrawings(); //選択状態配列を空にする
	}
	
	//高さ順に整列する
	public void sortByHight(){
		if(! selectedDrawings.isEmpty()){
			//一番下にある図形を確定させる
			MyDrawing d_low =  selectedDrawings.elementAt(0); //一番左保存のために用意
			for(int i=1; i < selectedDrawings.size(); i++){
				MyDrawing d_temp = selectedDrawings.elementAt(i);
				if(d_temp.getY() + d_temp.getH() > d_low.getY() + d_low.getH()){
				    d_low = d_temp;
				}
			}
			//高い順に選択配列を並び替えるように保存する
			//最初の配列のサイズを保存する
            int i_size = selectedDrawings.size();
            for(int i = 0; i < i_size-1;i++){
            	MyDrawing d_i = selectedDrawings.elementAt(i);
            	int num = 0;
            	for(int j = i+1; j < i_size;j++){
					MyDrawing d_j = selectedDrawings.elementAt(j);
					//高いものを要素の小さいものに持ってくる
					if(Math.sqrt(d_i.getH()*d_i.getH()) < Math.sqrt(d_j.getH()*d_j.getH())){
						d_i = d_j; //最大のものを置く
						num = j; //何番目か記憶する
					}
				   }
            	//入れ替える要素があったなら入れ替える
            	if(num!=0){
            	MyDrawing temp = selectedDrawings.elementAt(i);//比較していた要素を代入
				selectedDrawings.setElementAt(d_i,i); //最大のものを配置
				selectedDrawings.setElementAt(temp,num);
            	}
               }
	   	   
		//整列した配列を表示する
		int inter = 0; //図形が重ならにように間をとる
		int low = d_low.getY() + d_low.getH();  //
		for(int k=0 ; k < selectedDrawings.size(); k++){
			if(selectedDrawings.elementAt(k).getType() == 'S'){
			   //星形ならば
				selectedDrawings.elementAt(k).setLocation(30+inter+k*20+selectedDrawings.elementAt(k).getW()/2,low+selectedDrawings.elementAt(k).getR()-selectedDrawings.elementAt(k).getH());
			}else if(selectedDrawings.elementAt(k).getType() == 'T'){
				//三角形ならば
				if(selectedDrawings.elementAt(k).getH() >= 0){
					selectedDrawings.elementAt(k).setLocation(30+inter+k*20,low - selectedDrawings.elementAt(k).getH());
				}else{
					selectedDrawings.elementAt(k).setLocation(30+inter+k*20,low-selectedDrawings.elementAt(k).getH() + selectedDrawings.elementAt(k).getH());
				}
			}else{
				//それ以外ならば
		      selectedDrawings.elementAt(k).setLocation(30+inter+k*20, low- selectedDrawings.elementAt(k).getH());
		    	}
			selectedDrawings.elementAt(k).setRegion(); //領域の設定
			inter += selectedDrawings.elementAt(k).getW(); //次のために幅をとっておく
		}
		clearSelected(); //すべての選択状態を解除する
		clearSelectedDrawings(); //選択状態配列を空にする
	}
	}
	/////////色選択を選択されている図形と今後の図形に設定する///////////////////////
	//図形の色
	public void setColor(Color color){
			if(! selectedDrawings.isEmpty()){
				Enumeration<MyDrawing> e = selectedDrawingsElements();
				while(e.hasMoreElements()){
					e.nextElement().setFillColor(color);
					}
				}	
		this.nowcolor = color;
	}
	//図形の線の色
	public void setLineColor(Color color){
			if(! selectedDrawings.isEmpty()){
				Enumeration<MyDrawing> e = selectedDrawingsElements();
				while(e.hasMoreElements()){
					e.nextElement().setLineColor(color);
					}
			}
		this.nowlinecolor = color;
	}
	
	//現在の図形内部の色を取得
	public Color getMediatorColor(){return nowcolor;}
	//現在の線の色を取得
	public Color getMediatorLineColor(){return nowlinecolor;}
	
	//選択された図形の透明度を変更する
	public void setColorAlpha(float alpha){
		this.coloralpha = alpha;
		if(! selectedDrawings.isEmpty()){
			for(int i = 0; i < selectedDrawings.size(); i++){
				float c_alpha[] = {0,0,0,0}; //一時的な配列の用意
				selectedDrawings.elementAt(i).getFillColor().getRGBColorComponents(c_alpha); //色の取得
				c_alpha[3] = alpha; //透明度を設定する
				selectedDrawings.elementAt(i).setFillColor(new Color(c_alpha[0],c_alpha[1],c_alpha[2],c_alpha[3]));//色を設定する
			}
		}

	}
	
	/////////線の太さに関するメソッド///////////////////////////////////////
	//現在の線の太さを変更する
	public void setLinewidth(int width){
		this.linewidth = width;
		//選択図形配列を取り出す
		if(!selectedDrawings.isEmpty()){
		Enumeration<MyDrawing> e = selectedDrawingsElements();
		while(e.hasMoreElements()){
			e.nextElement().setLineWidth(width);
			}
		}
	}
	//現在の線の太さを取得する
	public int getLinewidth(){return linewidth;}
	////////////////////////////////////////////////////////////////////


//////////図形の配置関係に関するメソッド//////////////////////////////////////
	
	//選択図形を前面に持ってくる
	public void setFront(){
		MyDrawing d_m,d_r;
		for(int i = 0; i < drawings.size(); i++){
			 for(int j = 0; j < selectedDrawings.size(); j++){
		        if(drawings.elementAt(i) == selectedDrawings.elementAt(j)){
		        	d_r = drawings.elementAt(i);
		        	d_m = d_r;
		        	removeDrawing(d_r); //図形配列から削除
		        	addSetDrawing(d_m); //図形配列に追加=配列の最後の要素にする
		        	d_r = d_m;
		        	removeSelectedDrawings(d_m); //選択図形配列から削除
		        	setSelectedDrawing(d_r); //選択図形配列に追加
		        }
			 }
		 }
	}
	
	///選択図形を一つ前面にもってくる
	public void setFrontword(){
		MyDrawing d_m,d_r;
			for(int i = 0; i <selectedDrawings.size(); i++){
			 d_m = selectedDrawings.elementAt(i);
			 int again = 0;
			 for(int j = 0; j < drawings.size(); j++){
				 if(again == 0){
				 if(drawings.elementAt(j) == d_m){
					 d_r = drawings.elementAt(j+1);
					 drawings.setElementAt(d_m, j+1);
					 drawings.setElementAt(d_r,j);
					 again = 1;
				 }
				 }
			 }
			}
		}
	
	///選択図形をひとつ背面にもってくる
	public void setBackword(){
		MyDrawing d_m,d_r;
		for(int i = 0; i <selectedDrawings.size(); i++){
		 d_m = selectedDrawings.elementAt(i);
		 int again = 0;
		 for(int j = 0; j < drawings.size(); j++){
			 if(again == 0){
			 if(drawings.elementAt(j) == d_m){
				 d_r = drawings.elementAt(j-1);
				 drawings.setElementAt(d_m, j-1);
				 drawings.setElementAt(d_r,j);
				 again = 1;
			 }
			 }
		 }
		}
	}
	//選択図形を背面に持ってくる
	public void setBack(){
		MyDrawing d_m,d_r;
		for(int i = 0; i < drawings.size(); i++){
			 for(int j = 0; j < selectedDrawings.size(); j++){
		        if(drawings.elementAt(i) == selectedDrawings.elementAt(j)){
		        	d_r = drawings.elementAt(i);
		        	d_m = d_r;
		        	removeDrawing(d_r); //図形配列から削除
		        	drawings.add(0,d_m); //図形配列に追加=配列の最初の要素にする
		        	d_r = d_m;
		        	removeSelectedDrawings(d_m); //選択図形配列から削除
		        	selectedDrawings.add(0,d_r); //選択図形配列に追加
		        }
			 }
		 }
	}



}//mediatorの終了
