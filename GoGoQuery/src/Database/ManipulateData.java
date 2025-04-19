package Database;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/*
the idea about this class is to have the database be read only from calling the table name, so not making a
new class for every table other classes in the database. 

DynamicDatabase() will ensure every single table's variable will be read and each inputs type can be known
so the inputs will be easier. This is just to make connection, inputs, and outputs simpler from these 2 files;
instead of potentially making each classes for different table names.

*/


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ManipulateData {
	// This implementation strictly cant make this file (database access) to be abstract
	// the implementation relies on making the object and then doing actions on those object.;
	// therefore its better off to only extend the class.
	Connect connect = Connect.getInstance();
	private String table;
	public HashMap<String, String> columns = new HashMap<>();
	private String primaryKey;
	
	// sendData from each of the tables will be different. each datatype is different
	// its just what it is
	public void sendData() {
		this.insertData(new HashMap<String,String>());
		};
	
	public ManipulateData(String table) {
		super();
		this.table = table;
		
		columns = dynamicDatabase();
	}
	

	public  HashMap<String, String> dynamicDatabase() {
		HashMap<String, String> dataColumns = new HashMap<>();
        connect.rs = connect.execQuery("DESCRIBE " + this.table);

        try {
			while (connect.rs.next()) {

			    String columnName = connect.rs.getString("Field");
			    String columnType = connect.rs.getString("Type");
			    dataColumns.put(columnName, columnType);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        this.primaryKey = getPrimaryKey();
        
        return dataColumns;
    }
	
	
	public String getPrimaryKey() {
		// For too long, just realized how important it is to get primary key
		// so this is credit to claude for making it possible 
		// stackoverflow has things, but is just isnt clear and full of sql sooo yea
	    String primaryKeyColumn = null;
	    try {
	        
	        connect.rs = connect.execQuery("SHOW KEYS FROM " + this.table + " WHERE Key_name = 'PRIMARY'");
	        
	        // Check if there's a primary key result
	        if (connect.rs.next()) {
	            // Retrieve the primary key column name
	            primaryKeyColumn = connect.rs.getString("Column_name");
	        } else {
	            // Handle case where no primary key is found
	            System.out.println("No primary key found for table: " + this.table);
	        }
	    } catch (SQLException e) {
	        // Proper error handling
	        System.err.println("Error retrieving primary key for table " + this.table);
	        e.printStackTrace();
	    } finally {
	        // Ensure the result set is closed
	        try {
	            if (connect.rs != null) {
	                connect.rs.close();
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	    }
	    
	    return primaryKeyColumn;
	}

	

	public ArrayList<String> getData(String column) {
		ArrayList<String> res = new ArrayList<>();
		connect.rs = connect.execQuery("SELECT * FROM " + table);
        try {
            while (connect.rs.next()) {
                // Use the dataColumns HashMap to get and set the values
                   res.add(connect.rs.getString(column));
               
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		return res;
	}
	
	public void insertData(HashMap<String, String> inputs) {
		// input the data, make sure to put the input value to match the
		// types in the actual database. 
		if (inputs == null || inputs.isEmpty()) {
	        System.err.println("No input data provided");
	        return;
	    }
		String query1 = String.format("INSERT INTO %s (", table);
		String query2 = String.format("VALUES (");
		
		for (String column : columns.keySet()) {
			String columnType = columns.get(column);
            String inputValue = inputs.get(column);
            // query1 contains the column that includes names of each row, 
            // and query2 contains the values that user wants to input into.
            
            // Skip null or empty values
            if (inputValue == null || inputValue.trim().isEmpty()) {
                System.out.println("Skipping column " + column + " - no value provided");
                continue;
            }
            
            query1 += String.format(" %s,", column);
            
            // apparently the integers and doubles can't have quotes on them,
            // so validate as we go. and change anything that isn't varChar into its original format
            // TODO make else if for EACH TYPES that are not varchar
            try {
                if (columnType.contains("varchar")) {
                    query2 += String.format("\'%s\',", inputValue);
                } else if (columnType.contains("int")) {
                    query2 += String.format("%d,", Integer.parseInt(inputValue));
                } else if (columnType.contains("double") || columnType.contains("float")) {
                    query2 += String.format("%2f,", Double.parseDouble(inputValue));
                    
                } else if (columnType.contains("date")) {
                    query2 += String.format("DATE \'%s\',", inputValue);
                }
                else {
                    // For other types, add as is (might need more specific handling)
                    query2 += String.format(" %s,", inputValue);
                }
            } catch (NumberFormatException e) {
                System.err.println("Error parsing value for column " + column + ": " + inputs.get(column));
                throw e;
            }
        }
		// just trust aright
		query1 = query1.substring(0, query1.length()-1) + ") \n";
		query2 = query2.substring(0, query2.length()-1) + ");";
		
		connect.execUpdate(query1 + query2);
		System.out.println("Database input successful");
		System.out.println(query1 + query2);
	}
	
	
	public void delete(String ID) {
		// the delete, as simple as detecting ID and deleting the data.
		String query = String.format("DELETE FROM %s\n"+
									"WHERE %s = %s", table, primaryKey, ID);
		
		connect.execUpdate(query);
		System.out.println("Deletion successful");
		System.out.println(query);
	}
	
	public ArrayList<ArrayList<String>> getRequirements(boolean print) {
		connect.rs = connect.execQuery("SELECT * FROM " + table);
		ArrayList<ArrayList<String>> res = new ArrayList<>();
		
		if(print) 
			System.out.println("Please input only these columns: ");
		
        try {
            while (connect.rs.next()) {
                // Use the dataColumns HashMap to get and set the values
            	
                for (String column : columns.keySet()) {
                	ArrayList<String> col = new ArrayList<>();
                	col.add(column);
                	col.add(columns.get(column));
                	
                	res.add(col);
                	
                	if (print) 
                		System.out.printf("%s : %s \n", col.get(0), col.get(1));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return res;
	}
	
	// TODO fix this update shills 
	public void update(String id, HashMap<String, String> inputs) {
		// NOTE: I couldnt get preparedStatement working in any of my systems, Ive modified 
		// the connect.java and this file, I couldnt get it working for some reason
		// this is probably the next best thing, hope you understand
		// i do understand the concept, but might be different java sql build on my end.
		
		// the updates will be held from here, also the initial inputs should go here too.
		
		// Validation, make sure every one in the hash map can be inputed
		getRequirements(true);
		
		if (inputs == null || inputs.isEmpty()) {
	        System.err.println("No input data provided");
	        return;
	    }
		
		String query = String.format("UPDATE %s \n" +
				"SET ", table);
		
		for (String column : columns.keySet()) {
			String inputValue = inputs.get(column);
			
			// Skip null or empty values
            if (inputValue == null || inputValue.trim().isEmpty()) {
                System.out.println("Skipping column " + column + " - no value provided");
                continue;
            }
            
			query += column;
			String columnType = columns.get(column);
			 try {
	                if (columnType.contains("varchar")) {
	                	query += String.format("\'%s\',", inputValue);
	                } else if (columnType.contains("int")) {
	                	query += String.format("%d,", Integer.parseInt(inputValue));
	                } else if (columnType.contains("double") || columnType.contains("float")) {
	                	query += String.format("%2f,", Double.parseDouble(inputValue));
	                } else {
	                    // For other types, add as is (might need more specific handling)
	                	query += String.format(" %s,", inputValue);
	                }
	            } catch (NumberFormatException e) {
	                System.err.println("Error parsing value for column " + column + ": " + inputs.get(column));
	                throw e;
	            }
			
		}
		query = query.substring(0, query.length()-1);
		query += String.format("\nWHERE %s = %s", primaryKey, id);
		
		
		connect.execUpdate(query);
		System.out.println("Table Update Successful");
		System.out.println(query);
	}
	
	public ArrayList<ArrayList<String>> getIntersect(String column, String value) {
		// on God this looks over-engineered af im sorry
		
	    ArrayList<ArrayList<String>> res = new ArrayList<>();
	    try {
	        // Create the query where we start, like the above
	        String query = String.format("SELECT * FROM %s WHERE %s = '%s'", table, column, value);
	        connect.rs = connect.execQuery(query);
	        
	        // Get the metadata to dynamically retrieve column names
	        ResultSetMetaData metaData = connect.rs.getMetaData();
	        int columnCount = metaData.getColumnCount();
	        
	        // Iterate through the result set
	        while (connect.rs.next()) {
	            ArrayList<String> row = new ArrayList<>();
	            
	            // Retrieve all column values for each row
	            for (int i = 1; i <= columnCount; i++) {
	                row.add(connect.rs.getString(i));
	            }
	            
	            res.add(row);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    return res;
	}
		
}
		