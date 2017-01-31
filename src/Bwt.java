//http://www.cs4fn.org/compression/burrowswheelertransform.java


  // **********************************************
  // Implement Run length encoding and
  // the Burrows Wheeler Transform used to improve compression.
  // This version is fixed to compress "The wheels on the bus"

import java.util.HashSet;

class Bwt
{
	private static HashSet<Long> rndSet;

  public static void main (String[] args)
  {

		rndSet  = new HashSet<Long>();

	String[] allWords = randomWords(32,1000000);
	String[] rotated = new String[allWords.length];

    for(int i = 0 ; i < allWords.length ; i++){

    	String text = allWords[i];
    	int SIZE = text.length();


        String[] rotations = new String[SIZE];
        String result = "";

    // First generate all rotations
        generateRotations(text, rotations);

    // Next sort those strings
        sortStrings(rotations, SIZE);

    // Finally take the last character of each sorted string to
    // get the Burrows-Wheeler transform
        result = lastChars(rotations, SIZE);
        rotated[i] = result;

    }

    //compute(allWords, rotated);
		computeLinear(allWords, rotated);
		System.exit(0);
}

  // **********************************************
  // Do run length encoding on a string
 public static String runLengthEncode(String source)
 {
     String result = "";
     int count = 1;
     char current = source.charAt(0);

     for(int i=1; i< source.length(); i++)
     {
        if (source.charAt(i)==current)
           count = count + 1;
        else
        {
            result = result + count + current;
            count = 1;
            current = source.charAt(i);
        }
     }
     result = result + count + current;

     return result;
 }


  // **********************************************
  // Generate all the rotations of a String, rotating one position at a time
 public static void generateRotations(String source, String[] s)
 {
     s[0] = source;

     for(int i=1; i< source.length(); i++)
     {
        s[i] = s[i-1].substring(1) + s[i-1].charAt(0);
     }
 }

  // **********************************************
  // Get each of the last characters of an array of Strings
 public static String lastChars(String[] s, int SIZE)
 {
     String result = "";

     for(int i=0; i< SIZE; i++)
     {
        result = result + s[i].charAt(SIZE-1);
     }

     return result;
 }



  // **********************************************
  // Sort an array of strings
 public static void printStringArray(String[] a, int SIZE)
 {
     for(int i=0; i< SIZE; i++)
     {
        System.out.println(a[i]);
     }
     System.out.println();

 }


  // **********************************************
  // Sort an array of strings
 public static void sortStrings(String[] a, int SIZE)
 {
     for(int pass=0; pass<= SIZE-2; pass++)
     {
       for(int i=0; i<= SIZE-pass-2; i++)
       {
           if (a[i].compareTo(a[i+1])>0)
               swap(a, i, i+1);
       }
     }
 }

  // **********************************************
  // Swap 2 given positions in an array of Strings
  public static void swap(String[] a, int i, int j)
 {
    String t = a[i];
    a[i] = a[i+1];
    a[i+1] = t;
 }

 public static String[] randomWords(int bSize,int wCount){
	 String[] ret = new String[wCount];
	 for(int i = 0 ; i < wCount ; i++){
		 long rnd = (long)(Math.random()*Math.pow(2,bSize));
		 while(rndSet.contains(rnd))
		 		rnd = (long)(Math.random()*Math.pow(2,bSize));
		 rndSet.add(rnd);
		 //System.out.println(rnd);
		 ret[i] = String.format(("%"+bSize+"s"),Long.toBinaryString(rnd));
	 }
	 return ret;
 }

 public static String[] allWords(int bitSize){
	 String[] ret = new String[(int) Math.pow(2,bitSize)];
	 for(int i = 0 ; i < ret.length ; i++)
		 ret[i] = String.format("%16s",Integer.toBinaryString(i));
	 return ret;
 }

 public static void computeLinear(String[] orig, String[] rota){
	 long positive_count = 0,negative_count = 0;
	 long pos_total = 0,neg_total = 0;
	 long total = 0,total_flips = 0;
	 for(int i = 1 ; i < orig.length ; i++){
		 int j = i-1;
		 total++;
		 //int origCost = cost(orig[i],orig[j]);
		 int origCost = costDBI(orig[i],orig[j]);
		 int rotaCost = cost(rota[i],rota[j]);
		 //System.out.println("orig: "+orig[i]+" - "+orig[j]+" / Cost: "+origCost);
		 //System.out.println("rotated: "+rota[i]+" - "+rota[j]+" / Cost: "+rotaCost);
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
	 System.out.println("Total amount of string pairs with reduced bit flips = "+positive_count
			 			+"\nWith "+ pos_total*1.0/positive_count+" average blf. "+pos_total+" amount of bits less flipped in total.");
	 System.out.println("Total amount of string pairs with increased bit flips = "+negative_count
	 					+"\nWith "+ neg_total*1.0/negative_count+" average bmf. "+neg_total+" amount of bits more flipped in total.");
	 System.out.println("Over a total amount of "+total+" unique word pairs with "+total_flips+" total amount of flips, "+ (total-positive_count-negative_count)+" had the same cost after transformation.");
 }

 public static void compute(String[] orig, String[] rota){
	 long positive_count = 0,negative_count = 0;
	 long pos_total = 0,neg_total = 0;
	 long total = 0,total_flips = 0;
	 for(int i = 0 ; i < orig.length ; i++){
		 if(i%(int)(Math.pow(2, 8))==0)
			 System.out.println("some progress...");
		 for(int j = i ; j < orig.length ; j++){
			 total++;
			 //int origCost = cost(orig[i],orig[j]);
			 int origCost = costDBI(orig[i],orig[j]);
			 int rotaCost = cost(rota[i],rota[j]);
			 //System.out.println("orig: "+orig[i]+" - "+orig[j]+" / Cost: "+origCost);
			 //System.out.println("rotated: "+rota[i]+" - "+rota[j]+" / Cost: "+rotaCost);
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
	 }
	 //BFL -> Bits Less Flipped.
	 System.out.println("Total amount of string pairs with reduced bit flips = "+positive_count
			 			+"\nWith "+ pos_total*1.0/positive_count+" average blf. "+pos_total+" amount of bits less flipped in total.");
	 System.out.println("Total amount of string pairs with increased bit flips = "+negative_count
	 					+"\nWith "+ neg_total*1.0/negative_count+" average bmf. "+neg_total+" amount of bits more flipped in total.");
	 System.out.println("Over a total amount of "+total+" unique word pairs with "+total_flips+" total amount of flips, "+ (total-positive_count-negative_count)+" had the same cost after transformation.");
 }

 public static int cost(String b1,String b2){
	 int ret = 0;
	 for(int i = 0 ; i < b1.length() ; i++){
		 if(b1.charAt(i)!=b2.charAt(i))
		 	ret++;
	 }
	 return ret;
 }

 public static int costDBI(String b1,String b2){
	 int ret = 0;
	 for(int i = 0 ; i < b1.length() ; i++){
		 if(b1.charAt(i)!=b2.charAt(i))
		 	ret++;
	 }
	 return ret>(b1.length()/2) ? b1.length()-ret : ret;
 }

}
