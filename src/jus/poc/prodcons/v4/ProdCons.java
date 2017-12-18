package jus.poc.prodcons.v4;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;
import jus.poc.prodcons.v4.*;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class ProdCons implements Tampon {

	private Semaphore mutex = new Semaphore(1);
	private Semaphore notFull;
	private Semaphore notEmpty = new Semaphore(0);
	private LinkedList<Message> buffer;
	private int maxSizeBuffer = 8;
	private TestProdCons tpc;

	public ProdCons() {
		this.buffer = new LinkedList<Message>();
		this.notFull = new Semaphore(maxSizeBuffer);
	}

	public ProdCons(int sizeBuffer) {
		this.buffer = new LinkedList<Message>();
		this.maxSizeBuffer = sizeBuffer;
		this.notFull = new Semaphore(sizeBuffer);
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

		notEmpty.acquire();
		mutex.acquire();

		// check if buffer is empty, to shut down consumer
		if (buffer.isEmpty() && this.tpc.getSizeList() == 0) {
			if (this.tpc.getSizeList() == 0) {
				mutex.release();
				notFull.release();
				notEmpty.release();
			}
			return null;
		} else {
			if (buffer.isEmpty()) {
				mutex.release();
				notFull.release();
				return ((Consommateur) arg0).getLastMessage();
			} else {
				MessageX lastmssg = ((Consommateur) arg0).getLastMessage();
				MessageX check = (MessageX) buffer.peek();

				if (lastmssg.toString() == check.toString()) {// Message deja lu -> ne doit pas ê enlevé
					System.out.println("---Buffer peek:");
					for (Message name : buffer) {
						System.out.println(name);
					}
					System.out.println("---End_Buffer:\n");
					mutex.release();
					notEmpty.release();
					return check;
				} else { // Unread message
					check.readedOnce();
					if (check.jobsDone()) { // message a été lu par tout les lecteurs voulu
						Message popped = buffer.pop();
						System.out.println("---Buffer pop:");
						for (Message name : buffer) {
							System.out.println(name);
						}
						System.out.println("---End_Buffer:\n");

						if (buffer.isEmpty() && this.tpc.getSizeList() == 0) {
							if (this.tpc.getSizeList() == 0) {								
								mutex.release();
								notEmpty.release();
								return null;
							}
						}
						if (buffer.isEmpty()) {
							mutex.release();
							notFull.release();
							return popped;
						} else {
							mutex.release();
							notEmpty.release();
							return popped;
						}

					} else {// Le message doit encore être lu par au moins un lecteur
						mutex.release();
						notEmpty.release();
						return check;
					}
				}

			}
		}
	}

	@Override
	public void put(_Producteur arg0, Message arg1) throws Exception, InterruptedException {

		notFull.acquire();
		mutex.acquire();
		buffer.add(arg1);
		System.out.println("---Buffer add:");
		for (Message name : buffer) {
			System.out.println(name);
		}
		System.out.println("---End_Buffer:\n");
		mutex.release();
		notEmpty.release();

	}

	@Override
	public int taille() {
		return maxSizeBuffer;
	}
	
	public TestProdCons getTPC() {
		return this.tpc;
	}

}
