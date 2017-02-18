import java.util.ArrayList;

public class Encoder {
	
	public static final int BIJECTIVE_VARIANT_BWT = 0;
	public static final int DEFAULT_BWT = 1;
	
	public Encoder(){
		
	}
	
	//returns a string summarizing results.
	public String compute(String[] words, int blockSize, int wordSize,int ENCODING){
		
		String result = "None";
		
		switch(ENCODING){
			case BIJECTIVE_VARIANT_BWT:
				result = computeBijectiveVariant(words, blockSize, wordSize);
				break;
			case DEFAULT_BWT:
				System.out.println("DEFAULT_BWT has not been implemented yet.");
				break;
				
			default:
				System.out.println("Error: Encoding mode *"+ ENCODING + "* not defined.");
				break;
		}
		
		return result;
		
	}
	
	private String computeBijectiveVariant(String[] words, int blockSize, int wordSize){
		
		String[] encoded = encodeWordsBv(words, blockSize, wordSize);
		
		long positive_count = 0,negative_count = 0;
		long pos_total = 0,neg_total = 0;
		long total = 0,total_flips = 0;
		
		for(int i = 1 ; i < encoded.length ; i++){
			total++;
			int origCost = cost(words[i],words[i-1],true);
			int rotaCost = cost(encoded[i],encoded[i-1],false);
			total_flips += origCost;
			if(origCost>rotaCost){
				positive_count++;
				pos_total += (origCost-rotaCost);
			}
			else if(rotaCost>origCost){
				negative_count++;
				neg_total += (rotaCost-origCost);
			}
		}
		
		String output = "Using blocks of size "+blockSize+"\nTotal amount of string pairs with reduced bit flips = "+positive_count+
	 					"\nWith "+ pos_total*1.0/positive_count+" average blf. "+pos_total+" amount of bits less flipped in total.\n"+
	 					"Total amount of string pairs with increased bit flips = "+negative_count+
	 					"\nWith "+ neg_total*1.0/negative_count+" average bmf. "+neg_total+" amount of bits more flipped in total\n."+
	 					"Over a total amount of "+total+1+" unique word pairs with "+total_flips+" total amount of flips, "+
	 					(total-positive_count-negative_count)+" had the same cost after transformation.\n";
		
		return output;

	}
	
	private String[] encodeWordsBv(String[] words, int blockSize, int wordSize){	
		
		int blockCount = wordSize/blockSize;
		String[] encoded = new String[words.length];
		for(int k = 0 ; k < words.length ; k++){
			String textCmplete = words[k];
			String rotationCmplete = "";
			for(int i = 0 ; i < blockCount ; i++){
				String subs = textCmplete.substring(i*blockSize, (i+1)*blockSize);
				int size = subs.length();
				String[] lyndonWords = generateLyndonWords(subs);
				String[] rotations = generateRows(lyndonWords,size);
				sortStrings(rotations, size);
				rotationCmplete += lastChars(rotations);
			}
			encoded[k] = rotationCmplete;
		}
		return encoded;
		
	}
	
	private String[] generateRows(String[] lw,int size){
		
		String[] rows = new String[size];
		int r = 0;
		for(int i = 0 ; i < lw.length ; i++){
			String currLw = lw[i];
			for(int j = 0 ; j < currLw.length() ; j++){
				rows[r++] = currLw;
				currLw = rotateOnce(currLw);
			}
		}
		return rows;
		
	}
	
	private String rotateOnce(String a){
		
		char first = a.charAt(0);
		return a.substring(1) + first;
		
	}	
	private String lastChars(String[] s){
		
		String result = "";
		for(int i=0; i<s.length; i++)
			result = result + s[i].charAt(s[i].length()-1);
		return result;
	
	}

	private int cost(String b1,String b2,boolean dbiEnabled){
		
		int ret = 0;
		for(int i = 0 ; i < b1.length() ; i++){
			if(b1.charAt(i)!=b2.charAt(i))
				ret++;
		}
		if(dbiEnabled)
			return ret>(b1.length()/2) ? b1.length()-ret : ret;
		else
			return ret;
		
	}
	
	//return LyndonFactorization of a word.
	private String[] generateLyndonWords(String word){
		
		 int k = 0;
		 int len = word.length();
		 ArrayList<String> factorizations = new ArrayList<>();
		 int prevBreak = 0;
		 while (k < len){
			 int i = k + 1;
			 int j = k + 2;
			 while(true){
				 if(j == len + 1 || word.charAt(j-1) < word.charAt(i-1)){
					 while(k < i){
						 factorizations.add(word.substring(prevBreak, k+j-i));
						 prevBreak = k + j - i;
						 k = k + j - i;
					 }
					 break;
			 	 }else{
			 		 if(word.charAt(j-1) > word.charAt(i-1))
			 			 i = k + 1;
			 		 else
			 			 i = i + 1;
			 		 j = j + 1;
			 	 }
			 }
		 }
		 return factorizations.toArray(new String[]{});
		 
	}
	
	private int sortStrings(String[] a, int SIZE){
		
		int lastIndex = SIZE-1;
		for(int pass=0; pass<= SIZE-2; pass++){
			for(int i=0; i<= SIZE-pass-2; i++){
				if (a[i].compareTo(a[i+1])>0){
					if(i+1 == lastIndex)
						lastIndex = i;
					swap(a, i, i+1);
				}
			}
		}
		return lastIndex;
		
	}
	
	private void swap(String[] a, int i, int j){
		
		String t = a[i];
		a[i] = a[i+1];
		a[i+1] = t;
		
	}
	
}
