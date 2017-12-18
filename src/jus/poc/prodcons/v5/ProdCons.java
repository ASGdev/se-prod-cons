package jus.poc.prodcons.v5;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;
import jus.poc.prodcons.v5.TestProdCons;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class ProdCons implements Tampon{

	final Lock pclock = new ReentrantLock();
	final Condition notFull  = pclock.newCondition(); 
	final Condition notEmpty = pclock.newCondition(); 
	
	private LinkedList<Message> buffer;
	private int maxSizeBuffer = 8;
	private TestProdCons tpc;
	
	public ProdCons() {
		this.buffer = new LinkedList<Message>();
	}
	
	public ProdCons(int sizeBuffer) {
		this.buffer = new LinkedList<Message>() ;
		this.maxSizeBuffer = sizeBuffer;
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
		
		pclock.lock();
		Message tmp = null;
		
		try {
			// check if buffer is empty : to lock condition and to shut down consumer
			while(buffer.isEmpty()) {
				if(this.tpc.getSizeList() == 0) {
					// this is the only way found (but not correct) to terminate
					Thread.currentThread().stop();
				}
				notEmpty.await();
			}
		
			tmp = buffer.pop();
			
			// signals producer threads
			notFull.signalAll();
			
			System.out.println("---Buffer pop:");
			for (Message name : buffer) {
				System.out.println(name);
			}
			
			System.out.println("---End_Buffer:\n");
		} finally {
			pclock.unlock();
		}
		return tmp;	
	}

	@Override
	public void put(_Producteur arg0, Message arg1) throws Exception, InterruptedException {
		
		pclock.lock();
		
		try {
			// buffer is full condition
			while(buffer.size() == maxSizeBuffer) {
				notFull.await();
			}
			//
			buffer.add(arg1);
			
			// signals consumer thread (at least one element on buffer)
			notEmpty.signalAll();
			
			System.out.println("---Buffer add:");
			for (Message name : buffer) {
				System.out.println(name);
			}
			System.out.println("---End_Buffer:\n");
		} finally {
			pclock.unlock();
		}
		

	
	}

	@Override
	public int taille() {
		return maxSizeBuffer;
	}
	
	public TestProdCons getTPC() {
		return this.tpc;
	}
	
}
