package uk.co.maboughey.moqreq.type;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import uk.co.maboughey.moqreq.ModReq;

import java.util.Optional;
import java.util.UUID;

import static org.spongepowered.api.Sponge.*;

public class ModRequest {
    public int id;
    public UUID user;
    public String message;
    public Location<World> location;
    public Vector3d rotation;
    public int status;
    public UUID responder;
    public String response;

    public void setRotation(Double x, Double y, Double z) {
        Vector3d v3d = new Vector3d(x,y,z);
        rotation = v3d;
    }
    public void setLocation(Double x, Double y, Double z, UUID world) {
        //Sanity check on world
        Optional<World> worldEntity = getServer().getWorld(world);

        if (!worldEntity.isPresent()) {
            ModReq.log.error("Could not find world "+world);
            return;
        }

        location = new Location<World>(worldEntity.get(), x, y, z);
    }
    public void teleport(Player player) {
        player.setRotation(rotation);
        player.setLocation(location);
    }
}
