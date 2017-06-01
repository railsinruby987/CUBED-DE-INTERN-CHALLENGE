import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//Simple Java Maven project to extract 
//data from websites

public class dataScraper {
	
	public static void main(String[] args) throws Exception {
		
		//Jsoup.connect provides access to html content inside the passed web url
        final Document doc = Jsoup.connect("http://www.ebay.com/sch/Cameras-Photo/625/i.html?_ftrt=901&_sop=12&_sadis=15&_dmd=1&_stpos=11103&_ftrv=1&_from=R40&_nkw=camera&_ipg=200&rt=nc").get();
        
        String itemName, itemRating, itemSold;
        //using selector to select the required div
        Elements attribute = doc.select("#Results li");
        
        //using jdbc to connect tomy sql server
        String driver = "com.mysql.jdbc.Driver";
  		String url = "jdbc:mysql://localhost/inventoryebay";
  		String username = "root";
  		String password = "";
  		Class.forName(driver); Connection conn = DriverManager.getConnection(url,username,password);
		
  		//looping inside div to get information for individual products
  		for (Element row : attribute) {
        	itemName = row.select(".lvtitle").text();
        	itemRating = row.select(".star-ratings__review-num").text();
        	itemSold = row.select(".lvextras").text();
        	
        	PreparedStatement pst = null;
                String insertTableSQL = "INSERT INTO itemsCamera"
    				+ "(description,rating,status) VALUES"
    				+ "(?,?,?)";
    		pst = conn.prepareStatement(insertTableSQL);
    		pst.setString(1, itemName);
    		pst.setString(2, itemRating);
    		pst.setString(3, itemSold);
    		pst.executeUpdate();
		}
		
  		//closing the connection 
		if(conn!=null) {
		  conn.close();
        	}
    	}
}
