
public class Solution {
    public final GraphDatabase graphDatabase = GraphDatabase.createDatabase();

    public void databaseStatistics() {
        System.out.println(graphDatabase.runCypher("CALL db.labels()"));
        System.out.println(graphDatabase.runCypher("CALL db.relationshipTypes()"));
        System.out.println(graphDatabase.runCypher("CALL db.schema()"));
    }

    public String findAllRelationshipsForPerson(String firstName, String lastName){
        return graphDatabase.runCypher("MATCH (p:PERSON {FirstName: '" + firstName + "', LastName:'"+ lastName +"'}) -[]-> (m) RETURN m");
    }

    public String findShortestPathBetweenTwoPople(String firstName1, String lastName1, String firstName2, String lastName2){
        return graphDatabase.runCypher("MATCH (m1:PERSON {FirstName: '" + firstName1 + "', LastName:'"+ lastName1 +"'})," +
                "(m2:PERSON {FirstName: '" + firstName2 + "', LastName:'"+ lastName2 +"'}), " +
                "p = shortestPath((m1)-[*..5]-(m2)) RETURN p");
    }

    public void del(){
        graphDatabase.runCypher(" MATCH (n) DETACH DELETE n");
    }
}
