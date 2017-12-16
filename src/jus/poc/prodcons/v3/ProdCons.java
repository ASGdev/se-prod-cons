package jus.poc.prodcons.v3;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;


public class ProdCons implements Tampon{

	private Semaphore mutex = new Semaphore(1);
	private Semaphore notFull;
	private Semaphore notEmpty = new Semaphore(0);
	private LinkedList<Message> buffer;
	private int maxSizeBuffer = 8;
	
	public ProdCons() {
		this.buffer = new LinkedList<Message>() ;
	}
	
	public ProdCons(int sizeBuffer) {
		this.buffer = new LinkedList<Message>() ;
		this.maxSizeBuffer = sizeBuffer;
		this.notFull = new Semaphore(sizeBuffer);
	}
	
	@Override
	public int enAttente() {
		return this.buffer.size();
	}

	@Override
	public Message get(_Consommateur arg0) throws Exception, InterruptedException {
		
		notEmpty.acquire();
		mutex.acquire();
		Message tmp = buffer.pop();
		System.out.println("---Buffer pop:");
		for (Message name : buffer) {
			System.out.println(name);
		}
		System.out.println("---End_Buffer:\n");
		mutex.release();
		notFull.release();
		return tmp;	
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
	
}