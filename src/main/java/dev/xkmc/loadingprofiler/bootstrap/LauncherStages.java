package dev.xkmc.loadingprofiler.bootstrap;

import java.util.function.Consumer;

public enum LauncherStages implements ILoadingStage {
	TS_INIT("Services Initializing", LPBootCore::nop),
	TS_LOAD("Services Loading", LPBootCore::nop),
	DL_ARG("Mod Scanning", LPBootCore::nop),
	TS_SCAN_COMPLETE("Launch Plugin Working", LPBootCore::nop),
	TS_TRANSFORMER("Transformer Class Loader Working", LPBootCore::nop);

	private final String text;
	private final Consumer<LauncherStages> task;

	LauncherStages(String text, Consumer<LauncherStages> task) {
		this.text = text;
		this.task = task;
	}

	public LoadingStageGroup group() {
		return LoadingStageGroup.LAUNCHER;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public void run() {
		task.accept(this);
	}

}
