import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.awt.Cursor;
//ウィンドウを表すクラス
import java.util.Enumeration;

public class MyApplication extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;
	StateManager stateManager;
	MyCanvas canvas;
	Mediator mediator;
	JRadioButton[] radio;
	
	/////////////////メニュー関係//////////////////////////////////////////////////
	//色関係の変数設定
	private JMenuBar menuBar;
	private JMenu    colorMenu,objectFill,lineFill;
	private JMenuItem redItem, blueItem, greenItem, yellowItem, blackItem;
	private JMenuItem redItem_l, blueItem_l, greenItem_l,yellowItem_l,blackItem_l;
	//ソート関係の設定
	private JMenu sortMenu;
	private JMenuItem ByTop,ByHight;
	
	//図形のカット、コピー、ペースト、影をつける
	JPopupMenu popup;
	JMenu shadowMenu,setPosition;
	JMenuItem copyMenuItem, cutMenuItem, pasteMenuItem,shadow_on,shadow_off,setfirstfront,setlastback;
	JMenuItem setonefront,setoneback;
	int c_X,c_Y; //右クリックした位置を保存する
	
	//色
	 JComboBox colorBox,linecolorBox;//色選択のために用意
	 
	 /////////////////////////////////////////////////////////////////////////////////////
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////
	public MyApplication(){
		super("My Paint Application");
		
		//元のパネル
		canvas = new MyCanvas();
		canvas.setBackground(Color.white);
		JPanel jp = new JPanel();
		jp.setLayout(new FlowLayout());
		
		//追加パネル
		JPanel jp2 = new JPanel();
		//jp2.setLayout(new FlowLayout());
		jp2.setLayout(new GridLayout(4,1));
		//jp2.setLayout(new BoxLayout(jp2,BoxLayout.Y_AXIS));
		
		//追加パネル
		JPanel jp3 = new JPanel();
		jp3.setLayout(new FlowLayout());
		
		stateManager = new StateManager(canvas);
		mediator = stateManager.mediator;
		
		//ボタン表示およびボタン入力の処理
		RectButton rectButton = new RectButton(stateManager);
		jp2.add(rectButton);
		OvalButton ovalButton = new OvalButton(stateManager);
		jp2.add(ovalButton);
		StarButton starButton = new StarButton(stateManager);
		jp2.add(starButton);
		TriangleButton triangleButton = new TriangleButton(stateManager);
		jp2.add(triangleButton);
		
		//図形選択状態を切り替えるボタン
		SelectButton selectbutton = new SelectButton(stateManager);
		jp.add(selectbutton);
		
		//削除ボタンの実装
		DeleteButton deleteButton = new DeleteButton(mediator);
		jp.add(deleteButton);
		
		//影をつけるかどうかを決めるチェックボックス
		ShadowCheck shadowcheck = new ShadowCheck(stateManager);
		jp3.add(shadowcheck);
				
		//破線にするかいなか決めるチェックボックス
		DashCheck dashcheck = new DashCheck(stateManager);
		jp3.add(dashcheck);
		
		//色選択を行うコンボボックス
		JLabel o_color_label = new JLabel(); 
		o_color_label.setText("o-color");
		jp.add(o_color_label);
		ColorBox colorbox = new ColorBox(stateManager);
		jp.add(colorbox);
		
		JLabel l_color_label = new JLabel(); 
		l_color_label.setText("l-color");
		jp.add(l_color_label);
        LineColorBox linecolorbox = new LineColorBox(stateManager);
		jp.add(linecolorbox);
		
		/////////色選択メニューの実装///////////////////////////////////
		menuBar = new JMenuBar(); //メニューバーを作る
		setJMenuBar(menuBar); //メニュバーに設置
		//メニューリストの設計
		colorMenu = new JMenu("Color");
		objectFill = new JMenu("Object");
		lineFill = new JMenu("Line");
		colorMenu.add(objectFill);
		colorMenu.add(lineFill);
		redItem  = new JMenuItem("Red");
		blueItem = new JMenuItem("Blue");
		greenItem = new JMenuItem("Green");
		yellowItem = new JMenuItem("Yellow");
		blackItem = new JMenuItem("Black");
		redItem_l  = new JMenuItem("Red");
		blueItem_l = new JMenuItem("Blue");
		greenItem_l = new JMenuItem("Green");
		yellowItem_l = new JMenuItem("Yellow");
		blackItem_l = new JMenuItem("Black");
		objectFill.add(redItem);
		objectFill.add(blueItem);
		objectFill.add(greenItem);
		objectFill.add(yellowItem);
		objectFill.add(blackItem);
		lineFill.add(redItem_l);
		lineFill.add(blueItem_l);
		lineFill.add(greenItem_l);
		lineFill.add(yellowItem_l);
		lineFill.add(blackItem_l);
		//実行処理
		redItem.addActionListener(this);
		blueItem.addActionListener(this);
		greenItem.addActionListener(this);
		yellowItem.addActionListener(this);
		blackItem.addActionListener(this);
		redItem_l.addActionListener(this);
		blueItem_l.addActionListener(this);
		greenItem_l.addActionListener(this);
		yellowItem_l.addActionListener(this);
		blackItem_l.addActionListener(this);
		menuBar.add(colorMenu);
		 //ソート関係のメニューボタンを作る
		sortMenu = new JMenu("Sort");
		ByTop = new JMenuItem("ByTop");
		ByHight = new JMenuItem("ByHight");
		sortMenu.add(ByTop);
		sortMenu.add(ByHight);
		ByTop.addActionListener(this);
		ByHight.addActionListener(this);
		menuBar.add(sortMenu);
		
		
		//右クリックで出てくるポップアップメニュー
		//図形のcut,copy,paste
		popup = new JPopupMenu();
		copyMenuItem = new JMenuItem("Copy");
		cutMenuItem = new JMenuItem("Cut");
		pasteMenuItem = new JMenuItem("Paste");
		copyMenuItem.addActionListener(this);
		cutMenuItem.addActionListener(this);
		pasteMenuItem.addActionListener(this);
		popup.add(copyMenuItem);
		popup.add(cutMenuItem);
		popup.add(pasteMenuItem);
		//影をつける
		shadowMenu = new JMenu("Shadow");
		shadow_on = new JMenuItem("on");
		shadow_off = new JMenuItem("off");
		shadowMenu.add(shadow_on);
		shadowMenu.add(shadow_off);
		shadow_on.addActionListener(this);
		shadow_off.addActionListener(this);
		popup.add(shadowMenu);
		//図形位置関係を変更する
		setPosition = new JMenu("Position");
		setfirstfront = new JMenuItem("最前面に配置");
		setonefront = new JMenuItem("1つ前に配置");
		setoneback = new JMenuItem("1つ後ろに配置");
		setlastback = new JMenuItem("最背面に配置");
		setPosition.add(setfirstfront);
		setPosition.add(setonefront);
		setPosition.add(setoneback);
		setPosition.add(setlastback);
		setfirstfront.addActionListener(this);
		setonefront.addActionListener(this);
		setoneback.addActionListener(this);
		setlastback.addActionListener(this);
		popup.add(setPosition);
		////////////////線の太さを変える//////////////////////////////////////////////////
		LineWidthSlider linewidthslider = new LineWidthSlider(mediator); //スライダーの定義
		linewidthslider.setMinimum(1); //最小値
		linewidthslider.setMaximum(5); //最大値
		linewidthslider.setValue(1);
		//ラベルを付ける
		JLabel linewidthlabel = new JLabel(); 
		linewidthlabel.setText("linewidth");
		jp3.add(linewidthlabel);
		//
		linewidthslider.setMajorTickSpacing(1); //メモリの設定
		linewidthslider.setPaintTicks(true); //メモリの表示
		linewidthslider.setSnapToTicks(true); //メモリの幅でしか選択できない
		linewidthslider.setLabelTable(linewidthslider.createStandardLabels(1)); //目盛ラベルの設定
		linewidthslider.setPaintLabels(true); //メモリラベルの表示
		jp3.add(linewidthslider);
		mediator.setLinewidth(linewidthslider.getValue());
		/////////////////////////////////////////////////////////////////////////////////////
		
		///////////////////色の透明度を変える//////////////////////////////////////////////////
		ColorAlphaSlider coloralphaslider = new ColorAlphaSlider(mediator); //スライダーの定義
		coloralphaslider.setMinimum(0); //最小値
	    coloralphaslider.setMaximum(255); //最大値
		coloralphaslider.setValue(255);
		//ラベルを付ける
		JLabel coloralphalabel = new JLabel(); 
		coloralphalabel.setText("color alpha");
		jp.add(coloralphalabel);
		jp.add(coloralphaslider);
		/////////////////////////////////////////////////////////////////////////////////////
		
		getContentPane().add(jp,BorderLayout.NORTH);
		getContentPane().add(jp2,BorderLayout.WEST);
		getContentPane().add(jp3,BorderLayout.SOUTH);
		getContentPane().add(canvas,BorderLayout.CENTER);
		
		canvas.requestFocusInWindow();

		//クリックされた時の処理
		canvas.addMouseListener(
				new MouseAdapter(){
					//現在の状態のmouseDown処理の呼び出し
					public void mousePressed(MouseEvent e){
						canvas.requestFocusInWindow();
						if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
							//左
							stateManager.mouseDown(e.getX(),e.getY());
							repaint();
						}
						
						if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0) {
							//右
							popup.show(e.getComponent(), e.getX(), e.getY());
							//クリック位置情報の保存
							c_X = e.getX();
							c_Y = e.getY();
						}
						
					}
					//現在の状態のmouseUp処理の呼び出し
					public void mouseReleased(MouseEvent e){
						stateManager.mouseUp(e.getX(),e.getY());
						mediator.repaint();
					}
					
					
				});
		
		canvas.addMouseMotionListener(
				new MouseAdapter(){
					//現在の状態のmouseDrag処理の呼び出し
					public void mouseDragged(MouseEvent e){
						stateManager.mouseDrag(e.getX(), e.getY());
						mediator.repaint();
					}
					public void mouseMoved(MouseEvent e){
						setCursor(new Cursor(0));
						for(int i = 0; i < mediator.selectedDrawings.size(); i++){
							if(mediator.selectedDrawings.elementAt(i).contains(e.getX(), e.getY())){
								setCursor(new Cursor(12));
							}
						}
						mediator.repaint();
					}
				});
		
		canvas.setFocusable(true);

		canvas.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				int keycode = e.getKeyCode();
				
				//delete keyで削除
				if(keycode == 8){
					if(!mediator.selectedDrawings.isEmpty()){
						Enumeration<MyDrawing> se = mediator.selectedDrawingsElements();
						while(se.hasMoreElements()){
							mediator.removeDrawing(se.nextElement());
							}
						//再描写
						mediator.repaint();
					}
			    }
				int mod = e.getModifiersEx();
				  if ((mod & InputEvent.CTRL_DOWN_MASK) != 0){
				    //Ctrl + x でカット
					if(keycode == 88){
						if(!mediator.selectedDrawings.isEmpty()){
							mediator.cut();
							mediator.setCut(true);
							}
							mediator.repaint();
				    }
					//Ctrl + c でコピー
				    if(keycode == 67){
				    	if(!mediator.selectedDrawings.isEmpty()){	
							mediator.copy();
							}
							mediator.repaint();
				    }
				  //Ctrl + v でペースト
				    if(keycode == 86){
				    	if(! mediator.buffers.isEmpty()){
							mediator.paste(100,100);
							mediator.setCut(false);
							}
							mediator.repaint();
				    }
				  }
			}
		});
		
		//requestFocusInWindow();
		
		
		this.addWindowListener(new WindowAdapter(){
			//ウィンドウを閉じたら終了
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
	}
	public Dimension getPerferredSize(){
		return new Dimension(1000,800);
	}

	public static void main(String[] args) {
		MyApplication app = new MyApplication();
		app.pack();
		app.setVisible(true);
		app.setSize(1000,800);
	}
	
	
	//色の変更の実装
	public void actionPerformed(ActionEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		//色関係の条件分岐
		//内部
		if(e.getSource() == redItem){
			mediator.setColor(Color.red);
			mediator.repaint();
		}else if(e.getSource() == blueItem){
			mediator.setColor(Color.blue);
			mediator.repaint();
		}else if(e.getSource() == greenItem){
			mediator.setColor(Color.green);
			mediator.repaint();
		}else if(e.getSource() == yellowItem){
			mediator.setColor(Color.yellow);
			mediator.repaint();
		}else if(e.getSource() == blackItem){
			mediator.setColor(Color.black);
			mediator.repaint();
		}else{
		}
		//線
		if(e.getSource() == redItem_l){
			mediator.setLineColor(Color.red);
			mediator.repaint();
		}else if(e.getSource() == blueItem_l){
			mediator.setLineColor(Color.blue);
			mediator.repaint();
		}else if(e.getSource() == greenItem_l){
			mediator.setLineColor(Color.green);
			mediator.repaint();
		}else if(e.getSource() == yellowItem_l){
			mediator.setLineColor(Color.yellow);
			mediator.repaint();
		}else if(e.getSource() == blackItem_l){
			mediator.setLineColor(Color.black);
			mediator.repaint();
		}else{}
		
		if(e.getSource() == ByTop){
			mediator.sortByTop();
			mediator.repaint();
		}else if(e.getSource() == ByHight){
			mediator.sortByHight();
			mediator.repaint();
		}else{}
		
		//////////////////cut,copy,paste/////////////////////////////////////////////
		if(e.getActionCommand() == "Copy"){
			if(!mediator.selectedDrawings.isEmpty()){	
			mediator.copy();
			}
			mediator.repaint();
		}
		if(e.getActionCommand() == "Cut"){
			if(!mediator.selectedDrawings.isEmpty()){
			mediator.cut();
			mediator.setCut(true);
			}
			mediator.repaint();
		}
		if(e.getActionCommand() == "Paste"){
		 if(! mediator.buffers.isEmpty()){
			mediator.paste(c_X,c_Y);
			mediator.setCut(false);
			}
			mediator.repaint();
		}
		
		////////////////影のクリックon_off/////////////////////////////////////////////
		if(e.getActionCommand() == "on"){
			if(!mediator.selectedDrawings.isEmpty()){
				for(int i = 0; i < mediator.selectedDrawings.size(); i++){
				    if(mediator.selectedDrawings.elementAt(i).isShadow == false){
				    	mediator.selectedDrawings.elementAt(i).setShadow(true);
				    }
				}
			}
			repaint();
		}
		if(e.getActionCommand() == "off"){
			if(!mediator.selectedDrawings.isEmpty()){
				for(int i = 0; i < mediator.selectedDrawings.size(); i++){
				    if(mediator.selectedDrawings.elementAt(i).isShadow == true){
				    	mediator.selectedDrawings.elementAt(i).setShadow(false);
				    }
				}
			}
			repaint();
		}
		
		//////////////図形の配置関係///////////////////////////////////////////////////////
		
		if(e.getActionCommand() == "最前面に配置"){
			if(!mediator.selectedDrawings.isEmpty()){
				mediator.setFront();
			}
			repaint();
		}
		if(e.getActionCommand() == "1つ前に配置"){
			if(!mediator.selectedDrawings.isEmpty()){
				mediator.setFrontword();
			}
			repaint();
		}
		if(e.getActionCommand() == "1つ後ろに配置"){
			if(!mediator.selectedDrawings.isEmpty()){
				mediator.setBackword();
			}
			repaint();
		}
		if(e.getActionCommand() == "最背面に配置"){
			if(!mediator.selectedDrawings.isEmpty()){
				mediator.setBack();
			}
			repaint();
		}
	}

}
