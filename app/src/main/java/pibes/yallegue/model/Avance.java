package pibes.yallegue.model;

/**
 * Created by Edgar Salvador Maurilio on 06/03/2016.
 */
public class Avance {

    private String username;
    private String latitude;
    private String longitud;

    public Avance(String username, String latitude, String longitud) {
        this.username = username;
        this.latitude = latitude;
        this.longitud = longitud;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
}
