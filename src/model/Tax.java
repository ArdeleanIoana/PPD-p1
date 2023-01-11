package model;

public class Tax {
    private String date;
    private String cnp;
    private int sum;

    public Tax(String string) {
        if(string.charAt(0) == '(')
            fromFileString(string);
        else
            fromString(string);
    }

    public Tax(String date, String cnp, int sum) {
        this.date = date;
        this.cnp = cnp;
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "[Tax]{" +
                "date='" + date + '\'' +
                ", cnp=" + cnp +
                ", sum=" + sum +
                '}';
    }

    public String toFileString() {
        return "(" + date + "," + cnp + "," + sum + ")\n";
    }

    public void fromFileString(String fileString) {
        fileString = fileString.substring(1, fileString.length()-2);
        String[] elements = fileString.split(",");

        date = elements[0];
        cnp = elements[1];
        sum = Integer.parseInt(elements[2]);
    }

    private String getParameter(String string) {
        int i = string.indexOf("=");
        return string.substring(i+1);
    }

    public void fromString(String string) {
        string = string.substring(string.indexOf("{")+1, string.indexOf("}"));
        String[] elements = string.split(",");

        for(int i=0;i<elements.length;i++)
            elements[i] = getParameter(elements[i]);

        this.date = elements[0];
        this.cnp = elements[1];
        this.sum = Integer.parseInt(elements[2]);
    }

    public String getDate() {
        return date;
    }

    public String getCnp() {
        return cnp;
    }

    public int getSum() {
        return sum;
    }
}
