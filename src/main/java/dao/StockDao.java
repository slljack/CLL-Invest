package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

import model.RevenueItem;
import model.Stock;

public class StockDao {

    private String url = "jdbc:mysql://mysql4.cs.stonybrook.edu:3306/andliu?user=andliu";
    private String user= "andliu";
    private String pw = "111473795";

    public Stock getDummyStock() {
        Stock stock = new Stock();
        stock.setName("Apple");
        stock.setSymbol("AAPL");
        stock.setPrice(150.0);
        stock.setNumShares(1200);
        stock.setType("Technology");

        return stock;
    }

    public List<Stock> getDummyStocks() {
        List<Stock> stocks = new ArrayList<Stock>();

		/*Sample data begins*/
        for (int i = 0; i < 10; i++) {
            stocks.add(getDummyStock());
        }
		/*Sample data ends*/

        return stocks;
    }

    public List<Stock> getActivelyTradedStocks() {

        List<Stock> ActiveTradeList = new ArrayList<Stock>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pw);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select S.StockSymbol, S.CompanyName, S.Type, SUM(O.NumShares) as NumOfStock\n" +
                                                "from Trade Tr, Orders O, Stock S\n" +
                                                "where Tr.OrderId = O.Id and Tr.StockId = S.StockSymbol \n" +
                                                "group by S.StockSymbol\n" +
                                                "order by NumOfStock DESC\n" +
                                                "Limit 0, 10;");

            while(rs.next()) {
                Stock sto = new Stock();
                sto.setSymbol(rs.getString("StockSymbol"));
                sto.setName(rs.getString("CompanyName"));
                sto.setType(rs.getString("Type"));
                sto.setNumShares(rs.getInt("NumOfStock"));
                ActiveTradeList.add(sto);
            }
            /*
             * The students code to fetch data from the database will be written here
             * Query to fetch details of all the stocks has to be implemented
             * Return list of actively traded stocks
             */
        } catch (Exception e) {
            System.out.println(e);
        }

        return ActiveTradeList;
    }

	public List<Stock> getAllStocks() {

        List<Stock> ActiveTradeList = new ArrayList<Stock>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pw);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select S.CompanyName, S.StockSymbol, S.Type, S.TotalShare\n" +
                                                "from Stock S, Orders O\n" +
                                                "group by S.StockSymbol;");

            while(rs.next()) {
                Stock sto = new Stock();
                sto.setSymbol(rs.getString("StockSymbol"));
                sto.setName(rs.getString("CompanyName"));
                sto.setType(rs.getString("Type"));
                sto.setNumShares(rs.getInt("TotalShare"));
                ActiveTradeList.add(sto);
            }
		
		/*
		 * The students code to fetch data from the database will be written here
		 * Return list of stocks
		 */
        } catch (Exception e) {
            System.out.println(e);
        }
		
		return ActiveTradeList;

	}

    public Stock getStockBySymbol(String stockSymbol)
    {

        Stock sto = new Stock();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pw);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select S.CompanyName, S.StockSymbol, S.Type, S.PricePerShare, S.TotalShare\n" +
                                                "from Stock S\n" +
                                                "where S.StockSymbol = '"+stockSymbol+"';");


            while(rs.next()) {
                sto.setSymbol(rs.getString("StockSymbol"));
                sto.setName(rs.getString("CompanyName"));
                sto.setType(rs.getString("Type"));
                sto.setNumShares(rs.getInt("TotalShare"));
            }
        /*
		 * The students code to fetch data from the database will be written here
		 * Return stock matching symbol
		 */

        } catch (Exception e) {
            System.out.println(e);
        }

        return sto;
    }

    public String setStockPrice(String stockSymbol, double stockPrice) {

        String pattern = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date today = Calendar.getInstance().getTime();
        String time = sdf.format(today);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pw);
            Statement st = con.createStatement();
            st.executeUpdate("Update Stock S\n" +
                                    "set S.PricePerShare = "+stockPrice+"\n" +
                                    "Where S.StockSymbol = '"+stockSymbol+"';");
            st.executeUpdate("INSERT INTO StockHistory VALUES('"+stockSymbol+"', CONVERT('"+time+"', DATETIME) , "+stockPrice+");");

        } catch (Exception e) {
            System.out.println(e);
        }
        /*
         * The students code to fetch data from the database will be written here
         * Perform price update of the stock symbol
         */

        return "success";
    }
	
	public List<Stock> getOverallBestsellers() {

        List<Stock> bestSellerList = new ArrayList<Stock>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pw);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("Select S.CompanyName, S.StockSymbol, S.Type, S.PricePerShare, Count(S.StockSymbol) as frequency, sum(O.NumShares) as NumOfStock\n" +
                                                "From Orders O, Trade Tr, Stock S\n" +
                                                "Where O.Id = Tr. OrderId and Tr.StockId = S.StockSymbol\n" +
                                                "Group By S.StockSymbol\n" +
                                                "order by frequency desc");

            while(rs.next()) {
                Stock sto = new Stock();
                sto.setSymbol(rs.getString("StockSymbol"));
                sto.setName(rs.getString("CompanyName"));
                sto.setType(rs.getString("Type"));
                sto.setNumShares(rs.getInt("NumOfStock"));
                bestSellerList.add(sto);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
		/*
		 * The students code to fetch data from the database will be written here
		 * Get list of bestseller stocks
		 */

		return bestSellerList;

	}

    public List<Stock> getCustomerBestsellers(String customerID) {

        List<Stock> bestCusSellerList = new ArrayList<Stock>();

        if(customerID.contains("-")){
            customerID =  customerID.replace("-","");
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pw);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("Select S.CompanyName, S.StockSymbol, S.Type, S.PricePerShare, Count(S.StockSymbol) as frequency, sum(O.NumShares) as NumOfStock\n" +
                                                "From Orders O, Trade Tr, Stock S, Clients C, Accounts A\n" +
                                                "Where O.Id = Tr. OrderId and Tr.StockId = S.StockSymbol and A.ClientId = C.Id and Tr.AccountId = A.AccountId and C.Id = "+Integer.parseInt(customerID)+"\n" +
                                                "Group By S.StockSymbol\n" +
                                                "order by frequency desc;");

            while(rs.next()) {
                Stock sto = new Stock();
                sto.setSymbol(rs.getString("StockSymbol"));
                sto.setName(rs.getString("CompanyName"));
                sto.setType(rs.getString("Type"));
                sto.setNumShares(rs.getInt("NumOfStock"));
                bestCusSellerList.add(sto);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
		/*
		 * The students code to fetch data from the database will be written here.
		 * Get list of customer bestseller stocks
		 */

        return bestCusSellerList;

    }

	public List getStocksByCustomer(String customerId) {


        List<Stock> customerHoldings = new ArrayList<Stock>();

        if(customerId.contains("-")){
            customerId =  customerId.replace("-","");
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pw);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("Select H.StockId, H.NumShares, S.CompanyName, S.Type\n" +
                                                "From HasStock H, Accounts A, Clients C, Stock S\n" +
                                                "Where H.AccountId = A.AccountId and A.ClientId = C.Id and C.Id = "+Integer.parseInt(customerId)+" and H.StockId = S.StockSymbol;");

            while(rs.next()) {
                Stock sto = new Stock();
                sto.setSymbol(rs.getString("StockId"));
                sto.setName(rs.getString("CompanyName"));
                sto.setType(rs.getString("Type"));
                sto.setNumShares(rs.getInt("NumShares"));
                customerHoldings.add(sto);
            }

        } catch (Exception e) {
            System.out.println(e);
        }

		/*
		 * The students code to fetch data from the database will be written here
		 * Get stockHoldings of customer with customerId
		 */

		return customerHoldings;
	}

    public List<Stock> getStocksByName(String name) {

        List<Stock> stockByName = new ArrayList<Stock>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pw);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select S.CompanyName, S.StockSymbol, S.Type, S.TotalShare\n" +
                                                "from Stock S\n" +
                                                "where S.CompanyName = '"+name+"';");


            while(rs.next()) {
                Stock sto = new Stock();
                sto.setSymbol(rs.getString("StockSymbol"));
                sto.setName(rs.getString("CompanyName"));
                sto.setType(rs.getString("Type"));
                sto.setNumShares(rs.getInt("TotalShare"));
                stockByName.add(sto);
            }

        } catch (Exception e) {
            System.out.println(e);
        }

		/*
		 * The students code to fetch data from the database will be written here
		 * Return list of stocks matching "name"
		 */

        return stockByName;
    }

    public List<Stock> getStockSuggestions(String customerID) {


        List<Stock> stockSuggestions = new ArrayList<Stock>();

        if(customerID.contains("-")){
            customerID = customerID.replace("-","");
        }

        Stock ha = getCustomerBestsellers(customerID).get(0);

        if(ha == null){
            return null;
        }

        String haType = ha.getType();



        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pw);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select S.StockSymbol, S.CompanyName, S.Type, S.TotalShare\n" +
                                                "from Stock S\n" +
                                                "where S.Type = '"+haType+"';");



            while(rs.next()) {
                Stock sto = new Stock();
                sto.setSymbol(rs.getString("StockSymbol"));
                sto.setName(rs.getString("CompanyName"));
                sto.setType(rs.getString("Type"));
                sto.setNumShares(rs.getInt("TotalShare"));
                stockSuggestions.add(sto);
            }

        } catch (Exception e) {
            System.out.println(e);
        }


		/*
		 * The students code to fetch data from the database will be written here
		 * Return stock suggestions for given "customerId"
		 */

        return stockSuggestions;

    }

    public List<Stock> getStockPriceHistory(String stockSymbol) {

        List<Stock> stockHistoryList = new ArrayList<Stock>();


        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pw);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select S.StockSymbol, S.CompanyName, S.type, SH.NewPrice\n" +
                                                "from StockHistory SH, Stock S\n" +
                                                "where S.StockSymbol = SH.StockId and S.StockSymbol = '"+stockSymbol+"';");

            while(rs.next()) {
                Stock sto = new Stock();
                sto.setSymbol(rs.getString("StockSymbol"));
                sto.setName(rs.getString("CompanyName"));
                sto.setType(rs.getString("Type"));
                sto.setNumShares(rs.getInt("NewPrice"));
                stockHistoryList.add(sto);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
		/*
		 * The students code to fetch data from the database
		 * Return list of stock objects, showing price history
		 */

        return stockHistoryList;
    }

    public List<String> getStockTypes() {

		/*
		 * The students code to fetch data from the database will be written here.
		 * Populate types with stock types
		 */

        List<String> types = new ArrayList<String>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pw);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select S.Type\n" +
                                                "from Stock S\n" +
                                                "group by S.Type;");


            while(rs.next()) {
                types.add(rs.getString("Type"));
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return types;

    }

    public List<Stock> getStockByType(String stockType) {



        List<Stock> stockByType = new ArrayList<Stock>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pw);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select S.CompanyName, S.StockSymbol, S.Type, S.TotalShare\n" +
                                                "from Stock S\n" +
                                                "where S.Type = '"+stockType+"';");


            while(rs.next()) {
                Stock sto = new Stock();
                sto.setSymbol(rs.getString("StockSymbol"));
                sto.setName(rs.getString("CompanyName"));
                sto.setType(rs.getString("Type"));
                sto.setNumShares(rs.getInt("TotalShare"));
                stockByType.add(sto);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
		/*
		 * The students code to fetch data from the database will be written here
		 * Return list of stocks of type "stockType"
		 */

        return stockByType;
    }
}
