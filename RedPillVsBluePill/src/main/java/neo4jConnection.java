import org.neo4j.driver.v1.*;
public class neo4jConnection {

    public StatementResult endors(int id, int depth){
        String base = "";

        Driver driver = GraphDatabase.driver(
                "bolt://localhost:7687",
                AuthTokens.basic( "neo4j", "class" ) );
        Session session = driver.session();

        // Run a query matching all nodes
        for (int i = 1;i<depth;i++){
            base += "-[]->()";
        }
        StatementResult result;
        try {
            result = session.run(
                    "MATCH (:Users {node_id:223395})" + base + "-[]->(a) RETURN a");
            //System.out.println(base);
            int k = 0;
            /*while (result.hasNext()) {
                k++;
                Record record = result.next();
                //System.out.println( record.get("a").get("name").toString()  );
            }*/
        }finally {
            session.close();
            driver.close();
        }
        //System.out.println(k);

        return result;
    }
    public Long time(int id, int depth){
        Long start = System.currentTimeMillis();
        endors(id,depth);
        return System.currentTimeMillis()-start;

    }
    public static void main(String[] args) {
        neo4jConnection test = new neo4jConnection();
        test.endors(223395,4);
    }
}
