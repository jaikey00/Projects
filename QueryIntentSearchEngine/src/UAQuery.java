import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import objects.Document;
/********************************
Name: Jordan Aikey
Problem Set: PS3
Class: CS-4373 Information Retr.
Due Date: 7/16/2021
********************************/
public class UAQuery{
	private File inputFiles;
	private String inputDataDir;
	private int numThreads;
	
	private FileFilter filter = new FileFilter() {
      public boolean accept(File file) {
          if (file.getName().endsWith(".temp")) {
             return true;
          }
          return false;
      }
	};
	
	/***
    *
    * @param args
    * @throws IOException
    *
    * Main method to run program from command line.
    */
   public static void main(String[] args) throws IOException {
           UAQuery query = new UAQuery("inputs","outputs",6);

           for(Document d : query.runQuery(args)){
                   System.out.println("Document Name: " + d.name);
                   System.out.println("Document ID: " + d.doc_id);
                   System.out.println("Document Score: " + d.score);
                   System.out.println();
           }

   }
   
	/***
	 * 
	 * @param inputFilesDir
	 * @param inputDataDir
	 * 
	 * Initializes a new query
	 */
	public UAQuery(String inputFilesDir, String inputDataDir, int numThreads) throws IOException {
		this.inputDataDir = inputDataDir;
		inputFiles = new File(inputDataDir);
		this.numThreads = numThreads;
	}
	
	/***
	 * 
	 * @param keywords
	 * @return
	 * @throws IOException
	 * 
	 * Executes the query with the given keywords and returns a sorted document collection sorted by score.
	 */
	public ArrayList<Document> runQuery(String[] keywords) throws IOException{
		File[] input = inputFiles.listFiles(filter);
		UAQueryWorker[] workers = new UAQueryWorker[input.length];
		Hashtable<String,String> keys = new Hashtable<String,String>();
		
		for(String k : keywords) {
			keys.put(k, k);
		}
		
		Document[] docs = new Document[input.length];
		
		ExecutorService threadPool = Executors.newFixedThreadPool(numThreads);
		
		for(int i = 0; i < input.length; i++) {
			RAFHashtable raf = new RAFHashtable(inputDataDir+"/dict.raf");
			RandomAccessFile postFile = new RandomAccessFile(inputDataDir+"/post.raf","rw");
			docs[i] = new Document();
			UAQueryWorker worker = new UAQueryWorker(i, input[i], raf, postFile, keys, docs[i]);
			workers[i] = worker;
			
			threadPool.execute(worker);
		}
		
		threadPool.shutdown();
	    try {
			threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		TreeMap<Float,ArrayList<Document>> tm = new TreeMap<Float, ArrayList<Document>>();
		
		for(Document d : docs) {
			if(d != null && d.score > 0f) {
				
				if(!tm.containsKey(d.score)) {
					tm.put(d.score, new ArrayList<Document>(Arrays.asList(new Document[] {d})));
				}else {
					tm.get(d.score).add(d);
				}
			}
		}
		
		ArrayList<Document> sortedDocs = new ArrayList<Document>();
		
		while(!tm.isEmpty()) {
			for(Document d : tm.lastEntry().getValue()) {
				sortedDocs.add(d);
			}
			tm.remove(tm.lastKey());
		}

		return sortedDocs;
	}

	class UAQueryWorker implements Runnable{
		private static final int recLength = 36;
		int fileIndex;
		File inputFile;
		RAFHashtable raf;
		RandomAccessFile postFile;
		Hashtable<String,String> keys;
		
		Document doc;
		
		public UAQueryWorker(int fileIndex, File inputFile, RAFHashtable raf, RandomAccessFile postFile, Hashtable<String,String> keys, Document doc) {
			this.fileIndex = fileIndex;
			this.inputFile = inputFile;
			this.raf = raf;
			this.postFile = postFile;
			this.keys = keys;
			this.doc = doc;
		}
		
		@Override
		public void run() {
			try {
				float nominator = 0f;
				float denominatorL = 0f;
				float denominatorR = 0f;
				
				BufferedReader read = new BufferedReader(new FileReader(inputFile.getPath()));
				String docName = inputFile.getName().replace(".temp", "");
				String line;
				String postLine = "";
				while((line = read.readLine()) != null) {
					Term t = raf.get(line.substring(0,20).trim().toLowerCase());
					
					if(t != null && t.start != -1) {
						postFile.seek((long)t.start*recLength);
						
						String postDocName = "";
						int term_id = 0;
						do{
							postLine = postFile.readLine();
							if(postLine != null) {
								postDocName = postLine.substring(0,20).trim();
								term_id = Integer.parseInt(postLine.substring(20,27).trim());
							}else {
								break;
							}
	
						}while(!docName.equals(postDocName) && term_id == t.term_id);
	
						float rtfidf = 0f;
						
						if(postLine != null && term_id == t.term_id) {
							rtfidf = Float.parseFloat(postLine.substring(27,35));
						}
						
						float termVal = 0f;
						if(keys.containsKey(t.term)) {
							termVal = 1f;
						}
						
						nominator += rtfidf * termVal;
						denominatorL += Math.pow(rtfidf, 2);
						denominatorR += Math.pow(termVal, 2);
						
					}
				}
				
				float cos = (nominator)/(float)((Math.sqrt(denominatorL))*(Math.sqrt(denominatorR)));
				cos = Float.isNaN(cos) ? 0f : cos*100;
				
				doc.name = docName;
				doc.doc_id = fileIndex;
				doc.score = cos;
				
			}catch(Exception ex) {
				System.out.println("An error occurred when trying to process a document.");
				ex.printStackTrace();
				System.exit(101);
			}
		}
		
	}
}


