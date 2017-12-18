package jus.poc.prodcons.v3;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons.v3.ProdCons;
import jus.poc.prodcons.v3.TestProdCons;

public class Consommateur extends Acteur implements _Consommateur, Runnable{
	private int idConsommateur;
	private int nbMessagesConsommes = 0;
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
				
				sleep(random_timegap);
				
				if(mssg != null) {
					tpc.observateur.consommationMessage(this, mssg, random_timegap); // to correct please !
				} else {
					break;
				}
				
				
				//String s = "---" + this.toString() + " a reçu le message: " + "\""+mssg+"\"";
				//System.out.println(s);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public String toString() {
		String s = "Consommateur numéro : " + this.idConsommateur +" ";
		return s;
	}
	
	public Observateur getObservateur() {
		return this.observateur;
	}
}
