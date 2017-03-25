package br.com.margel.postitdigital;

import javax.swing.UIManager;

import br.com.margel.postitdigital.visualizacao.JFCriarNota;

public class Main {

	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		new JFCriarNota().setVisible(true);
	}

}
