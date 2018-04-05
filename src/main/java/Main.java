import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Solution solution = new Solution();
        solution.del();
        solution.graphDatabase.simpleDataBaseGenerator();
        //solution.graphDatabase.dataBaseGenerator();
        System.out.println(solution.findShortestPathBetweenTwoPople("Adam", "Badam", "Tom", "Man"));
        System.out.println(solution.findAllRelationshipsForPerson("Adam", "Badam"));
        solution.databaseStatistics();
    }
}
