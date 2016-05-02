
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

//import java.util.Enumeration;

public class ColorAlphaSlider extends JSlider {

	private static final long serialVersionUID = 1L;
	StateManager statemanager;
	Mediator mediator;
	MyDrawing d;
	float alpha;
	
	public ColorAlphaSlider(Mediator mediator){	
		addChangeListener(new ColorAlphaListener());
		this.mediator = mediator;
	}
	

	class ColorAlphaListener implements ChangeListener{
	@Override
		public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();
		alpha = (float)source.getValue();
		alpha = alpha/255;
		mediator.setColorAlpha(alpha);
	    mediator.repaint();
	}
 }

}
