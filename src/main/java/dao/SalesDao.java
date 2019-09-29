package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.RevenueItem;

public class SalesDao {

    private String url = "jdbc:mysql://mysql4.cs.stonybrook.edu:3306/andliu?user=andliu";
    private String user= "andliu";
    private String pw = "111473795";

    private List<RevenueItem> getDummyRevenueItems()
    {
        List<RevenueItem> items = new ArrayList<RevenueItem>();

		/*Sample data begins*/
        for (int i = 0; i < 10; i++) {
            RevenueItem item = new RevenueItem();
            item.setDate(new Date());
            item.setNumShares(5);
            item.setAccountId("foo");
            item.setPricePerShare(50.0);
            item.setStockSymbol("AAPL");
            item.setAmount(150.0);
            items.add(item);
        }
        /*Sample data ends*/

        return items;
    }
    public List<RevenueItem> getSalesReport(String month, String year) {

        List<RevenueItem> SalesReportList = new ArrayList<RevenueItem>();
        String dateStr = year+"-"+month+"-00";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pw);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("Select T.Id, O.NumShares, T.PricePerShare, T.DateTime, Tr.StockId, Tr.AccountId, Tr.BrokerId\n" +
                                                "From Transactions T, Orders O, Trade Tr\n" +
                                                "Where O.DateTime > CONVERT ('"+year+"-"+month+"-00', DATETIME) and O.DateTime < CONVERT ('"+year+"-"+month+"-30', DATETIME) and O.Id = TR.OrderId and T.Id = Tr. TransactionId;");
            //  String date = year+"-"+month+"-00"; and O. DateandTime < ’2006.12.1’
                    while(rs.next()){
                        RevenueItem revn = new RevenueItem();
                        revn.setDate(rs.getDate("DateTime"));
                        revn.setAccountId(rs.getString("AccountId"));
                        revn.setStockSymbol(rs.getString("StockId"));
                        revn.setNumShares(rs.getInt("NumShares"));
                        revn.setPricePerShare(rs.getDouble("PricePerShare"));
                        revn.setAmount(Math.round(0.01*revn.getPricePerShare()*revn.getNumShares()));
                        SalesReportList.add(revn);
                    }
        }catch(Exception e){
            System.out.println(e);
        }
		/*
		 * The students code to fetch data from the database will be written here
		 * Query to get sales report for a particular month and year
		 */


        return SalesReportList;

    }



    public List<RevenueItem> getSummaryListing(String searchKeyword) {

        Boolean bName = false;
        String fN = "";
        String lN = "";
        List<RevenueItem> SummaryList = new ArrayList<RevenueItem>();
        if(searchKeyword.contains(" ")){
            String temp[]  = new String[2];
            temp = searchKeyword.split(" ");
            fN = temp[0];
            lN = temp[1];
            bName = true;
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pw);
            Statement st = con.createStatement();
            if(bName){
                ResultSet rs = st.executeQuery("select S.StockSymbol, S.Type, T.DateTime, Tr.AccountId, O.NumShares, T.PricePerShare\n" +
                                                    "from Stock S, Trade Tr, Orders O, Transactions T, Person P, Accounts A\n" +
                                                    "where S.StockSymbol = Tr.StockId and T.Id = Tr.TransactionId and O.Id = Tr.OrderId and A.AccountId = Tr.AccountId and A.ClientId = P.SSN and P.FirstName = '"+fN+"' and P.LastName = '"+lN+"';");

                while(rs.next()) {
                    RevenueItem revn = new RevenueItem();
                    revn.setDate(rs.getDate("DateTime"));
                    revn.setAccountId(rs.getString("AccountId"));
                    revn.setStockSymbol(rs.getString("StockSymbol"));
                    revn.setNumShares(rs.getInt("NumShares"));
                    revn.setPricePerShare(rs.getDouble("PricePerShare"));
                    revn.setAmount(Math.round(0.01 * revn.getPricePerShare() * revn.getNumShares()));
                    SummaryList.add(revn);
                }
            }

            else {
                ResultSet rs = st.executeQuery("select S.StockSymbol, S.Type, T.DateTime, Tr.AccountId, O.NumShares, T.PricePerShare\n" +
                                                    "from Stock S, Trade Tr, Orders O, Transactions T\n" +
                                                    "where S.StockSymbol = Tr.StockId and T.Id = Tr.TransactionId and O.Id = Tr.OrderId and (S.StockSymbol =  '"+searchKeyword+"' or S.type = '"+searchKeyword+"' );");

                while(rs.next()) {
                    RevenueItem revn = new RevenueItem();
                    revn.setDate(rs.getDate("DateTime"));
                    revn.setAccountId(rs.getString("AccountId"));
                    revn.setStockSymbol(rs.getString("StockSymbol"));
                    revn.setNumShares(rs.getInt("NumShares"));
                    revn.setPricePerShare(rs.getDouble("PricePerShare"));
                    revn.setAmount(Math.round(0.01*revn.getPricePerShare()*revn.getNumShares()));
                    SummaryList.add(revn);
            }
            }
        }catch(Exception e){
            System.out.println(e);
        }

		/*
		 * The students code to fetch data from the database will be written here
		 * Query to fetch details of summary listing of revenue generated by a particular stock,
		 * stock type or customer must be implemented
		 * Store the revenue generated by an item in the amount attribute
		 */

        return SummaryList;
    }
}
