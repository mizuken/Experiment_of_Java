
import java.awt.*;

//破線状態の記述
public class MyDashStroke extends BasicStroke
{
	private static float pattern[] = {10,15};
	
	public MyDashStroke(float lineWidth){
		super(lineWidth, CAP_BUTT, JOIN_BEVEL,1.0f,pattern,0);
	}
}
