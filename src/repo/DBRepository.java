package repo;

import domain.Workout;

import java.sql.*;
import java.util.*;

public class DBRepository implements IRepository<String, Workout> {
    private String url;
    private Connection connection;

    public DBRepository(String url){
        this.url = url;

        try{
            Class.forName("org.sqlite.JDBC");
            createTableIfNotExists();

        }catch(ClassNotFoundException e){
            throw new RuntimeException("SQLite driver not found", e);
        }
    }

    private Connection openConnection() throws SQLException{
        return DriverManager.getConnection(this.url);
    }

    private void createTableIfNotExists(){
        String createTableSQL = "CREATE TABLE IF NOT EXISTS workouts(" +
                "id integer primary key autoincrement," +
                "start int not null," +
                "end int not null," +
                "name text not null," +
                "intensity int not null," +
                "desc text not null)";
        try(Connection conn = openConnection();
            Statement stm = conn.createStatement()){
            stm.execute(createTableSQL);
        }catch (SQLException e){
            System.err.println("Error creating table " + e.getMessage());
        }
        loadInitialData();
    }

    private void loadInitialData(){
        boolean isEmpty = false;
        try(Connection conn = openConnection();
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery("SELECT COUNT(*) FROM workouts")){
            if(rs.next() && rs.getInt(1) == 0){
                isEmpty = true;
            }
        }catch (SQLException e)
        {
            System.err.println("error checking the table" + e.getMessage());
        }


        if(isEmpty){
            addInitialMedication(6, 7, "Running", 86, "cardio");
            addInitialMedication(7, 8, "Weight Lifting", 90, "Strength training");
            addInitialMedication(8, 9, "Yoga", 50, "Flexibility");
            addInitialMedication(9, 10, "Cycling", 80, "cardio");
            addInitialMedication(10, 11, "HIIT", 90, "High Intensity");
            return;
        }
    }

    private void addInitialMedication(int start, int end, String name, int intensity, String desc) {
        String sql = "INSERT INTO workouts (start, end, name, intensity, desc) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = openConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, start);
            pstmt.setInt(2, end);
            pstmt.setString(3, name);
            pstmt.setInt(4, intensity);
            pstmt.setString(5, desc);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding medication: " + e.getMessage());
        }
    }


    @Override
    public void add(String s, Workout workout) throws RepositoryException {
        String sql = "INSERT INTO workouts (start, end, name, intensity, desc) VALUES (?,?,?,?,?)";
        try(Connection conn = openConnection();
            PreparedStatement pstm = conn.prepareStatement(sql);){
            pstm.setInt(1, workout.getStart());
            pstm.setInt(2, workout.getEnd());
            pstm.setString(3, workout.getName());
            pstm.setInt(4, workout.getIntensity());
            pstm.setString(5, workout.getDesc());
            pstm.executeUpdate();
        }catch(SQLException e){
            System.err.println("Error addding" + e.getMessage());
        }
    }

    @Override
    public Optional<Workout> delete(String s) {
        Optional<Workout> work = findById(s);
        if(work.isPresent()){
            String sql ="DELETE FROM workouts WHERE id = ?";
            try(Connection conn = openConnection();
            PreparedStatement pstm = conn.prepareStatement(sql)){
                pstm.setString(1, s);
                pstm.executeUpdate();
        }catch(SQLException e ){
                System.err.println("error deleting" + e.getMessage());
            }}

        return work;
    }

    @Override
    public void modify(String s, Workout entity) throws RepositoryException {
        String sql = "UPDATE workouts SET start = ?, end = ?, name = ?, intensity = ?, desc = ? WHERE id = ?";

        try(Connection conn = openConnection();
        PreparedStatement pstm = conn.prepareStatement(sql)){
            pstm.setInt(1, entity.getStart());
            pstm.setInt(2, entity.getEnd());
            pstm.setString(3, entity.getName());
            pstm.setInt(4, entity.getIntensity());
            pstm.setString(5, entity.getDesc());
            int rowsAffected = pstm.executeUpdate();

            if(rowsAffected == 0){
                throw new RepositoryException("No workout found " + entity.getID());
            }

        }catch (SQLException e){
            throw new RepositoryException("Error updating" + e.getMessage());
        }
    }

    @Override
    public Optional<Workout> findById(String s) {
        String sql = "SELECT id, start, end, name, intensity, desc FROM workouts WHERE id = ?";

        try(Connection conn = openConnection();
            PreparedStatement pstm = conn.prepareStatement(sql)){
            pstm.setString(1, s);
            ResultSet rs = pstm.executeQuery();

            if(rs.next()){
                String id = String.valueOf(rs.getInt("id"));
                int start = rs.getInt("start");
                int end = rs.getInt("end");
                String name = rs.getString("name");
                int intensity = rs.getInt("intensity");
                String desc = rs.getString("desc");
                return Optional.of(new Workout(id, start, end, name, intensity, desc));
            }
        }catch (SQLException e){
            System.err.println("Error finding " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Workout> getAll() {
        List<Workout> workouts = new ArrayList<>();
        String sql = "SELECT id, start, end, name, intensity, desc FROM workouts";
        try(Connection conn = openConnection();
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery(sql)){
            while(rs.next()){
                String id = String.valueOf(rs.getInt("id"));
                int start = rs.getInt("start");
                int end = rs.getInt("end");
                String name = rs.getString("name");
                int intensity = rs.getInt("intensity");
                String desc = rs.getString("desc");
                workouts.add(new Workout(id, start, end, name, intensity, desc));
            }
        }catch(SQLException e){
            System.err.println("error reading workouts" + e.getMessage());

        }
        return  workouts;
    }
}
