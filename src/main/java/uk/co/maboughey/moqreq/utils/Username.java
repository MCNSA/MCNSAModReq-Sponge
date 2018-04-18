package uk.co.maboughey.moqreq.utils;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;

import java.util.Optional;
import java.util.UUID;

public class Username {

    public static String getUsername(UUID uuid) {
        String name = uuid.toString();

        //Try getting user from server of players that have logged in before
        try {
            UserStorageService uss = Sponge.getServiceManager().provideUnchecked(UserStorageService.class);
            Optional<User> oUser = uss.get(uuid);
            if (oUser.isPresent()) {
                name = oUser.get().getName();
            }
        }
        catch (IllegalArgumentException e) {
            //Do nothing
        }
        return name;
    }
}
