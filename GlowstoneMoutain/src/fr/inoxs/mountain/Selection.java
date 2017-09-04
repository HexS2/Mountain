package fr.inoxs.mountain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import org.bukkit.Location;

@Data
@AllArgsConstructor
public class Selection {

    @Setter
    private Location location1;
    @Setter
    private Location location2;

    public Selection() {

    }

    public void setLocation1(Location location1) {
        this.location1 = location1;
    }

    public void setLocation2(Location location2) {
        this.location2 = location2;
    }

    public Location getLocation1() {
        return location1;
    }

    public Location getLocation2() {
        return location2;
    }

    public Cuboid getArea() {
        return new Cuboid(location1, location2);
    }
}