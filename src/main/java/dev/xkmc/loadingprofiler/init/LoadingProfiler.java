package dev.xkmc.loadingprofiler.init;

import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(LoadingProfiler.MODID)
public class LoadingProfiler {

	public static final String MODID = "loadingprofiler";
	public static final Logger LOGGER = LogManager.getLogger();

	public LoadingProfiler() {
	}

}
