package br.com.margel.postitdigital.controle;

import java.util.List;
import javax.persistence.EntityTransaction;
import br.com.margel.postitdigital.modelos.Nota;

public class NotaControle {

	public void salvar(Nota nota){
		EntityTransaction tr = null;
		try {
			tr = Db.em().getTransaction();
			tr.begin();
			
			if(nota.getId()==null){
				Db.em().persist(nota);
			}else{
				Db.em().merge(nota);
			}
			
			tr.commit();
		} catch (Throwable e) {
			if(tr!=null){
				tr.rollback();
			}
			throw e;
		}
	}
	
	public void excluir(Nota nota){
		EntityTransaction tr = null;
		try {
			tr = Db.em().getTransaction();
			tr.begin();
			
			Db.em().remove(nota);
			
			tr.commit();
		} catch (Throwable e) {
			if(tr!=null){
				tr.rollback();
			}
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Nota> listarTodas(){
		return Db.em()
				.createQuery("FROM Nota")
				.getResultList();
	}
	
}