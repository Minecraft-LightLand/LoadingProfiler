package dev.xkmc.loadingprofiler.bootstrap;

public interface ILoadingStage {

	LoadingStageGroup group();

	int ordinal();

	String getText();

	void run();

}
