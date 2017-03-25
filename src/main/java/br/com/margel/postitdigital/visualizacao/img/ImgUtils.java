package br.com.margel.postitdigital.visualizacao.img;

import javax.swing.ImageIcon;

public class ImgUtils {

	public static final String IMGS_PATH = "/br/com/margel/postitdigital/visualizacao/img/";

	public static ImageIcon getImageIcon(String img) {
		return new ImageIcon(ImgUtils.class.getResource(IMGS_PATH+img));
	}
	
}