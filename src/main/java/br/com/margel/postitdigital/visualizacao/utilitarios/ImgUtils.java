package br.com.margel.postitdigital.visualizacao.utilitarios;

import javax.swing.ImageIcon;

public class ImgUtils {

	public static final String IMGS_PATH = "imagens/";

	public static ImageIcon getImageIcon(String img) {
		return new ImageIcon(ImgUtils.class.getClassLoader().getResource(IMGS_PATH+img));
	}
	
}