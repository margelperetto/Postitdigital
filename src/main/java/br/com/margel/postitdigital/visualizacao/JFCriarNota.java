package br.com.margel.postitdigital.visualizacao;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import br.com.margel.postitdigital.controle.Db;
import br.com.margel.postitdigital.controle.NotaControle;
import br.com.margel.postitdigital.modelos.Nota;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import br.com.margel.postitdigital.visualizacao.componentes.MoveComponentListener;
import br.com.margel.postitdigital.visualizacao.img.ImgUtils;

@SuppressWarnings("serial")
public class JFCriarNota extends JFrame{
	
	private NotaControle ctrl = new NotaControle();
	private JButton jbCriarNota = new JButton("Criar Nova Nota");
	private Dimension screenSize;
	private List<Window> dialogs = new LinkedList<>();
	
	public JFCriarNota() {
		
		jbCriarNota.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirDialogNota(new Nota());
			}
		});
		
		setLayout(new MigLayout(new LC().insetsAll("40").fill()));
		add(jbCriarNota, new CC().alignX("center"));
		
		getContentPane().setBackground(new Color(58, 108, 203));
		
		pack();
		setMinimumSize(getSize());
		setTitle("Criar Nota");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		screenSize = getToolkit().getScreenSize();
		setLocation(screenSize.width-getWidth()-10, screenSize.height-getHeight()-70);
		MoveComponentListener.install(this);
		
		dialogs.add(this);
		
		carregarNotas();
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Db.close();
			}
		});
	}
	
	private void carregarNotas(){
		jbCriarNota.setEnabled(false);
		jbCriarNota.setText("Carregando...");
		new SwingWorker<List<Nota>, Void>() {
			@Override
			protected List<Nota> doInBackground() throws Exception {
				return ctrl.listarTodas();
			}
			@Override
			protected void done() {
				try {
					for(Nota nota : get()){
						abrirDialogNota(nota);
					}
					jbCriarNota.setEnabled(true);
					jbCriarNota.setText("Criar Nova Nota");
					jbCriarNota.setIcon(ImgUtils.getImageIcon("add_nota18x18.png"));
				} catch (Exception e) {
					e.printStackTrace();
					jbCriarNota.setText("Erro!");
					JOptionPane.showMessageDialog(JFCriarNota.this, 
							"Não foi possível carregar notas! \n"+e.getMessage(),
							"Erro",JOptionPane.ERROR_MESSAGE);
				}
			}
		}.execute();
	}

	private void abrirDialogNota(Nota nota){
		JDNota dialog = new JDNota(this);
		dialog.setNota(nota);
		if(nota.getX()==null||nota.getY()==null){
			Point p = obterPosicaoNovaJanela(dialog.getSize().width,dialog.getSize().height);
			dialog.setLocation(p);
		}
		dialog.setVisible(true);
		
		dialogs.add(dialog);
	}
	
	private Point obterPosicaoNovaJanela(int w, int h) {
		int maxX = screenSize.width-w;
		int maxY = screenSize.height-h-20;
		
		int x =  new Random().nextInt(maxX);
		int y =  new Random().nextInt(maxY);

		if(w<=0 || h<= 0){
			return new Point(x, y);
		}
		
		Rectangle2D rd2 = new Rectangle2D.Double(x, y, w, h);
		boolean intersect = false;
		for(Window d : dialogs){
			if(rd2.intersects(d.getX(),d.getY(),d.getWidth(),d.getHeight())){
				intersect = true;
				break;
			}
		}
		
		return intersect?obterPosicaoNovaJanela(w-1, h-1):new Point(x, y);
	}
	
	public void removerDialog(JDNota dialog){
		dialogs.remove(dialog);
	}
	
}
