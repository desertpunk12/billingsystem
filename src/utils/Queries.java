package utils;

public class Queries {

    private static String ASSESSMENT_CERTIFICATE_OF_REGISTRATION = "select subject.subjcode,subjlec_units,subjlab_units, CAST(subjcredit as REAL) as subjcred_units from subject, registration\n" +
            "where registration.subjcode=subject.subjcode AND studid='%s' and sy='%s' and registration.sem='%s'";


    public static String getAssessmentCertificateOfRegistration(String studid, String sy, String sem){
        return String.format(ASSESSMENT_CERTIFICATE_OF_REGISTRATION,studid,sy,sem);
    }


    public static String getStudFullName(){return "SELECT fullname from srgb.stufullnames;";}
    public static String getCurrUser(){return "SELECT current_user";}

    public static String getTotalMiscAndTuition(){
        return "select sy,sem,SUM(amt) as totalmisc, totaltuition from ass_details LEFT JOIN\n" +
            "  (select amt as totaltuition,sy,sem from ass_details where\n" +
            "    feecode='TUITIONFEE' and studid='%s' GROUP BY sy,sem,amt) as tuitiondetails\n" +
            "  USING(sy,sem)\n" +
            "where feecode != 'TUITIONFEE' and studid='%s' GROUP BY sy,sem,totaltuition ORDER BY sy, sem;";
    }

    public static String getTotalTuition(){
        return "select sy,sem,amt as totaltuition from ass_details where feecode = 'TUITIONFEE' and studid='2004-0001' GROUP BY sy,sem,amt ORDER BY sy,sem;";
    }

    public static String getTotalMisc(){
        return "select sy,sem,amt as totalmisc from ass_details where feecode != 'TUITIONFEE' and studid='2004-0001' GROUP BY sy,sem,amt ORDER BY sy,sem;";
    }




}
