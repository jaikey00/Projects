import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map.Entry;

import objects.Document;

import java.util.TreeMap;
/********************************
Name: Jordan Aikey
Problem Set: PS3
Class: CS-4373 Information Retr.
Due Date: 7/16/2021
********************************/
public class UAInvertedIndex {
	private RAFHashtable GH;
	private int termId = 0;
	private int docId = 0;
	
	private FileFilter filter = new FileFilter() {
      public boolean accept(File file) {
          if (file.getName().endsWith(".txt")) {
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
		UAInvertedIndex ii = new UAInvertedIndex();
		ii.buildInvertedIndex(args[0],args[1]);
	}
	
	/***
	 * 
	 * @param inputDirPath
	 * @param outputDirPath
	 * @throws IOException
	 * 
	 * Builds a new inverted index from the given input path files.
	 */
	public void buildInvertedIndex(String inputDirPath, String outputDirPath) throws IOException {
		File inputDir = new File(inputDirPath);
		File outputDir = new File(outputDirPath);
		
		termId = 0;
		docId = 0;
		
		GH = new RAFHashtable(outputDir+"/dict.raf");
		
		File[] files = inputDir.listFiles(filter);
		File[] sortedFiles = new File[files.length];
		
		Hashtable<String, ArrayList<Integer>> DH = new Hashtable<String, ArrayList<Integer>>();
		Hashtable<String, int[]> DHTF = new Hashtable<String, int[]>();
		int[] totalFreqs = new int[files.length];
		int totalFreq = 0;
		
		//FIRST PASS
		for(File f : files) {
			totalFreq = 0;
			
			BufferedReader reader = new BufferedReader(new FileReader(f));
			
			String term;
			while((term = reader.readLine()) != null) {
				totalFreq++;
				
				term = term.toLowerCase();
				term = term.length() > 20 ? term.substring(0,20).trim() : term.trim();
				if(!DH.containsKey(term)) {
					DH.put(term, new ArrayList<Integer>(Arrays.asList(new Integer[]{docId})));
					DHTF.put(term, new int[files.length]);
					DHTF.get(term)[docId] += 1;
				}else { 
					DHTF.get(term)[docId] += 1;
					if(!DH.get(term).contains(docId)){
						DH.get(term).add(docId);
					}
				}
			}
			
			for(String t : DH.keySet()) {
				if(!GH.containsKey(t)) {
					GH.put(t, new Term(t,termId,1,-1));
					termId++;
				}else {
					Term temp = GH.get(t);
					temp.doc_count = DH.get(t).size();
					GH.put(t, temp);
				}
			}
			
			String[] terms = new String[DH.keySet().size()];
			DH.keySet().toArray(terms);
			
			Arrays.sort(terms);
			
			File tempFile = new File(outputDir+"/"+f.getName()+".temp");
			tempFile.createNewFile();
			
			PrintWriter writer = new PrintWriter(new FileWriter(tempFile));
			
			totalFreqs[docId] = totalFreq;
			
			for(String t : terms) {
				float RTF = rtf(DHTF.get(t)[docId],totalFreqs[docId]);
				t = t.length() > 20 ? t.substring(0,20) : t;
				writer.printf("%-20s%-7d%-7f\n",t,docId,RTF);
			}
			writer.close();
			
			sortedFiles[docId] = tempFile;
			
			docId++; 
		}
		
		//SECOND PASS
		int recordCount = 0;
		
		RandomAccessFile post = new RandomAccessFile(outputDirPath+"/post.raf","rw");
		TreeMap<String, ArrayList<Integer>> sortedBuffer = new TreeMap<String, ArrayList<Integer>>();
		BufferedReader[] readers = new BufferedReader[files.length];
		
		for(int i = 0; i < files.length; i++) {
			readers[i] = new BufferedReader(new FileReader(files[i]));
			
			String line = readers[i].readLine();
			String word = line.length() > 20 ? line.substring(0,20).trim() : line.trim();
			
			if(!sortedBuffer.containsKey(word)) {
				sortedBuffer.put(word.toLowerCase(), new ArrayList<Integer>(Arrays.asList(new Integer[]{i})));
			}else {
				sortedBuffer.get(word.toLowerCase()).add(i);
			}
		}
		
		while(!sortedBuffer.isEmpty()) {
			Entry<String, ArrayList<Integer>> entry = sortedBuffer.firstEntry();
			String token = entry.getKey();
			
			Term temp = GH.get(token);

			if(temp != null && temp.start == -1) {
				temp.start = recordCount;
				GH.put(token, temp);
				
				ArrayList<Integer> indices = DH.get(token);
				
				for(int i = 0; i < indices.size(); i++) {
					int index = indices.get(i);
					
					float freq = rtfidf(rtf(DHTF.get(token)[index],totalFreqs[index]),files.length,temp.doc_count);
					
					post.writeBytes(String.format("%-20s%-7d%-7f\n", files[index].getName(),temp.term_id, freq)); 
					
					String line = readers[index].readLine();
					if(line != null) {
						String word = line.length() > 20 ? line.substring(0,20).trim() : line.trim();
						if(!sortedBuffer.containsKey(word)) {
							sortedBuffer.put(word.toLowerCase(), new ArrayList<Integer>(Arrays.asList(new Integer[]{index})));
						}else {
							sortedBuffer.get(word.toLowerCase()).add(index);
						}
					}
					recordCount++;
				}
			}
			sortedBuffer.remove(token);
		}
	} 
	
	/***
	 * 
	 * @param freq
	 * @param totalFreq
	 * @return
	 * 
	 * Returns the relative term frequency from the given term frequency and total frequency
	 */
	public float rtf(int freq, int totalFreq) {
		return (float)freq/(float)totalFreq;
	}
	
	/***
	 * 
	 * @param rtf
	 * @param doc_count
	 * @param doc_freq
	 * @return
	 * 
	 * Returns the relative term frequency - inverse document frequency for the given parameters.
	 */
	public float rtfidf(float rtf, int doc_count, int doc_freq) {
		return (float) (rtf * Math.log(1+(doc_count/doc_freq)));
	}
	
}
