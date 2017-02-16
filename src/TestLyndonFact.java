import java.util.ArrayList;

public class TestLyndonFact {

	public static void main(String[] args){
		lyndonFactorize("abacabab");
	}
	
	 //https://en.wikipedia.org/wiki/Lyndon_word
	 //incomplete
	 public static String[] lyndonFactorize(String word){
		 int m = 1, k = 0;
		 int N = word.length();
		 ArrayList<String> factorization = new ArrayList<>();
		 String buffer = "";
		 while(m < N){
			 System.out.println("word: " + word+" buffer: "+buffer+" ------ m: "+m+" k: "+k);
			 char wM = word.charAt(m),wK = word.charAt(k);
			 if(wK <= wM){
				 buffer += wM;
				 m++;
				 k++;
			 }else if(wK > wM){
				 buffer = word.charAt(0) + buffer;			 
				 System.out.println("buffer: "+buffer);
				 word = word.substring(m);
				 factorization.add(buffer);
				 N = word.length();
				 buffer = "";
				 m = 1;
				 k = 0;
			 }
		 }
		 if(word.length()>0)
			 factorization.add(word);
		 System.out.println(factorization.toString());
		 return null;
	 }
	
}
