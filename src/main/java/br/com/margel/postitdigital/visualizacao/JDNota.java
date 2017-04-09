package br.com.margel.postitdigital.visualizacao;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import br.com.margel.postitdigital.controle.NotaControle;
import br.com.margel.postitdigital.modelos.Nota;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import br.com.margel.postitdigital.visualizacao.utilitarios.ImgUtils;
import br.com.margel.postitdigital.visualizacao.utilitarios.MoveComponentListener;

@SuppressWarnings("serial")
public class JDNota extends JDialog{

	private static final Color BG = new Color(251, 230, 87);
	private JTextArea jtaDescricao = new JTextArea();
	private JButton jbSalvar = new JButton();
	private JButton jbRemover = new JButton();
	
	private NotaControle ctrl = new NotaControle();
	private Nota nota;
	private MoveComponentListener mv;
	private JFCriarNota framePrincipal;
	
	public JDNota(JFCriarNota framePrincipal) {
		super(framePrincipal);
		this.framePrincipal = framePrincipal;
		
		jtaDescricao.setLineWrap(true);
		jtaDescricao.setWrapStyleWord(true);
		jtaDescricao.setMargin(new Insets(5, 5, 5, 5));
		jtaDescricao.setOpaque(false);
		jtaDescricao.setFont(jtaDescricao.getFont().deriveFont(Font.PLAIN,18f));
		jtaDescricao.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				jbSalvar.setVisible(true);
			}
		});
		
		JScrollPane scroll = new JScrollPane(jtaDescricao);
		scroll.setOpaque(false);
		scroll.getViewport().setOpaque(false);
		scroll.setBorder(new EmptyBorder(0, 0, 0, 0));
		
		jbSalvar.addActionListener(new SalvarAction());
		jbSalvar.setFocusable(false);
		jbSalvar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		jbSalvar.setIcon(ImgUtils.getImageIcon("salvar20x20.png"));
		jbSalvar.setVisible(false);
		
		jbRemover.addActionListener(new RemoverAction());
		jbRemover.setFocusable(false);
		jbRemover.setCursor(new Cursor(Cursor.HAND_CURSOR));
		jbRemover.setIcon(ImgUtils.getImageIcon("remover20x20.png"));
		
		JPanel jpTopo = new JPanel(new MigLayout(new LC().fillX().noGrid()));
		jpTopo.setCursor(new Cursor(Cursor.MOVE_CURSOR));
		jpTopo.setBackground(new Color(231, 210, 67));
		jpTopo.add(jbSalvar, new CC().growY().alignX("right"));
		jpTopo.add(jbRemover, new CC().growY().alignX("right"));
		
		setLayout(new MigLayout(new LC().insets("0", "0", "3", "0").gridGapY("0")));
		add(jpTopo, new CC().width("100%").wrap());
		add(scroll, new CC().gap("3","3","3","3").width("250:100%:").height("180:100%:"));
		
		getContentPane().setBackground(BG);
		mv = MoveComponentListener.install(this);
		
		setUndecorated(true);
		pack();
		setMinimumSize(getSize());
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				if(nota.getId()!=null && 
						(nota.getW()!=getSize().width || nota.getH()!=getSize().height)){
					nota.setH(getSize().height);
					nota.setW(getSize().width);
					jbSalvar.setVisible(true);
				}
			}
			@Override
			public void componentMoved(ComponentEvent e) {
				if(nota.getId()!=null && 
						(nota.getX()!=getLocation().x || nota.getY()!=getLocation().y)){
					nota.setX(getLocation().x);
					nota.setY(getLocation().y);
					jbSalvar.setVisible(true);
				}
			}
		});
	}
	
	public void setNota(Nota nota){
		if(nota.getX()!=null && nota.getY()!=null){
			setLocation(nota.getX(), nota.getY());
		}
		if(nota.getW()!=null && nota.getH()!=null){
			setSize(nota.getW(), nota.getH());
		}
		setTitle(nota.getId()==null?"Nova nota":"Nota "+nota.getId());
		jtaDescricao.setText(nota.getTexto());
		this.nota = nota;
	}
	
	private class SalvarAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			nota.setX(getLocation().x);
			nota.setY(getLocation().y);
			nota.setH(getSize().height);
			nota.setW(getSize().width);
			nota.setTexto(jtaDescricao.getText());
			ctrl.salvar(nota);
			setTitle("Nota "+nota.getId());
			jbSalvar.setVisible(false);
		}
		
	}
	
	private class RemoverAction implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(nota.getId()!=null ){
				if(JOptionPane.showConfirmDialog(JDNota.this, "Deseja remover esta nota?",
						"Remover",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
					ctrl.excluir(nota);
				}else{
					return;
				}
			}
			
			framePrincipal.removerDialog(JDNota.this);
			dispose();
		}
		
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		mv.paintResized(this, g);
	}
	
}
