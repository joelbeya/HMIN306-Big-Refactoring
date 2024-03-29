package extraction;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

public class extractionInterface2019 {

		
		private ArrayList<String> CharacteristicList;
		private ArrayList<String> EntityList;
		private boolean[][] table;
		
		public void createTable(int i) throws SecurityException, ClassNotFoundException{
			// Characteristic list and entity list are assigned
			if (CharacteristicList==null || EntityList==null) return;
			
			table = new boolean[EntityList.size()][CharacteristicList.size()];
			
			for (String e:EntityList)
				for (String c:CharacteristicList){
					switch(i){
						case 0:
							if (methodNames(e).contains(c))
								table[EntityList.indexOf(e)][CharacteristicList.indexOf(c)] = true;
							break;
						case 1:
							if (finalStaticFieldNamesTypes(e).contains(c))
								table[EntityList.indexOf(e)][CharacteristicList.indexOf(c)] = true;
							break;
						case 2:
							if (signatures(e).contains(c))
								table[EntityList.indexOf(e)][CharacteristicList.indexOf(c)] = true;
							break;
						case 3:
							if (implementNames(e).contains(c)){
								table[EntityList.indexOf(e)][CharacteristicList.indexOf(c)] = true;
							}
					}
				}
		}
		
		public void afficheTable(){
			if (table == null) return;
			System.out.print("FormalContext Collections"+"\n"+"| |");
			for (int k=0; k< table[0].length; k++){
				System.out.print(CharacteristicList.get(k)+"|");
			}
			System.out.println();
			for (int i=0; i< table.length; i++){
				System.out.print("|"+EntityList.get(i)+"|");
				for (int j=0; j< table[0].length; j++){
					if (table[i][j])
						System.out.print("x");
					System.out.print("|");
				}
				System.out.println();
			}
		}
		
		public void ecrireTable(String nomFichier) throws IOException{
			if (table == null) return;
			BufferedReader fc = new BufferedReader
			        (new InputStreamReader (System.in));
			BufferedWriter ff = new BufferedWriter
			              (new FileWriter (nomFichier));

			ff.write("FormalContext Collections"+"\n"+"| |");
			for (int k=0; k< table[0].length; k++){
				ff.write(CharacteristicList.get(k)+"|");
			}
			ff.newLine();;
			for (int i=0; i< table.length; i++){
				ff.write("|"+EntityList.get(i)+"|");
				for (int j=0; j< table[0].length; j++){
					if (table[i][j])
						ff.write("x");
					ff.write("|");
				}
				ff.newLine();;
			}
			ff.close(); 
		}
		
		public ArrayList<String> getCharacteristicList() {
			return CharacteristicList;
		}

		public void setCharacteristicList(ArrayList<String> characteristicList) {
			CharacteristicList = characteristicList;
		}

		public ArrayList<String> getEntityList() {
			return EntityList;
		}

		public void setEntityList(ArrayList<String> entityList) {
			EntityList = entityList;
		}

		public boolean[][] getTable() {
			return table;
		}

		public void setTable(boolean[][] table) {
			this.table = table;
		}
		
		public static List<Field> getAllFields(List<Field> fields, String className) throws ClassNotFoundException {
		    fields.addAll(Arrays.asList(Class.forName(className).getDeclaredFields()));

		    if (Class.forName(className).getSuperclass() != null) {
		        fields = getAllFields(fields, Class.forName(className).getSuperclass().getName());
		    }

		    return fields;
		}
		
		public static boolean isCollectionOrMap(String className) throws ClassNotFoundException {
			
			if(Class.forName(className).isAssignableFrom(Collection.class) || Class.forName(className).isAssignableFrom(Map.class)){			
				return true;
			}
			
			if(Class.forName(className).getInterfaces().length != 0){
				
				 if(isCollectionOrMap(Class.forName(className).getInterfaces()[0].getName())){
					 return true;
				 }
			}
			
			return false;
		}

		public static ArrayList<String> finalStaticFieldNamesTypes(String className) throws SecurityException, ClassNotFoundException
		{	
			ArrayList<String> liste = new ArrayList<>();
			//Object objectInstance = new Object();
				
			for (Field a : getAllFields(new LinkedList<Field>(), className))
			if (Modifier.isFinal(a.getModifiers()) && Modifier.isStatic(a.getModifiers())){		
				/*
				if(!a.isAccessible()){	
					a.setAccessible(true);
				}
				s+="\t" + a.getName()+" "+a.getType().getName()+" = "+a.get(objectInstance)+"\n";
				*/
				liste.add(a.getName()+" "+a.getType().getName());
			}
			
			return liste;
		}
		
		public static void createFile(String filename, String s) throws IOException{
			PrintWriter writer = new PrintWriter(filename, "UTF-8");
			writer.println(s);
			writer.close();
		}
		
