
public class TestBwt {
	public static void main(String[] args){
		Encoder encObj = new Encoder();
		String[] randomWords = Utilities.generateRandomWords(64, 100000);
		String result = encObj.compute(randomWords, 8, 64, Encoder.BIJECTIVE_VARIANT_BWT);
		System.out.println(result);
	}
}
