import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;

public class Common {

    public void initTable(Statement stmt) throws Exception {

        String query = "drop table if exists customer_rate;"
                + "drop table if exists feedback_rate;"
                + "drop table if exists feedback;"
                + "drop table if exists orders;"
                + "drop table if exists customer;"
                + "drop table if exists book;"
                + "drop table if exists test;"
                + "drop table if exists authors;"
                + "create table authors("
                + "    name char(50) primary key);"
                + "create table book ("
                + "    ISBN char(50) primary key,"
                + "    title char(50) not null,"
                + "    author char(100) not null,"
                + "    publisher char(100),"
                + "    publish_year date,"
                + "    copies integer,price decimal,"
                + "    format char(20),"
                + "    keywords char(50),"
                + "    subject char(50));              "
                + "create table customer ("
                + "    cid integer primary key,"
                + "    login_name char(20) not null,"
                + "    password char(20) not null,"
                + "    full_name char(50),"
                + "    address char(200),"
                + "    phone char(20),"
                + "    constraint unique_login_name unique (login_name));"
                + "create table orders ("
                + "    oid integer primary key,"
                + "    ISBN char(50) not null,"
                + "    cid integer not null,"
                + "    buy_date date,"
                + "    amount int,"
                + "    constraint relation"
                + "    foreign key (ISBN) references book(ISBN),"
                + "    foreign key (cid) references customer(cid));                      "
                + "create table feedback ("
                + "    fid integer primary key,"
                + "    ISBN char(50),"
                + "    cid integer,"
                + "    comment char(200),"
                + "    rate integer,"
                + "    fb_date date,"
                + "    foreign key (ISBN) references book(ISBN),"
                + "    foreign key (cid) references customer(cid));              "
                + "create table feedback_rate (" + "    fid integer,"
                + "    cid integer," + "    fb_rate integer,"
                + "    primary key (fid, cid),"
                + "    foreign key (fid) references feedback(fid),"
                + "    foreign key (cid) references customer(cid));"
                + "create table customer_rate (" + "    cid1 integer,"
                + "    cid2 integer," + "    trusted boolean,"
                + "	   primary key(cid1, cid2),"
                + "    foreign key (cid1) references customer(cid),"
                + "    foreign key (cid2) references customer(cid));";
        try {
            stmt.execute(query);
        } catch (Exception e) {
            System.err.println("Unable to execute query:" + query + "\n");
            System.err.println(e.getMessage());
            throw (e);
        }

        Book books = new Book();
        Customer customers = new Customer();
        Orders orders = new Orders();
        Feedback feedbacks = new Feedback();
        Feedback_Rate feedback_rates = new Feedback_Rate();
        Customer_Rate customer_rates = new Customer_Rate();

        books.initBook(stmt);
        customers.initCustomer(stmt);
        orders.initOrders(stmt);
        feedbacks.initFeedback(stmt);
        feedback_rates.initFeedback_Rate(stmt);
        customer_rates.initCustomer_Rate(stmt);

    }

    public void display() {

        System.out.println("0 Exit");
        System.out.println("1 Sign Up");
        System.out.println("2 Sign In");
        System.out.println("3 Browse book");
        System.out.println("4 New Order");
        System.out.println("5 Increase Copies");
        System.out.println("6 New Feedback");
        System.out.println("7 New Feedback's Rate");
        System.out.println("8 New Customer's Trust");
        System.out.println("9 Most Usefull Feedback");
        System.out.println("10 Buy Seggestion");
        System.out.println("11 Degree of Two Customers");
        System.out.println("12 Statistics");
        System.out.println("13 User Awards");

    }

    public void showResultSet(ResultSet results) throws Exception {

        ResultSetMetaData m = null;
        m = results.getMetaData();
        int cols = m.getColumnCount();

        for (int i = 1; i <= cols; i++) {
            System.out.print(m.getColumnName(i));
            System.out.print("\t\t\t");
        }

        System.out.println();
        while (results.next()) {
            for (int i = 1; i <= cols; i++) {
                System.out.print(results.getString(i));
                System.out.print("\t\t\t");
            }
            System.out.println();
        }
    }

    public void newTuple(String attrValue[], String tableName,
                         String[] attrName, Statement stmt) throws Exception {

        String query;
        int len = attrName.length;

        query = "insert into " + tableName + " (";
        for (int i = 0; i < len - 1; i++)
            query += attrName[i] + ",";
        query += attrName[len - 1] + ") values (";
        len = attrValue.length;
        for (int i = 0; i < len - 1; i++)
            query += attrValue[i] + ",";
        query += attrValue[len - 1];
        len = attrName.length - attrValue.length;
        for (int i = 0; i < len; i++)
            query += ",null";
        query += ");";

        System.out.println(query);

        try {
            stmt.execute(query);
        } catch (Exception e) {
            System.err.println("Unable to execute query:" + query + "\n");
            System.err.println(e.getMessage());
            throw (e);
        }

    }

