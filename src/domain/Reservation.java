package domain;

public class Reservation {
    private String nameClient;
    private String cnpClient;
    private String reservationDate;
    private int treatmentLocation;
    private int treatmentType;
    private String treatmentDate;
    private String treatmentHour;

    public Reservation(String name, String cnp, String date, int treatmentLocation, int treatmentType, String treatmentDate, String treatmentHour) {
        this.nameClient = name;
        this.cnpClient = cnp;
        this.reservationDate = date;
        this.treatmentLocation = treatmentLocation;
        this.treatmentType = treatmentType;
        this.treatmentDate = treatmentDate;
        this.treatmentHour = treatmentHour;
    }

    public Reservation(String string) {
        if (string.charAt(0) != '(')
            fromStringToReservation(string);
        else
            fromFileToReservation(string);


    }

    @Override
    public String toString() {
        return "[Reservation]{" +
                "name=" + nameClient +
                ", cnp=" + cnpClient +
                ", date=" + reservationDate +
                ", treatmentLocation=" + treatmentLocation +
                ", treatmentType=" + treatmentType +
                ", treatmentDate=" + treatmentDate +
                ", treatmentHour=" + treatmentHour +
                '}';
    }

    public String toFileString() {
        return "(" + nameClient + "," + cnpClient + "," + reservationDate + "," + treatmentLocation + "," + treatmentType + "," + treatmentDate + "," + treatmentHour + ")\n";
    }

    public void fromFileToReservation(String fileString) {
        fileString = fileString.substring(1, fileString.length()-1);
        String[] elements = fileString.split(",");

        nameClient = elements[0];
        cnpClient = elements[1];
        reservationDate = elements[2];
        treatmentLocation = Integer.parseInt(elements[3]);
        treatmentType = Integer.parseInt(elements[4]);
        treatmentDate = elements[5];
        treatmentHour = elements[6];
    }

    private String getParameter(String string) {
        return string.substring(string.indexOf("=")+1);
    }

    public void fromStringToReservation(String string) {
        string = string.substring(string.indexOf("{")+1, string.indexOf("}"));
        String[] elements = string.split(",");

        for(int i=0;i<elements.length;i++)
            elements[i] = getParameter(elements[i]);

        this.nameClient = elements[0];
        this.cnpClient = elements[1];
        this.reservationDate = elements[2];
        this.treatmentLocation = Integer.parseInt(elements[3]);
        this.treatmentType = Integer.parseInt(elements[4]);
        this.treatmentDate = elements[5];
        this.treatmentHour = elements[6];
    }


    public String getCnpClient() {
        return cnpClient;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public int getTreatmentLocation() {
        return treatmentLocation;
    }

    public int getTreatmentType() {
        return treatmentType;
    }


    public String getTreatmentHour() {
        return treatmentHour;
    }
}
