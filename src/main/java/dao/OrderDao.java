package dao;

import model.*;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDao {

    private String url = "jdbc:mysql://mysql4.cs.stonybrook.edu:3306/andliu?user=andliu";
    private String user= "andliu";
    private String pw = "111473795";

    public Order getDummyTrailingStopOrder() {
        TrailingStopOrder order = new TrailingStopOrder();

        order.setId(1);
        order.setDatetime(new Date());
        order.setNumShares(5);
        order.setPercentage(12.0);
        return order;
    }

    public Order getDummyMarketOrder() {
        MarketOrder order = new MarketOrder();

        order.setId(1);
        order.setDatetime(new Date());
        order.setNumShares(5);
        order.setBuySellType("buy");
        return order;
    }

    public Order getDummyMarketOnCloseOrder() {
        MarketOnCloseOrder order = new MarketOnCloseOrder();

        order.setId(1);
        order.setDatetime(new Date());
        order.setNumShares(5);
        order.setBuySellType("buy");
        return order;
    }

    public Order getDummyHiddenStopOrder() {
        HiddenStopOrder order = new HiddenStopOrder();

        order.setId(1);
        order.setDatetime(new Date());
        order.setNumShares(5);
        order.setPricePerShare(145.0);
        return order;
    }

    public List<Order> getDummyOrders() {
        List<Order> orders = new ArrayList<Order>();

        for (int i = 0; i < 3; i++) {
            orders.add(getDummyTrailingStopOrder());
        }

        for (int i = 0; i < 3; i++) {
            orders.add(getDummyMarketOrder());
        }

        for (int i = 0; i < 3; i++) {
            orders.add(getDummyMarketOnCloseOrder());
        }

        for (int i = 0; i < 3; i++) {
            orders.add(getDummyHiddenStopOrder());
        }

        return orders;
    }

    public String submitOrder(Order order, Customer customer, Employee employee, Stock stock) {

		/*
		 * Student code to place stock order
		 * Employee can be null, when the order is placed directly by Customer
         * */

        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pw);
            Statement st = con.createStatement();

            String type = "";
            int percent = 0;
            double pps = 0;
            String buyOrSell = null;


            if(order instanceof MarketOrder){
                type = "Market";
                buyOrSell = ((MarketOrder) order).getBuySellType();
            }
            else if(order instanceof MarketOnCloseOrder){
                type = "MarketOnClose";
                buyOrSell = ((MarketOrder) order).getBuySellType();
            }
            else if(order instanceof TrailingStopOrder){
                type = "TrailingStop";
                percent = (int)((TrailingStopOrder) order).getPercentage();
            }
            else if(order instanceof HiddenStopOrder){
                type = "HiddenStop";
                pps = ((HiddenStopOrder) order).getPricePerShare();
            }
            else{
                return "fail";
            }

            //int id = (int)Math.random() * 999999998 + 1;
            int id = order.getId();
            int numShare = order.getNumShares();
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String date = simpleDateFormat.format(order.getDatetime());
            int fee = 0;

            int cutomerId = customer.getAccountNumber();
            String employeeId = employee.getEmployeeID();
            String stockSymbol = stock.getSymbol();



            //Insert Transaction
            String sql = String.format("INSERT INTO Transactions VALUES(%1$s,%2$s,%3$s,%4$s)", id, fee, date, numShare);
            PreparedStatement ps = con.prepareStatement(sql);
            ps.executeQuery();

            //Insert Order
            sql = String.format("INSERT INTO Orders VALUES(%1$s,%2$s,%3$s,%4$s,%5$s,%6$s,%7$s)",
                    numShare, pps, id, date, percent, type, buyOrSell);
            ps = con.prepareStatement(sql);
            ps.executeQuery();


            //Insert Trade
            sql = String.format("INSERT INTO Trade VALUES(%1$s,%2$s,%3$s,%3$s,%4$s)", cutomerId, employeeId, id, id, stockSymbol);
            ps = con.prepareStatement(sql);
            ps.executeQuery();

        }
        catch(Exception e){
            System.out.println(e);
            return "fail";
        }

		/*Sample data begins*/
        return "success";
		/*Sample data ends*/

    }

    public List<Order> getOrderByStockSymbol(String stockSymbol) {
        /*
		 * Student code to get orders by stock symbol
         */
        //return getDummyOrders();

        List<Order> list = new ArrayList<Order>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pw);
            Statement st = con.createStatement();
            String sql = String.format("SELECT O.* FROM Orders O, Trade T"+
                                " WHERE O.Id = T.OrderId AND T.StockId = '%s'",stockSymbol);
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int numShare = rs.getInt("NumShares");
                double pps = rs.getDouble("PricePerShare");
                int id = rs.getInt("Id");
                Date date = rs.getDate("DateTime");
                int percent = rs.getInt("Percentage");
                String type = rs.getString("PriceType");
                String buySell = rs.getString("OrderType");

                if(type.equals("Market")){
                    MarketOrder order = new MarketOrder();
                    order.setId(id);
                    order.setDatetime(date);
                    order.setNumShares(numShare);
                    order.setBuySellType(buySell);
                    list.add(order);
                }
                else if(type.equals("MarketOnClose")){
                    MarketOnCloseOrder order = new MarketOnCloseOrder();
                    order.setId(id);
                    order.setDatetime(date);
                    order.setNumShares(numShare);
                    order.setBuySellType(buySell);
                    list.add(order);
                }
                else if(type.equals("TrailingStop")){
                    TrailingStopOrder order = new TrailingStopOrder();
                    order.setId(id);
                    order.setDatetime(date);
                    order.setNumShares(numShare);
                    order.setPercentage(percent);
                    list.add(order);
                }
                else{// type.equals("HiddenStop")
                    HiddenStopOrder order = new HiddenStopOrder();
                    order.setId(id);
                    order.setDatetime(date);
                    order.setNumShares(numShare);
                    order.setPricePerShare(pps);
                    list.add(order);
                }
            }
        }
        catch(Exception e){
            System.out.println(e);
        }

        return list;
    }

    public List<Order> getOrderByCustomerName(String customerName) {
         /*
		 * Student code to get orders by customer name
         */
        //return getDummyOrders();
        List<Order> list = new ArrayList<Order>();
        //System.out.println(customerName);
        String name[] = customerName.split(" ");
        //System.out.println(name[0]);
        //System.out.println(name[1]);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pw);
            Statement st = con.createStatement();
            String sql = String.format("SELECT O.* FROM Orders O, Trade T, Clients C, Person P, Accounts A " +
                    "WHERE O.Id = T.OrderId AND T.AccountId = A.AccountId AND A.ClientId = C.Id AND C.Id = P.SSN AND " +
                    "P.FirstName = '%1$s' AND P.LastName = '%2$s'",name[0],name[1]);
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int numShare = rs.getInt("NumShares");
                //System.out.println(numShare);
                double pps = rs.getDouble("PricePerShare");
                //System.out.println(pps);
                int id = rs.getInt("Id");
                //System.out.println(id);
                Date date = rs.getDate("DateTime");
                //System.out.println(date.toString());
                int percent = rs.getInt("Percentage");
                //System.out.println(percent);
                String type = rs.getString("PriceType");
                //System.out.println(type);
                String buySell = rs.getString("OrderType");
                //System.out.println(buySell);

                if(type.equals("Market")){
                    MarketOrder order = new MarketOrder();
                    order.setId(id);
                    order.setDatetime(date);
                    order.setNumShares(numShare);
                    order.setBuySellType(buySell);
                    list.add(order);
                }
                else if(type.equals("MarketOnClose")){
                    MarketOnCloseOrder order = new MarketOnCloseOrder();
                    order.setId(id);
                    order.setDatetime(date);
                    order.setNumShares(numShare);
                    order.setBuySellType(buySell);
                    list.add(order);
                }
                else if(type.equals("TrailingStop")){
                    TrailingStopOrder order = new TrailingStopOrder();
                    order.setId(id);
                    order.setDatetime(date);
                    order.setNumShares(numShare);
                    order.setPercentage(percent);
                    list.add(order);
                }
                else{// type.equals("HiddenStop")
                    HiddenStopOrder order = new HiddenStopOrder();
                    order.setId(id);
                    order.setDatetime(date);
                    order.setNumShares(numShare);
                    order.setPricePerShare(pps);
                    list.add(order);
                }
            }
        }
        catch(Exception e){
            System.out.println(e);
        }

        return list;
    }

    public List<Order> getOrderHistory(String customerId) {
        /*
		 * The students code to fetch data from the database will be written here
		 * Show orders for given customerId
		 */
        //return getDummyOrders();
        System.out.println(customerId + " FROM getOrderHistory");
        List<Order> list = new ArrayList<Order>();

        String url = "jdbc:mysql://mysql4.cs.stonybrook.edu:3306/andliu?user=andliu";
        String user= "andliu";
        String pw = "111473795";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pw);
            Statement st = con.createStatement();
            String sql = String.format("SELECT O.* FROM Orders O, Trade T, Clients C, Accounts A" +
                                    " WHERE O.Id = T.OrderId AND T.AccountId = A.AccountId AND A.ClientId = C.Id AND C.Id = '%s'",customerId);
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int numShare = rs.getInt("NumShares");
                //System.out.println(numShare);
                double pps = rs.getDouble("PricePerShare");
                //System.out.println(pps);
                int id = rs.getInt("Id");
                //System.out.println(id);
                Date date = rs.getDate("DateTime");
                //System.out.println(date.toString());
                int percent = rs.getInt("Percentage");
                //System.out.println(percent);
                String type = rs.getString("PriceType");
                //System.out.println(type);
                String buySell = rs.getString("OrderType");
                //System.out.println(buySell);

                if(type.equals("Market")){
                    MarketOrder order = new MarketOrder();
                    order.setId(id);
                    order.setDatetime(date);
                    order.setNumShares(numShare);
                    order.setBuySellType(buySell);
                    list.add(order);
                }
                else if(type.equals("MarketOnClose")){
                    MarketOnCloseOrder order = new MarketOnCloseOrder();
                    order.setId(id);
                    order.setDatetime(date);
                    order.setNumShares(numShare);
                    order.setBuySellType(buySell);
                    list.add(order);
                }
                else if(type.equals("TrailingStop")){
                    TrailingStopOrder order = new TrailingStopOrder();
                    order.setId(id);
                    order.setDatetime(date);
                    order.setNumShares(numShare);
                    order.setPercentage(percent);
                    list.add(order);
                }
                else{// type.equals("HiddenStop")
                    HiddenStopOrder order = new HiddenStopOrder();
                    order.setId(id);
                    order.setDatetime(date);
                    order.setNumShares(numShare);
                    order.setPricePerShare(pps);
                    list.add(order);
                }
            }
        }
        catch(Exception e){
            System.out.println(e);
        }

        return list;
    }


    public List<OrderPriceEntry> getOrderPriceHistory(String orderId) {

        /*
		 * The students code to fetch data from the database will be written here
		 * Query to view price history of hidden stop order or trailing stop order
		 * Use setPrice to show hidden-stop price and trailing-stop price
		 */
        List<OrderPriceEntry> orderPriceHistory = new ArrayList<OrderPriceEntry>();
        /*for (int i = 0; i < 10; i++) {
            OrderPriceEntry entry = new OrderPriceEntry();
            entry.setOrderId(orderId);
            entry.setDate(new Date());
            entry.setStockSymbol("aapl");
            entry.setPricePerShare(150.0);
            entry.setPrice(100.0);
            orderPriceHistory.add(entry);
        }*/

        String url = "jdbc:mysql://mysql41.cs.stonybrook.edu:3306/andliu?user=andliu";
        String user= "andliu";
        String pw = "111473795";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pw);
            Statement st = con.createStatement();
            String sql = String.format("SELECT SH.NewPrice, O.PricePerShare, O.Id, O.DateTime, S.StockSymbol+"+
                    " FROM Orders O, Trade T, Stock S, StockHistory SH"+
                    " WHERE O.Id = T.OrderId AND T.StockId = S.StockSymbol AND O.Id = '%s'",orderId);
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int price = rs.getInt("NewPrice");
                //System.out.println(numShare);
                double pps = rs.getDouble("PricePerShare");
                //System.out.println(pps);
                int id = rs.getInt("Id");
                //System.out.println(id);
                Date date = rs.getDate("DateTime");
                //System.out.println(date.toString());
                String stock = rs.getString("StockSymbol");

                OrderPriceEntry entry = new OrderPriceEntry();
                entry.setOrderId(id+"");
                entry.setDate(date);
                entry.setStockSymbol(stock);
                entry.setPricePerShare(pps);
                entry.setPrice(price);
                orderPriceHistory.add(entry);
            }
        }
        catch(Exception e){
            System.out.println(e);
        }

        return orderPriceHistory;
    }
}
