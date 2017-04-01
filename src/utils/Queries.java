package utils;

/**
 * Created by dpunk12 on 3/24/2017.
 */
public class Queries {

    private static String ASSESSMENT_CERTIFICATE_OF_REGISTRATION = "select subject.subjcode,subjlec_units,subjlab_units, CAST(subjcredit as REAL) as subjcred_units from subject, registration\n" +
            "where registration.subjcode=subject.subjcode AND studid='%s' and sy='%s' and registration.sem='%s'";


    public static String getAssessmentCertificateOfRegistration(String studid, String sy, String sem){
        return String.format(ASSESSMENT_CERTIFICATE_OF_REGISTRATION,studid,sy,sem);
    }


    public static String getStudFullName(){return "SELECT fullname from srgb.stufullnames;";}
    public static String getCurrUser(){return "SELECT current_user";}

}
