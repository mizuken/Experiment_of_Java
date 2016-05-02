
import java.awt.*;

public class MyStar extends MyDrawing
{	
	Polygon pol,pol_s;
	//MyStarコンストラクタの設定
	public MyStar(int x,int y,int w,int h,
			int r,Color lc,Color fc, int lw){
			super(x,y,w,h,r,lc,fc,lw);
			setType('S');
		}
	//座標、大きさ,色を指定する
	public MyStar(int x,int y,int r,Color lc, Color fc){
		this(x,y,0,0,r,lc ,fc,1);
	}
	//座標と色を指定する
	public MyStar(int x,int y, Color lc, Color fc){
		this(x,y,30,30,30,lc,fc,1);
	}
	//座標と大きさを指定する
	public MyStar(int x,int y,int r){
		this(x,y,30,30,r,Color.black ,Color.blue,1);
	}
	//座標を指定する
	public MyStar(int x,int y){
		this(x,y,30,30,30,Color.black ,Color.blue,1);
	}
	//色を指定する
	public MyStar(Color lc,Color fc){
		this(30,30,30,30,30, lc , fc,1);
	}
	//何も指定しない
	public MyStar(){
		this(20,20,20,20,30,Color.black ,Color.white,1);
	}
	
	public void draw(Graphics g){
		int x = getX();
		int y = getY();
		//int w = getW();
		//int h = getH();
		int r = getR();
		
		//大きさが負の場合の処理
		if(r < 0){
			r *= -1;
		}
		
		double theta = Math.PI/2;
		int[] xPoints_d = new int[10];
		int[] yPoints_d = new int[10];
		for(int i=0; i<10; i++){
			if(i % 2 == 0){
			xPoints_d[i]
			 = (int)(r * Math.cos(i*Math.PI*1/5 + theta) + x );
			yPoints_d[i]
			 = -(int)(r * Math.sin(i*Math.PI*1/5 + theta)) + y;
			}else{
				xPoints_d[i]
				= (int)(0.37*r * Math.cos(i*Math.PI*1/5 + theta) + x );
				yPoints_d[i]
			    = -(int)(0.37*r * Math.sin(i*Math.PI*1/5 + theta)) + y;
				
			}
		}	
		pol = new Polygon(xPoints_d,yPoints_d,10);
		
		Graphics2D g2 = (Graphics2D) g;
		
		if(getShadow()){
			int[] xPoints_s = new int[5];
			int[] yPoints_s = new int[5];
			for(int i=0; i<5; i++){
				xPoints_s[i]
				 = (int)(r * Math.cos(2.0*i*Math.PI*2/5 + theta) + x +3);
				yPoints_s[i]
				 = -(int)(r * Math.sin(2.0*i*Math.PI*2/5 + theta)) + y +3;
			}	
			pol_s = new Polygon(xPoints_s,yPoints_s,5);
			g2.setColor(Color.black);
			g2.fillPolygon(pol_s);
			g2.setColor(Color.black);
			g2.drawPolygon(pol_s);
		}
		
		
		if(getDashed()){
			g2.setStroke(new MyDashStroke(getLineWidth()));
		}else{
			g2.setStroke(new BasicStroke(getLineWidth()));
		}
		g2.setColor(getFillColor());
		g2.fillPolygon(pol);
		g2.setColor(getLineColor());
		g2.drawPolygon(pol);
		super.draw(g);
		
	}
}
