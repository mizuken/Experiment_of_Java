import java.awt.*;
//import java.awt.event.*;
import java.awt.geom.Area;
//import javax.swing.*;

public class MyDrawing implements Cloneable
{
	private int x, y, w, h, r=0; //x座標, y座標, 幅, 高さ,大きさ
	private Color lineColor,fillColor; //線の色,塗り絵
	private int lineWidth; //線の太さ
	boolean isDashed = false; //破線であるかどうか
	boolean isShadow = false; //影であるかどうか
	
	//課題３関係
	//boolean Selected;
	boolean isSelected; //図形選択状態有無
	boolean isStarSelected;//星選択状態有無
	Rectangle region; //長方形含有判定用
	Area star_region; //楕円含有判定用
	final int SIZE  = 3;
	
	//課題４関係
	char objectType = 'N'; //ここに変数の名前が入る
	//変数の名前について
	//長方形なら　'R' 楕円なら 'O' 三角形なら 'T' 星なら 'S'が入る
	//デフォルトならノーマルとして 'N'をいれておく

	public MyDrawing(int x,int y,int w,int h,
					int r,Color lc,Color fc, int lw){
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.r = r;
		lineColor = lc;
		fillColor = fc;
		lineWidth = lw;
		setLocation(x,y);
		setRegion();
	}
	public MyDrawing(){
		x = y = 0;
		w = h = 40;
		lineColor = Color.black;
		fillColor = Color.white;
		lineWidth = 1;
		setRegion();
	}
	//破線状態管理
	public void setDashed(boolean b){isDashed = b;}
	public boolean getDashed(){return isDashed;}
	//影状態管理
	public void setShadow(boolean b){isShadow = b;}
	public boolean getShadow(){return isShadow;}
    //図形種類管理(課題4より新たに導入)
	public void setType(char c){objectType = c;}
	public char getType(){return objectType;}
	
	////////////////////////////////図形描写処理//////////////////////////////////////
	
	public void draw(Graphics g){
		//選択状態を表す四角形を描く
		//System.out.println("MyDrawing drawメソッドです");
		 if(isSelected){
			//System.out.println("draw内isSelected=true");
			if(r == 0){
			g.setColor(Color.black);
			g.fillRect(x+w/2-SIZE, y-SIZE, SIZE*2, SIZE*2);//上中
			g.fillRect(x-SIZE, y+h/2-SIZE, SIZE*2, SIZE*2);//左中
			g.fillRect(x+w/2-SIZE, y+h-SIZE, SIZE*2, SIZE*2);//下中
			g.fillRect(x+w-SIZE, y+h/2-SIZE, SIZE*2, SIZE*2); //右中
			g.fillRect(x-SIZE, y-SIZE, SIZE*2, SIZE*2); //左上
			g.fillRect(x+w-SIZE, y-SIZE, SIZE*2, SIZE*2); //右上
			g.fillRect(x-SIZE, y+h-SIZE, SIZE*2, SIZE*2); //左下
			g.fillRect(x+w-SIZE, y+h-SIZE, SIZE*2, SIZE*2); //右下
			} else {
				g.setColor(Color.black);
				g.fillRect(x-SIZE-3/4*r, y-SIZE-r, SIZE*2, SIZE*2);//上中
				g.fillRect(x-SIZE+r, y-SIZE-3/4*r, SIZE*2, SIZE*2);//左中
				g.fillRect(x-SIZE-3/4*r, y-SIZE+r, SIZE*2, SIZE*2);//下中
				g.fillRect(x-SIZE-r, y-SIZE-3/4*r, SIZE*2, SIZE*2); //右中
				g.fillRect(x-SIZE-r, y-SIZE-r, SIZE*2, SIZE*2); //左上
				g.fillRect(x-SIZE+r, y-SIZE-r, SIZE*2, SIZE*2); //右上
				g.fillRect(x-SIZE-r, y-SIZE+r, SIZE*2, SIZE*2); //左下
				g.fillRect(x-SIZE+r, y-SIZE+r, SIZE*2, SIZE*2); //右下
			}
		}
	}
	
