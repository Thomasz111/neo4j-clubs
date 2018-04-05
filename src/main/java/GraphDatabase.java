import java.io.File;
import java.util.Random;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;

public class GraphDatabase {
    private final GraphDatabaseService graphDatabaseService;
    private static final String GRAPH_DIR_LOC = "./neo4j";

    public static GraphDatabase createDatabase() {
        return new GraphDatabase();
    }

    private GraphDatabase() {
        graphDatabaseService = new GraphDatabaseFactory()
                .newEmbeddedDatabaseBuilder(new File(GRAPH_DIR_LOC))
                .setConfig(GraphDatabaseSettings.allow_upgrade, "true")
                .newGraphDatabase();
        registerShutdownHook();
    }

    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDatabaseService.shutdown();
            }
        });
    }

    public String runCypher(final String cypher) {
        try (Transaction transaction = graphDatabaseService.beginTx()) {
            final Result result = graphDatabaseService.execute(cypher);
            transaction.success();
            return result.resultAsString();
        }
    }

    public void createRelationShipFriend(Node node1, Node node2){
        try(Transaction transaction = graphDatabaseService.beginTx()){
            transaction.success();
            node1.createRelationshipTo(node2, Relationships.FRIEND);
            node2.createRelationshipTo(node1, Relationships.FRIEND);
        }
    }

    public void createRelationShipFamily(Node node1, Node node2, String familyMember){
        try(Transaction transaction = graphDatabaseService.beginTx()){
            transaction.success();
            Relationship r = node1.createRelationshipTo(node2, Relationships.FAMILY);
            r.setProperty("Who", familyMember);
        }
    }

    public void createRelationShipAttends(Node node1, Node node2){
        try(Transaction transaction = graphDatabaseService.beginTx()){
            transaction.success();
            node1.createRelationshipTo(node2, Relationships.ATTENDS);
        }
    }

    public Node createClub(String name, String description){
        if(name == null)
            return null;

        try(Transaction transaction = graphDatabaseService.beginTx()){
            transaction.success();

            Node newNode = graphDatabaseService.createNode(Labels.CLUB);
            newNode.setProperty("Name", name);
            if(description != null) newNode.setProperty("Description", description);

            return newNode;
        }
    }

    public Node createPerson(String firstName, String lastName, int born, String city){
        if(firstName == null)
            return null;

        try(Transaction transaction = graphDatabaseService.beginTx()){
            transaction.success();

            Node newNode = graphDatabaseService.createNode(Labels.PERSON);
            newNode.setProperty("FirstName", firstName);
            if(lastName != null) newNode.setProperty("LastName", lastName);
            newNode.setProperty("Age", born);
            if(city != null) newNode.setProperty("City", city);

            return newNode;
        }
    }

    public void dataBaseGenerator(){
        String[] firstNames = {"Adam", "Marcin", "Damian", "Tomasz", "Piotr"};
        String[] lastNames = {"Nowak", "Kowalski", "Kurek", "Lony", "Piech"};
        String[] cities = {"Rybnik", "Warszawa", "Katowice", "Piece", "Szczyrk"};

        String[] names = {"Albireo", "Coco jambo", "Polonia niewiadom", "Matemax", "Skauci europy"};
        String[] descriptions = {"Klub astronomiczny", "Coco jambo i do przodu", "zespół piłkarski", "koło matematyczne", "miłośnicy natury"};

        String[] bloodbnds = {"Rodzic", "Dziecko", "Kuzynostwo", "Rodzeństwo", "Wujostwo"};

        try(Transaction transaction = graphDatabaseService.beginTx()){
            transaction.success();

            Random r = new Random();
            Node[] people = new Node[5];
            Node[] clubs = new Node[5];
            for(int i = 0; i < 5; i++){
                people[i] = createPerson(firstNames[i], lastNames[i], Math.abs(r.nextInt())%20+1980 ,cities[i]);
                clubs[i] = createClub(names[i], descriptions[i]);
            }

            int a;
            for(int i = 0; i < 10; i++){
                a = Math.abs(r.nextInt()%5);
                people[i%5].createRelationshipTo(people[((i%5==a)?(i+1):(a))%5], Relationships.FRIEND);
                a = Math.abs(r.nextInt()%5);
                people[i%5].createRelationshipTo(people[((i%5==a)?(i+1):(a))%5], Relationships.FAMILY).setProperty("Who", bloodbnds[i%5]);
                a = Math.abs(r.nextInt()%5);
                people[i%5].createRelationshipTo(clubs[((i%5==a)?(i+1):(a))%5], Relationships.ATTENDS);
            }
        }
    }

    public void simpleDataBaseGenerator(){
        try(Transaction transaction = graphDatabaseService.beginTx()){
            transaction.success();

            Node newNode1 = graphDatabaseService.createNode(Labels.PERSON);
            newNode1.setProperty("FirstName", "Adam");
            newNode1.setProperty("LastName", "Badam");
            newNode1.setProperty("Age", 1999);
            newNode1.setProperty("City", "Warszawa");

            Node newNode2 = graphDatabaseService.createNode(Labels.PERSON);
            newNode2.setProperty("FirstName", "Marcin");
            newNode2.setProperty("LastName", "Ziel");
            newNode2.setProperty("Age", 1989);
            newNode2.setProperty("City", "York");

            Node newNode3 = graphDatabaseService.createNode(Labels.PERSON);
            newNode3.setProperty("FirstName", "Tom");
            newNode3.setProperty("LastName", "Man");
            newNode3.setProperty("Age", 2000);
            newNode3.setProperty("City", "Old York");

            Node newClub1 = graphDatabaseService.createNode(Labels.CLUB);
            newClub1.setProperty("Name", "Matematix");
            newClub1.setProperty("Description", "Koło matematyczne");

            Node newClub2 = graphDatabaseService.createNode(Labels.CLUB);
            newClub2.setProperty("Name", "Fiziks");
            newClub2.setProperty("Description", "Koło naukowe fizyczne");

            newNode1.createRelationshipTo(newNode2, Relationships.FRIEND);
            newNode2.createRelationshipTo(newNode1, Relationships.FRIEND);

            newNode2.createRelationshipTo(newNode3, Relationships.FRIEND);
            newNode3.createRelationshipTo(newNode2, Relationships.FRIEND);

            newNode2.createRelationshipTo(newNode3, Relationships.FAMILY).setProperty("Who", "Rodzic");

            newNode2.createRelationshipTo(newClub1, Relationships.ATTENDS);
            newNode1.createRelationshipTo(newClub1, Relationships.ATTENDS);
            newNode3.createRelationshipTo(newClub2, Relationships.ATTENDS);
        }
    }
}