    public ResultSet showTable(String[] sigmaAttr, String[] sigmaValue,
                               String tableName, Statement stmt) throws Exception {

        String query;
        int len = sigmaAttr.length;
        query = "select * from " + tableName;
        if (len != 0) {
            query += " where ";
            for (int i = 0; i < len - 1; i++)

                query += sigmaAttr[i] + "=" + sigmaValue + " and ";
            query = query + sigmaAttr[len - 1] + "=" + sigmaValue[len - 1];
        }
        query += ";";
        System.out.println(query);

        ResultSet results;
        try {
            results = stmt.executeQuery(query);
        } catch (Exception e) {
            System.err.println("Unable to execute query:" + query + "\n");
            System.err.println(e.getMessage());
            throw (e);
        }

        return results;

    }

    public int countTuple(String tableName, Statement stmt) throws Exception {

        String query;
        query = "select count(*) as cnt from " + tableName + ";";
        System.out.println(query);

        ResultSet results;
        try {
            results = stmt.executeQuery(query);
        } catch (Exception e) {
            System.err.println("Unable to execute query:" + query + "\n");
            System.err.println(e.getMessage());
            throw (e);
        }

        int count = 0;
        if (results.next())
            count = results.getInt("cnt");
        System.out.println(count);

        return count;
    }

    public void updateTuple(String keyAttr, String keyValue, String[] attr,
                            String[] value, String tableName, Statement stmt) throws Exception {

        String query;

        query = "update " + tableName + " set ";
        for (int i = 0; i < attr.length - 1; i++)
            if (value[i] != "null")
                query += attr[i] + "=" + value[i] + ",";
        if (value[attr.length - 1] != "null") {
            query += attr[attr.length - 1] + "=" + value[attr.length - 1] + " ";
        }
        query += "where " + keyAttr + "=" + keyValue + ";";
        System.out.println(query);

        try {
            stmt.execute(query);
        } catch (Exception e) {
            System.err.println("Unable to execute query:" + query + "\n");
            System.err.println(e.getMessage());
            throw (e);
        }
    }

    public ResultSet mostUseFB(String ISBN, String number, Statement stmt)
            throws Exception {

        String query = "select f.cid, f.comment, f.rate, f.fb_date"
                + " from feedback as f natural join feedback_rate as fr"
                + " where f.ISBN = " + ISBN
                + " group by f.fid order by avg(fr.fb_rate) limit " + number
                + ";";
        System.out.println(query);

        ResultSet results;
        try {
            results = stmt.executeQuery(query);
        } catch (Exception e) {
            System.err.println("Unable to execute query:" + query + "\n");
            System.err.println(e.getMessage());
            throw (e);
        }
        return results;
    }

    public ResultSet suggest(String cid, Statement stmt) throws Exception {

        String query = "select * from book where book.ISBN in (	select o3.ISBN "
                + "from orders as o1, orders as o2, orders as o3 "
                + "where o1.cid = "
                + cid
                + " and o1.ISBN = o2.ISBN and o2.cid = o3.cid and o2.cid != "
                + cid + " and o3.ISBN != o1.ISBN	)	order by book.price;";

        ResultSet results;
        System.out.println(query);
        try {
            results = stmt.executeQuery(query);
        } catch (Exception e) {
            System.err.println("Unable to execute query:" + query + "\n");
            System.err.println(e.getMessage());
            throw (e);
        }
        return results;
    }

