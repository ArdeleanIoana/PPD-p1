package domain;

public class Confirmation {
    private boolean accepted;

    public Confirmation(boolean accepted) {
        this.accepted = accepted;
    }

    public Confirmation(String string) {
        fromString(string);
    }

    @Override
    public String toString() {
        return "[Confirmation]{" +
                "accepted=" + accepted +
                '}';
    }

    private String getParameter(String string) {
        int i = string.indexOf("=");
        return string.substring(i+1);
    }

    public void fromString(String string) {
        string = string.substring(string.indexOf("{")+1, string.indexOf("}"));
        String[] parts = string.split(",");

        for(int i=0;i<parts.length;i++)
            parts[i] = getParameter(parts[i]);

        this.accepted = Boolean.parseBoolean(parts[0]);
    }

    public boolean getAccepted() {
        return accepted;
    }
}
