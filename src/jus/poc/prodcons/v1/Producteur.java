package jus.poc.prodcons.v1;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Producteur;

public class Producteur extends Acteur implements _Producteur, Runnable {
	private int idProducteur;
	private int nbMessage = 4;
	private ProdCons pc;
	
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
		for (int i = nbMessage; i > 0; i--) {
			try {
				mssg = new MessageX(this.toString() + " | Message numero :" + i);
				pc.put(this, mssg);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		tpc.decreaseListProd();
		System.out.println(this.toString() + " finished producing -> tpc.sizeoflist : " + tpc.getListProdSize());
	}

	public String toString() {
		String s = "Producteur numero : " + this.idProducteur;
		return s;
	}
}