    public String degree(String author1, String author2, Statement stmt)
            throws Exception {

        String query = "";
        ResultSet results;
        ArrayList<String> book1 = new ArrayList<String>();
        ArrayList<String> book2 = new ArrayList<String>();

        query = "select ISBN from book where author like '%" + author1 + "%';";

        System.out.println(query);
        try {
            results = stmt.executeQuery(query);
        } catch (Exception e) {
            System.err.println("Unable to execute query:" + query + "\n");
            System.err.println(e.getMessage());
            throw (e);
        }

        while (results.next()) {
            book1.add(results.getString("ISBN"));
        }

        query = "select ISBN from book where author like '%" + author2 + "%';";

        System.out.println(query);
        try {
            results = stmt.executeQuery(query);
        } catch (Exception e) {
            System.err.println("Unable to execute query:" + query + "\n");
            System.err.println(e.getMessage());
            throw (e);
        }

        while (results.next()) {
            book2.add(results.getString("ISBN"));
        }

        int degree = 0;
        String ans = "0";
        for (int i = 0; i < book1.size(); i++) {
            for (int j = 0; j < book2.size(); j++) {
                if (book1.get(i).equals(book2.get(j))) {
                    degree = 1;
                    ans = "1";
                    ans += book1.get(i);
                    break;
                }
            }
            if (degree != 0)
                break;
        }

        if (degree == 1)
            return ans;

        ArrayList<String> a1 = new ArrayList<String>();
        ArrayList<String> a2 = new ArrayList<String>();
        for (int i = 0; i < book1.size(); i++) {
            query = "select author from book where ISBN='" + book1.get(i)
                    + "';";
            try {
                results = stmt.executeQuery(query);
            } catch (Exception e) {
                System.err.println("Unable to execute query:" + query + "\n");
                System.err.println(e.getMessage());
                throw (e);
            }
            while (results.next()) {
                String tmp = results.getString("author");
                String[] group = tmp.split(",");
                for (int j = 0; j < group.length; j++) {
                    a1.add(group[j]);
                }
            }
        }
        for (int i = 0; i < book2.size(); i++) {
            query = "select author from book where ISBN='" + book2.get(i)
                    + "';";
            try {
                results = stmt.executeQuery(query);
            } catch (Exception e) {
                System.err.println("Unable to execute query:" + query + "\n");
                System.err.println(e.getMessage());
                throw (e);
            }
            while (results.next()) {
                String tmp = results.getString("author");
                String[] group = tmp.split(",");
                for (int j = 0; j < group.length; j++) {
                    a2.add(group[j]);
                }
            }
        }

        degree = 0;
        for (int i = 0; i < a1.size(); i++) {
            for (int j = 0; j < a2.size(); j++) {
                if (a1.get(i).equals(a2.get(j))) {
                    degree = 2;
                    ans = "2";
                    ans += a1.get(i);
                    break;
                }
            }
            if (degree != 0)
                break;
        }
        return ans;

    }

    public ResultSet mostPopular(String name, String number, Statement stmt)
            throws Exception {

        String query = "select b.ISBN as ISBN, b.title as title, b.author as author, b.publisher as publisher, b.price as price "
                + "from book as b, orders as o "
                + "where b.ISBN = o.ISBN "
                + "group by b."
                + name
                + " order by sum(o.amount) desc limit "
                + number + ";";

        ResultSet results;

        System.out.println(query);
        try {
            results = stmt.executeQuery(query);
        } catch (Exception e) {
            System.err.println("Unable to execute query:" + query + "\n");
            System.err.println(e.getMessage());
            throw (e);
        }
        return results;
    }

    public ResultSet mostPopularAuthor(String number, Statement stmt) throws Exception {

        String query = "";
        query = "select a.name as author "
                + " from book as b, orders as o, authors as a "
                + " where b.ISBN = o.ISBN and locate(a.name, b.author) > 0"
                + " group by a.name"
                + " order by sum(o.amount) desc limit "
                + number + ";";
        ResultSet results;

        System.out.println(query);
        try {
            results = stmt.executeQuery(query);
        } catch (Exception e) {
            System.err.println("Unable to execute query:" + query + "\n");
            System.err.println(e.getMessage());
            throw (e);
        }

        return results;
    }

    public ResultSet mostTrustUser(Statement stmt) throws Exception {

        String query = "select t1.login_name as login_name, (t1.sum-t2.sum) as score "
                + " from ( select c1.cid as cid, c1.login_name as login_name, count(*) as sum "
                + "from customer as c1, customer_rate as r "
                + "where c1.cid = r.cid2 and r.trusted = true "
                + "group by c1.cid ) as t1 left join ( "
                + "select c2.cid as cid, count(*) as sum "
                + "from customer as c2 ,customer_rate as r "
                + "where c2.cid = r.cid2 and r.trusted = false "
                + "group by c2.cid ) as t2 on t2.cid=t1.cid "
                + "order by (t1.sum-t2.sum) desc;";
        ResultSet results;

        System.out.println(query);
        try {
            results = stmt.executeQuery(query);
        } catch (Exception e) {
            System.err.println("Unable to execute query:" + query + "\n");
            System.err.println(e.getMessage());
            throw (e);
        }
        return results;
    }

    public ResultSet mostUsefulUser(Statement stmt) throws Exception {

        String query = "select c.cid, c.login_name, avg(r.fb_rate) as score "
                + " from customer as c, feedback as f, feedback_rate as r "
                + "where c.cid = f.cid and f.fid = r.fid " + "group by c.cid "
                + "order by avg(r.fb_rate) desc;";
        ResultSet results;

        System.out.println(query);
        try {
            results = stmt.executeQuery(query);
        } catch (Exception e) {
            System.err.println("Unable to execute query:" + query + "\n");
            System.err.println(e.getMessage());
            throw (e);
        }
        return results;
    }
}