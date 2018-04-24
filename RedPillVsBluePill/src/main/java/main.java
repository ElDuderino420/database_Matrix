import java.util.*;

public class main {


    public static void main(String[] args) {
        List<Integer> list = new ArrayList();
        Random ran = new Random();
        ConnectionTest whiteRabbit = new ConnectionTest();
        neo4jConnection theOne = new neo4jConnection();
        for (int i = 0; i < 20; i++) {
            list.add(ran.nextInt(500000));
        }
        System.out.println(list.toString());
        //System.out.printf("%10s %10s %10s %10s %10s %10s %10s %10s %10s %10s ", "psql 1", "psql 2", "psql 3", "psql 4", "psql 5", "neo 1", "neo 2", "neo 3", "neo 4", "neo 5");

        for (int k = 1; k < 6; k++) {
            List<Long> neoL = new ArrayList();
            List<Long> psqlL = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                neoL.add(theOne.time(list.get(i), k));
                psqlL.add(whiteRabbit.time(list.get(i), k));
                System.out.println(i + ":  neo: "+neoL.get(i)+"  , sql: "+psqlL.get(i));
                /*
                System.out.printf("%10s", whiteRabbit.time(list.get(i), 1));
                System.out.printf("%10s", whiteRabbit.time(list.get(i), 2));
                System.out.printf("%10s", whiteRabbit.time(list.get(i), 3));
                System.out.printf("%10s", whiteRabbit.time(list.get(i), 4));
                System.out.printf("%10s", whiteRabbit.time(list.get(i), 5));
                System.out.printf("%10s", theOne.time(list.get(i), 1));
                System.out.printf("%10s", theOne.time(list.get(i), 2));
                System.out.printf("%10s", theOne.time(list.get(i), 3));
                System.out.printf("%10s", theOne.time(list.get(i), 4));
                System.out.printf("%10s", theOne.time(list.get(i), 5));
                System.out.printf("\n");*/
            }
            System.out.println("\tdepth\t meanSQL\t medianSQL\t meanNEO\t medianNEO");
            Long meanNeo = 0L;
            Long meanSQL = 0L;
            for (int i = 0;i<neoL.size();i++){
                meanNeo += neoL.get(i);
                meanSQL += psqlL.get(i);
            }
            Collections.sort(neoL);
            Collections.sort(psqlL);
            meanNeo = meanNeo/20;
            meanSQL = meanSQL/20;

            System.out.println("\tDepth "+k+"\t"+meanSQL+"\t"+psqlL.get(10)+"\t"+meanNeo+"\t"+neoL.get(10));
        }

    }
}
