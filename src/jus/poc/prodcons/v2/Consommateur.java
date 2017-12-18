package jus.poc.prodcons.v2;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons.v2.ProdCons;
import jus.poc.prodcons.v2.TestProdCons;

public class Consommateur extends Acteur implements _Consommateur, Runnable{
	private int nbMessagesConsommes = 0;
	private int idConsommateur;
	private ProdCons pc;
	public TestProdCons tpc;
	private Aleatoire random_generator;
	private int random_timegap; 
	
	protected Consommateur(int type, Observateur observateur, int moyenneTempsDeTraitement,
			int deviationTempsDeTraitement) throws ControlException {
		super(Acteur.typeConsommateur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
	}
	
	protected Consommateur(int type, Observateur observateur, int moyenneTempsDeTraitement,
			int deviationTempsDeTraitement, ProdCons pc, int id) throws ControlException {
		super(Acteur.typeConsommateur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
		this.setProdCons(pc);
		this.idConsommateur = id;
		random_generator = new Aleatoire(moyenneTempsDeTraitement, deviationTempsDeTraitement);
	}

	@Override
	public int nombreDeMessages() {
		return nbMessagesConsommes;
	}
	
	public void setProdCons(ProdCons pc) {
		this.pc = pc;
	}
	
	public void setTestProdCons(TestProdCons tpc) {
		this.tpc = tpc;
	}
	
	public void run () {
		Message mssg;
		for(;;) {
			random_timegap = this.random_generator.next() * 100;
			
			try {
				mssg = pc.get(this);
				if(mssg == null) break;
				nbMessagesConsommes++;
				
				sleep(random_timegap);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(this.toString() + " finished reading");
	}
	
	public String toString() {
		String s = "Consommateur numéro : " + this.idConsommateur +" ";
		return s;
	}
}
