package HospitalManagementSystem;

import javax.print.Doc;
import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static final String url = "jdbc:mysql://localhost:3306/Hospital";
    private static final String username = "root";
    private static final String password = "2003";
    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            // sare k sare drivers ko database k saath connect krta hai
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        try{
            Connection connection = DriverManager.getConnection(url,username,password);
            Patient patient = new Patient(connection,scanner);
            Doctor doctor = new Doctor(connection);

            while(true){
                System.out.println("HOSPITAL MANAGEMENT SYSTEM ");
                System.out.println("1. Add Patient");
                System.out.println("2. View patient");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.println("Enter your choice: ");

                int choice = scanner.nextInt();

                switch (choice){
                    case 1:
                        //Add Patient
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        //view patient
                        patient.viewPatients();
                        System.out.println();
                        break;
                    case 3:
                        //view doctors
                        doctor.viewDoctors();
                        System.out.println();
                        break;
                    case 4:
                        //Book Appointment
                        bookAppointment(patient,doctor,connection,scanner);
                        System.out.println();
                        break;
                    case 5:
                        System.out.println("THNAKYOU for using HOSPITAL MANAGEMENT SYSTEM !!!!!");
                        return;
                    default:
                        System.out.println("Enter Valid Choice");
                        break;
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void bookAppointment(Patient patient,Doctor doctor,Connection connection ,Scanner scanner){
        System.out.println("Enter patient id: ");
        int patientid = scanner.nextInt();
        System.out.println("Enter doctor id: ");
        int doctorid = scanner.nextInt();
        System.out.println("Enter Appointment date (YYYY-MM-DD): ");
        String appointmentdate = scanner.next();
        if(patient.getPatientById(patientid) && doctor.getDoctorById(doctorid)){
            if(checkDoctorAvailability(doctorid,appointmentdate,connection)){
                String appointmentQuery = "INSERT INTO appointments(patient_id,doctor_id,appointment_date) VALUES(? ? ?)";
                try{
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1,patientid);
                    preparedStatement.setInt(2,doctorid);
                    preparedStatement.setString(1,appointmentdate);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if(rowsAffected>0){
                        System.out.println("Appointment Booked");
                    }
                    else {
                        System.out.println("Failed to Book Appointment");
                    }

                }
                catch (SQLException e){
                    e.printStackTrace();
                }
            }
            else {
                System.out.println("Doctor not available on this date !!!");
            }
        }
        else {
            System.out.println("Either or patient doesn't exist !!!");
        }
    }

    public static boolean checkDoctorAvailability(int doctorid,String appointmentdate,Connection connection) {
        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";
        // iska mylb no. of rows btaya ga for this perticular query
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, doctorid);
            preparedStatement.setString(2, appointmentdate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                if (count == 0) {
                    return true;
                } else {
                    return false;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
