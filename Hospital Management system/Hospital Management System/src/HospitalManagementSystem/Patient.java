package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    private Connection connection;
    private Scanner scanner;
    public Patient(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner=scanner;
    }

    public void addPatient(){
        System.out.println("Enter patient name: ");
        String name = scanner.next();
        System.out.println("Enter patient age: ");
        int age = scanner.nextInt();
        System.out.println("Enter patient Gender: ");
        String gender = scanner.next();

        try{
            String query="INSERT INTO patients(name,age,gender) VALUES(?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,name);
            preparedStatement.setInt(2,age);
            preparedStatement.setString(3,gender);

            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows>0){
                System.out.println("Patient added Successfully !!");
            }
            else{
                System.out.println("Failed to add patient !!");
            }

        }
        catch (SQLException e){
            e.printStackTrace();
            //jo bhi exception ayi gi ek ek kr ke print krna
        }
    }

    public void viewPatients(){
        String query = "select * from patients";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            // jo bhi table aa rhi hai usko hold krta print krna
            // resultset pointer set krega aor line by line data ko print krega

            // print krna hai in the form of SQL table
            System.out.println("Patients: ");
            System.out.println("+------------+------------------+-------------+-----------+");
            System.out.println("| Patient id | Name             | age          | Gender    |");
            System.out.println("+------------+------------------+-------------+-----------+");

            while(resultSet.next()){
                // java k local veriable hai
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age =resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                System.out.printf("|% -12s|%-18s|%-14s|%-11s\n", id, name, age, gender);
                System.out.println("+------------+------------------+-------------+-----------+");

            }

        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public boolean getPatientById(int id){
        String query = " SELECT * FROM patients WHERE id = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            // result indicate if any data has bee camed or not
            if(resultSet.next()){
                return true;
            }else{
                return false;
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return false;

    }


}