	///////////////////////////図形選択処理メソッド//////////////////////////////////////
	
	//その図形インスタンスはクリックした位置に含まれるか　true or false
	public boolean contains (int x, int y){
		return region.contains(x,y); //含まれていればtrueを返す
	}
	//その図形インスタンスは選択範囲内に重なっているのか true of false
	public boolean intersect (int x, int y, int w, int h){
		return region.intersects(x, y, w, h); //重なっていればtrueを返す
	}
	
	//図形を選択状態にするアクセッサ
	public boolean getSelected(){return isSelected;}
	public void setSelected(boolean isSelected){this.isSelected = isSelected;}
	//領域の設定
	public void setRegion(){
		//範囲指定のため一時的な変数を用いて設定する
		int xs = this.x,ys = this.y,ws = this.w,hs = this.h;
		if ( ws < 0 ) {
			xs += ws;
			ws *= -1;
		}
		if ( hs < 0 ) {
			ys += hs;
			hs *= -1;
		}
		//星の場合の特別な処理
		if(r > 0){
			xs -= r*Math.sin(Math.toRadians(72));
			ys -= r;
			ws = (int) (2*r*Math.sin(Math.toRadians(72)));
			hs = (int) (r+ r*Math.cos(Math.toRadians(36)));
		}
		region = new Rectangle(xs,ys,ws,hs);
	}
	//////////////////////////////////////////////////////////////
	
	public void move( int dx, int dy){
	    //オブジェクトを移動する処理を書く
		this.x += dx;
		this.y += dy;
		setRegion();
	}
	
	public void setLocation(int x, int y){
		//オブジェクトの位置を変更する処理を書く
		this.x = x;
		this.y = y;
	}

	public void setbufferLocation(int x, int y){
		this.x = this.x-x;
		this.y = this.y-y;
		setRegion();
	}
	public void setSize( int w, int h){
		//オブジェクトのサイズを変更する処理を書く
		this.w = w;
		this.h = h;
	}
	
	public void setStar (int w, int h, int r){
		this.w = w;
		this.h = h;
		this.r = r;
	}
	
	//コピー、カットに関して一時的に保存するためのオブジェクト生成
	public MyDrawing clone() {
        try {
            return (MyDrawing) (super.clone());
        } catch (CloneNotSupportedException e) {
            throw (new InternalError(e.getMessage()));
        }
    }
	
	//座標に関するアクセッサの定義
	public int getX(){ return this.x; }
	public void setX(int x){ this.x = x;}
	public int getY(){ return this.y; }
	public void setY(int y){ this.y = y;}
	public int getW(){ return this.w; }
	public void setW(int w){ this.w = w; }
	public int getH(){ return this.h; }
	public void setH(int h){ this.h = h; }
	public int getR() { return this.r; }
	public void getR(int r){this.r = r; }
	
	//図形の線分に関するアクセッサの定義
	public Color getLineColor(){return lineColor;}
	public void setLineColor(Color c){this.lineColor = c;}
	public Color getFillColor(){return fillColor;}
	public void setFillColor(Color c){this.fillColor = c;}
	public int getLineWidth(){return lineWidth;}
	public void setLineWidth(int width){this.lineWidth = width;}
	
	//図形の座標を正しくする
	public void setPoint(MyDrawing d){
		int xg,yg,wg,hg;
		xg = getX();
		yg = getY();
		wg = getW();
		hg = getH();
		//幅が負なら正にして始点のx座標を変更する
		if (wg < 0 ) {
			wg *= -1;
			xg = xg- wg;
		}
		if(getType() != 'T'){
		//高さが負なら正にする
		if ( hg < 0 ) {
			hg *= -1;
			yg = yg-hg;
		}
		}
		//変更したものを新たに代入する
		d.setX(xg);
		d.setY(yg);
		d.setW(wg);
		d.setH(hg);
	}	
}
