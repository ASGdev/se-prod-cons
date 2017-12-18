package jus.poc.prodcons.v2;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Producteur;
import jus.poc.prodcons.v2.*;

public class Producteur extends Acteur implements _Producteur, Runnable{
	private int idProducteur;
	private int nbMessage;
	private ProdCons pc;
	private Aleatoire random_generator;
	private int random_timegap; 
	private TestProdCons tpc;


	protected Producteur(int type, Observateur observateur, int moyenneTempsDeTraitement,
			int deviationTempsDeTraitement) throws ControlException {
		super(Acteur.typeProducteur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
	}
	
	protected Producteur(int type, Observateur observateur, int moyenneTempsDeTraitement,
			int deviationTempsDeTraitement, ProdCons pc, int id) throws ControlException {
		super(Acteur.typeProducteur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
		this.setProdCons(pc);
		this.idProducteur = id;
		this.random_generator = new Aleatoire(moyenneTempsDeTraitement, deviationTempsDeTraitement);
		this.nbMessage = new Aleatoire(pc.getTPC().nombreMoyenDeProduction, pc.getTPC().deviationNombreMoyenDeProduction).next();
	}
	
	@Override
	public int nombreDeMessages() {
		return nbMessage;
	}
	
	public void setProdCons(ProdCons pc) {
		this.pc = pc;
	}
	
	public void setTestProdCons(TestProdCons tpc) {
		this.tpc = tpc;
	}

	public void run() {
		MessageX mssg;
		for(int i = nbMessage ; i > 0; i--) {
			random_timegap = random_generator.next() * 100;
			try {
				mssg = new MessageX(this.toString() +" | Message numero :" + i);
				
				// wait for randomly-generated message production timespan
				sleep(random_timegap);
				
				pc.put(this,mssg);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		tpc.decreaseProdList();
		System.out.println(this.toString() + " finished producing ");
	}
	public String toString() {
		String s = "Producteur numero : " + this.idProducteur;
		return s;
	}
}
