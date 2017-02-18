
public class Utilities {

	//Slow function for generating random bSize bit words.
	public static String[] generateRandomWords(int bSize,int wCount){
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
	
}
