package net.moonjink.dynamic_daylight;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamicDaylight implements ModInitializer {
	public static final String MOD_ID = "dynamic-daylight";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private boolean isDaylightCycleActive;
	private int tickCount = 0;

	@Override
	public void onInitialize() {
		ServerTickEvents.END_SERVER_TICK.register((MinecraftServer server) -> {
			tickCount++;

			if(tickCount % 20 == 0) {
				int playerCount = server.getCurrentPlayerCount();

				GameRules.BooleanRule daylightCycleGameRule = server.getGameRules().get(GameRules.DO_DAYLIGHT_CYCLE);

				if (playerCount == 0 && isDaylightCycleActive)
					daylightCycleGameRule.set(false, server);
				else if (playerCount > 0 && !isDaylightCycleActive)
					daylightCycleGameRule.set(true, server);

				isDaylightCycleActive = daylightCycleGameRule.get();
				tickCount = 0;
			}
		});

		LOGGER.info("Loading Dynamic Daylight!");
	}
}