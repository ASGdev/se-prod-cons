package jus.poc.prodcons.loris;

import java.util.LinkedList;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class ProdCons implements Tampon {

	private LinkedList<Message> buffer;
	private TestProdCons tpc;
	private int maxSizeBuffer = 8;

	public ProdCons() {
		this.buffer = new LinkedList<Message>();
	}

	public ProdCons(int maxSizeBuffer) {
		this.buffer = new LinkedList<Message>();
		this.maxSizeBuffer = maxSizeBuffer;
	}

	public void setTestProdCons(TestProdCons tpc) {
		this.tpc = tpc;
	}
	@Override
	public int enAttente() {
		return this.buffer.size();
	}

	@Override
	public Message get(_Consommateur arg0) throws Exception, InterruptedException {
		synchronized (this) {
			while (buffer.isEmpty()) {
				//System.out.println(arg0.toString() + " wait");
				if(this.tpc.getListProdSize() == 0) return null;
				this.wait();
			}
			Message tmp = buffer.pop();
			System.out.println("---Buffer pop:");
			for (Message name : buffer) {
				System.out.println(name);
			}
			System.out.println("---End_Buffer:\n");
			this.notifyAll();
			return tmp;
		}
	}

	@Override
	public void put(_Producteur arg0, Message arg1) throws Exception, InterruptedException {
		synchronized (this) {
			while (buffer.size() == maxSizeBuffer) {
				// System.out.println(arg0.toString() + " wait");
				this.wait();
			}
			buffer.add(arg1);
			System.out.println("---Buffer add:");
			for (Message name : buffer) {
				System.out.println(name);
			}
			System.out.println("---End_Buffer:\n");
			this.notifyAll();
		}
	}

	@Override
	public int taille() {
		return maxSizeBuffer;
	}

}
