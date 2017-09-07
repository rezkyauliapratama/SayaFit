package android.cybereye_community.com.sayafit.model;

/**
 * Created by Rezky Aulia Pratama on 9/7/2017.
 */

public class StepsObject {
    private PolylineObject polyline;
    public StepsObject(PolylineObject polyline) {
        this.polyline = polyline;
    }
    public PolylineObject getPolyline() {
        return polyline;
    }
}