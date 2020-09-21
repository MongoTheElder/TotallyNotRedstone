package tv.mongotheelder.tnr.setup;

import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import tv.mongotheelder.tnr.keypad.KeypadScreen;

public class ClientSetup {
    public static void init(final FMLClientSetupEvent event) {
        ScreenManager.registerFactory(Registration.KEYPAD_CONTAINER.get(), KeypadScreen::new);

    }

}
