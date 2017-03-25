package br.com.margel.postitdigital.controle;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class Db {

	private static EntityManager manager;
	
	public static EntityManager em(){
		if(manager==null){
			manager = Persistence
					.createEntityManagerFactory("postits_h2")
					.createEntityManager();
		}
		return manager;
	}

	public static void close() {
		if(manager!=null && manager.isOpen()){
			manager.close();
		}
	}
	
}