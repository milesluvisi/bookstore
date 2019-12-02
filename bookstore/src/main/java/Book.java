import java.sql.ResultSet;
import java.sql.Statement;

public class Book {

    private String[] attrName = {"ISBN", "title", "authors", "edition", "year", "category", "publisher", "price", "stock"};
    private String tableName = "book";

    public Book() {

    }

    public void initBook(Statement s) throws Exception {
        String[] attrValue;

        attrValue = new String[] {"'0000000001'", "'Database Management Systems'", "'Xintao Wu'", "'1st", "'2019'", "'Academia'", "'UofA'", "'$100.00", "'2'"};
        this.addBook(attrValue, s);
        attrValue = new String[] {"'0000000002'", "'How to Code: An Overview'", "'John Smith'", "'2nd'", "'2018'", "'Tutorials'", "'UofA'", "'$25'", "'43'"};
        this.addBook(attrValue, s);
        attrValue = new String[] {"'0101010101'", "'SQL For Dummies'", "'Mike Jones'", "'10th'", "'2020'", "'Tutorials'", "'UofA'", "'$10'", "'100'"};
        this.addBook(attrValue, s);
    }

    public void addBook(String[] attrValue, Statement s) throws Exception {
        Common c = new Common();
        c.newTuple(attrValue, tableName, attrName, s);

        String authors = attrValue[2].substring(1, attrValue[2].length()-1);
        String[] temp = authors.split(",");
        String[] name = {"name"};
        for(int i = 0; i < temp.length; i++) {
            String value[] = new String[] {"'"+temp[i]+"'"};

            String query = "SELECT name from authors WHERE authors.name=" + "'" + temp[i]+"';";
            ResultSet results;

            try {
                results = s.executeQuery(query);
            } catch (Exception e) {
                System.err.println("Cannot execute: " + query + "\n");
                System.err.println(e.getMessage());
                throw e;
            }

            String t = "";
            if(results.next()) {
                t = results.getString("name");
            }
            if(!t.equals(temp[i])) {
                c.newTuple(value, "authors", name, s);
            }
        }
    }

    public void removeBook(String ISBN, Statement s) throws Exception {
        String query = "DELETE FROM book WHERE book.ISBN=";
        query += "'" + ISBN + "';";
        System.out.println(query);

        try {
            s.executeQuery(query);
        } catch (Exception e) {
            System.err.println("Cannot execute: " + query + "\n");
            System.err.println(e.getMessage());
            throw e;
        }
    }

    public void updateBook(String key, String keyValue, String[] attr, String[] value, Statement s) throws Exception {
        Common c = new Common();
        c.updateTuple(key, keyValue, attr, value, tableName, s);
    }

    public int bookCounter(Statement s) throws Exception {
        Common c = new Common();
        int count = c.countTuple(tableName, s);
        return count;
    }
}
