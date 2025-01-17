package dev.xkmc.loadingprofiler.bootstrap;

import net.minecraftforge.forgespi.locating.IDependencyLocator;
import net.minecraftforge.forgespi.locating.IModFile;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class LPDependencyLocator implements IDependencyLocator {

	@Override
	public List<IModFile> scanMods(Iterable<IModFile> loadedMods) {
		return List.of();
	}

	@Override
	public String name() {
		return "Loading Profiler - Mod Loading Watcher";
	}

	@Override
	public void scanFile(IModFile modFile, Consumer<Path> pathConsumer) {
	}

	@Override
	public void initArguments(Map<String, ?> arguments) {
		LPBootCore.info(LauncherStages.DL_ARG);
	}

	@Override
	public boolean isValid(IModFile modFile) {
		return true;
	}

}
