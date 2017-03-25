package br.com.margel.postitdigital.visualizacao.componentes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class MoveComponentListener extends MouseAdapter{
	private int baseX = -1;
	private int baseY = -1;
	private Component moveComp;
	private boolean resize;
	public final int wresize = 15;
	public final int hresize = 3;
	private final Rectangle rh = new Rectangle();
	private final Rectangle rv = new Rectangle();
	private final Rectangle rm = new Rectangle();

	public MoveComponentListener() {

	}
	public MoveComponentListener(Component moveComp) {
		this.moveComp = moveComp;
	}
	
	public static MoveComponentListener install(Container container, Component component) {
		MoveComponentListener l = new MoveComponentListener(container);
		component.addMouseMotionListener(l);
		component.addMouseListener(l);
		return l;
	}
	
	public static MoveComponentListener install(Container container) {
		return install(container, container);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if ((baseX != -1) && (baseY != -1)) {  
			Component c = moveComp!=null?moveComp:e.getComponent();
			if(c!=null){
				if(resize){
					c.setSize(e.getX(), e.getY());
					c.setPreferredSize(new Dimension(e.getX(), e.getY()));
				}else{
					int x = c.getX() + e.getX() - baseX;  
					int y = c.getY() + e.getY() - baseY;  
					c.setLocation(x, y);
				}
				c.repaint();
			}
		}  
	}
	private boolean isResizable(Component c) {
		if(c instanceof JFrame)
			return ((JFrame)c).isResizable();
		if(c instanceof JDialog)
			return ((JDialog)c).isResizable();
		return false;
	}
	@Override
	public void mousePressed(MouseEvent e) {
		baseX = e.getX();  
		baseY = e.getY();
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		baseX = -1;  
		baseY = -1;
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		Component c = moveComp!=null?moveComp:e.getComponent();
		if(c!=null && (( c instanceof Window && isResizable(c)) || !(c instanceof Window) )){
			int mx = e.getX();
			int my = e.getY();
			int cx = c.getWidth();
			int cy = c.getHeight();

			rh.setBounds(cx-wresize,cy-hresize, wresize, hresize);
			rv.setBounds(cx-hresize,cy-wresize, hresize, wresize);
			rm.setBounds(mx,my,1,1);

			if( rh.intersects(rm) || rv.intersects(rm) ){
				resize = true;
				c.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
			}else{
				resize = false;
				c.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		}
	}
	public void setMoveComp(Component c){
		moveComp = c;
	}
	public boolean isResize(){
		return resize;
	}
	public void paintResized(Component c, Graphics g) {
		Graphics2D g2d = (Graphics2D)g.create();
		g2d.setColor(Color.black);

		g2d.fillRect(c.getWidth()-wresize, c.getHeight()-hresize, wresize, hresize);
		g2d.fillRect(c.getWidth()-hresize, c.getHeight()-wresize, hresize, wresize);
		g2d.dispose();
	}
}