import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.Map.Entry;


public class WordDensity1 {
	
	ArrayList<String> newWords = new ArrayList<String>();
	ArrayList<String> bigrams = new ArrayList<String>();
	ArrayList<String> trigrams = new ArrayList<String>();
	HashMap<String, Integer> wordCount = new HashMap<String, Integer>();
	HashMap<String, Integer> bigramCount = new HashMap<String, Integer>();
	HashMap<String, Integer> trigramCount = new HashMap<String, Integer>();
	
	//This method returns all the text from the HTML specified by the 'Url' parameter
	//jsoup is a Java library to parse HTML
	public String textInWebPage(String Url) throws IOException {
		
		try{
			Document doc = Jsoup.connect(Url).ignoreContentType(true).timeout(100000000).get();
			String text = doc.text();
			return text;
		}
		catch(IOException e){
			System.out.println(e.getMessage());
			return "";
		}	
	}
	
    // Method returns domain-name/host of a URL
    public String getDomainName(String url) throws URISyntaxException {
	    URI uri = new URI(url);
	    String domain = uri.getHost();
	    return domain.startsWith("www.") ? domain.substring(4) : domain;
	}
	
	public HashMap<String,Integer> buildNGramCount(ArrayList<String> words){
		
		HashMap<String, Integer> gramCount = new HashMap<String, Integer>();
		for(String x : words){
			if(gramCount.get(x)!=null){
				Integer count1 = gramCount.get(x);
				gramCount.put(x, count1+1);
			}
			else{
				gramCount.put(x,1);
			}
		}
		return gramCount;
	}
	
	// A method to remove stopwords like {a, the, is, was etc}
	public void removeStopWords(String text, String domainName) throws IOException{
		text = text.replaceAll("[^a-zA-Z0-9-]"," ");
		String txt = text.trim().replaceAll("\\s+", " ");
		String words[] = txt.split(" ");
		HashSet<String> stopWordSet = new HashSet<String>();
        BufferedReader br = new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("stopwords.txt")));
		String str;
		while((str = br.readLine())!=null){
			stopWordSet.add(str.replace("\'", ""));
		}
		for(String word1 : words){
			word1 = word1.replace("\'", "");
			String word = word1.replace("?", "");
				try{
					Double c = Double.parseDouble(word);
				}
				catch(Exception e){
					if(!stopWordSet.contains(word.toLowerCase())&&!(word.toLowerCase().equals(domainName))&&!(word.equals("com"))){
						newWords.add(word.toLowerCase());
					}
				}	
		}
		br.close();
	}	
	
	public void formBigrams(){
		int j=0;

		for(j=0;j<newWords.size()-1;j++){
			if(newWords.get(j).equals(newWords.get(j+1))){
				
			}
			else{
				String temp = newWords.get(j)+" "+newWords.get(j+1);
				bigrams.add(temp);
			}
		}
	}
	
	public void formTrigrams(){
	 	int k=0;
		for(k=0;k<newWords.size()-2;k++){
			if(newWords.get(k).equals(newWords.get(k+1)) || newWords.get(k+1).equals(newWords.get(k+2))){
			
			}
			else{
				String temp = newWords.get(k)+" "+newWords.get(k+1)+" "+newWords.get(k+2);
				trigrams.add(temp);
			}
		}		
	}
	
	public void determineTags(){
		wordCount = buildNGramCount(newWords);
		bigramCount = buildNGramCount(bigrams);
		trigramCount = buildNGramCount(trigrams);
		ArrayList<Entry<String, Integer>> wc = sortHashMap(wordCount,10);
		ArrayList<Entry<String, Integer>> bgc = sortHashMap(bigramCount,20);
		ArrayList<Entry<String, Integer>> tgc = sortHashMap(trigramCount,20);
		ArrayList<String> tags1 = new ArrayList<String>();
		//ArrayList<String> tags2 = new ArrayList<String>();
		for(Map.Entry<String, Integer> entry:wc){
        	String word3 = entry.getKey();
        	for(Map.Entry<String, Integer> entry1:tgc){
        		String word4 = entry1.getKey();
            	if(word4.contains(word3)){
            		tags1.add(word4);
            	}
            	if(tags1.size()>=6)
            		break;
            }
        	if(tags1.size()>=6)
        		break;
        }
		for(Map.Entry<String, Integer> entry:wc){
        	String word3 = entry.getKey();
        	for(Map.Entry<String, Integer> entry1:bgc){
        		String word4 = entry1.getKey();
            	if(word4.contains(word3)){
            		tags1.add(word4);
            	}
            	if(tags1.size()>=10)
            		break;
            }
        	if(tags1.size()>=10)
        		break;
        }
		for(String s : tags1){
			System.out.println(s);
		}
	}
	
	public static ArrayList<Entry<String, Integer>> sortHashMap(HashMap<String, Integer> map, int count){
		Set<Entry<String, Integer>> set = map.entrySet();
        ArrayList<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(set);
        Collections.sort( list, new Comparator<Map.Entry<String, Integer>>()
        {
            public int compare( Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2 )
            {
                return (o2.getValue()).compareTo( o1.getValue() );
            }
        } );
        int meetCount = 0;
        ArrayList<Entry<String, Integer>> a = new ArrayList<Entry<String, Integer>>();
        for(Map.Entry<String, Integer> entry:list){
        	a.add(entry);
        	meetCount++;
        	if(meetCount==count){
        		break;
        	}
        }
		return a;
	}
	
	

}
