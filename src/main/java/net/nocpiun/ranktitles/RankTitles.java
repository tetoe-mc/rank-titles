package net.nocpiun.ranktitles;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RankTitles implements ModInitializer {
	public static final String MOD_ID = "rank-titles";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static RankTitlesPlugin plugin;

	@Override
	public void onInitialize() {
		plugin = new RankTitlesPlugin();

		LOGGER.info("Rank Titles Plugin initialized.");
	}
}
