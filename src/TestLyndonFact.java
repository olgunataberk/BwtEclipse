import java.util.ArrayList;

public class TestLyndonFact {

	public static void main(String[] args){
		String[] arr = lyndonFactorize("mehmettan");
		for(int i = 0 ; i < arr.length ; i++){
			System.out.println(arr[i]);
		}
	}
	
	 //https://en.wikipedia.org/wiki/Lyndon_word
	 //incomplete
	 public static String[] lyndonFactorize(String word){
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
