import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
/********************************
Name: Jordan Aikey
Problem Set: PS3
Class: CS-4373 Information Retr.
Due Date: 7/16/2021
********************************/
public class RAFHashtable {
	static final int MAX_TERM_LENGTH = 20;
	static final int MAX_NUM_LENGTH = 7;
	static final int RECORD_LENGTH = MAX_TERM_LENGTH+(MAX_NUM_LENGTH*3)+3;
	
	static final int TERM_INDEX = 0;
	static final int TERM_ID_INDEX = MAX_TERM_LENGTH;
	static final int DOC_COUNT_INDEX = MAX_TERM_LENGTH+MAX_NUM_LENGTH;
	static final int START_INDEX = MAX_TERM_LENGTH+(MAX_NUM_LENGTH*2);
	
	private String rafHashPath;
	private int size = 0;
	private long fileLength = 1000000;
	private float loadFactor = 0.0f;
	
	public RAFHashtable(String hashFile) throws IOException{
		this.rafHashPath = hashFile;
		
		RandomAccessFile rafHash = getRafInstance();
		
		if(fileLength * RECORD_LENGTH > rafHash.length()) {
			for(int i = 0; i < fileLength; i++) {
				String format = "%-"+MAX_TERM_LENGTH+"s%-"+MAX_NUM_LENGTH+"d%-"+MAX_NUM_LENGTH+"d%-"+MAX_NUM_LENGTH+"d\n";
				rafHash.writeUTF(String.format(format,"",-1,-1,-1));
			}
		}
		rafHash.close();
	}
	
	public void put(String key, Term value) throws IOException {
		key = key.length() > MAX_TERM_LENGTH ? key.substring(0,MAX_TERM_LENGTH) : key;
		long index = getHashedIndex(key);
		
		RandomAccessFile rafHash = getRafInstance();
		rafHash.seek(index);
		
		String currentTerm = rafHash.readUTF().substring(TERM_INDEX,MAX_TERM_LENGTH).trim();
		
		int count = 1;
		while(currentTerm.length() > 0 && !currentTerm.equals(key)) {
			index += RECORD_LENGTH;
			rafHash.seek(index);
			currentTerm = rafHash.readUTF().substring(TERM_INDEX,MAX_TERM_LENGTH).trim();
		}
		
		String format = "%-"+MAX_TERM_LENGTH+"s%-"+MAX_NUM_LENGTH+"d%-"+MAX_NUM_LENGTH+"d%-"+MAX_NUM_LENGTH+"d\n";
		String trimmedKey = key.length() > MAX_TERM_LENGTH ? key.substring(0,MAX_TERM_LENGTH) : key;
		rafHash.seek(index);
		rafHash.writeUTF(String.format(format,trimmedKey,value.term_id,value.doc_count,value.start));
		rafHash.close();
		size++;

	}
	
	public Term get(String key) throws IOException {
		key = key.length() > MAX_TERM_LENGTH ? key.substring(0,MAX_TERM_LENGTH) : key;
		long index = getHashedIndex(key);
		
		RandomAccessFile rafHash = getRafInstance();
		
		rafHash.seek(index);
		try {
			String line = rafHash.readUTF();
			String term = line.substring(TERM_INDEX,MAX_TERM_LENGTH).trim();
			
			long baseIndex = index;
			int count = 1;
			while(!key.equals(term) && !term.isEmpty()) {
				rafHash.seek(baseIndex+(RECORD_LENGTH*count));
				line = rafHash.readUTF();
				term = line.substring(TERM_INDEX,MAX_TERM_LENGTH).trim();
				count++;
			}
			
			rafHash.close();
			
			if(term.isEmpty()) {
				return null;
			}else {
				int term_id = Integer.parseInt(line.substring(TERM_ID_INDEX,DOC_COUNT_INDEX).trim());
				int doc_count = Integer.parseInt(line.substring(DOC_COUNT_INDEX,START_INDEX).trim());
				int start = Integer.parseInt(line.substring(START_INDEX,RECORD_LENGTH-2).trim());
				
				return new Term(term,term_id,doc_count,start);
			}
		}catch(Exception ex) {
			rafHash.close();
			return null;
		}
	}
	
	public Boolean remove(String key) throws IOException {
		if(get(key) != null) {
			key = key.length() > MAX_TERM_LENGTH ? key.substring(0,MAX_TERM_LENGTH) : key;
			long index = getHashedIndex(key);
			
			RandomAccessFile rafHash = getRafInstance();
			String format = "%-"+MAX_TERM_LENGTH+"s%-"+MAX_NUM_LENGTH+"d%-"+MAX_NUM_LENGTH+"d%-"+MAX_NUM_LENGTH+"d\n";
			rafHash.writeUTF(String.format(format,"",-1,-1,-1));
			rafHash.close();
			
			return true;
		}else {
			return false;
		}
	}
	
	public Boolean containsKey(String key) throws IOException {
		Term temp = get(key);
		return get(key) != null;
	}
	
	private long getHashedIndex(String key) {
		long hash = key.hashCode() < 0 ? key.hashCode()*-1 : key.hashCode();
		long index = hash%fileLength;
		
		return index*RECORD_LENGTH;
	}
	
	
	private RandomAccessFile getRafInstance() throws FileNotFoundException {
		return new RandomAccessFile(rafHashPath, "rw");
	}
}