		public static void createFileFromList(String filename, ArrayList<String> l) throws FileNotFoundException, UnsupportedEncodingException{
			PrintWriter writer = new PrintWriter(filename, "UTF-8");
			for(String s : l){
				writer.println(s);
			}
			
			writer.close();
		}
		
		public static void createFileAppend(String filename, String s) throws IOException{
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
			
			writer.println(s);
			writer.close();
		}
		
		public static ArrayList<String> methodNames(String className)throws SecurityException, ClassNotFoundException
		{
			ArrayList<String> liste = new ArrayList<>();;
			for (Method m : Class.forName(className).getMethods())
				liste.add(m.getName());
			return liste;
		}
		
		public static ArrayList<String> implementNames(String className)throws SecurityException, ClassNotFoundException
		{
			ArrayList<String> liste = new ArrayList<>();
			
			Class<?> current = Class.forName(className);
			
			while(current != null){
				for(Class<?> i : current.getInterfaces()){
					if(!isCollectionOrMap(i.getName())){
						if(!liste.contains(i.getName())){
							liste.add(i.getName());
						}
					}
				}
				current = current.getSuperclass();
				
			}
			
			return liste;
		}
		
		public static ArrayList<String> implementNamesSet(ArrayList<String> classNameList)throws SecurityException, ClassNotFoundException
		{
			ArrayList<String> liste = new ArrayList<>();
			
			for(String c : classNameList){	
				Class<?> current = Class.forName(c);
				
				while(current != null){
					for(Class<?> i : current.getInterfaces()){
						if(!isCollectionOrMap(i.getName())){
							if(!liste.contains(i.getName())){
								liste.add(i.getName());
							}
						}
					}
					current = current.getSuperclass();
				}
			}
			
			return liste;
		}
		
		public static ArrayList<String> attributeNamesSet(ArrayList<String> classNameList)throws SecurityException, ClassNotFoundException
		{
			ArrayList<String> liste = new ArrayList<>();
			
			for(String c : classNameList){		
				for (Field f : getAllFields(new LinkedList<Field>(), c)){
					if (Modifier.isFinal(f.getModifiers()) && Modifier.isStatic(f.getModifiers())){
						if (!liste.contains(f.getName()+" "+f.getType().getName())){
							liste.add(f.getName()+" "+f.getType().getName());
						}
					}
				}
			}
			
			return liste;
		}
		
		public static ArrayList<String> methodNameSet(ArrayList<String> classNameList)throws SecurityException, ClassNotFoundException
		{
			ArrayList<String> liste = new ArrayList<>();
			for (String c: classNameList)
				for (Method m : Class.forName(c).getMethods())
					if (! liste.contains(m.getName())) liste.add(m.getName());
			return liste;
		}	
		
		// extrait toutes les signatures de toutes les classes de la liste passée en parametre
		public static ArrayList<String> signatureSet(ArrayList<String> classNameList)throws SecurityException, ClassNotFoundException
		{
			ArrayList<String> liste = new ArrayList<>();
			for (String c: classNameList)
				for (String signature : signatures(c))
					if (! liste.contains(signature)) liste.add(signature);
			liste.removeAll(signatures("java.lang.Object"));
			return liste;
		}	
		
		public static ArrayList<String> signatures(String className)throws SecurityException, ClassNotFoundException
		{
			ArrayList<String> liste = new ArrayList<>();
			//for (String c: classNameList)
			for (Method m : Class.forName(className).getMethods())
			{
				String result ="";
				result += m.getReturnType() + " ";
				result += m.getName() + "(";
				for (Class paramClass : m.getParameterTypes())
					result += paramClass.getName() + ", ";
				if(m.getParameterTypes().length != 0)
					result = result.substring(0, result.length()-2);
				result += ") ";
				/*
				for (Class exceptionType : m.getExceptionTypes())
					result += exceptionType.getName() + " ";
				if(m.getExceptionTypes().length != 0)
					result = result.substring(0, result.length()-1);
					*/
					
				liste.add(result);
			}
			return liste;
		}
		
		
		public static String methodNamesParamTypes(String className)throws SecurityException, ClassNotFoundException
		{
			String s="";
			System.out.println(Class.forName(className).getMethods().length);
			for (Method m : Class.forName(className).getMethods())
				{
					s+=m.getName()+"(";//+""+a.getType().getName()+"\n";
					for (Class paramType : m.getParameterTypes())
						s+=paramType.getName()+",";
					s+=")\n";
				}
			return s;
		}	
		
		public static String interfaceListNames(String[] classNameList)throws SecurityException, ClassNotFoundException, FileNotFoundException, UnsupportedEncodingException
		{			
			String s="";
			for (String c : classNameList)
				if (Class.forName(c).isInterface()){
					s+=c+"\n";
				}
				
			return s;
		}
		
