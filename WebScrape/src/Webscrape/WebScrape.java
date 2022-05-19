package Webscrape;

import org.jsoup.*;
import org.jsoup.nodes.*;
import java.nio.file.*;
import org.json.*;
import java.io.*;

public class WebScrape {
	public static void main(String[] args) {
		final String url = "https://pokemon.fandom.com/wiki/List_of_Generation_I_Pok%C3%A9mon";
		final String url1 = "https://www.serebii.net/pokemon/gen1pokemon.shtml";
		JSONArray pokemon = null;
		int x =0;
		try {
			final Document document = Jsoup.connect(url).get();
			final Document document1 =Jsoup.connect(url1).get();
			
			pokemon = new JSONArray();
			//go through the rows in the table of the url and add them to the json array
			
			for (Element row : document.select("table.wikitable tr")) {
				if(row.select("td:nth-of-type(1)").text().equals("")) {
					//check if the first row is empty if so skip it
					continue;
				}
				else {
					//get the correct box for the correct variable; 
					final String ticker =row.select("td:nth-of-type(1)").text();//pokemon id
					final String name = row.select("td:nth-of-type(3)").text();//pokemon name
					final String t = row.select("td:nth-of-type(4)").text();//pokemon type1
					final String t1 = row.select("td:nth-of-type(5)").text();//pokemon type2
					
					
					JSONObject type = new JSONObject();
					type.put("type", t);
					type.put("type1", t1);
					JSONArray types = new JSONArray();
					types.put(type);
					
					JSONObject p = new JSONObject();
					p.put("Types", types);
					p.put("pokemon", name);
					p.put("ID", ticker.replaceFirst("^0+(?!$)", "")); //remove all 0 from start										
					pokemon.put(p);									
					
				}
												
			}
			
			
			

			for (Element row : document1.select("table.dextable tr")) {
				
				if (row.select("td.fooinfo:nth-of-type(1)").text().equals("")) {
					//check if the first row is empty if so skip it
					continue;
				}
				else {
					final String hp = row.select("td.fooinfo:nth-of-type(6)").text();
					final String att = row.select("td.fooinfo:nth-of-type(7)").text();
					final String def = row.select("td.fooinfo:nth-of-type(8)").text();
					final String sAtt = row.select("td.fooinfo:nth-of-type(9)").text();
					final String sDef = row.select("td.fooinfo:nth-of-type(10)").text();
					final String spd = row.select("td.fooinfo:nth-of-type(11)").text();
					
					
					//add the base states of pokemons 
					JSONObject p = pokemon.getJSONObject(x).put("HP", hp);
					p = pokemon.getJSONObject(x).put("HP", hp);
					p = pokemon.getJSONObject(x).put("Att", att);
					p = pokemon.getJSONObject(x).put("Def", def);
					p = pokemon.getJSONObject(x).put("sAtt", sAtt);
					p = pokemon.getJSONObject(x).put("sdef", sDef);
					p = pokemon.getJSONObject(x).put("spd", spd);
					x++;
					
					
				}
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		try (BufferedWriter out = new BufferedWriter(new FileWriter("gen1.json"))) {
		    out.write(pokemon.toString());
		}catch(IOException e) {
			System.err.println(e);
		}
		
		
		System.out.println(pokemon);
	}
}
