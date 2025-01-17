package dev.xkmc.loadingprofiler.bootstrap;

import cpw.mods.modlauncher.api.IEnvironment;
import cpw.mods.modlauncher.api.IModuleLayerManager;
import cpw.mods.modlauncher.api.ITransformationService;
import cpw.mods.modlauncher.api.ITransformer;
import joptsimple.OptionSpecBuilder;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class LPTransformationService implements ITransformationService {

	@Override
	public @NotNull String name() {
		return "Loading Profiler - Transformation Service Watcher";
	}

	@Override
	public void initialize(IEnvironment environment) {
	}

	@Override
	public void onLoad(IEnvironment env, Set<String> otherServices) {
		LPBootCore.info(LauncherStages.TS_INIT);
	}

	@Override
	public void arguments(BiFunction<String, String, OptionSpecBuilder> argumentBuilder) {
		LPBootCore.info(LauncherStages.TS_LOAD);
	}

	@Override
	public List<Resource> completeScan(IModuleLayerManager layerManager) {
		LPBootCore.info(LauncherStages.TS_SCAN_COMPLETE);
		return List.of();
	}

	@Override
	public Map.Entry<Set<String>, Supplier<Function<String, Optional<URL>>>> additionalClassesLocator() {
		return ITransformationService.super.additionalClassesLocator();
	}

	@Override
	public Map.Entry<Set<String>, Supplier<Function<String, Optional<URL>>>> additionalResourcesLocator() {
		return ITransformationService.super.additionalResourcesLocator();
	}

	@Override
	public @NotNull List<ITransformer> transformers() {
		LPBootCore.info(LauncherStages.TS_TRANSFORMER);
		return List.of();
	}
}