		private static ArrayList<String> interfaceList(String[] nameList) throws ClassNotFoundException {
			ArrayList<String> liste = new ArrayList<>();
			for (String c : nameList)
				if (Class.forName(c).isInterface())
					{liste.add(c);}
			return liste;
		}
		
		public static String abstractClassListNames(String[] classNameList)throws SecurityException, ClassNotFoundException, FileNotFoundException, UnsupportedEncodingException
		{	
			String s="";
			for (String c : classNameList)
				if (! Class.forName(c).isInterface() && Modifier.isAbstract(Class.forName(c).getModifiers())){
					s+=c+"\n";
				}
			
			return s;
		}
		
		public static ArrayList<String> abstractClassList(String[] classNameList)throws SecurityException, ClassNotFoundException, FileNotFoundException, UnsupportedEncodingException
		{	
			ArrayList<String> liste = new ArrayList<>();
			for (String c : classNameList)
				if (! Class.forName(c).isInterface() && Modifier.isAbstract(Class.forName(c).getModifiers()))
				{liste.add(c);}
			return liste;
		}
		
		/*private static String concreteClassListNames(String[] classNameList) throws ClassNotFoundException, UnsupportedEncodingException {
			
			String s=""; int nb =0;
			for (String c : classNameList)
				if (! Modifier.isAbstract(Class.forName(c).getModifiers())){
					s+=c+"\n";nb++;
				}
			
			System.out.println(nb+" classes concrètes");
			return s;
		}*/
		
		private static ArrayList<String> concreteClassList(String[] classNameList) throws ClassNotFoundException, IOException {
			
			ArrayList<String> liste = new ArrayList<>();
			for (String c : classNameList)
				if (! Modifier.isAbstract(Class.forName(c).getModifiers())){
					liste.add(c);
				}
			
			return liste;
		}
		
		private static ArrayList<String> concreteAbstractClassList(String[] classNameList) throws ClassNotFoundException, IOException {
			
			ArrayList<String> liste = new ArrayList<>();
			for (String c : classNameList)
				if (!Class.forName(c).isInterface()){
					liste.add(c);
				}
			
			return liste;
		}

		public static void ajoutPrefixe(String prefixe, String[] noms){
			for (int i=0; i < noms.length; i++)
				if (noms[i].equals("Object"))
					noms[i]= "java.lang."+noms[i];
				else
				noms[i]= prefixe+noms[i];			
		}
		
		public static void main(String[] args) throws SecurityException, ClassNotFoundException, IOException, IllegalArgumentException, IllegalAccessException {
			

			// on se concentre sur quelques classes
			// les deux premières viennent de l'API Guava
			// qui a été au préalable ajoutée dans les librairies du projet sous eclipse
			// par le java build path
			// les autres sont des classes de l'API Java
			String[] listeDesClassesInterfaces = {
					"com.google.common.collect.AbstractMultiset",
					"com.google.common.collect.ConcurrentHashMultiset",
					"com.google.common.collect.EnumMultiset",
					"com.google.common.collect.ForwardingMultiset",
					"com.google.common.collect.ForwardingSortedMultiset",
					"com.google.common.collect.HashMultiset",
					"com.google.common.collect.ImmutableMultiset",
					"com.google.common.collect.ImmutableSortedMultiset",
					"com.google.common.collect.LinkedHashMultiset",
					"com.google.common.collect.TreeMultiset"
					};
		
			//ajoutPrefixe("java.util.",listeDesClassesInterfaces);
			
			ArrayList<String> listeComplete = new ArrayList<>();
			listeComplete.addAll(Arrays.asList(listeDesClassesInterfaces));
					
			/* Create File containing the names of concrete classes*/
			// le répertoire resultats doit exister dans votre projet
			//DataExtractionClassSet dFile = new DataExtractionClassSet();
			extractionInterface2019 dFile = new extractionInterface2019();
			dFile.setEntityList(concreteClassList(listeDesClassesInterfaces));
			createFileFromList("resultats/concrete.txt", dFile.getEntityList());
			
			
			/* Create table for concrete classes signatures*/
			// cette table peut aider à comprendre quelle classe contient quelle signature de methode
			// le fichier csv peut etre ouvert dans open office (separateur '|') pour manipuler les colonnes
			// et trouver des groupes de signatures communes à des groupes de classes concretes
			
			ArrayList<String> classesConcretesAbstraites = new ArrayList<>();
			classesConcretesAbstraites.addAll(concreteAbstractClassList(listeDesClassesInterfaces));
			
			extractionInterface2019 d4 = new extractionInterface2019();
			d4.setEntityList(classesConcretesAbstraites);
			d4.setCharacteristicList(signatureSet(d4.getEntityList()));
			d4.createTable(2);
			d4.afficheTable();
			d4.ecrireTable("resultats/formatConc.rcft");
			d4.ecrireTable("resultats/formatConc.csv");}
	}

