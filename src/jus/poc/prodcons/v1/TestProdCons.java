package jus.poc.prodcons.v1;

import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Simulateur;
import jus.poc.*;


public class TestProdCons extends Simulateur{

	private int nbConsProd = 2;
	private Consommateur tabCons[] = new Consommateur[nbConsProd];
	private Producteur tabProd[] = new Producteur[nbConsProd];
	private ProdCons pc = new ProdCons(10);
	
	public TestProdCons(Observateur observateur){
		super(observateur);
		for(int i = 0 ; i < nbConsProd; i++) {
			try {
				tabCons[i]= new Consommateur(2, observateur, 5, 2, pc, i);
				tabProd[i]= new Producteur(1, observateur, 5, 2, pc, i);
			} catch (ControlException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			this.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void run() throws Exception{
		for(int i = 0 ; i < nbConsProd; i++) {
			new Thread(tabCons[i]).start();
			new Thread(tabProd[i]).start();
		}
	}
	
	public static void main(String[] args){
		new TestProdCons(new Observateur()).start();
	}
}
