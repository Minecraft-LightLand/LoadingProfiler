package dev.xkmc.loadingprofiler.bootstrap;

import net.minecraftforge.fml.loading.progress.ProgressMeter;
import net.minecraftforge.fml.loading.progress.StartupNotificationManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LPBootCore {

	public record TimeChart(ILoadingStage stage, long time) {
	}

	public static final Logger LOGGER = LogManager.getLogger("LoadingProfilerBootstrap");

	private static ProgressMeter meter;
	private static ILoadingStage prev;
	private static int offset = 0;
	private static long prevTime = 0;
	private static boolean closed = false;

	public static final List<TimeChart> TIME_CHART = new ArrayList<>();

	public static void info(ILoadingStage stage) {
		if (closed) return;
		if (stage == prev) return;
		if (meter == null) {
			meter = StartupNotificationManager.addProgressBar("Loading Profiler", 0);
		}
		if (prev != null && prev.group() == stage.group() && stage.ordinal() != prev.ordinal() + 1) {
			LOGGER.warn("Jumping Loading Stages! From {} to {}", prev, stage);
		}
		if (stage.group() != LoadingStageGroup.LAUNCHER && (prev == null || prev.group() == LoadingStageGroup.LAUNCHER))
			offset = LauncherStages.values().length;
		int index = offset + stage.ordinal() + 1;
		String text = "LOADING STAGE " + index + " - " + stage.getText();
		long time = System.currentTimeMillis();
		meter.label(text);
		meter.setAbsolute(index);
		if (prev != null) {
			long diff = time - prevTime;
			String diffTex = "LOADING STAGE <" + prev.getText() + "> elapsed " + diff + " ms";
			LOGGER.info(diffTex);
			TIME_CHART.add(new TimeChart(prev, diff));
		}
		LOGGER.info(text);
		stage.run();
		prev = stage;
		prevTime = time;
	}

	public static void close() {
		closed = true;
	}

	@Nullable
	public static ILoadingStage prev() {
		return prev;
	}

	@Nullable
	public static ProgressMeter meter() {
		return meter;
	}


	public static void nop(ILoadingStage stage) {

	}

}
