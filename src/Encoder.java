import java.util.ArrayList;

public class Encoder {
	
	private static final int BIJECTIVE_VARIANT_BWT = 0;
	private static final int DEFAULT_BWT = 1;
	
	public Encoder(){
		
	}
	
	//returns a string summarizing results.
	public String compute(String[] words, int blockSize, int wordSize,int ENCODING){
		
		switch(ENCODING){
			case BIJECTIVE_VARIANT_BWT:
				
				break;
			case DEFAULT_BWT:
				
				break;
				
			default:
				System.out.println("Error: Encoding mode *"+ ENCODING + "* not defined.");
				break;
		}
		return null;
	}
	
	private String computeBijectiveVariant(){
		String[] rota
		return null;
	}
	
	private String[] encodeAllWords(String[] words, int ENCODING){
		
		return null;
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
	
}
