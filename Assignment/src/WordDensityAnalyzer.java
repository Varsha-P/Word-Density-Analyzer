import java.io.IOException;
import java.util.Scanner;

public class WordDensityAnalyzer {

	public static void main(String[] args) throws IOException {
		try{
		String Url = args[0];
		WordDensity1 wd1 = new WordDensity1();
		String domainName = wd1.getDomainName(Url);
		int indexOfDot = domainName.indexOf('.');
		domainName = domainName.substring(0, indexOfDot);
		String text = wd1.textInWebPage(Url);
		if(text==""){
			System.out.println("Could not extract text from the web page specified by the URL");
		}
		else{
			wd1.removeStopWords(text,domainName);
			wd1.formBigrams();
			wd1.formTrigrams();
			wd1.determineTags();
		}
		}
		catch(IOException e1){
			System.out.println(e1.getMessage());
		}
		catch(Exception e2){
			System.out.println(e2.getMessage());
		}
	}

}
