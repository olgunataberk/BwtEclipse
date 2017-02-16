//http://www.cs4fn.org/compression/burrowswheelertransform.java


  // **********************************************
  // Implement Run length encoding and
  // the Burrows Wheeler Transform used to improve compression.
  // This version is fixed to compress "The wheels on the bus"

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;

class Bwt
{
	private static HashSet<Long> rndSet;
	private static int BLOCKSIZE;
	private static int BITLENGTH;
	private static int WORDCOUNT;
	private static int BLOCKCOUNT;


  public static void main (String[] args)
  {

	if(args.length != 3)
		System.exit(1);

	BLOCKSIZE = Integer.parseInt(args[0]);
	BITLENGTH = Integer.parseInt(args[1]);
	WORDCOUNT = Integer.parseInt(args[2]);

	BLOCKCOUNT = BITLENGTH / BLOCKSIZE;

	rndSet  = new HashSet<Long>();

	String[] allWords = slowRandomWords(BITLENGTH,WORDCOUNT);
	String[] rotated = new String[allWords.length];

    for(int i = 0 ; i < allWords.length ; i++){

    	String textCmplete = allWords[i];
    	String transformed = "";
		String format = "%"+((int)(log2(BLOCKSIZE)))+"s";
		//System.out.println(format);
		//Divide bit string into blocks
    		for(int j = 0 ; j < BLOCKCOUNT ; j++){
			//rotate one block
    			String text = textCmplete.substring(j*BLOCKSIZE,j*BLOCKSIZE+BLOCKSIZE);
				//text += "$";
				int SIZE = text.length();
		        String[] rotations = new String[SIZE];
		        String result = "";
		    // First generate all rotations
		        generateRotations(text, rotations);
		    // Next sort those strings
		        int eof = sortStrings(rotations, SIZE);
		    // Finally take the last character of each sorted string to
		    // get the Burrows-Wheeler transform
		        result = lastChars(rotations, SIZE);
				String append = String.format(format,Integer.toBinaryString(eof).substring(1)).replace(" ","0");
			//append rotated block to the new string
		        transformed += result;
				transformed += append;
    		}
		//transformed = transformed.replace("$","");
		//System.out.println(transformed.length());
        rotated[i] = transformed;
    }

    //compute(allWords, rotated);
    	System.out.println(rotated[0].length());
		computeLinear(allWords, rotated);
		System.exit(0);
}

  public static int log2(int a){
	  int i = 0;
	  while(a>1){
	  	a = a>>1;
		i++;
	  }
	  return i;
  }

  //Function for generating random bSize bit words.
  public static String[] slowRandomWords(int bSize,int wCount){
 	 String[] ret = new String[wCount];
 	 for(int i = 0 ; i < wCount ; i++){
 		 String temp = "";
 		 for(int j = 0 ; j < bSize ; j++){
 			 if(Math.random()>0.5)
 				 temp += "1";
 			 else
 				 temp += "0";
 		 }
 		 ret[i] = temp;
 	 }
 	 return ret;
  }
 //Function for generating random bSize bit words but faster.
 public static String[] randomWords(int bSize,int wCount){
	 String[] ret = new String[wCount];
	 for(int i = 0 ; i < wCount ; i++){
		 long rnd = (long)(Math.random()*Math.pow(2,bSize));
		 while(rndSet.contains(rnd))
		 		rnd = (long)(Math.random()*Math.pow(2,bSize));
		 rndSet.add(rnd);
		 //System.out.println(rnd);
		 ret[i] = String.format(("%"+bSize+"s"),Long.toBinaryString(rnd)).replace(' ','0');
	 }
	 return ret;
 }

 //https://en.wikipedia.org/wiki/Lyndon_word
 //returns an array of lyndon words that form the string "word"
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
 
 public static String[] allWords(int bitSize){
	 String[] ret = new String[(int) Math.pow(2,bitSize)];
	 for(int i = 0 ; i < ret.length ; i++)
		 ret[i] = String.format("%16s",Integer.toBinaryString(i));
	 return ret;
 }

 //Compute costs of
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
	 System.out.println("Using blocks of size "+BLOCKSIZE+"\nTotal amount of string pairs with reduced bit flips = "+positive_count
			 			+"\nWith "+ pos_total*1.0/positive_count+" average blf. "+pos_total+" amount of bits less flipped in total.");
	 System.out.println("Total amount of string pairs with increased bit flips = "+negative_count
	 					+"\nWith "+ neg_total*1.0/negative_count+" average bmf. "+neg_total+" amount of bits more flipped in total.");
	 System.out.println("Over a total amount of "+total+" unique word pairs with "+total_flips+" total amount of flips, "+ (total-positive_count-negative_count)+" had the same cost after transformation.");

	 try {
		File dmpFile = new File("bitwt-"+BLOCKSIZE+"-"+BITLENGTH+".dmp");
		FileOutputStream fos = new FileOutputStream(dmpFile,true);
		PrintWriter pw = new PrintWriter(fos);
		pw.print("TP " + total +
				"\nTF " + total_flips +
				"\nPC " + positive_count +
				"\nNC " + negative_count +
				"\nBLF " + pos_total +
				"\n");
		pw.print("^^^^^^^^^^^\n");
		pw.close();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

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

 /*
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
 */
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
  public static int sortStrings(String[] a, int SIZE)
  {
	  int lastIndex = SIZE-1;
      for(int pass=0; pass<= SIZE-2; pass++)
      {
        for(int i=0; i<= SIZE-pass-2; i++)
        {
            if (a[i].compareTo(a[i+1])>0){
            	if(i+1 == lastIndex)
            		lastIndex = i;
            	swap(a, i, i+1);
            }
        }
      }
      return lastIndex;
  }

  public static int findEof(String arr){
	  for(int i = 0 ; i < arr.length() ; i++)
	  	  if(arr.charAt(i) == '$')
		  	return i;
	  return -5;
  }

   // **********************************************
   // Swap 2 given positions in an array of Strings
   public static void swap(String[] a, int i, int j)
  {
     String t = a[i];
     a[i] = a[i+1];
     a[i+1] = t;
  }
}
