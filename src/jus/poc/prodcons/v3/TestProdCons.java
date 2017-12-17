package jus.poc.prodcons.v3;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Properties;

import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Simulateur;
import jus.poc.prodcons.v3.*;



public class TestProdCons extends Simulateur{

	// log
		ArrayList<String> log = new ArrayList<String>();
		int nbProd = 0;
		int nbCons = 0;
		int nbBuffer = 1;
		int deviationTempsMoyenProduction;
		int tempsMoyenConsommation;
		int deviationTempsMoyenConsommation;
		int tempsMoyenProduction;
		int nombreMoyenDeProduction;
		int deviationNombreMoyenDeProduction;
		int nombreMoyenNbExemplaire;
		int deviationNombreMoyenNbExemplaire;
		private ProdCons pc = new ProdCons();

		private ArrayList<Producteur> list_prod= new ArrayList<Producteur>(); //used to close conso threads
		private Producteur[] producteurs_holder;
		private Consommateur[] consommateurs_holder;

		public TestProdCons(Observateur observateur) {
			super(observateur);

			try {
				this.init();
				
				// init observer
				try {
					observateur.init(nbProd, nbCons, nbBuffer) ;
				} catch (ControlException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				System.out.println("===== New TestProd v2 =====");
				System.out.println("# producteurs : " + nbProd);
				System.out.println("# consommateurs : " + nbCons);

				producteurs_holder = new Producteur[nbProd];
				consommateurs_holder = new Consommateur[nbCons];

				for (int i = 0; i < nbCons; i++) {
					try {
						consommateurs_holder[i] = new Consommateur(2, observateur, tempsMoyenConsommation, deviationTempsMoyenConsommation, pc, i);
						
						observateur.newConsommateur(consommateurs_holder[i]);
						
						consommateurs_holder[i].setTestProdCons(this);
					} catch (ControlException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				for (int i = 0; i < nbProd; i++) {
					try {
						producteurs_holder[i] = new Producteur(1, observateur, tempsMoyenProduction,
								deviationTempsMoyenProduction, pc, i);
						
						// update observer
						observateur.newProducteur(producteurs_holder[i]);
						
						producteurs_holder[i].setTestProdCons(this);

					} catch (ControlException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException
					| ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		protected void run() throws Exception {
			for (int i = 0; i < nbCons; i++) {
				new Thread(consommateurs_holder[i]).start();
			}
			for (int i = 0; i < nbProd; i++) {
				new Thread(producteurs_holder[i]).start();
			}
		}

		public static void main(String[] args)
				throws ControlException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException,
				SecurityException, InvalidPropertiesFormatException, IOException, ClassNotFoundException {
			new TestProdCons(new Observateur()).start();

		}

		/**
		 * * Retreave the parameters of the application. * @param file the final name of
		 * the file containing the options.
		 * 
		 * @throws SecurityException
		 * @throws NoSuchFieldException
		 * @throws IllegalAccessException
		 * @throws IllegalArgumentException
		 * @throws IOException
		 * @throws InvalidPropertiesFormatException
		 * @throws ClassNotFoundException
		 */
		protected void init() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException,
				SecurityException, InvalidPropertiesFormatException, IOException, ClassNotFoundException {
			Properties properties = new Properties();
			InputStream optionsfile = ClassLoader.getSystemResourceAsStream("jus/poc/prodcons/options/options.xml");
			properties.loadFromXML(optionsfile);
			String key;
			int value;
			Class<?> thisOne = getClass();
			for (Map.Entry<Object, Object> entry : properties.entrySet()) {
				key = (String) entry.getKey();
				value = Integer.parseInt((String) entry.getValue());
				thisOne.getDeclaredField(key).set(this, value);
			}
		}
		
		/*Used to remove the prod from the list*/
		public void removeProdList(Producteur prod) {
			list_prod.remove(prod);
		}
		
		/*Used by the Consommateur to close themself*/
		public int getSizeList() {
			return list_prod.size();
		}
}
