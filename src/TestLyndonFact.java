import java.util.ArrayList;

public class TestLyndonFact {

	private static String testStr = "101010101010";
	
	public static void main(String[] args){
		String[] arr = lyndonFactorize(testStr);
		for(int i = 0 ; i < arr.length ; i++){
			System.out.println(arr[i]);
		}
		String[] rows = generateRows(arr,testStr.length());
		for(int i = 0 ; i < rows.length ; i++){
			System.out.println(rows[i]);
		}
		System.out.println(rows.length);
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
	 
	 public static String[] generateRows(String[] lw,int size){
		 String[] rows = new String[size];
		 int r = 0;
		 for(int i = 0 ; i < lw.length ; i++){
			 String currLw = lw[i];
			 int rpTime = size/currLw.length() + 1;
			 //System.out.println("LYWORD : "+currLw+" RPTIME: "+rpTime);
			 String lwExtended = repeat(currLw,rpTime);
			 lwExtended = lwExtended.substring(0,size);
			 for(int j = 0 ; j < currLw.length() ; j++){
				 //System.out.println(lwExtended);
				 rows[r++] = lwExtended;
				 lwExtended = rotateOnce(lwExtended);
			 }
		 }
		 return rows;
	 }
	 
	 //http://stackoverflow.com/questions/1235179/simple-way-to-repeat-a-string-in-java
	 public static String repeat(String s, int n) {
		    if(s == null) {
		        return null;
		    }
		    final StringBuilder sb = new StringBuilder(s.length() * n);
		    for(int i = 0; i < n; i++) {
		        sb.append(s);
		    }
		    return sb.toString();
		}
	 
	 public static String rotateOnce(String a){
		 char first = a.charAt(0);
		 return a.substring(1) + first;
	 }
	 
	
}
